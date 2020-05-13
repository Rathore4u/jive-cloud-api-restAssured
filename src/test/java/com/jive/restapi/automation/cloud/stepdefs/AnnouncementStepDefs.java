package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.configs.AppConfig;
import com.jive.restapi.automation.cloud.configs.Role;
import com.jive.restapi.automation.cloud.configs.User;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.LoginStepdefs;
import com.jive.restapi.automation.utilities.AnnouncementUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.PlaceConstants;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.generated.person.models.Announcement;
import com.jive.restapi.generated.person.models.AnnouncementEntities;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Place;
import com.xo.restapi.automation.actions.UserActionUtils;
import com.xo.restapi.automation.context.UserContext;
import com.xo.restapi.automation.context.UserData;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;

public class AnnouncementStepDefs {
    private final LoginStepdefs loginStepdefs;
    private final PlacesStepDefs placesStepDefs;
    private UserData userData = new UserData();
    private Response announcementResponse;
    private Response searchResponse;

    public AnnouncementStepDefs(LoginStepdefs loginStepdefs, PlacesStepDefs placesStepDefs) {
        this.loginStepdefs = loginStepdefs;
        this.placesStepDefs = placesStepDefs;
    }

    @Given("^Space ([a-zA-Z]+) has been created")
    @When("^Admin creates Space ([a-zA-Z]+)")
    public void adminRequestToCreateASpace(String spaceTag) {
        Place space = new Place();
        String random = RandomStringUtils.randomAlphanumeric(10);
        space.name(random).
                description("Test Description " + random).
                type(PlaceTypes.space.toString()).
                displayName(random);
        Response createPlaceResponse = PlacesUtils.createPlace(space);
        FeatureRegistry.getCurrentFeature().setData(Place.class, spaceTag, createPlaceResponse.as(Place.class));
    }

