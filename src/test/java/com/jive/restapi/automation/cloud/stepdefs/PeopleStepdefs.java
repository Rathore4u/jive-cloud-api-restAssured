package com.jive.restapi.automation.cloud.stepdefs;

import static junit.framework.TestCase.assertTrue;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertFalse;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.FilterParameterConstants;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.TestContextKeys;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.LoginStepdefs;
import com.jive.restapi.automation.utilities.PeopleUtils;
import com.jive.restapi.automation.utilities.v3.PersonUtil;
import com.jive.restapi.generated.person.models.Activity;
import com.jive.restapi.generated.person.models.ActivityEntities;
import com.jive.restapi.generated.person.models.Jive;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Profile;
import com.jive.restapi.generated.person.models.StreamEntities;
import com.jive.restapi.generated.person.models.Update;
import com.xo.restapi.automation.context.TestContext;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;
import org.hamcrest.core.IsEqual;

public class PeopleStepdefs {

    public static final String CHANGED_GIVEN_NAME = "Franco";
    public static String randomSeed = null;
    public static LoginStepdefs login = new LoginStepdefs();
    private final LoginStepdefs userLoginSteps;
    private static final String JIVE_LABEL = "jiveLabel";
    private static final String JIVE_VALUE = "jiveValue";
    private static final String ENABLED = "enabled";
    private static final String SKILL = "skill";
    private static final String DASH = "-";
    private TestContext testContext;
    private Response createPersonResponse;
    private Response updatePersonResponse;
    private Response getResponse;
    private Response streamListResponse;
    private DataTable stepChangedData;

    public PeopleStepdefs(LoginStepdefs loginSteps, TestContext context) {
        testContext = context;
        userLoginSteps = loginSteps;
    }

    @When("^Requests to create a person$")
    public void adminRequestsToCreateAPerson() {
        createPersonResponse = PeopleUtils.createStandardUser();
        testContext.addValue(TestContextKeys.CREATE_PERSON_RESPONSE, createPersonResponse);
    }

    @Then("^person is created successfully$")
    public void personIsReturnedSuccessfully() {
        createPersonResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @Then("^person can be updated$")
    public void personCanBeUpdated() {

    }

    @When("^admin wants to update the person$")
    public void adminWantsToUpdateThePerson() {
        Person person = createPersonResponse.as(Person.class);
        Person updatedPerson = new Person()
                .id(person.getId())
                .emails(person.getEmails())
                .jive(new Jive()
                        .username(person.getJive().getUsername()))
                .name(person.getName()
                        .givenName(CHANGED_GIVEN_NAME));
        updatePersonResponse = PeopleUtils.updatePerson(updatedPerson);
    }

    @When("^User requests to update person (\\S+)$")
    public void userRequestToUpdatePerson(String userTag, DataTable personData) {
        List<Map<String, String>> rows = personData.asMaps(String.class, String.class);
        Person person = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                userTag);
        // Get current person value in order not to loose any data, and cleanup some ro fields.
        person = PersonUtil.getPerson(person.getId()).as(Person.class)
                .thumbnailId(null)
                .thumbnailUrl(null)
                .jive(new Jive()
                    .username(person.getJive().getUsername()))
                .directReportCount(null)
                .displayName(null)
                .mentionName(null)
                .type(null)
                .initialLogin(null)
                .updated(null)
                .published(null)
                .followed(null)
                .followerCount(null)
                .followingCount(null);

        randomSeed = getRandomSeed();

        if (rows.get(0).get(JIVE_LABEL) != null) {
            Person finalPerson = person;
            rows.forEach(p -> {
                Profile profile = new Profile();
                profile.setJiveLabel(p.get(JIVE_LABEL));
                profile.setValue(replaceRandomSeed(p.get(JIVE_VALUE), randomSeed));
                finalPerson.getJive().addProfileItem(profile);
            });
        }

        if (rows.get(0).get(ENABLED) != null) {
            person.getJive()
                    .setEnabled(Boolean.parseBoolean(rows.get(0).get(ENABLED)));
        }

        updatePersonResponse = PeopleUtils.updatePerson(person);
        updatePersonResponse.then().assertThat().statusCode(SC_OK);
        FeatureRegistry.getCurrentFeature().setData(
                Person.class,
                userTag,
                updatePersonResponse.as(Person.class)
        );
        stepChangedData = personData;

        FeatureRegistry.getCurrentFeature().setData(
                String.class,
                TestContextKeys.RANDOM_SEED,
                randomSeed);
    }

    @Then("^Person (\\S+) is updated successfully$")
    public void personIsUpdated(String userTag) {
        updatePersonResponse.then().assertThat().statusCode(SC_OK);
        Person person = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                userTag);
        List<Map<String, String>> rows = stepChangedData.asMaps(String.class, String.class);

        for (Map<String, String> item : rows) {
            if (rows.get(0).get(JIVE_LABEL) != null) {
                for (Profile profile : person.getJive().getProfile()) {
                    if (profile.getJiveLabel().equals(item.get(JIVE_LABEL))) {
                        assertTrue(
                                ValidationConstants.getErrorMessage(EntityConstants.PERSON, ValidationConstants.UPDATED),
                                replaceRandomSeed(item.get(JIVE_VALUE), randomSeed).equals(profile.getValue())
                        );
                    }
                }
            }
        }

