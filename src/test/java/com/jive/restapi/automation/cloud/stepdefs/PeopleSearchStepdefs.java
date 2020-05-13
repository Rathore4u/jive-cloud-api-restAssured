package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.TestContextKeys;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.LoginStepdefs;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.PeopleUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.PersonEntities;
import com.jive.restapi.generated.person.models.Profile;
import com.xo.restapi.automation.context.TestContext;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

public class PeopleSearchStepdefs {

    private final LoginStepdefs userLoginSteps;
    private TestContext testContext;
    private Response searchPeopleResponse;
    private static final String DEPARTMENT = "department";
    private static final String SKILL = "skill";
    private static final String ALL = "*";

    public PeopleSearchStepdefs(LoginStepdefs loginSteps, TestContext context) {
        testContext = context;
        userLoginSteps = loginSteps;
    }

    @When("^admin searches people by the created person's username$")
    public void adminSearchesPeopleByTheCreatedPersonSUsername() {
        Response createPersonResponse = testContext.<Response>getValue(TestContextKeys.CREATE_PERSON_RESPONSE);
        Person person = createPersonResponse.as(Person.class);
        searchPeopleResponse = PeopleUtils.searchPeopleByName(person.getJive().getUsername());
    }

    @Then("^person is found successfully$")
    public void personIsFoundSuccessfully() {
        searchPeopleResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Admin searches people by the created person's partially name query (.*)$")
    public void adminSearchesPeopleByTheCreatedPersonSUsername(String searchQuery) {
        Person person = FeatureRegistry.getCurrentFeature().getData(Person.class, searchQuery);
        PersonEntities personEntity = PeopleUtils.searchPeople(null, searchQuery, null);
        FeatureRegistry.getCurrentFeature().setData(Integer.class, searchQuery, personEntity.getList().size());
    }

    @Then("^Person name (.*) is searched successfully$")
    public void personIsSearchSuccessfully(String searchQuery) {
        int user = FeatureRegistry.getCurrentFeature().getData(Integer.class, searchQuery);
        assertTrue(user > 0);
    }

    @When("^User search person (\\S+)(?: )?(?:-)?(?: )?(include deactivated person)?$")
    public void userSearcheActivePeople(String userTag, String includeDisabled) {
        Person person = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        FilterBuilder filter = new FilterBuilder();

        if (includeDisabled != null) {
            filter.addCondition(SearchFilterConstants.INCLUDE_DISABLED, (Boolean.toString(true)));
        }

        filter.addCondition(SearchFilterConstants.searchFilterKey, person.getJive().getUsername());
        searchPeopleResponse = SearchUtils.searchPeople(
                filter.buildList(),
                SearchFilterConstants.searchFilterKey
        );
    }

    @When("^User search all person(?: )?(?:by)?(?: )?(department|skill)?(?: )?(\\S+)?(?: )?(?:-)?(?: )?(include deactivated person)?$")
    public void userSearcheAllPeople(String searchField, String searchValue, String includeDisabled) {
        FilterBuilder filter = new FilterBuilder();
        String randomSeed = FeatureRegistry.getCurrentFeature().getData(String.class, TestContextKeys.RANDOM_SEED);

        if (includeDisabled != null) {
            filter.addCondition(SearchFilterConstants.INCLUDE_DISABLED, (Boolean.toString(true)));
        }

        if (searchField != null) {
            if (searchField.equals(SKILL)) {
                filter.addCondition(
                        SearchFilterConstants.tagFilterKey,
                        PeopleStepdefs.replaceRandomSeed(searchValue, randomSeed).toLowerCase()
                );
                filter.addCondition(SearchFilterConstants.searchFilterKey, ALL);
            } else {
                filter.addCondition(
                        SearchFilterConstants.searchFilterKey,
                        PeopleStepdefs.replaceRandomSeed(searchValue, randomSeed)
                );
            }
        } else {
            filter.addCondition(SearchFilterConstants.searchFilterKey, randomSeed);
        }

        searchPeopleResponse = SearchUtils.searchPeople(
                filter.buildList(),
                SearchFilterConstants.searchFilterKey
        );
    }

    @Then("^Search results (not)?(?: )?include person (\\S+)$")
    public void verifySearchResults(String includePerson, String userTag) {
        searchPeopleResponse.then().assertThat().statusCode(SC_OK);
        Person person = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        PersonEntities personList = searchPeopleResponse.as(PersonEntities.class);
        boolean assertResult = personList
                .getList()
                .stream()
                .anyMatch(item ->
                                person.getJive().getUsername().toUpperCase().equals(item.getJive().getUsername().toUpperCase()));

        if (includePerson != null) {
            assertFalse(
                    ValidationConstants.ERROR_SEARCHING + EntityConstants.PERSON,
                    assertResult
            );
        } else {
            assertTrue(
                    ValidationConstants.ERROR_SEARCHING + EntityConstants.PERSON,
                    assertResult
            );
        }
    }

    @Then("^Search results (not)?(?: )?include (department|skill) (\\S+)$")
    public void verifySearchResultsByDeparment(String notIncludePerson, String searchField, String searchValue) {
        String randomSeed = FeatureRegistry.getCurrentFeature().getData(String.class, TestContextKeys.RANDOM_SEED);
        PersonEntities personList = searchPeopleResponse.as(PersonEntities.class);
        boolean assertResult = false;
        String assertOption = DEPARTMENT;

        searchPeopleResponse.then().assertThat().statusCode(SC_OK);

        for (Person item : personList.getList()) {
            if (assertResult){ break; }
            if (searchField.equals(DEPARTMENT)){
                String department = null;
                if (item.getJive().getProfile() != null) {
                    for (Profile profile : item.getJive().getProfile()) {
                        if (profile.getJiveLabel().toUpperCase().equals(DEPARTMENT.toUpperCase())) {
                            department = profile.getValue().toUpperCase();
                        }
                        if (PeopleStepdefs.replaceRandomSeed(searchValue, randomSeed).toUpperCase().equals(department)) {
                            assertResult = true;
                            break;
                        }
                    }
                }
            } else {
                assertResult = item.getTags().contains(PeopleStepdefs.replaceRandomSeed(searchValue, randomSeed).toLowerCase());
                assertOption = SKILL;
            }
        }

        if (notIncludePerson != null) {
            assertFalse(
                    ValidationConstants.ERROR_SEARCHING + assertOption,
                    assertResult
            );
        } else {
            assertTrue(
                    ValidationConstants.ERROR_SEARCHING + assertOption,
                    assertResult
            );
        }
    }
}
