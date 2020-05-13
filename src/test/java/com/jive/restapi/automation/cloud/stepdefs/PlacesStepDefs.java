package com.jive.restapi.automation.cloud.stepdefs;

import static com.jive.restapi.automation.cloud.data.TestContextKeys.CREATE_PLACE_RESPONSE;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.configs.Role;
import com.jive.restapi.automation.cloud.data.GroupTypes;
import com.jive.restapi.automation.cloud.data.GroupTypesNew;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.PlacesConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.LoginStepdefs;
import com.jive.restapi.automation.utilities.CommonApiUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.PlaceConstants;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.ProjectUtils;
import com.jive.restapi.automation.utilities.v3.PlacesUtil;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.jive.restapi.generated.person.models.Settings;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import com.xo.restapi.automation.context.TestContext;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;

public class PlacesStepDefs {

    private final LoginStepdefs userLoginSteps;
    private TestContext testContext;
    private Response createPlaceResponse;
    private Place searchedPlace;
    private Response searchResponse;
    private Response updatePlaceResp;
    private Place spaceData;
    private Response deleteSubSpaceResp;

    public PlacesStepDefs(LoginStepdefs loginSteps, TestContext context) {
        testContext = context;
        userLoginSteps = loginSteps;
    }

    @When("^(?:admin|User) requests to create a space (.*)$")
    public void userRequestCreateSpace(String placeTag) {
        Place place = new Place();
        place.setName(RandomStringUtils.randomAlphanumeric(10));
        place.setDescription("Test Description " + RandomStringUtils.randomAlphanumeric(30));
        place.setType(PlaceTypes.space.toString());
        place.setDisplayName(RandomStringUtils.randomAlphanumeric(10));
        Place space = PlaceConstants.getDefaultSpaceData();
        // Aleays create place as admin
        userLoginSteps.executeAs(Role.ADMIN, () -> createPlaceResponse = PlacesUtils.createPlace(space));

        createPlaceResponse.then().log().all().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Place.class, placeTag, createPlaceResponse.as(Place.class));
    }

    @Then("space is created successfully")
    public void verifySpaceCreated() {
        createPlaceResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("admin requests to create a project (.*)")
    public void adminRequestToCreateAProject(String projectTag) {
        Response openGroup = GroupUtils.createOpenGroup();
        Place openGroupPlace = openGroup.as(Place.class);
        createPlaceResponse = ProjectUtils.createProject(openGroupPlace);
        testContext.addValue(CREATE_PLACE_RESPONSE, createPlaceResponse);
        FeatureRegistry.getCurrentFeature().setData(Place.class, projectTag, createPlaceResponse.as(Place.class));
    }

    @Then("project is created successfully")
    public void verifyProjectCreated() {
        createPlaceResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^(?:admin|User) requests to create a group (\\S+)$")
    public void adminRequestToCreateAGroup(String groupTag) {
        createPlaceResponse = GroupUtils.createOpenGroup();
        testContext.addValue(CREATE_PLACE_RESPONSE, createPlaceResponse);
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, createPlaceResponse.as(Place.class));
    }

    @Then("group is created successfully")
    public void verifyGroupCreated() {
        createPlaceResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @Given("^User requests to create a group (.*) in (.*)")
    public void createAGroupInPlace(String groupTag, String spaceTag) {
        Place space = FeatureRegistry.getCurrentFeature().getData(Place.class, spaceTag);
        createPlaceResponse = GroupUtils.createGroupUnderPlace(space);
        testContext.addValue(CREATE_PLACE_RESPONSE, createPlaceResponse);
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, createPlaceResponse.as(Place.class));
    }

    @When("^admin request to search (?:space|group|project) (.*)$")
    public void adminRequestSearchPlace(String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        searchedPlace = PlacesUtils.getPlaceById(place.getPlaceID());
    }

    @Then("^It should returns (?:space|group|project) (.*)$")
    public void adminVerifySearchedPlace(String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        assertEquals(place.getName(), searchedPlace.getName());
    }

    @When("User requests to create a project (.*) under place (.*)")
    public void requestToCreateAProjectUnderPlace(String projectTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        createPlaceResponse = ProjectUtils.createProject(place);
        testContext.addValue(CREATE_PLACE_RESPONSE, createPlaceResponse);
        FeatureRegistry.getCurrentFeature().setData(Place.class, projectTag, createPlaceResponse.as(Place.class));
    }

    @When("User requests to create a private group (.*)")
    public void createPrivateGroup(String groupTag) {
        createPlaceResponse = GroupUtils.createPrivateGroup();
        testContext.addValue(CREATE_PLACE_RESPONSE, createPlaceResponse);
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, createPlaceResponse.as(Place.class));
    }

    @When("User requests to create a private unlisted group (.*)")
    public void createPrivateUnlistedGroup(String groupTag) {
        String displayName = "regular-" + RandomStringUtils.randomAlphanumeric(8);
        Group group = new Group();
        group.setName(displayName);
        group.setDescription(displayName);
        group.setDisplayName(displayName);
        group.setType(PlaceTypes.group.toString());
        group.setGroupType(GroupTypes.PRIVATE.toString());
        group.setGroupTypeV2(GroupTypesNew.PRIVATE_UNLISTED.toString());
        createPlaceResponse = PlacesUtils.createPlace(group);
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, createPlaceResponse.as(Place.class));
    }

    @When("^(?:Admin|User) requests to create a subSpace (.*) under space (.*)$")
    public void adminRequestToCreateASpace(String subSpaceTag, String spaceTag) {
        Place space = FeatureRegistry.getCurrentFeature().getData(Place.class, spaceTag);
        Place place = new Place();
        place.name(RandomStringUtils.randomAlphanumeric(10))
                .description("Test Description " + RandomStringUtils.randomAlphanumeric(30))
                .type(PlaceTypes.space.toString())
                .displayName(RandomStringUtils.randomAlphanumeric(10))
                .setParent(CloudCommonUtils.getParentPlaceUri(space));
        createPlaceResponse = PlacesUtils.createPlace(place);
        createPlaceResponse.then().assertThat().statusCode(SC_CREATED);
        createPlaceResponse.then().log().all();
        FeatureRegistry.getCurrentFeature().setData(Place.class, subSpaceTag, createPlaceResponse.as(Place.class));
    }

    @When("User requests to create a public restricted group (.*)")
    public void createPublicRestrictedGroup(String groupTag) {
        createPlaceResponse = GroupUtils.createGroup("MEMBER ONLY");
        testContext.addValue(CREATE_PLACE_RESPONSE, createPlaceResponse);
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, createPlaceResponse.as(Place.class));
    }

    @When("^User requests to create a personal Blog (\\S+) for user (.*)$")
    public void userRequestToBlog(String blogTag, String userTag) {
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        Place blog = PlaceConstants.getDefaultBlogData();
        blog.setParent(CloudCommonUtils.getPersonUri(user));
        createPlaceResponse = PlacesUtils.createPlace(blog);
        createPlaceResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Place.class, blogTag, createPlaceResponse.as(Place.class));
    }

    @Then("^Verify place (.*) default view as overview tab$")
    public void overviewTabSettingEnabled(String groupTag) {
        Place groupData = FeatureRegistry.getCurrentFeature().getData(Place.class, groupTag);
        Response settingResponse = PlacesUtils.getPlaceSetting(groupData);
        Settings settingData = settingResponse.as(Settings.class);
        assertEquals(PlacesConstants.OVERVIEW_TAB_DEFAULT,
                PlacesConstants.OVERVIEW_TAB_TEXT, settingData.getDefaultTab());
    }

    @When("User requests to get contents by tag (.*) from place (.*)")
    public void getDiscussionByOutcomeFromPlace(String tag, String placeTag)
            throws InterruptedException {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = PlacesUtils.getContentWithFilters(place.getPlaceID(),
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.TAG_FILTER_KEY, tag)
                            .buildList());
            if (searchResponse.as(ContentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^Verify count of contents by tag is (\\d+)$")
    public void verifyContentByTagCount(int expectedCount) {
        Assert.assertEquals(ValidationConstants.CONTENT_COUNT_MISMATCH,
                searchResponse.as(ContentEntities.class).getList().size(), expectedCount);
    }

    @Then("^SubSpace (.*) has (.*) as Parent$")
    public void subSpaceHasParent(String subspacePlace, String parentPlace) throws Throwable {
        Place subspace = FeatureRegistry.getCurrentFeature().getData(Place.class, subspacePlace);
        Place parent = FeatureRegistry.getCurrentFeature().getData(Place.class, parentPlace);
        assertTrue(String.format(
                ValidationConstants.SUB_SPACE_PARENT, subspace.getParent()),
                subspace.getParent().contains(CommonApiUtils.getParentPlaceUri(parent)));
    }

    @When("^Admin Updates SubSpace (.*)")
    public void adminUpdatesSubSpace(String subSpaceTag) throws Throwable {
        Place subspace = FeatureRegistry.getCurrentFeature().getData(Place.class, subSpaceTag);
        spaceData = PlaceConstants.getDefaultSpaceData();
        updatePlaceResp = PlacesUtil.updatePlace(subspace.getPlaceID(), spaceData);
        FeatureRegistry.getCurrentFeature().setData(Place.class, subSpaceTag,
                updatePlaceResp.as(Place.class));
        updatePlaceResp.then().assertThat().statusCode(SC_OK);
    }

    @Then("^SubSpace (.*) Updated Successfully$")
    public void subSpaceUpdatedSuccessfully(String subspacePlace) throws Throwable {
        Place subspace = FeatureRegistry.getCurrentFeature().getData(Place.class, subspacePlace);
        assertTrue(String.format(
                ValidationConstants.SUB_SPACE_NAME, subspace.getName())
                , subspace.getName().contains(spaceData.getName()));
    }

    @When("^Admin Deletes SubSpace (.*)")
    public void deleteSubSpace(String subspacePlace) throws Throwable {
        Place subspace = FeatureRegistry.getCurrentFeature().getData(Place.class, subspacePlace);
        deleteSubSpaceResp = PlacesUtil.deletePlace(subspace.getPlaceID());
    }

    @Then("^SubSpace is deleted successfully$")
    public void subspaceSubSpaceIsDeletedSuccessfully() throws Throwable {
        deleteSubSpaceResp.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("User requests to create a open public group (.*)")
    public void createOpenGroup(String groupTag) {
        createPlaceResponse = GroupUtils.createOpenGroup();
        testContext.addValue(CREATE_PLACE_RESPONSE, createPlaceResponse);
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, createPlaceResponse.as(Place.class));
        assertNotNull(ValidationConstants.GROUP_DISPLAY_NAME, createPlaceResponse.as(Place.class).getDisplayName());
    }

    @When("User requests to create a externally accessible group (.*)")
    public void createExternallyAccessibleGroup(String groupTag) {
        createPlaceResponse = GroupUtils.createOpenGroup();
        createPlaceResponse.as(Group.class).visibleToExternalContributors(true);
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, createPlaceResponse.as(Place.class));
        assertNotNull(ValidationConstants.GROUP_DISPLAY_NAME, createPlaceResponse.as(Place.class).getDisplayName());
    }
}

enum PlaceTypes {
    space,
    project,
    group,
    blog
}
