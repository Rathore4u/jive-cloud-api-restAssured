package com.jive.restapi.automation.cloud.stepdefs;

import static com.xo.restapi.automation.configs.CloudCommonUtils.getBaseUrl;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.data.ContentConsts;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.DocumentUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.InboxUtils;
import com.jive.restapi.automation.utilities.MessageUtils;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.QuestionUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.Discussion;
import com.jive.restapi.generated.person.models.DocumentEntities;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Image;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.Message;
import com.jive.restapi.generated.person.models.Outcome;
import com.jive.restapi.generated.person.models.Place;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.val;

public class QuestionStepDefs {

    private Response questionCreationResponse;
    private Response questionUnderPlaceCreationResponse;
    private Response searchResponse;
    private Response contentOutcomeResponse;
    private InboxEntry inbox;
    private Response commentCreationResponse;

    @When("Request to create a question")
    public void adminRequestToCreateQuestion() {
        questionCreationResponse = QuestionUtils.createQuestion(ContentConstants.getDefaultContentDataForQuestion());
    }

    @When("Request to create a question Under Place")
    public void adminRequestToCreateQuestionUnderPlace() {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        questionUnderPlaceCreationResponse = QuestionUtils.createQuestion(ContentConstants.getDefaultContentDataForQuestionUnderPlace(openGroupCreationResponse.as(Place.class)));
    }

