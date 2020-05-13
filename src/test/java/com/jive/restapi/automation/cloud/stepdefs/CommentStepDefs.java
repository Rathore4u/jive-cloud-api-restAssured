package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.OutcomeConstants;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.CommentsUtil;
import com.jive.restapi.automation.utilities.v3.OutcomesUtil;
import com.jive.restapi.generated.person.models.Comment;
import com.jive.restapi.generated.person.models.CommentEntities;
import com.jive.restapi.generated.person.models.Outcome;
import com.jive.restapi.generated.person.models.Person;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.val;

public class CommentStepDefs {

    private Response outcomeResponse;
    private Response commentResponse;
    private Response likeResponse;

    private static boolean isCommentContainsOutcome(@NonNull Comment comment, String outcomeType, int outcomeQty) {
        boolean result = false;

        for(Map.Entry<String, Integer> item : comment.getOutcomeCounts().entrySet()){
            if (item.getKey().equals(outcomeType) && item.getValue() == outcomeQty) {
                result = true;
            }
        }

        return result;
    }

    @When("^Mark comment (.*) as \"Decision\" and get outcome (.*)$")
    public void markCommentAsDecision(String commentTag, String outcomeTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(Comment.class, commentTag);
        outcomeResponse = CommentsUtil.createOutcome(
                comment.getId(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.DECISION)
        );
        FeatureRegistry.getCurrentFeature().setData(
                Outcome.class,
                outcomeTag,
                outcomeResponse.as(Outcome.class)
        );
    }