    @When("^Admin creates a system announcement (\\S+)")
    public void createSystemAnnouncement(String announcementTag) {
        Announcement announcement = new Announcement();
        String random = RandomStringUtils.randomAlphanumeric(10);
        ContentBody content = new ContentBody();
        announcement
                .subject(random)
                .type("announcement")
                .content(content.
                        text("Test Description " + random).
                        type("text/html"));
        announcementResponse = AnnouncementUtils.createSystemAnnouncement(announcement);
        announcementResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Announcement.class, announcementTag, announcementResponse.as(Announcement.class));
    }

    @When("^(\\S+) creates an announcement (\\S+) in (\\S+)")
    public void createAnnouncementInPlace(String userRole, String announcementTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        User userCredentials = AppConfig.getInstance().getRole(Role.valueOf(userRole));
        userData.addUser(userCredentials.getUsername(), userCredentials.getPassword());
        UserContext.setUserData(userData);
        UserActionUtils.perform(userCredentials.getUsername(), UserContext::getUser);
        announcementResponse = AnnouncementUtils.createAnnouncementUnderPlace(place.getPlaceID());
        FeatureRegistry.getCurrentFeature().setData(Announcement.class, announcementTag, announcementResponse.as(Announcement.class));
    }

    @When("^User add an announcement (\\S+) in (?:group|space|project) (\\S+)$")
    public void addAnnouncementInGroup(String announcementTag, String groupTag, DataTable announcementValues) {
        List<Map<String, String>> data = announcementValues.asMaps(String.class, String.class);
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        Announcement announcement = PlaceConstants.getAnnouncementData();
        announcement.setSubjectURI(data.get(0).get("blogLink"));
        announcement.setContent(new ContentBody().text(data.get(0).get("description")));
        announcementResponse = AnnouncementUtils.createAnnouncementUnderPlaceWithData(group.getPlaceID(), announcement);
        announcementResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Announcement.class, announcementTag, announcementResponse.as(Announcement.class));
    }

    @Then("^Announcement is (created|updated) successfully")
    public void verifyAnnouncementProccesedSuccesully(String typeRequest) {
        int statusCode = "created".equals(typeRequest) ? HttpStatus.SC_CREATED : HttpStatus.SC_OK;
        announcementResponse.then().assertThat().statusCode(statusCode);
    }

    @Then("^Announcement ([a-zA-Z]+) is visible to regular user")
    public void announcementIsDisplayed(String announcementTag) {
        Announcement announcement = FeatureRegistry.getCurrentFeature().getData(Announcement.class, announcementTag);
        Response response = AnnouncementUtils.getAnnouncement(announcement.getId());
        response.then().assertThat().statusCode(SC_OK);
    }

    @When("^Logged in user updates announcement ([a-zA-Z]+)")
    public void updateAnnouncement(String announcementTag) {
        Announcement announcement = FeatureRegistry.getCurrentFeature().getData(Announcement.class, announcementTag);
        Announcement updatedAnnouncement = announcement;
        updatedAnnouncement.subject(announcement.getSubject() + " - updated");
        announcementResponse = AnnouncementUtils.updateAnnouncement(announcement.getId(), updatedAnnouncement);
    }

    @When("^Logged in user expires announcement ([a-zA-Z]+)")
    public void expireAnnouncement(String announcementTag) {
        Announcement announcement = FeatureRegistry.getCurrentFeature().getData(Announcement.class, announcementTag);
        Announcement updatedAnnouncement = announcement;
        updatedAnnouncement.status("expired");
        announcementResponse = AnnouncementUtils.updateAnnouncement(announcement.getId(), updatedAnnouncement);
    }

    @When("^Create an announcement (\\S+) in (group|project|space) (\\S+) with link to Blog (\\S+)$")
    public void addAnnouncementInGroupBlogLink(String announcementTag,
                                               String placeType,
                                               String placeTag,
                                               String blogTag,
                                               DataTable announcementValues) {
        BlogPostStepDefs blogPostStepDefs = new BlogPostStepDefs();

        switch (placeType) {
            case "space":
                // Space can only be created by admins
                loginStepdefs.executeAs(Role.ADMIN, () -> placesStepDefs.userRequestCreateSpace(placeTag));
                break;
            case "project":
                ProjectStepDefs projectStepDefs = new ProjectStepDefs();
                projectStepDefs.adminRequestToCreateOpenProject(placeTag);
                break;
            default:
                GroupStepDefs groupStepDefs = new GroupStepDefs();
                groupStepDefs.userCreateGroup("open", placeTag);
                break;
        }

        blogPostStepDefs.adminRequestToCreateBlogPostUnderPlace(blogTag, placeTag);
        addAnnouncementInGroup(announcementTag, placeTag, announcementValues);
    }

    @When("^User request to search announcement (\\S+) from (main|spotlight)$")
    public void searchAnnouncementBySubject(String announcementTag, String origin) {
        Announcement announcement = FeatureRegistry.getCurrentFeature().getData(Announcement.class, announcementTag);
        searchResponse = SearchUtils.searchContent(
                FilterBuilder
                        .builder()
                        .addCondition(SearchFilterConstants.searchFilterKey, announcement.getSubject())
                        .addCondition(SearchFilterConstants.typeFilterKey, SearchFilterConstants.announcementFilterKey)
                        .buildList(), origin);
    }

    @Then("^Search results include announcement (.*)$")
    public void announcementIsSearched(String announcementTag, DataTable announcementValues) {
        List<Map<String, String>> data = announcementValues.asMaps(String.class, String.class);
        Announcement announcementData = FeatureRegistry.getCurrentFeature().getData(Announcement.class, announcementTag);
        assertTrue(searchResponse.as(AnnouncementEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(announcementData.getSubject())
                        && c.getSubjectURI().equals(announcementData.getSubjectURI())
                        && checkSearchResults(c.getContent().getText(), data)));
    }

    @When("^User request to update announcement (\\S+)$")
    public void userRequestToUpdateAnnouncement(String announcementTag, DataTable announcementValues) {
        List<Map<String, String>> data = announcementValues.asMaps(String.class, String.class);
        Announcement announcementData = FeatureRegistry.getCurrentFeature().getData(Announcement.class, announcementTag);
        announcementData.setContent(new ContentBody().text(data.get(0).get("description")));
        announcementResponse = AnnouncementUtils.updateAnnouncement(announcementData.getId(), announcementData);
        announcementResponse.then().assertThat().statusCode(SC_OK);
    }

    private static boolean checkSearchResults(String key, List<Map<String, String>> values) {
        return values.stream().allMatch(c -> key.contains(c.get("description")));
    }
}