    @Then("Question is Created Successfully")
    public void adminVerifyQuestionCreated() {
        questionCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @Then("Question is Created Successfully Under Place")
    public void adminVerifyQuestionCreatedUnderPlace() {
        questionUnderPlaceCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Logged in user requests to create question (.*) with tags (.*)$")
    public void userCreatesAQuestionWithTags(String questionTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.setTags(tagList);
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @When("^Logged in user requests to update question (.*) with tags (.*)$")
    public void userUpdatesAQuestion(String questionTag, String tag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        questionData.setTags(tagList);
        questionCreationResponse = QuestionUtils.updateQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @Then("^Question (.*) is updated with tags (.*) successfully$")
    public void questionIsUpdatedSuccessfully(String questionTag, String tag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        assertTrue(questionData.getTags().contains(tag));
    }

    @When("User request to search question (.*) by subject from (.*)")
    public void searchByQuestion(String questionTag, String origin) throws InterruptedException {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        for (int counter = 0; counter < 15; counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition("search", questionData.getSubject())
                            .buildList(), origin);
            if (searchResponse.as(DocumentEntities.class).getList().size() != 0) {
                break;
            }
            Thread.sleep(2000);
        }
    }

    @Then("^Question (.*) is searched successfully$")
    public void questionIsSearched(String questionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        assertTrue(searchResponse.as(DocumentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(questionData.getSubject())));
    }

    @When("User request to delete question (.*)")
    public void deletequestion(String questionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        searchResponse = QuestionUtils.deleteQuestion(questionData.getContentID());
    }

    @Then("^Question (.*) is deleted successfully$")
    public void questionIsDeleted(String questionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        searchResponse.then().assertThat().statusCode(SC_NO_CONTENT);
        questionCreationResponse = QuestionUtils.createQuestion(questionData);
        FeatureRegistry.getCurrentFeature().setData(Discussion.class, questionTag, questionCreationResponse.as(Discussion.class));
    }

    @When("^Logged in user requests to create Question (\\S+)$")
    public void userCreatesAQuestion(String questionTag) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @Then("^Question (.*) is created successfully$")
    public void QuestionIsCreatedSuccessfully(String QuestionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, QuestionTag);
        Response createDocumentResponse = DocumentUtils.getResponseOfDocumentById(questionData.getContentID());
        createDocumentResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Logged in user requests to create Question (.*) with description (.*) under place$")
    public void userCreatesAQuestionWithDescriptionUnderPlace(String questionTag, String description) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.content((new ContentBody()).text(description));
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @When("^Logged in user requests to create Question (.*) with description (.*) in community$")
    public void userCreatesAQuestionWithDescription(String questionTag, String description) {
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.content((new ContentBody()).text(description));
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @When("^User (.*) mark content question (.*) as Mark for Action (.*)$")
    public void userMarkForAction(String userName, String questionTag, String outcomeName) {
        Content question = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.PENDING.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(question.getContentID(), outcomeData);
        FeatureRegistry.getCurrentFeature().setData(Outcome.class, outcomeName, contentOutcomeResponse.as(Outcome.class));
    }

    @When("^User requests to mark question (.*) as Mark for Official (.*)$")
    public void markQuestionOfficial(String questionTag, String outcomeName) {
        Content question = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.OFFICIAL.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(question.getContentID(), outcomeData);
        FeatureRegistry.getCurrentFeature().setData(Outcome.class, outcomeName, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("Verify content question (.*) is Mark for action (.*)$")
    public void verifyUserMarkForAction(String questionTag, String outcomeName) {
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        assertEquals(contentOutcomeResponse.as(Outcome.class).getOutcomeType().getName(),
                StandardOutcomeTypes.PENDING.getOutcomeType().getName());
    }

    @Then("Verify question is marked as Official (.*)$")
    public void verifyQuestionMarkedOfficial(String outcomeName) {
        Outcome outcome = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeName);
        assertEquals(outcome.getOutcomeType().getName(),
                StandardOutcomeTypes.OFFICIAL.getOutcomeType().getName());
    }

    @When("Logged in user Like question (.*)")
    public void likeQuestion(String questionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        Response like = ContentUtil.contentLikes(questionData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("Logged in user remove Like added on question (.*)")
    public void removeLikeOnQuestion(String questionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        Response like = ContentUtil.deleteContentLike(questionData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("User request all unread messages for Question")
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
                Thread.sleep(1500); // Sleep is require to give some time to message to be received before doing a retry
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        inbox = response.as(InboxEntry.class);
    }

    @Then("^Notification of Question ([a-zA-Z]+) is present in inbox")
    public void verifyNotificationHasDiscussion(String questionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        assertNotNull(questionData.getSubject(), inbox.getUnread());
    }

    @When("^User requests to create Question (.*) under place (.*)$")
    public void createQuestionUnderPlace(String questionTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        if (place == null) {
            place = FeatureRegistry.getCurrentFeature().getData(Group.class, placeTag);
        }
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(place));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        questionCreationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @When("^Add a comment (.*) to question (.*)$")
    public void addCommentToQuestion(String commentTag, String questionTag) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        commentCreationResponse = MessageUtils.createMessageWithDefaultData(CloudCommonUtils.getContentUri(questionData));
        FeatureRegistry.getCurrentFeature().setData(Message.class, commentTag, commentCreationResponse.as(Message.class));
    }

    @Then("^Comment has been added to the question successfully$")
    public void commentAddedSuccessfullyOnQuestion() {
        commentCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Verify Question (.*) Like Count Equals (\\d+)$")
    public void getLikeCount(String questionTag, int likeCountExpected) {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        Response likeCount = ContentUtil.getLikes(questionData.getContentID());
        JsonArray likeList = likeCount.as(JsonObject.class).get("list").getAsJsonArray();
        assertEquals(likeCountExpected, likeList.size());
    }

    @When("^User requests to create a draft Question (\\S+)$")
    public void createDraftQuestion(String questionTag) {
        Content question = ContentConstants.getDefaultContentDataForQuestion();
        question.setStatus(Content.StatusEnum.INCOMPLETE);
        questionCreationResponse = QuestionUtils.createQuestion(question);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @When("User requests to get question (.*) by outcome (.*) from place (.*)")
    public void getQuestionByOutcomeFromPlace(String contentTag, String outcomeTag, String placeTag) throws InterruptedException {
        Content questionData = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = PlacesUtils.getContentWithFilters(place.getPlaceID(),
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, questionData.getSubject())
                            .addCondition(SearchFilterConstants.outcomeTypeFilterKey, "\"" + outcomeTag + "\"")
                            .buildList());
            if (searchResponse.as(ContentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @When("^Create Content (.*) with attachment file (.*) with Type (.*)$")
    public void createQuestionWithAttachment(String contentTag, String fileName, String mimeType) throws Throwable {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = ContentUtil.createContent(
                questionData, null,
                Options.attachResource("file", fileName, fileName, mimeType));
        FeatureRegistry.getCurrentFeature().setData(Content.class, contentTag,
                questionCreationResponse.as(Content.class));
    }

    @Then("^Question (.*) Attachment (.*) is attached successfully$")
    public void attachmentIsSuccessful(String quesitonTag, String fileName) throws Throwable {
        Content qusetion = FeatureRegistry.getCurrentFeature().getData(Content.class, quesitonTag);
        assertTrue(String.format(ValidationConstants.ATTACHMENT_VALIDATION_MESSAGE,
                qusetion.getAttachments().get(0).getName())
                ,questionCreationResponse.as(Discussion.class).getAttachments().get(0)
                        .getName().contains(fileName));
    }

    @When("^Create Content (.*) with Image (.*) in RTE In Place$")
    public void createQuestionWithImageFromRTE(String contentTag, String imageTag) throws Throwable {
        Image imageData = FeatureRegistry.getCurrentFeature().getData(Image.class, imageTag);

        val imageURL1 = String.format(ContentConsts.SHOW_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        val imageURL2 = String.format(ContentConsts.DOWNLOAD_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());

        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Content questionData = ContentConstants.getDefaultContentDataWithImageInRTE(Content.TypeEnum.DISCUSSION, imageURL1, imageURL2);
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionData.setQuestion(true);
        questionCreationResponse = ContentUtil.createContent(questionData, null);
        questionCreationResponse.then().log().all();
        FeatureRegistry.getCurrentFeature().setData(Content.class, contentTag, questionCreationResponse.as(Content.class));
    }

    @Then("^Content (.*) is created successfully with Image (.*) In RTE$")
    public void questionIsCreatedWithImageInRTE(String contentTag, String imageName) throws Throwable {
        Content content = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        assertTrue(String.format(ValidationConstants.CONTENT_IMAGE, content
                        .getContent().getText()),
                content.getContent().getText().contains(imageName));
    }

    @When("^Create question (.*) with tags (.*) Under Place$")
    public void createQuestionWithTagsUnderPlace(String questionTag, String tag) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.setTags(tagList);
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @When("^User move question (\\S+) to group (\\S+)$")
    public void userMoveQuestionToGroup(String questionTag, String groupTag) {
        Content question = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);

        question.setParent(CloudCommonUtils.getParentPlaceUri(group));
        question.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = ContentUtil.updateContent(question.getContentID(), question);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, question);
    }

    @Then("^Question (\\S+) moved successfully$")
    public void verifyQuestionMovedSuccessfully(String questionTag) {
        questionCreationResponse.then().assertThat().statusCode(SC_OK);
        Content question = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        Content questionUpdated = questionCreationResponse.as(Content.class);
        assertTrue(
                ValidationConstants.getErrorMessage(EntityConstants.QUESTION, ValidationConstants.UPDATED),
                question.getParent().equals(questionUpdated.getParent())
        );
    }

    @When("^Logged in user requests to create Question (.*) Under Space (.*)$")
    public void createQuestionUnderSpace(String questionTag, String spaceTag) throws Throwable {
        Place space = FeatureRegistry.getCurrentFeature().getData(Place.class, spaceTag);
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(space));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }

    @When("^Create Content (.*) with Image (.*) in RTE In Space (.*)")
    public void createQuestionWithImageFromRTE(String contentTag, String imageTag, String spaceTag) throws Throwable {
        Place space = FeatureRegistry.getCurrentFeature().getData(Place.class, spaceTag);
        Image imageData = FeatureRegistry.getCurrentFeature().getData(Image.class, imageTag);

        val imageURL1 = String.format(ContentConsts.SHOW_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        val imageURL2 = String.format(ContentConsts.DOWNLOAD_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        Content questionData = ContentConstants.getDefaultContentDataWithImageInRTE(Content.TypeEnum.DISCUSSION, imageURL1, imageURL2);
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(space));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionData.setQuestion(true);
        questionCreationResponse = ContentUtil.createContent(questionData, null);
        FeatureRegistry.getCurrentFeature().setData(Content.class, contentTag, questionCreationResponse.as(Content.class));
    }

    @When("^Request To Create Content (.*) In Space (.*) with attachment file (.*) with Type (.*)$")
    public void createNewQuestionWithAttachment(String contentTag, String spaceTag, String fileName, String mimeType) throws Throwable {
        Place space = FeatureRegistry.getCurrentFeature().getData(Place.class, spaceTag);
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(space));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = ContentUtil.createContent(
                questionData, null,
                Options.attachResource(EntityConstants.PART_NAME, fileName, fileName, mimeType));
        FeatureRegistry.getCurrentFeature().setData(Content.class, contentTag,
                questionCreationResponse.as(Content.class));
    }

    @When("^Request To Create(.*) In Space (.*) with tags (.*)")
    public void createQuestionWithTagsUnderSpace(String questionTag, String spaceTag, String tag) {
        Place space = FeatureRegistry.getCurrentFeature().getData(Place.class, spaceTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Content questionData = ContentConstants.getDefaultContentDataForQuestion();
        questionData.setTags(tagList);
        questionData.setParent(CloudCommonUtils.getParentPlaceUri(space));
        questionData.setVisibility(Content.VisibilityEnum.PLACE);
        questionCreationResponse = QuestionUtils.createQuestionToReturnResponse(questionData);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, questionCreationResponse.as(Content.class));
    }
}
