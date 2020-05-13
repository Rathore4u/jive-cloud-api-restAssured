package com.jive.restapi.automation.cloud.stepdefs;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.assertEquals;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;

import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.DirectMessageUtils;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.DirectMessage;
import com.jive.restapi.generated.person.models.DirectMessageRequest;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Attachment;
import com.jive.restapi.generated.person.models.ImageEntities;
import com.xo.restapi.automation.configs.CloudCommonUtils;

import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import java.util.List;
import java.util.Map;



public class DirectMessageStepDefs {
    private Response directMessageResponse;
    private static final String IMAGE = "image";
    private static final String DM_ENTITY = "DirectMessage";

    @When("Create a direct message to ([a-zA-Z0-9]+)")
    public void createDirectMessage(String userTag) {
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        DirectMessageRequest dmRequest = ContentConstants.getDefaultDirectMessageRequestData();
        dmRequest.addParticipantsItem(CloudCommonUtils.getPersonUri(user));
        directMessageResponse = DirectMessageUtils.sendDirectMessage(dmRequest);
    }

    @Then("Direct message created successfully")
    public void directMessageIsCreatedSuccessfully() {
        directMessageResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^User send Direct Message \"(\\S+)\" with (?:animated gif|JPG image) to user \"(\\S+)\"$")
    public void createDirectMessageWithImage(String dmTag, String userTag, DataTable data) {
        List<Map<String, String>> rows = data.asMaps(String.class, String.class);
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        DirectMessageRequest dmRequest = ContentConstants.getDefaultDirectMessageRequestData();
        dmRequest.addParticipantsItem(CloudCommonUtils.getPersonUri(user));

        rows.forEach(p -> {
            Attachment attachment = new Attachment();
            attachment.setDoUpload(true);
            attachment.setUrl(p.get(IMAGE));
            dmRequest.addAttachmentsItem(attachment);
        });

        directMessageResponse = DirectMessageUtils.sendDirectMessage(dmRequest);
        FeatureRegistry.getCurrentFeature().setData(DirectMessageRequest.class, dmTag, dmRequest);
    }

    @When("^User send Direct Message (\\S+) to user (\\S+)$")
    public void createDirectMessageToUser(String dmTag, String userTag) {
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        DirectMessageRequest dmRequest = ContentConstants.getDefaultDirectMessageRequestData();
        dmRequest.addParticipantsItem(CloudCommonUtils.getPersonUri(user));
        directMessageResponse = DirectMessageUtils.sendDirectMessage(dmRequest);
        FeatureRegistry.getCurrentFeature().setData(DirectMessageRequest.class, dmTag, dmRequest);
    }

    @Then("^Direct message \"(\\S+)\" sent successfully$")
    public void directMessageSentSuccessfully(String dmTag) {
        directMessageResponse.then().assertThat().statusCode(SC_CREATED);
        DirectMessage directMessage = directMessageResponse.as(DirectMessage.class);
        DirectMessageRequest directMessageRequest = FeatureRegistry.getCurrentFeature()
                .getData(DirectMessageRequest.class, dmTag);
        assertEquals(ValidationConstants.getErrorMessage(DM_ENTITY, ValidationConstants.CREATED),
                directMessage.getSubject(),
                directMessageRequest.getSubject());
        FeatureRegistry.getCurrentFeature().setData(DirectMessage.class, dmTag, directMessage);
    }

    @Then("^Direct message has attached images$")
    public void directMessageHasAttachedImages(DataTable data) {
        List<Map<String, String>> rows = data.asMaps(String.class, String.class);
        DirectMessage directMessage = directMessageResponse.as(DirectMessage.class);
        ImageEntities images = DirectMessageUtils.getDirectMessageContentImages(directMessage).as(ImageEntities.class);
        images.getList().forEach((q) ->
                assertTrue(ValidationConstants.getErrorMessage(IMAGE.toUpperCase(), ValidationConstants.CREATED),
                        rows.stream().anyMatch((p) -> q.getName().equals(p.get(IMAGE)))));
    }

    @When("^User request direct message \"(\\S+)\"$")
    public void userRequestDirectMessage(String dmTag) {
        DirectMessage savedDirectMessage = FeatureRegistry.getCurrentFeature().getData(DirectMessage.class, dmTag);
        directMessageResponse = DirectMessageUtils.getDirectMessageById(savedDirectMessage.getId());
        directMessageResponse.then().assertThat().statusCode(SC_OK);
        FeatureRegistry.getCurrentFeature().setData(DirectMessage.class, dmTag, directMessageResponse.as(DirectMessage.class));
    }

    @When("Create a direct message to ([a-zA-Z0-9]+) with description (.*)")
    public void createDirectMessageWithDescription(String userTag, String description) {
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        DirectMessageRequest dmRequest = ContentConstants.getDefaultDirectMessageRequestData();
        dmRequest.addParticipantsItem(CloudCommonUtils.getPersonUri(user)).content((new ContentBody()).text(description));
        directMessageResponse = DirectMessageUtils.sendDirectMessage(dmRequest);
    }
}
