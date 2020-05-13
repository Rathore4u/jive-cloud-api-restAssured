package com.jive.restapi.automation.cloud.stepdefs;

import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertHttpResponseEquals;
import static com.xo.restapi.automation.configs.CloudCommonUtils.getBaseUrl;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;

import com.jive.restapi.automation.cloud.data.PlacesConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.data.ContentConsts;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.IdeaConstants;
import com.jive.restapi.automation.utilities.IdeaUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.automation.utilities.v3.ImagesUtil;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.DocumentEntities;
import com.jive.restapi.generated.person.models.Idea;
import com.jive.restapi.generated.person.models.Image;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Assert;

public class IdeaStepDefs {

    private Response creationResponse;
    private Response searchResponse;
    private Response ideaCreationResponse;
    private Response ideaUnderPlaceCreationResponse;
    private Response imageCreationResponse;

    @When("Request to create an idea (.*) Under Place")
    public void adminRequestToCreateIdeaUnderPlace(String ideaName) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Content ideaData = ContentConstants.getDefaultIdeaDataUnderPlace(openGroupCreationResponse.as(Place.class));
        ideaData.setSubject(ideaName);
        creationResponse = IdeaUtils.createIdea(ideaData);
    }

    @Then("^Idea is Created Successfully(?: )?(?:Under Place)?$")
    public void userVerifyContentCreatedSuccesfully() {
        creationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("Request to create an (.*) idea")
    public void adminRequestToCreateIdea(String ideaName) {
        Idea ideaData = ContentConstants.getDefaultIdeaData();
        ideaData.setSubject(ideaName);
        creationResponse = IdeaUtils.createIdea(ideaData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, ideaName, creationResponse.as(Content.class));
    }

    @When("^Logged in user requests to create idea (.*) with tags (.*)$")
    public void userCreatesAIdeaWithTags(String ideaTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Idea ideaData = ContentConstants.getDefaultIdeaData();
        ideaData.setTags(tagList);
        ideaCreationResponse = IdeaUtils.createIdeaToReturnResponse(ideaData);
        FeatureRegistry.getCurrentFeature().setData(String.class, tag, tag);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, ideaCreationResponse.as(Idea.class));
    }

    @Then("^Idea (.*) is created successfully$")
    public void ideaIsCreatedSuccessfully(String ideaTag) {
        ideaCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Logged in user requests to update idea (.*) with tags (.*)$")
    public void userUpdatesAIdea(String ideaTag, String tag) {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        ideaData.setTags(tagList);
        creationResponse = IdeaUtils.updateIdeaToReturnResponse(ideaData);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, creationResponse.as(Idea.class));
    }

    @Then("^Idea (.*) is updated with tags (.*) successfully$")
    public void ideaIsUpdatedSuccessfully(String ideaTag, String tag) {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Assert.assertTrue(ideaData.getTags().contains(tag));
    }

    @When("User request to search idea (.*) by subject from (.*)")
    public void searchByIdeaSubject(String ideaTag, String origin) throws InterruptedException {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        for (int counter = 0; counter < 15; counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition("search", ideaData.getSubject())
                            .buildList(), origin);
            if (searchResponse.as(DocumentEntities.class).getList().size() != 0) {
                break;
            }
            Thread.sleep(2000);
        }
    }

    @Then("^Idea (.*) is searched successfully$")
    public void ideaIsSearched(String ideaTag) {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        Assert.assertTrue(searchResponse.as(DocumentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(ideaData.getSubject())));
    }

    @When("User request to delete idea (.*)")
    public void deleteIdea(String ideaTag) {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        searchResponse = IdeaUtils.deleteIdea(ideaData.getContentID());
    }

    @Then("^Idea (.*) is deleted successfully$")
    public void ideaIsDeleted(String ideaTag) {
        FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        searchResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^User request to create an ([a-zA-Z0-9]+) idea in the ([a-zA-Z0-9]+) group")
    public void createIdeaInGroup(String ideaTag, String groupTag) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        FeatureRegistry.getCurrentFeature().setData(Place.class, groupTag, openGroupCreationResponse.as(Place.class));
        Idea ideaData = ContentConstants.getDefaultIdeaDataUnderPlace(openGroupCreationResponse.as(Place.class));
        ideaUnderPlaceCreationResponse = IdeaUtils.createIdea(ideaData);
        ideaUnderPlaceCreationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, ideaUnderPlaceCreationResponse.as(Idea.class));
    }

    @When("^User request to update idea ([a-zA-Z0-9]+) title as ([a-zA-Z0-9]+)")
    public void updateIdeaTitle(String ideaTag, String updatedIdeaTitle) {
        String updatedTitle = RandomStringUtils.randomAlphanumeric(10);
        FeatureRegistry.getCurrentFeature().setData(String.class, updatedIdeaTitle, updatedTitle);
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        ideaData.setSubject(updatedTitle);
        ideaCreationResponse = IdeaUtils.updateIdeaToReturnResponse(ideaData);
        ideaCreationResponse.then().assertThat().statusCode(SC_OK);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, ideaCreationResponse.as(Idea.class));
    }

    @Then("^Idea ([a-zA-Z0-9]+) should be updated with title ([a-zA-Z0-9]+)")
    public void ideaTitleShouldBeUpdated(String ideaTag, String updatedTitle) {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        String updatedIdeaTitle = FeatureRegistry.getCurrentFeature().getData(String.class, updatedTitle);
        Assert.assertTrue(ideaData.getSubject().contains(updatedIdeaTitle));
    }

    @Then("^Idea ([a-zA-Z0-9]+) is Created Successfully with ([a-zA-Z0-9]+)")
    public void verifyCreatedIdeaTag(String ideaDetails, String tagToVerify) {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaDetails);
        String tagExisting = FeatureRegistry.getCurrentFeature().getData(String.class, tagToVerify);
        Assert.assertTrue(ideaData.getTags().contains(tagExisting.toLowerCase()));
    }

    @When("^User adds a tag ([a-zA-Z0-9]+) to the existing Idea ([a-zA-Z0-9]+)")
    public void userAddsTagToIdeaCreated(String tagToAdd, String ideaDetails) {
        Idea ideaData = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaDetails);
        ideaData.addTagsItem(tagToAdd);
        ideaCreationResponse = IdeaUtils.updateIdeaToReturnResponse(ideaData);
        ideaCreationResponse.then().assertThat().statusCode(SC_OK);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaDetails, ideaCreationResponse.as(Idea.class));
        FeatureRegistry.getCurrentFeature().setData(String.class, tagToAdd, tagToAdd);
    }

    @When("^User requests to create idea (.*) with tags (.*)$")
    public void userCreatesIdeaWithSingleTag(String ideaTag, String tag) {
        String tagToAdd = RandomStringUtils.randomAlphabetic(5);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tagToAdd.split(",")));
        Idea ideaData = ContentConstants.getDefaultIdeaData();
        ideaData.setTags(tagList);
        ideaCreationResponse = IdeaUtils.createIdeaToReturnResponse(ideaData);
        ideaCreationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(String.class, tag, tagToAdd);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, ideaCreationResponse.as(Idea.class));
    }

    @When("Request to create an idea (.*) Under Place (.*)")
    public void createIdeaUnderPlace(String ideaTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Content ideaData = ContentConstants.getDefaultIdeaDataUnderPlace(place);
        creationResponse = IdeaUtils.createIdea(ideaData);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, creationResponse.as(Idea.class));
    }

    @When("^User requests to create a draft Idea (\\S+)$")
    public void createDraftIdea(String ideaTag) {
        Idea idea = ContentConstants.getDefaultIdeaData();
        idea.setStatus(Content.StatusEnum.INCOMPLETE);
        creationResponse = IdeaUtils.createIdea(idea);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, creationResponse.as(Idea.class));
    }

    @When("User requests to create an Idea (.*) with description (.*)")
    public void createIdeaWithDescription(String ideaTag, String description) {
        Content ideaData = ContentConstants.getDefaultIdeaData();
        ideaData.content((new ContentBody()).text(description));
        ideaCreationResponse = IdeaUtils.createIdea(ideaData);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, ideaCreationResponse.as(Idea.class));
    }

    @When("^User request to create an ([a-zA-Z0-9]+) idea with specific person (.*)$")
    public void createIdeaWithSpecificPerson(String ideaTag, String personTag) {
        Idea ideaData = ContentConstants.getDefaultIdeaData();
        List<Person> personList = new ArrayList<>(1);
        ideaData.setExtendedAuthors(personList);
        creationResponse = IdeaUtils.createIdea(ideaData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, creationResponse.as(Idea.class));
    }

    @When("^User request to create an (.*) idea with image from Web Under Place (.*)$")
    public void createIdeaWithImage(String ideaTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Content ideaData = new Idea().subject(RandomStringUtils.randomAlphanumeric(8)).type(Content.TypeEnum.IDEA)
                .parent(CloudCommonUtils.getParentPlaceUri(place))
                .visibility(Content.VisibilityEnum.PLACE)
                .content(new ContentBody().text(PlacesConstants.CONTENT_IMAGE_TAG));
        creationResponse = IdeaUtils.createIdea(ideaData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, creationResponse.as(Idea.class));
    }

    @When("^Upload an image (.*) using file (.*)$")
    public void uploadImage(String imageTag, String fileTag) {
        imageCreationResponse = ImagesUtil.uploadImage(null, Options.attachResource(PlacesConstants.FILE_RESOURCE_TYPE,
                fileTag, fileTag, PlacesConstants.CONTENT_TYPE_PNG));
        imageCreationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Image.class, imageTag, imageCreationResponse.as(Image.class));
    }

    @Then("^Image is uploaded successfully$")
    public void imageUploadedSuccessfully() {
        imageCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Create Idea (.*) with an image (.*) in RTE under place (.*)$")
    public void createIdeaWithImageFromFileUnderPlace(String ideaTag, String imageTag, String placeTag) {
        Image imageData = FeatureRegistry.getCurrentFeature().getData(Image.class, imageTag);
        Place placeData = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        val imageURL1 = String.format(ContentConsts.SHOW_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        val imageURL2 = String.format(ContentConsts.DOWNLOAD_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        val ideaData = IdeaConstants.getDefaultIdeaDataWithImageInRTE(imageURL1, imageURL2);
        ideaData.setParent(CloudCommonUtils.getParentPlaceUri(placeData));
        ideaData.setVisibility(Content.VisibilityEnum.PLACE);
        ideaCreationResponse = ContentUtil.createContent(ideaData, null);
        ideaCreationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, ideaCreationResponse.as(Idea.class));
    }

    @When("^User request to create an (.*) idea with Video from Web Under Place (.*)$")
    public void createIdeaWithVideo(String ideaTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Content ideaData = new Idea().subject(RandomStringUtils.randomAlphanumeric(8)).type(Content.TypeEnum.IDEA)
                .parent(CloudCommonUtils.getParentPlaceUri(place))
                .content(new ContentBody().text(PlacesConstants.EMBED_VIDEO_LINK));
        creationResponse = IdeaUtils.createIdea(ideaData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Idea.class, ideaTag, creationResponse.as(Idea.class));
    }

    @Then("^Verify Idea is created successfully$")
    public void ideaCreatedSuccessfully() {
        assertHttpResponseEquals(SC_CREATED, creationResponse.getStatusCode());
    }
}
