package com.jive.restapi.automation.cloud.stepdefs;

import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.*;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.generated.person.models.*;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PollStepDefs {

    private Response pollUnderPlaceCreationResponse;
    private Response pollCreationResponse;
    private InboxEntry inbox;
    private Response commentCreationResponse;
    private Content pollSearchResponse;

    @When("Request to create an Poll Under Place")
    public void adminRequestToCreatePollUnderPlace() {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        pollUnderPlaceCreationResponse = PollUtils.createPollUnderPlace(ContentConstants.getDefaultPollDataUnderPlace(openGroupCreationResponse.as(Place.class)));
    }

    @Then("Poll is Created Successfully Under Place")
    public void adminVerifyPollUnderPlace() {
        pollUnderPlaceCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("Request to create a Poll (.*)")
    public void adminRequestToCreatePoll(String pollTag) {
        pollCreationResponse = PollUtils.createPoll(ContentConstants.getDefaultPollData());
        FeatureRegistry.getCurrentFeature().setData(Poll.class, pollTag, pollCreationResponse.as(Poll.class));
    }

    @Then("Poll is Created Successfully")
    public void adminVerifyCreatePoll() {
        pollCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Logged in user requests to create a Poll (.*) with tags (.*)$")
    public void userCreatesAPoll(String pollTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Content pollData = ContentConstants.getDefaultPollData();
        pollData.setTags(tagList);
        pollCreationResponse = PollUtils.createPoll(pollData);
        FeatureRegistry.getCurrentFeature().setData(Poll.class, pollTag, pollCreationResponse.as(Poll.class));
        FeatureRegistry.getCurrentFeature().setData(Content.class, pollTag, pollCreationResponse.as(Content.class));
    }

    @Then("^Poll (.*) is created successfully$")
    public void pollIsCreatedSuccessfully(String pollTag) {
        Poll PollData = FeatureRegistry.getCurrentFeature().getData(Poll.class, pollTag);
        Response createDocumentResponse = DocumentUtils.getResponseOfDocumentById(PollData.getContentID());
        createDocumentResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("Request to create an Poll Under Place With Description (.*)")
    public void adminRequestToCreatePollUnderPlaceWithDescription(String description) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Content pollData = ContentConstants.getDefaultPollData();
        pollData.content((new ContentBody()).text(description));
        pollData.parent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class))).visibility(Content.VisibilityEnum.PLACE);
        pollUnderPlaceCreationResponse = PollUtils.createPoll(pollData);
    }

    @When("Request to create an Poll With Description (.*)")
    public void adminRequestToCreatePollWithDescription(String description) {
        Content pollData = ContentConstants.getDefaultPollData();
        pollData.content((new ContentBody()).text(description));
        pollCreationResponse = PollUtils.createPoll(pollData);
    }

    @When("Logged in user Like poll (.*)")
    public void likeDocument(String pollTag) {
        Poll pollData = FeatureRegistry.getCurrentFeature().getData(Poll.class, pollTag);
        Response like = ContentUtil.contentLikes(pollData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("Logged in user remove Like added on poll (.*)")
    public void removeLikeOnPoll(String pollTag) {
        Poll pollData = FeatureRegistry.getCurrentFeature().getData(Poll.class, pollTag);
        Response like = ContentUtil.deleteContentLike(pollData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("User request all unread messages for Poll")
    public void userRequestUnreadMessages() {
        Response response = null;

        FilterBuilder filter = new FilterBuilder();
        filter.addCondition("unread", null);

        for (int counter = 0; counter < 15; counter++) {
            response = InboxUtils.readInboxAsResponse(filter);
            response.then().assertThat().statusCode(SC_OK);
            InboxEntry inboxEntry = response.as(InboxEntry.class);
            if (inboxEntry.getUnread() > 0) {
                break;
            }

            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        inbox = response.as(InboxEntry.class);
    }

    @Then("^Notification of Poll ([a-zA-Z]+) is present in inbox")
    public void verifyNotificationHasDiscussion(String pollTag) {
        Poll pollData = FeatureRegistry.getCurrentFeature().getData(Poll.class, pollTag);
        assertNotNull(pollData.getSubject(), inbox.getUnread());
    }

    @When("^User request to create an Poll (.*) under place (.*)$")
    public void userRequestToCreatePollUnderPlace(String pollTag, String placeTag) {
        Place placeData = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Content pollData = ContentConstants.getDefaultPollData();
        pollData.parent(CloudCommonUtils.getParentPlaceUri(placeData)).visibility(Content.VisibilityEnum.PLACE);
        pollUnderPlaceCreationResponse = PollUtils.createPoll(pollData);
        FeatureRegistry.getCurrentFeature().setData(Poll.class, pollTag, pollUnderPlaceCreationResponse.as(Poll.class));
    }

    @When("^Add a comment (.*) to the poll (.*)$")
    public void addCommentToDocument(String commentTag, String pollTag) {
        Poll poll = FeatureRegistry.getCurrentFeature().getData(Poll.class, pollTag);
        commentCreationResponse = CommentUtils.createComment(ContentConstants.getDefaultCommentData(), poll);
        FeatureRegistry.getCurrentFeature().setData(Comment.class, commentTag, commentCreationResponse.as(Comment.class));
    }

    @Then("^Comment has been added to the poll successfully$")
    public void commentAddedSuccessfully() {
        commentCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^User request to create an Poll (.*) with extended author (.*)$")
    public void userRequestToCreatePollWithExtendedAuthor(String pollTag, String personTag) {
        List<Person> personData = new ArrayList<>(1);
        personData.add(FeatureRegistry.getCurrentFeature().getData(Person.class, personTag));
        Content pollData = ContentConstants.getDefaultPollData();
        pollData.setExtendedAuthors(personData);
        pollUnderPlaceCreationResponse = PollUtils.createPoll(pollData);
        FeatureRegistry.getCurrentFeature().setData(Poll.class, pollTag, pollUnderPlaceCreationResponse.as(Poll.class));
    }

    @When("^User requests to create a draft Poll (\\S+)$")
    public void createDraftPoll(String pollTag) {
        Poll poll = ContentConstants.getDefaultPollData();
        poll.setStatus(Content.StatusEnum.INCOMPLETE);
        pollCreationResponse = PollUtils.createPoll(poll);
        FeatureRegistry.getCurrentFeature().setData(Poll.class, pollTag, pollCreationResponse.as(Poll.class));
    }

    @When("^User request to search poll (.*) by (.*)$")
    public void searchPoll(String pollTag, String pollSearchTag) {
        Poll pollDate = FeatureRegistry.getCurrentFeature().getData(Poll.class, pollTag);
        pollSearchResponse = PollUtils.getPollById(pollDate.getContentID());
        FeatureRegistry.getCurrentFeature().setData(Content.class, pollSearchTag, pollSearchResponse);
    }

    @Then("^It should returns poll (.*)$")
    public void pollSearchResult(String pollSearchTag) {
        Content pollData = FeatureRegistry.getCurrentFeature().getData(Content.class, pollSearchTag);
        assertNotNull(ValidationConstants.searchResults, pollData.getContentID());
    }
}
