package com.jive.restapi.automation.cloud.stepdefs;

import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertKeyTrue;
import static com.xo.restapi.automation.configs.CloudCommonUtils.getBaseUrl;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.html.HtmlEscapers;
import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.TestContextKeys;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.data.ContentConsts;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.DiscussionUtils;
import com.jive.restapi.automation.utilities.DocumentUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.InboxUtils;
import com.jive.restapi.automation.utilities.MessageUtils;
import com.jive.restapi.automation.utilities.OutcomeConstants;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.Discussion;
import com.jive.restapi.generated.person.models.DiscussionEntities;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Image;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.Message;
import com.jive.restapi.generated.person.models.MessageEntities;
import com.jive.restapi.generated.person.models.Outcome;
import com.jive.restapi.generated.person.models.Place;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;

public class DiscussionStepDefs {

    private Response createDiscussionResponse;
    private Response deleteResponse;
    private Response replyCreationResponse;
    private DiscussionEntities searchResult;
    private Response discussionOutcomeResponse;
    private Response searchResponse;
    private String umlauts = "f�r alle & jeden zum testen � � �";
    private Response contentOutcomeResponse;
    private InboxEntry inbox;

    @When("Request to create a Discussion")
    public void adminRequestToCreateDiscussion() {
        createDiscussionResponse = DiscussionUtils.createDiscussion(ContentConstants.getDefaultDiscussionData());
    }

