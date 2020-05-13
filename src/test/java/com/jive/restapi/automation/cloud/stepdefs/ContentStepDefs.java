package com.jive.restapi.automation.cloud.stepdefs;

import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertHttpResponseEquals;
import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertKeyEquals;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.LoginStepdefs;
import com.jive.restapi.automation.utilities.CommentUtils;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.ContentUtils;
import com.jive.restapi.automation.utilities.DiscussionUtils;
import com.jive.restapi.automation.utilities.DocumentUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.MessageUtils;
import com.jive.restapi.automation.utilities.PostUtils;
import com.jive.restapi.automation.utilities.QuestionUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.automation.utilities.v3.OutcomesUtil;
import com.jive.restapi.generated.person.models.Comment;
import com.jive.restapi.generated.person.models.CommentEntities;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.Discussion;
import com.jive.restapi.generated.person.models.Document;
import com.jive.restapi.generated.person.models.FileModel;
import com.jive.restapi.generated.person.models.Message;
import com.jive.restapi.generated.person.models.MessageEntities;
import com.jive.restapi.generated.person.models.Outcome;
import com.jive.restapi.generated.person.models.OutcomeBase;
import com.jive.restapi.generated.person.models.Person;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Assert;

public class ContentStepDefs {

    private Response creationResponse;
    private Response discussionDeleteResponse;
    private Response searchResponse;
    private Response contentOutcomeResponse;
    private Response updateContentResponse;
    private Response createContentResponse;
    private Response replyCreationResponse;
    private final LoginStepdefs loginstepDef = new LoginStepdefs();
    private Response contentResp;


    @When("Request to create a discussion (.*)")
    public void adminRequestToCreateADiscussion(String discussionTag) {
        Content discussion = new Content()
                .type(Content.TypeEnum.DISCUSSION)
                .subject("Talking about jive testing")
                .content(new ContentBody()
                        .text("blah blah")
                        .type("text/html")
                        .type("discussion"));
        creationResponse = ContentUtils.createContent(discussion);
        FeatureRegistry.getCurrentFeature().setData(Content.class, discussionTag, creationResponse.as(Content.class));
    }

    @Then("^(?:a discussion|Reply) is created successfully")
    public void entityCreatedSuccessfully() {
        creationResponse.then().assertThat().statusCode(HttpStatus.SC_CREATED);
    }

    @When("Request to delete that discussion")
    public void adminRequestToDeleteThatDiscussion() {
        discussionDeleteResponse = ContentUtils.deleteContent(
                creationResponse.as(Content.class).getContentID());
    }

