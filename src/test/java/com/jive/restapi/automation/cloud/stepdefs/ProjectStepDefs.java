package com.jive.restapi.automation.cloud.stepdefs;

import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.CommonApiUtils;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.PlaceConstants;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.ProjectUtils;
import com.jive.restapi.generated.person.models.Place;
import com.jive.restapi.generated.person.models.Settings;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

public class ProjectStepDefs {

    private Response openProjectCreationResponse;


    @When("^Request to create an Project (.*)$")
    public void adminRequestToCreateOpenProject(String projectTag) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        openProjectCreationResponse = ProjectUtils.createProject(openGroupCreationResponse.as(Place.class));
        FeatureRegistry.getCurrentFeature().setData(Place.class, projectTag, openProjectCreationResponse.as(Place.class));
    }

    @When("^User requests to create a Project (\\S+) under space (\\S+)$")
    public void userRequestToCreateProjectUnderPlace(String projectTag, String placeTag) {
        Place placeData = PlaceConstants.getDefaultSpaceData();
        Response creationResponse = PlacesUtils.createPlace(placeData);
        FeatureRegistry.getCurrentFeature().setData(Place.class, placeTag, creationResponse.as(Place.class));
        Place projectData = PlaceConstants.getDefaultProjectData(creationResponse.as(Place.class));
        openProjectCreationResponse = ProjectUtils.createProject(projectData);
        FeatureRegistry.getCurrentFeature().setData(Place.class, projectTag, openProjectCreationResponse.as(Place.class));
    }

    @Then("Project (.*) is Created Successfully")
    public void adminVerifyOpenProjectCreated(String projectTag) {
        openProjectCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Enable Overview tab visibility for project (.*)$")
    public void enableOverviewTabVisibility(String projectTag) throws InterruptedException {
        Place projectData = FeatureRegistry.getCurrentFeature().getData(Place.class, projectTag);
        Response settingResponse = PlacesUtils.getPlaceSetting(projectData);
        Settings settingData = settingResponse.as(Settings.class);
        settingData.setOverviewTab(true);
        Response editSettingResponse = PlacesUtils.editPlaceSetting(projectData, settingData);
        editSettingResponse.then().assertThat().statusCode(SC_OK);
        // Adding 2 sec wait as next API request needs to read above action data
        Thread.sleep(TimeoutConstants.XS);
    }

    @When("^Create Project (.*) In Place (.*)$")
    public void createProjectInPlace(String projectTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        openProjectCreationResponse = ProjectUtils.createProject(place);
        FeatureRegistry.getCurrentFeature().setData(Place.class, projectTag, openProjectCreationResponse.as(Place.class));
        assertTrue(String.format(ValidationConstants.PLACE_PARENT, place.getParent()),
                openProjectCreationResponse.as(Place.class)
                .getParent().contains(CommonApiUtils.getParentPlaceUri(place)));
    }
}