    @Then("Discussion is created successfully")
    public void adminVerifyDiscussionCreated() {
        createDiscussionResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Request to create a Discussion (\\S+) Under Place(?: )?(\\S+)?$")
    public void adminRequestToCreateDiscussionUnderPlace(String discussionTag, String groupTag) {
        Group documentGroup;
        documentGroup = CloudCommonUtils.retrieveGroup(groupTag);

        createDiscussionResponse = DiscussionUtils.createDiscussion(
                ContentConstants.getDefaultDiscussionDataUndetPlace(documentGroup));
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @Then("Discussion is Created Successfully Under Place")
    public void adminVerifyDiscussionCreatedUnderPlace() {
        createDiscussionResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Logged in user requests to create a discussion (.*) with tags (.*)$")
    public void userCreatesADiscussion(String discussionTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Discussion discussionData = ContentConstants.getDefaultDiscussionData();
        discussionData.setTags(tagList);
        createDiscussionResponse = DiscussionUtils.createDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
        FeatureRegistry.getCurrentFeature()
                .setData(Content.class, discussionTag, createDiscussionResponse.as(Content.class));
    }

    @When("^Logged in user requests to update a discussion (.*) with tags (.*)$")
    public void userUpdatesTagsOfDiscussion(String discussionTag, String tag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        discussionData.tags(tagList);
        createDiscussionResponse = DiscussionUtils.updateDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @When("^Logged in user requests to create a discussion (\\S+)$")
    public void userCreateDiscussion(String discussionTag) {
        Discussion discussionData = ContentConstants.getDefaultDiscussionData();
        createDiscussionResponse = DiscussionUtils.createDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @Then("^Discussion (.*) is created successfully$")
    public void discussionIsCreatedSuccessfully(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Content discussion = DiscussionUtils.getDiscussionById(discussionData.getContentID());

        Assert.assertTrue(discussionData.getSubject().equals(discussion.getSubject()));
    }

    @Then("^Discussion (.*) is updated with tags (.*) successfully$")
    public void discussionIsUpdatedSuccessfully(String discussionTag, String tag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Assert.assertTrue(discussionData.getTags().contains(tag));
    }

    @When("^Logged in user requests to update a discussion with description (.*)$")
    public void userUpdatesDescriptionOfDiscussion(String description) {
        Discussion discussionData = createDiscussionResponse.as(Discussion.class);
        discussionData.content(new ContentBody().text(description));
        createDiscussionResponse = DiscussionUtils.updateDiscussionToReturnResponse(discussionData);
    }

    @When("User request to search discussion (.*) by subject from (.*)")
    public void searchByDiscussionSubject(String discussionTag, String origin) throws InterruptedException {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        for (int counter = 0; counter < 15; counter++) {
            Response resp = SearchUtils.searchContent(new FilterBuilder()
                    .addCondition("search", discussionData.getSubject())
                    .buildList(), origin);
            searchResult = resp.as(DiscussionEntities.class);
            if (searchResult.getList() != null && searchResult.getList().size() != 0) {
                break;
            }
            Thread.sleep(2000);
        }
    }

    @Then("^Discussion (.*) is searched successfully$")
    public void discussionIsSearched(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        boolean check = searchResult.getList().stream()
                .anyMatch((c) -> c.getSubject().equals(discussionData.getSubject()));
        Assert.assertTrue(check);
    }


    @When("User request to delete discussion (.*)")
    public void deleteDiscussion(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        deleteResponse = DocumentUtils.deleteDocument(discussionData.getContentID());
    }

    @Then("^Discussion (.*) is deleted successfully$")
    public void discussionIsDeleted(String discussionTag) {
        FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        deleteResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^user creates a umlauts Discussion (.*) with tags (.*)$")
    public void userCreatesUmlautsDiscussion(String discussionTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Discussion discussionData = ContentConstants.getDefaultDiscussionData();
        discussionData.content(new ContentBody().text(HtmlEscapers.htmlEscaper().escape(umlauts))).subject(umlauts);
        discussionData.setTags(tagList);
        createDiscussionResponse = DiscussionUtils.createDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @When("^user updates a Discussion (.*) with Umlauts$")
    public void userUpdatesDiscussionWithUmlauts(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        discussionData.content(new ContentBody().text(HtmlEscapers.htmlEscaper().escape(umlauts))).subject(umlauts);
        createDiscussionResponse = DiscussionUtils.updateDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @Then("^Discussion (.*) is updated with umlauts successfully$")
    public void discussionIsUpdatedWithUmlautsSuccessfully(String discussionTag) {
        String umlautsedit = "jeden zum testen";
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Assert.assertTrue(discussionData.getSubject().contains(umlautsedit));
        Assert.assertTrue(discussionData.getContent().getText().contains(umlautsedit));
    }

    @When("^Add reply (.*) to the discussion (.*)$")
    public void addReplyToDiscussion(String replyTag, String discussionTag) {
        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        replyCreationResponse = MessageUtils.createMessageWithDefaultData(CloudCommonUtils.getContentUri(discussion));
        FeatureRegistry.getCurrentFeature().setData(Message.class, replyTag, replyCreationResponse.as(Message.class));
    }

    @Then("^Reply has been added to the discussion successfully$")
    public void replyAddedSuccessfully() {
        replyCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^User request to create a discussion (.*)$")
    public void userCreateDocument(String discussionTag) {
        Discussion discussionData = ContentConstants.getDefaultDiscussionData();
        discussionData.setSubject(discussionData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        createDiscussionResponse = DocumentUtils.createDocumentToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @Then("^Discussion (.*) should be created successfully$")
    public void discussionCreatedSuccessfully(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        createDiscussionResponse = DocumentUtils.getResponseOfDocumentById(discussionData.getContentID());
        createDiscussionResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("user request to mark discussion (.*) as Official (.*)")
    public void markDiscussionAsOfficialOutcome(String discussionTag, String officialTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.OFFICIAL.getOutcomeType());
        discussionOutcomeResponse = ContentUtil.createOutcome(discussionData.getContentID(), outcomeData);
        discussionOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, officialTag, discussionOutcomeResponse.as(Outcome.class));
    }

    @Then("(.*) should be created in (.*) discussion")
    public void officialOutcomeShouldBeCreatedInDiscussion(String officialTag, String discussionTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        assertTrue(discussionData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("User request to search discussion (.*) by outcome (.*) from (.*)")
    public void searchByDiscussionOutcome(String discussionTag, String outcomeTag, String origin)
            throws InterruptedException {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, discussionData.getSubject())
                            .addCondition(SearchFilterConstants.outcomeTypeFilterKey, outcomeTag)
                            .buildList(), origin);
            if (searchResponse.as(DiscussionEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^Discussion (.*) is searched successfully in main search page$")
    public void discussionIsSearchedSuccessfully(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        assertTrue(searchResponse.as(DiscussionEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(discussionData.getSubject())));
    }

    @When("^Logged in user requests to update discussion (.*) with title (.*)$")
    public void userUpdatesADiscussionTitle(String discussionTag, String titleTag) {
        String title = RandomStringUtils.randomAlphanumeric(10);
        FeatureRegistry.getCurrentFeature().setData(String.class, titleTag, title);
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        discussionData.setSubject(title);
        createDiscussionResponse = DiscussionUtils.updateDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @Then("^Discussion (.*) is updated with title (.*) successfully$")
    public void discussionIsUpdatedWithTitleSuccessfully(String discussionTag, String tag) throws InterruptedException {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        String updatedTitle = FeatureRegistry.getCurrentFeature().getData(String.class, tag);
        assertTrue(discussionData.getSubject().contains(updatedTitle));
        // Added static wait of 2 sec as next action is to read the updated data
        Thread.sleep(TimeoutConstants.XS);
    }

    @Then("(.*) should be removed in (.*) discussion")
    public void officialOutcomeShouldBeRemovedInDiscussion(String officialTag, String discussionTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        assertFalse(discussionData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("user request to mark discussion (.*) as Outdated (.*)")
    public void markDiscussionAsOutdatedOutcome(String discussionTag, String outdatedTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.OUTDATED.getOutcomeType());
        discussionOutcomeResponse = ContentUtil.createOutcome(discussionData.getContentID(), outcomeData);
        discussionOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outdatedTag, discussionOutcomeResponse.as(Outcome.class));
    }

    @Then("Discussion outcome (.*) should be created in discussion (.*)")
    public void officialOutcomeShouldBeCreated(String officialTag, String discussionTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        assertTrue(discussionData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("^Logged in user requests to create Discussion (.*) with description (.*) under place$")
    public void userCreatesADiscussionWithDescriptionUnderPlace(String discussionTag, String description) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Discussion discussionData = ContentConstants.getDefaultDiscussionData();
        discussionData.content((new ContentBody()).text(description));
        discussionData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        discussionData.setVisibility(Content.VisibilityEnum.PLACE);
        createDiscussionResponse = DiscussionUtils.createDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @When("^Logged in user requests to create Discussion (.*) with description (.*) in community$")
    public void userCreatesADiscussionWithDescription(String discussionTag, String description) {
        Discussion discussionData = ContentConstants.getDefaultDiscussionData();
        discussionData.content((new ContentBody()).text(description));
        createDiscussionResponse = DiscussionUtils.createDiscussionToReturnResponse(discussionData);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @When("^User (.*) mark content discussion (.*) as Mark for Action (.*)$")
    public void userMarkForAction(String userName, String discussionTag, String outcomeName) {
        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.PENDING.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(discussion.getContentID(), outcomeData);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outcomeName, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("Verify content discussion (.*) is Mark for action (.*)$")
    public void verifyUserMarkForAction(String discussionTag, String outcomeName) {
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        Assert.assertEquals(contentOutcomeResponse.as(Outcome.class).getOutcomeType().getName(),
                StandardOutcomeTypes.PENDING.getOutcomeType().getName());
    }

    @When("User request to mark discussion (.*) as Success (.*)")
    public void markDiscussionAsSuccessOutcome(String discussionTag, String outcomeTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.SUCCESS.getOutcomeType());
        discussionOutcomeResponse = ContentUtil.createOutcome(discussionData.getContentID(), outcomeData);
        discussionOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outcomeTag, discussionOutcomeResponse.as(Outcome.class));
    }

    @When("Logged in user Like discussion (.*)")
    public void likeDiscussion(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Response like = ContentUtil.contentLikes(discussionData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("Logged in user remove Like added on discussion (.*)")
    public void removeLikeOnDiscussion(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Response like = ContentUtil.deleteContentLike(discussionData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("User request to mark discussion (.*) as Final (.*)")
    public void markDiscussionAsFinalOutcome(String discussionTag, String outcomeTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.FINALIZED.getOutcomeType());
        discussionOutcomeResponse = ContentUtil.createOutcome(discussionData.getContentID(), outcomeData);
        discussionOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outcomeTag, discussionOutcomeResponse.as(Outcome.class));
    }

    @When("^User request all unread messages for Discussion$")
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

    @Then("^Notification of Discussion ([a-zA-Z]+) is present in inbox")
    public void verifyNotificationHasDiscussion(String discussionTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        assertNotNull(discussionData.getSubject(), inbox.getUnread());
    }

    @When("^User requests to create Discussion (.*) under place (.*)$")
    public void createDiscussionUnderPlace(String discussionTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Discussion discussionData = ContentConstants.getDefaultDiscussionData();
        discussionData.setParent(CloudCommonUtils.getParentPlaceUri(place));
        discussionData.setVisibility(Content.VisibilityEnum.PLACE);
        createDiscussionResponse = DiscussionUtils.createDiscussionToReturnResponse(discussionData);
        createDiscussionResponse.then().log().all().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @When("^User requests to create a draft Discussion (\\S+)$")
    public void createDraftDiscussion(String discussionTag) {
        Discussion discussion = ContentConstants.getDefaultDiscussionData();
        discussion.setStatus(Content.StatusEnum.INCOMPLETE);
        createDiscussionResponse = DiscussionUtils.createDiscussionToReturnResponse(discussion);
        FeatureRegistry.getCurrentFeature()
                .setData(Discussion.class, discussionTag, createDiscussionResponse.as(Discussion.class));
    }

    @When("^Mark discussion (.*) as \"Final\" and get outcome (.*)$")
    public void markDiscussionAsFinal(String discussionTag, String outcomeTag) {
        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(
                Discussion.class,
                discussionTag);

        discussionOutcomeResponse = ContentUtil.createOutcome(
                discussion.getContentID(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.FINALIZED));

        FeatureRegistry.getCurrentFeature().setData(
                Outcome.class,
                outcomeTag,
                discussionOutcomeResponse.as(Outcome.class));
    }

    @Then("^Outcome (.*) is created for discussion (.*)$")
    public void verifyOutcomeIsCreatedForDiscussion(String outcomeTag, String discussionTag) {
        Outcome outcome = FeatureRegistry.getCurrentFeature().getData(
                Outcome.class,
                outcomeTag);
        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(
                Discussion.class,
                discussionTag);

        assertTrue(discussion.getOutcomeTypes().contains(outcome.getOutcomeType()));
    }

    @When("User requests to get discussion (.*) by outcome (.*) from place (.*)")
    public void getDiscussionByOutcomeFromPlace(String contentTag, String outcomeTag, String placeTag)
            throws InterruptedException {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(Discussion.class, contentTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = PlacesUtils.getContentWithFilters(place.getPlaceID(),
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, discussionData.getSubject())
                            .addCondition(SearchFilterConstants.outcomeTypeFilterKey, "\"" + outcomeTag + "\"")
                            .buildList());
            if (searchResponse.as(ContentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
        searchResult = searchResponse.as(DiscussionEntities.class);
    }

    @When("User requests to get pdf of discussion (.*) and gets response (.*)")
    public void getPdf(String discussionTag, String response) throws Throwable {
        Content discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        String pdfUrl = discussion.getResources().get("pdf").getRef();
        Response pdfURlResp = RestAssured.get(new URL(pdfUrl));
        FeatureRegistry.getCurrentFeature().setData(int.class, response, pdfURlResp.getStatusCode());
    }

    @Then("Pdf request (.*) is successful")
    public void verifyPdfRequestSuccessful(String responseTag) {
        int response = FeatureRegistry.getCurrentFeature().getData(int.class, responseTag);
        Assert.assertEquals(ValidationConstants.PDF_NOT_FOUND, response, SC_OK);
    }

    @When("^User move discussion (\\S+) to group (\\S+)$")
    public void userMoveDiscussionToGroup(String discussionTag, String groupTag) {
        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);

        discussion.setParent(CloudCommonUtils.getParentPlaceUri(group));
        discussion.setVisibility(Content.VisibilityEnum.PLACE);
        createDiscussionResponse = ContentUtil.updateContent(discussion.getContentID(), discussion);
        FeatureRegistry.getCurrentFeature().setData(Discussion.class, discussionTag, discussion);
    }

    @Then("^Discussion (\\S+) moved successfully$")
    public void verifyDiscussionMovedSuccessfully(String discussionTag) {
        createDiscussionResponse.then().assertThat().statusCode(SC_OK);
        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Discussion discussionUpdated = createDiscussionResponse.as(Discussion.class);
        assertTrue(
                ValidationConstants.getErrorMessage(EntityConstants.DISCUSSION, ValidationConstants.UPDATED),
                discussion.getParent().equals(discussionUpdated.getParent())
        );
    }

    @When("^Get replies from discussion (.*) to list (.*)$")
    public void getRepliesFromDiscussion(String discussionTag, String listTag) {
        Discussion discussionData = FeatureRegistry.getCurrentFeature().getData(
                Discussion.class,
                discussionTag);

        Response repliesResponse = MessageUtils.getContentReplies(
                discussionData);

        FeatureRegistry.getCurrentFeature().setData(
                MessageEntities.class,
                listTag,
                repliesResponse.as(MessageEntities.class));
    }

    @When("Logged in user search discussion (.*) replies$")
    public void userSearchQuestionComments(String questionTag) {
        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, questionTag);
        searchResponse = MessageUtils.getContentReplies(discussion);
        searchResponse.then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @Then("Verify discussion contains reply (.*)$")
    public void contentIncludedReply(String replyTag) {
        Message reply = FeatureRegistry.getCurrentFeature().getData(Message.class, replyTag);
        assertKeyTrue(Message.SERIALIZED_NAME_SUBJECT, searchResponse.as(MessageEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(reply.getSubject())));
    }

    @When("Create discussion (.*) with Image (.*)$")
    public void createQuestionWithImageFromRTE(String contentTag, String imageTag) throws Throwable {
        Image imageData = FeatureRegistry.getCurrentFeature().getData(Image.class, imageTag);
        val imageURL1 = String.format(ContentConsts.SHOW_IMAGE_SERVER_URL, getBaseUrl(), imageData.getId());
        val imageURL2 = String.format(ContentConsts.DOWNLOAD_IMAGE_SERVER_URL, getBaseUrl(), imageData.getId());
        Content questionData = ContentConstants.getDefaultContentDataWithImageInRTE(Content.TypeEnum.DISCUSSION,
                imageURL1, imageURL2);
        questionData.setQuestion(false);
        createDiscussionResponse = ContentUtil.createContent(questionData, null);
        FeatureRegistry.getCurrentFeature().setData(Discussion.class, contentTag,
                createDiscussionResponse.as(Discussion.class));
    }

    @Then("^Discussion (.*) is created successfully with Image (.*)$")
    public void questionIsCreatedWithImageInRTE(String contentTag, String imageName) throws Throwable {
        Discussion content = FeatureRegistry.getCurrentFeature().getData(Discussion.class, contentTag);
        assertTrue(String.format(ValidationConstants.CONTENT_IMAGE, content
                        .getContent().getText()),
                content.getContent().getText().contains(imageName));
    }

    @When("Logged in user to add reply (.*) to the discussion (.*) with image (.*)$")
    public void addReplyToDiscussionWithImage(String replyTag, String discussionTag, String imageTag) {
        Image imageData = FeatureRegistry.getCurrentFeature().getData(Image.class, imageTag);
        val imageURL1 = String.format(ContentConsts.SHOW_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());
        val imageURL2 = String.format(ContentConsts.DOWNLOAD_IMAGE_SERVER_URL,
                getBaseUrl(), imageData.getId());

        Discussion discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Message reply = new Message()
                .subject(TestContextKeys.SUBJECT)
                .discussion(CloudCommonUtils.getContentUri(discussion))
                .parent(CloudCommonUtils.getContentUri(discussion))
                .content(new ContentBody()
                        .text("<body><img alt='' src=" + imageURL1 + "'/></body>\"")
                        .type(TestContextKeys.CONTENT_TYPE_HTML)
                        .type(EntityConstants.DISCUSSION)
                );
        replyCreationResponse = MessageUtils.createMessageWithMessageData(reply);
        FeatureRegistry.getCurrentFeature().setData(Message.class, replyTag, replyCreationResponse.as(Message.class));
    }

    @When("Logged in user to delete discussion reply (.*)")
    public void deleteDiscussionReply(String discussionReply) {
        Message messageData = FeatureRegistry.getCurrentFeature().getData(Message.class, discussionReply);
        deleteResponse = MessageUtils.deleteMessage(messageData);
    }

    @Then("^Reply is deleted successfully$")
    public void discussionReplyIsDeleted() {
        deleteResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }
}
