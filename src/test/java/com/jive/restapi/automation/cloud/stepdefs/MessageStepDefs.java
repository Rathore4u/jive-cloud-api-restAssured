package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.MessageUtils;
import com.jive.restapi.automation.utilities.OutcomeConstants;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.ApiUtils;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.automation.utilities.v3.OutcomesUtil;
import com.jive.restapi.generated.person.api.MessageApi;
import com.jive.restapi.generated.person.models.Message;
import com.jive.restapi.generated.person.models.MessageEntities;
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

public class MessageStepDefs {

    private Response outcomeResponse;

    /**
     * getMessage.
     *
     * Path   : "/messages/{messageID}"
     * Method : get
     * OpId   : getMessage
     * Return the specified content object with the specified fields.
     * @param messageID required parameter
     * @return Message
     */
    private static Response getMessage(
            Integer messageID,
            Options.OptionsBuilder<MessageApi.GetMessageOper>... options) {
        return Options.execute(ApiUtils.apiClient().message()
                .getMessage()
                .messageIDPath(messageID), options);

    }

    private static boolean doesMessageContainOutcome(@NonNull Message message, String outcomeType, int outcomeQty) {
        val result = ((Map<String, Double>)message.getOutcomeCounts())
                .entrySet()
                .stream()
                .anyMatch(item -> item.getKey().equals(outcomeType) && item.getValue() == outcomeQty);

        return result;
    }

    @When("^Mark reply (.*) for action and get outcome (.*)$")
    public void markMessageAsPending(String messageTag, String outcomeTag) {
        Message message = FeatureRegistry.getCurrentFeature().getData(Message.class, messageTag);
        outcomeResponse = MessageUtils.createOutcome(
                message.getId().toString(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.PENDING)
        );
        FeatureRegistry.getCurrentFeature().setData(Outcome.class, outcomeTag, outcomeResponse.as(Outcome.class));
    }

    @When("^Mark reply (.*) for \"Action\" with \"Bring in others to resolve this action item\" option and get outcome (.*):$")
    public void markMessageForActionWithParticipants(String replyTag, String outcomeTag, List<String> usersTags) {
        val usersList = usersTags
                .stream()
                .map(userTag -> FeatureRegistry.getCurrentFeature().getData(Person.class, userTag))
                .collect(Collectors.toList());
        Message messageData = FeatureRegistry.getCurrentFeature().getData(
                Message.class,
                replyTag);

        val outcomeData = OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.PENDING);
        val usersURIs = new ArrayList<String>();
        usersList.forEach(item ->  usersURIs.add(CloudCommonUtils.getPersonUri(item)));
        outcomeData.putPropertiesItem(ContentConsts.APPROVERS_KEY_NAME, usersURIs);
        outcomeResponse = MessageUtils.createOutcome(
                messageData.getId().toString(),
                outcomeData);

        FeatureRegistry.getCurrentFeature().setData(
                Outcome.class,
                outcomeTag,
                outcomeResponse.as(Outcome.class)
        );
    }

    @Then("Outcome (.*) is created for reply (.*)")
    public void verifyOutcomeCreatedForReply(String outcomeTag, String messageTag) {
        Outcome outcome = FeatureRegistry.getCurrentFeature().getData(Outcome.class, outcomeTag);
        Message message = FeatureRegistry.getCurrentFeature().getData(Message.class, messageTag);
        assertTrue(ValidationConstants.OUTCOME_CREATION_FAILED,
                message.getOutcomeTypes().contains(outcome.getOutcomeType()));
    }

    @When("^Mark reply (.*) as decision and get outcome (.*)$")
    public void markMessageAsDecision(String messageTag, String outcomeTag) {
        Message message = FeatureRegistry.getCurrentFeature().getData(Message.class, messageTag);
        outcomeResponse = MessageUtils.createOutcome(
                message.getId().toString(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.DECISION)
        );
        FeatureRegistry.getCurrentFeature().setData(Outcome.class, outcomeTag, outcomeResponse.as(Outcome.class));
    }

    @When("^Mark reply (.*) as success and get outcome (.*)$")
    public void markMessageAsSuccess(String messageTag, String outcomeTag) {
        Message message = FeatureRegistry.getCurrentFeature().getData(Message.class, messageTag);
        outcomeResponse = MessageUtils.createOutcome(message.getId().toString(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.SUCCESS));
        FeatureRegistry.getCurrentFeature().setData(Outcome.class, outcomeTag, outcomeResponse.as(Outcome.class));
    }

    @When("^Unmark reply (.*) as \"Decision\" using outcome (.*)$")
    public void unmarkMessageAsDecision(String replyTag, String outcomeTag) {
        Message messageData = FeatureRegistry.getCurrentFeature().getData(
                Message.class,
                replyTag);
        Outcome outcomeData = FeatureRegistry.getCurrentFeature().getData(
                Outcome.class,
                outcomeTag);

        outcomeResponse = OutcomesUtil.deleteOutcome(outcomeData.getId());
        outcomeResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("^Reply (.*) is unmarked as \"Decision\" successfully$")
    public void messageUnmarkedAsDecision(String replyTag) {
        Message messageData = FeatureRegistry.getCurrentFeature().getData(
                Message.class,
                replyTag);

        assertNull(ValidationConstants.REPLY_DOESNT_CONTAIN_OUTCOMES_VALIDATION,
                messageData.getOutcomeCounts());
    }

    @Then("^Verify that replies list (.*) contains replies with outcomes:$")
    public void verifyMessagesListContainsMessagesWithOutcomes(String listTag, Map<String, Integer> outcomesMap) {
        MessageEntities repliesList = FeatureRegistry.getCurrentFeature().getData(
                MessageEntities.class,
                listTag);

        for(Map.Entry<String, Integer> mapItem : outcomesMap.entrySet()) {
            assertTrue(String.format(ValidationConstants.REPLY_CONTAINS_OUTCOME_VALIDATION, mapItem.getKey()),
                    repliesList
                            .getList()
                            .stream()
                            .allMatch(item -> doesMessageContainOutcome(item, mapItem.getKey(), mapItem.getValue()))
            );
        }
    }

    @Then("^Verify that reply (.*) contains outcomes:$")
    public void verifyMessagesContainsOutcomes(String messageTag, Map<String, Integer> outcomesMap) {
        Message messageData = FeatureRegistry.getCurrentFeature().getData(
                Message.class,
                messageTag);

        Response getMessageResponse = getMessage(messageData.getId());

        for(Map.Entry<String, Integer> mapItem : outcomesMap.entrySet()) {
            assertTrue(String.format(ValidationConstants.REPLY_CONTAINS_OUTCOME_VALIDATION, mapItem.getKey()),
                    doesMessageContainOutcome(getMessageResponse.as(Message.class), mapItem.getKey(), mapItem.getValue()));
        }
    }
}