    @Then("^Comment has been marked as \"Decision\" successfully with creation of outcome (.*)$")
    public void commentMarkedAsDecision(String outcomeTag) {
        Outcome outcomeData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeTag);
        outcomeResponse = OutcomesUtil.getOutcome(outcomeData.getId());
        outcomeResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Unmark comment (.*) as \"Decision\" using outcome (.*)$")
    public void unmarkCommentAsDecision(String commentTag, String outcomeTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(Comment.class, commentTag);
        Outcome outcomeData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeTag);
        outcomeResponse = OutcomesUtil.deleteOutcome(outcomeData.getId());
        outcomeResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("^Comment has been unmarked as \"Decision\" successfully using outcome (.*)$")
    public void commentUnmarkedAsDecision(String outcomeTag) {
        Outcome outcomeData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeTag);
        outcomeResponse = OutcomesUtil.getOutcome(outcomeData.getId());
        outcomeResponse.then().assertThat().statusCode(SC_NOT_FOUND);
    }

    @When("^User request to mark comment (.*) as Success and get outcome (.*)$")
    public void markCommentAsSuccess(String commentTag, String outcomeTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(Comment.class, commentTag);
        outcomeResponse = CommentsUtil.createOutcome(comment.getId(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.SUCCESS));
        FeatureRegistry.getCurrentFeature().setData(Outcome.class, outcomeTag, outcomeResponse.as(Outcome.class));
    }

    @Then("^Comment has been marked as outcomeType successfully with creation of outcome (.*)$")
    public void commentMarkedAsSuccess(String outcomeTag) {
        Outcome outcomeData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeTag);
        outcomeResponse = OutcomesUtil.getOutcome(outcomeData.getId());
        outcomeResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Mark comment (.*) for \"Action\" and get outcome (.*)$")
    public void markCommentForAction(String commentTag, String outcomeTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(
                Comment.class,
                commentTag);

        outcomeResponse = CommentsUtil.createOutcome(
                comment.getId(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.PENDING)
        );

        FeatureRegistry.getCurrentFeature().setData(
                Outcome.class,
                outcomeTag,
                outcomeResponse.as(Outcome.class)
        );
    }

    @When("^Mark comment (.*) for \"Action\" with \"Bring in others to resolve this action item\" option and get outcome (.*):$")
    public void markCommentForActionWithParticipants(String commentTag, String outcomeTag, List<String> usersTags) {
        val usersList = usersTags
                .stream()
                .map(userTag -> FeatureRegistry.getCurrentFeature().getData(Person.class, userTag))
                .collect(Collectors.toList());
        Comment comment = FeatureRegistry.getCurrentFeature().getData(
                Comment.class,
                commentTag);

        val outcomeData = OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.PENDING);
        val usersURIs = new ArrayList<String>();
        usersList.forEach(item ->  usersURIs.add(CloudCommonUtils.getPersonUri(item)));
        outcomeData.putPropertiesItem(ContentConsts.APPROVERS_KEY_NAME, usersURIs);
        outcomeResponse = CommentsUtil.createOutcome(
                comment.getId(),
                outcomeData);

        FeatureRegistry.getCurrentFeature().setData(
                Outcome.class,
                outcomeTag,
                outcomeResponse.as(Outcome.class)
        );
    }

    @When("User request to search comment (.*) by subject from (.*)")
    public void searchByCommentSubject(String contentTag, String origin) {
        Comment commentData = FeatureRegistry.getCurrentFeature().getData(Comment.class, contentTag);
        Response resp = SearchUtils.searchContent(new FilterBuilder()
                .addCondition(SearchFilterConstants.searchFilterKey, commentData.getSubject())
                .buildList(), origin);
        resp.then().statusCode(SC_OK);
    }

    @Then("^Verify that comments list (.*) contains (.*) comments$")
    public void verifyCommentsListContainsNObjects(String listTag, String commentsQty) {
        CommentEntities commentsList = FeatureRegistry.getCurrentFeature().getData(CommentEntities.class, listTag);
        assertEquals(commentsList.getList().size(), Integer.parseInt(commentsQty));
    }

    @Then("^Verify that comments list (.*) contains comments with outcomes:$")
    public void verifyCommentsListContainsCommentsWithOutcomes(String listTag, Map<String, Integer> outcomesMap) {
        CommentEntities commentsList = FeatureRegistry.getCurrentFeature().getData(
                CommentEntities.class,
                listTag);

        for(Map.Entry<String, Integer> mapItem : outcomesMap.entrySet()) {
            assertTrue(String.format(ValidationConstants.COMMENT_CONTAINS_OUTCOME_VALIDATION, mapItem.getKey()),
                    commentsList
                    .getList()
                    .stream()
                    .allMatch(item -> isCommentContainsOutcome(item, mapItem.getKey(), mapItem.getValue()))
            );
        }
    }

    @Then("^Verify that comment (.*) contains outcomes:$")
    public void verifyCommentContainsOutcomes(String commentTag, Map<String, Integer> outcomesMap) {
        Comment commentData = FeatureRegistry.getCurrentFeature().getData(
                Comment.class,
                commentTag);

        Response getCommentResponse = CommentsUtil.getComment(commentData.getId());

        for(Map.Entry<String, Integer> mapItem : outcomesMap.entrySet()) {
            assertTrue(String.format(ValidationConstants.COMMENT_CONTAINS_OUTCOME_VALIDATION, mapItem.getKey()),
                    isCommentContainsOutcome(getCommentResponse.as(Comment.class), mapItem.getKey(), mapItem.getValue()));
        }
    }

    @Then("^Verify that comments list (.*) contains comment (.*)$")
    public void verifyCommentsListContainsComment(String listTag, String commentTag) {
        CommentEntities commentsList = FeatureRegistry.getCurrentFeature().getData(
                CommentEntities.class,
                listTag);
        Comment comment = FeatureRegistry.getCurrentFeature().getData(
                Comment.class,
                commentTag);

        assertTrue(commentsList
                .getList()
                .stream()
                .anyMatch(item -> item.getId().equals(comment.getId())));
    }

    @When("^Delete comment (.*)$")
    public void deleteComment(String commentTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(
                Comment.class,
                commentTag);

        commentResponse = CommentsUtil.deleteComment(
                comment.getId());
        commentResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("^Comment deleted successfully$")
    public void commentDeletedSuccessfully() {
        commentResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^Like comment (.*)")
    public void likeComment(String commentTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(
                Comment.class,
                commentTag);

        likeResponse = CommentsUtil.createCommentLike(
                comment.getId());
        likeResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("^Comment is liked successfully$")
    public void commentLikedSuccessfully() {
        likeResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^Unlike comment (.*)")
    public void unlikeComment(String commentTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(
                Comment.class,
                commentTag);

        likeResponse = CommentsUtil.deleteCommentLike(
                comment.getId());
        likeResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("^Comment is unliked successfully$")
    public void commentUnlikedSuccessfully() {
        likeResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^Add reply (.*) to the comment (.*)$")
    public void replyToComment(String replyTag, String commentTag) {
        Comment comment = FeatureRegistry.getCurrentFeature().getData(Comment.class, commentTag);

        commentResponse = CommentsUtil.createReply(
                comment.getId(),
                ContentConstants.getDefaultCommentData());
        commentResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Comment.class,
                replyTag,
                commentResponse.as(Comment.class));
    }

    @Then("^Reply is added to the comment successfully$")
    public void commentAddedSuccessfully() {
        commentResponse.then().assertThat().statusCode(SC_CREATED);
    }
}