        if (rows.get(0).get(ENABLED) != null) {
            assertTrue(
                    ValidationConstants.getErrorMessage(EntityConstants.PERSON, ValidationConstants.UPDATED),
                    person.getJive().getEnabled().equals(Boolean.parseBoolean(rows.get(0).get(ENABLED)))
            );
        }
    }

    @Then("^person is updated$")
    public void personIsUpdated() {
        updatePersonResponse.then().assertThat().statusCode(SC_OK).body(PeopleUtils.GIVEN_NAME_PATH, IsEqual.equalTo(CHANGED_GIVEN_NAME));
    }

    @When("^Get list (.*) of user (.*) Streams$")
    public void getUserStreams(String listTag, String userTag) {
        Person user = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                userTag);

        getResponse = PersonUtil.getStreams(user.getId());
        getResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                StreamEntities.class,
                listTag,
                getResponse.as(StreamEntities.class)
        );
    }

    @Then("^List (.*) of user streams is received successfully$")
    public void userStreamsReceivedSuccessfully(String listTag) {
        StreamEntities streamsList = FeatureRegistry.getCurrentFeature().getData(
                StreamEntities.class,
                listTag);

        assertFalse(streamsList.getList().isEmpty());
    }

    @When("^User requests to add skill for person (\\S+)$")
    public void userRequestAddSkill(String userTag, DataTable skillData) {
        List<Map<String, String>> rows = skillData.asMaps(String.class, String.class);
        Person person = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                userTag);
        List<String> tags = new ArrayList<>(1);
        randomSeed = getRandomSeed();
        rows.forEach(item -> tags.add(replaceRandomSeed(item.get(SKILL), randomSeed).toLowerCase()));
        getResponse = PersonUtil.addExpertiseTag(person.getId(), tags);
        stepChangedData = skillData;
        FeatureRegistry.getCurrentFeature().setData(
                String.class,
                TestContextKeys.RANDOM_SEED,
                randomSeed);
    }

    @Then("^Skill is added successfully for user (\\S+)$")
    public void verifySkillAddedSuccessfully(String userTag) {
        getResponse.then().assertThat().statusCode(SC_NO_CONTENT);
        List<Map<String, String>> rows = stepChangedData.asMaps(String.class, String.class);
        Person person = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                userTag);
        try {
            TimeUnit.MILLISECONDS.sleep(NumberConstants.twoThousand);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Person personReloaded = PersonUtil.getPerson(person.getId()).as(Person.class);
        for (Map<String, String> item : rows) {
            assertTrue(
                    ValidationConstants.getErrorMessage(SKILL, ValidationConstants.CREATED),
                    personReloaded.getTags().contains(replaceRandomSeed(item.get(SKILL), randomSeed).toLowerCase())
            );
        }
    }

    @When("^Add Skill for user (\\S+)$")
    public void addSkillForUser(String userTag, DataTable skillData) {
        login.executeAs(userTag, () -> {
            userRequestAddSkill(userTag, skillData);
            verifySkillAddedSuccessfully(userTag);
        });
    }

    @When("^User (\\S+) follow a user (\\S+)$")
    public void personFollowUser(String user1Tag, String user2Tag) {
        Person user1 = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                user1Tag);
        Person user2 = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                user2Tag);

        Response streams = PersonUtil.getStreams(user1.getId());
        streamListResponse = PersonUtil.setFollowingIn(
                user2.getId(),
                streams.as(StreamEntities.class).getList()
        );
    }

    @When("^User (\\S+) is following by user (\\S+)$")
    public void verifyPersonStreamList(String user1Tag, String user2Tag) {
        Person user1 = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                user1Tag);

        user1 = PeopleUtils.searchPeopleByName(user1.getJive().getUsername()).as(Person.class);
        assertTrue(ValidationConstants.FOLLOW_USER_FAILED, user1.getFollowingCount().equals(1));
    }

    public static String replaceRandomSeed(String value, String seed) {
        return value + DASH + seed;
    }

    private String getRandomSeed() {
        if (randomSeed == null) {
            return RandomStringUtils.randomAlphanumeric(10).toLowerCase();
        } else {
            return randomSeed;
        }
    }

    @When("^Get (document|update) activities (.*) from the user (.*)$")
    public void getActivitiesByTypeFromUser(String typeTag, String activitiesTag, String userTag) throws InterruptedException {
        Person personData = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                userTag);

        // Static wait is used to give time for content to appear in activities
        Thread.sleep(TimeoutConstants.S);
        Response getActivitiesResponse = PersonUtil.getPersonActivity(
                personData.getId(),
                Options.custom(op -> op.filterQuery(String.format(
                        FilterParameterConstants.TYPE_FILTER_OPTION, typeTag))));
        getActivitiesResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                ActivityEntities.class,
                activitiesTag,
                getActivitiesResponse.as(ActivityEntities.class));
    }

    @Then("^User activities (.*) (dont-contain|contain) update (.*)$")
    public void userActivitiesContainUpdate(String activitiesTag, String equalityTag, String updateTag) {
        ActivityEntities activities = FeatureRegistry.getCurrentFeature().getData(
                ActivityEntities.class,
                activitiesTag);
        Update updateData = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                updateTag);

        Predicate<Activity> predicate = activity ->
                activity
                        .getObject()
                        .getDisplayName()
                        .replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING)
                        .contains(updateData.getSubject()
                                .replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING));

        val activityList = activities.getList();
        val errorMessage = String.format(ValidationConstants.USER_ACTIVITIES_CONTAIN_UPDATE_VALIDATION,
                updateData.getSubject());

        assertTrue(
                errorMessage,
                equalityTag.contains(ContentConsts.DONT_WORD)
                        ? activityList.stream().noneMatch(predicate)
                        : activityList.stream().anyMatch(predicate));
    }
}