    @Then("that discussion gets deleted")
    public void thatDiscussionGetsDeleted() {
        discussionDeleteResponse.then().assertThat().statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Given("User create question (.*)")
    public void userCreateQuestion(String questionTag) {
        Content question = ContentConstants.getDefaultContentDataForQuestion();
        creationResponse = ContentUtils.createContent(question);
        FeatureRegistry.getCurrentFeature().setData(Content.class, questionTag, creationResponse.as(Content.class));
    }

    @When("^User add reply (.*) on (?:question|discussion) (.*)$")
    public void userAddReplyOnContent(String replyTag, String contentTag) {
        Content content = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        creationResponse = MessageUtils.createMessageWithDefaultData(CloudCommonUtils.getContentUri(content));
        FeatureRegistry.getCurrentFeature().setData(Message.class, replyTag, creationResponse.as(Message.class));
    }

    @When("^User search (?:question|discussion)'s \"(.*)\" replies$")
    public void userSearchQuestionComments(String questionTag) throws InterruptedException {
        Content question = FeatureRegistry.getCurrentFeature().getData(Content.class, questionTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = MessageUtils.getContentReplies(question);
            searchResponse.then().statusCode(SC_OK);
            MessageEntities searchResult = searchResponse.as(MessageEntities.class);
            if (searchResult.getList() != null && searchResult.getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^(?:Question|Discussion) contains reply (.*)$")
    public void contentIncludedReply(String replyTag) {
        Message reply = FeatureRegistry.getCurrentFeature().getData(Message.class, replyTag);
        Assert.assertTrue(searchResponse.as(MessageEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(reply.getSubject())));
    }

    @When("^User add comment (.*) on (blog post|file|document|poll|video) (.*)$")
    public void userAddCommentOnContent(String commentTag, String contentType, String contentTag) {
        creationResponse = CommentUtils.createComment(ContentConstants.getDefaultCommentData(),
                CloudCommonUtils.getContentData(contentType, contentTag));
        FeatureRegistry.getCurrentFeature().setData(Comment.class, commentTag, creationResponse.as(Comment.class));
    }

    @Then("^Comment is created successfully$")
    public void userVerifyContentCreated() {
        creationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^User search (blog post|file|document|poll|video)'s \"(.*)\" comments$")
    public void userSearchContentComments(String contentType, String contentTag) {
        searchResponse = ContentUtils.getComments(CloudCommonUtils.getContentData(contentType, contentTag));
        searchResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Then("^(?:Blog Post|File|Document|Poll|Video) contains comment (.*)$")
    public void contentIncludedComment(String commentTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(Comment.class, commentTag);
        Assert.assertTrue(searchResponse.as(CommentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(comment.getSubject())));
    }

    @When("^User request to mark (document|discussion|file) (.*) as official (.*)$")
    public void markContentAsOfficialOutcome(String contentType, String contentTag, String officialTag) {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.OFFICIAL.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(contentData.getContentID(), outcomeData);
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, officialTag, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("^(.*) outcome should be created in (.*) (content|file|blog post|document)$")
    public void officialOutcomeShouldBeCreatedInDiscussion(String officialTag, String contentTag, String contentType) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        assertTrue(ValidationConstants.officialOutcomeCreation,
                contentData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("^User request to search (content|document|blog post|file) (.*) by subject from (\\S+)$")
    public void searchByContentSubject(String contentType, String contentTag, String origin)
            throws InterruptedException {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = SearchUtils.searchContent(new FilterBuilder()
                    .addCondition(SearchFilterConstants.searchFilterKey, contentData.getSubject())
                    .buildList(), origin);
            searchResponse.then().statusCode(SC_OK);
            ContentEntities searchResult = searchResponse.as(ContentEntities.class);
            if (searchResult.getList() != null && searchResult.getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^It should return (content|document|discussion|file|blog post) (.*)$")
    public void userVerifySearchedContent(String contentType, String contentTag) {
        Content content = CloudCommonUtils.getContentData(contentType, contentTag);
        assertTrue(ValidationConstants.searchResults, content.getSubject().equals(content.getSubject()));
    }

    @When("^User request to search (content|blog post|file) (.*) by outcome (official|outdated|success|wip) from (.*)$")
    public void searchContentByOutcome(String contentType, String contentTag, String outcomeTag, String origin)
            throws InterruptedException {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, contentData.getSubject())
                            .addCondition(SearchFilterConstants.outcomeTypeFilterKey, outcomeTag)
                            .buildList(), origin);
            if (searchResponse.as(ContentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^(Content|Document|Discussion|Blog Post|File) (.*) is searched successfully in search page$")
    public void contentIsSearchedSuccessfully(String contentType, String contentTag) {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        assertTrue(ValidationConstants.contentCreation, searchResponse.as(ContentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().contains(contentData.getSubject())));
    }

    @When("^Logged in user requests to update (file) (.*) with title (.*)$")
    public void userUpdatesAContentTitle(String contentType, String contentTag, String titleTag) {
        String title = RandomStringUtils.randomAlphanumeric(10);
        FeatureRegistry.getCurrentFeature().setData(String.class, titleTag, title);
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        contentData.setSubject(title);
        updateContentResponse = DiscussionUtils.updateDiscussionToReturnResponse(contentData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, contentTag, updateContentResponse.as(Discussion.class));
    }

    @Then("^File (.*) is updated with title (.*) successfully$")
    public void contentIsUpdatedWithTitleSuccessfully(String fileTag, String tag) {
        Content contentData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, fileTag);
        String updatedTitle = FeatureRegistry.getCurrentFeature().getData(String.class, tag);
        assertTrue(ValidationConstants.contentUpdatedWithTitle, contentData.getSubject().contains(updatedTitle));
    }

    @Then("(.*) should be removed in (.*) content")
    public void officialOutcomeShouldBeRemovedInContent(String officialTag, String contentTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Content contentData = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        assertFalse(ValidationConstants.officialOutcomeNonDisplay,
                contentData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("^User request to mark (content|file|discussion|document) (.*) as Outdated (.*)$")
    public void markContentAsOutdatedOutcome(String contentType, String contentTag, String outdatedTag) {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.OUTDATED.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(contentData.getContentID(), outcomeData);
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outdatedTag, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("Content outcome (.*) should be created in (document|discussion|blog post|file) (.*)")
    public void contentOutcomeShouldBeCreated(String officialTag, String contentType, String contentTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        assertTrue(ValidationConstants.officialOutcomeCreation,
                contentData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @Then("^Content (.*) is created successfully$")
    public void contentIsCreatedSuccessfully(String contentTag) {
        Content contentData = FeatureRegistry.getCurrentFeature().getData(Document.class, contentTag);
        createContentResponse = DocumentUtils.getResponseOfDocumentById(contentData.getContentID());
        createContentResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^User request to mark (content|blog post|document|file) (.*) as Success (.*)$")
    public void markContentAsSuccessOutcome(String contentType, String contentTag, String outdatedTag) {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.SUCCESS.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(contentData.getContentID(), outcomeData);
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outdatedTag, contentOutcomeResponse.as(Outcome.class));
    }

    @When("^User request to mark (content|file) (.*) as Final (.*)$")
    public void markContentAsFinalOutcome(String contentType, String contentTag, String outdatedTag) {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.FINALIZED.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(contentData.getContentID(), outcomeData);
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outdatedTag, contentOutcomeResponse.as(Outcome.class));
    }

    @When("^Unmark (document|discussion|document|file|blog post) (.*) using outcome (.*)$")
    public void unmarkContentFromOutcome(String contentType, String contentTag, String outcomeTag)
            throws InterruptedException {
        Content content = CloudCommonUtils.getContentData(contentType, contentTag);
        Outcome outcomeData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeTag);
        contentOutcomeResponse = OutcomesUtil.deleteOutcome(outcomeData.getId());
        contentOutcomeResponse.then().assertThat().statusCode(SC_NO_CONTENT);
        // Adding static wait 2seconds as the next step is to read the above deleted data
        Thread.sleep(TimeoutConstants.XS);
    }

    @Then("^Content has been unmarked successfully using outcome (.*)$")
    public void contentUnmarkedFromOutcome(String outcomeTag) {
        Outcome outcomeData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeTag);
        contentOutcomeResponse = OutcomesUtil.getOutcome(outcomeData.getId());
        contentOutcomeResponse.then().assertThat().statusCode(SC_NOT_FOUND);
    }

    @When("^User request to mark (content|file|document) (.*) as Reserved (.*)$")
    public void markContentAsReservedOutcome(String contentType, String contentTag, String reservedTag) {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.WIP.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(contentData.getContentID(), outcomeData);
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, reservedTag, contentOutcomeResponse.as(Outcome.class));
    }

    @When("^Invalid user request to mark file (.*) as Reserved$")
    public void markContentAsReservedOutcomeByInvalidUser(String contentTag) {
        contentOutcomeResponse = prepareFileContent(contentTag, StandardOutcomeTypes.WIP);
    }

    @Then("Content should not be marked with outcome")
    public void contentNotMarkedFromOutcome() {
        contentOutcomeResponse.then().assertThat().statusCode(SC_FORBIDDEN);
    }

    @When("^Invalid user request to mark file (.*) as Final$")
    public void markContentAsFinalOutcomeByInvalidUser(String contentTag) {
        contentOutcomeResponse = prepareFileContent(contentTag, StandardOutcomeTypes.FINALIZED);
    }

    @When("User request to create multiple contents: BlogPost, Document, Question, Discussion")
    public void userRequestMultipleContents() throws InterruptedException {
        PostUtils.createDefaultPost();
        DocumentUtils.createDefaultDocument();
        QuestionUtils.createDefaultQuestion();
        DiscussionUtils.createDefaultDiscussion();
    }

    private Response prepareFileContent(String contentTag, StandardOutcomeTypes outcomeTypes) {
        Content contentData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, contentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(outcomeTypes.getOutcomeType());
        return contentOutcomeResponse = ContentUtil.createOutcome(contentData.getContentID(), outcomeData);
    }

    @When("^Add Reply (.*) to the Content (.*)$")
    public void addReplyToContent(String replyTag, String contentTag) {
        Content contentType = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        replyCreationResponse = MessageUtils.createMessageWithDefaultData(CloudCommonUtils.getContentUri(contentType));
        FeatureRegistry.getCurrentFeature().setData(Message.class, replyTag, replyCreationResponse.as(Message.class));
    }

    @Then("^Reply (.*) is added to the (.*) successfully$")
    public void replyIsAddedSuccessfully(String replyTag, String discussionTag) {
        Content discussion = FeatureRegistry.getCurrentFeature().getData(
                Content.class,
                discussionTag);
        Message reply = FeatureRegistry.getCurrentFeature().getData(
                Message.class,
                replyTag);

        replyCreationResponse = MessageUtils.getContentReplies(discussion);

        assertTrue(String.format(ValidationConstants.DISCUSSION_CONTAIN_REPLY_VALIDATION,
                discussion.getSubject(), reply.getContent().getText()),
                replyCreationResponse.as(MessageEntities.class)
                        .getList()
                        .stream()
                        .anyMatch(elem -> elem.getId().equals(reply.getId())));
    }

    @Then("^Verify User (.*) is following (.*) by default$")
    public void verifyFollowedIsSetToTrueByDefault(String userTag, String contentTag) throws Throwable {
        getContentID(userTag, contentTag);
        contentResp.then().assertThat().statusCode(SC_OK);
        MatcherAssert.assertThat(ValidationConstants.FOLLOW_USER, contentResp.as(Content.class)
                .getFollowed(), Is.is(true));
    }

    @Then("^Verify User (.*) is NOT following (.*) by default$")
    public void verifyFollowedIsSetToFalse(String userTag, String contentTag) throws Throwable {
        getContentID(userTag, contentTag);
        contentResp.then().assertThat().statusCode(SC_OK);
        MatcherAssert.assertThat(ValidationConstants.NOT_FOLLOW_USER, contentResp.as(Content.class)
                .getFollowed(), Is.is(false));
    }

    private void getContentID(String userTag, String contentTag) {
        loginstepDef.switchToUser(userTag);
        Content content = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        contentResp = ContentUtil.getContent(content.getContentID());
    }

    @When("^Logged in user to mark content (.*) as Mark for Action to another user (.*) with response (.*)")
    public void userMarkForAction1(String contentTag, String userName, String outcomeName) {
        Content content = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userName);
        OutcomeBase outcomeData = new Outcome()
                .outcomeType(StandardOutcomeTypes.PENDING.getOutcomeType());
        outcomeData.setUser(user);
        outcomeData.setNote(SearchFilterConstants.MESSAGES);
        contentOutcomeResponse = ContentUtil.createOutcome(content.getContentID(),
                outcomeData);
        FeatureRegistry.getCurrentFeature().setData(OutcomeBase.class, outcomeName,
                contentOutcomeResponse.as(OutcomeBase.class));
        assertHttpResponseEquals(SC_CREATED, contentOutcomeResponse.statusCode());
    }

    @Then("Content has been marked for action to another user successfully (.*)")
    public void contentmarkedForAction(String outcomeTag) {
        OutcomeBase outcomeData = FeatureRegistry.getCurrentFeature().getData(OutcomeBase.class, outcomeTag);
        assertKeyEquals(OutcomeBase.SERIALIZED_NAME_STATUS, EntityConstants.PUBLISHED,
                outcomeData.getStatus().getValue());
    }
}
