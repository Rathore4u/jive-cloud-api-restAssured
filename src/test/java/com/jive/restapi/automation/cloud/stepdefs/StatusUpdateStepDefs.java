package com.jive.restapi.automation.cloud.stepdefs;

import static com.xo.restapi.automation.configs.CloudCommonUtils.getBaseUrl;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertEquals;

import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.FeatureRegistryStepDefs;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Update;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;

public class StatusUpdateStepDefs {

    @When("^Create status update (.*) with link$")
    public void createStatusUpdateWithLink(String updateTag) {
        Content updateData = ContentConstants.getDefaultContentDataOfType(Content.TypeEnum.UPDATE);
        String defaultDescription = "status update " + RandomStringUtils.randomAlphanumeric(4);

        updateData
                .getContent()
                .setText(String.format(ContentConsts.STATUS_UPDATE_TEXT_WITH_LINK_SNIPPET,
                        defaultDescription,
                        getBaseUrl(),
                        ContentConsts.DEFAULT_LINK));
        Response creationResponse = ContentUtil.createContent(
                updateData,
                null);
        creationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Update.class,
                updateTag,
                creationResponse.as(Update.class));
    }

    @Then("^Status update (.*) is created successfully$")
    public void updateIsCreatedSuccessfully(String updateTag) {
        Update updateData = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                updateTag);

        Response getResponse = ContentUtil.getContent(
                updateData.getContentID());

        assertEquals(String.format(ValidationConstants.STATUS_UPDATE_IS_CREATED_VALIDATION,
                updateData.getSubject()),
                getResponse.getStatusCode(), SC_OK);
    }

    @When("^Create status update (.*) with image (.*)")
    public void createStatusUpdateWithImage(String updateTag, String fileName) {
        Content contentData = ContentConstants.getDefaultContentDataOfType(Content.TypeEnum.UPDATE);

        Response creationResponse = ContentUtil.createContent(
                contentData,
                null,
                Options.attachResource(ContentConsts.FILE_RESOURCE_TYPE, fileName, fileName, ContentConsts.CONTENT_TYPE_PNG));
        creationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Update.class,
                updateTag,
                creationResponse.as(Update.class));
    }

    @When("^Create status update (.*) with person (.*) mention$")
    public void createStatusUpdateWithPersonMention(String updateTag, String personTag) {
        Content updateData = ContentConstants.getDefaultContentDataOfType(Content.TypeEnum.UPDATE);
        Person personData = FeatureRegistry.getCurrentFeature().getData(
                Person.class,
                personTag);

        String defaultDescription = "status update " + RandomStringUtils.randomAlphanumeric(4);
        updateData
                .getContent()
                .setText(String.format(ContentConsts.STATUS_UPDATE_TEXT_WITH_PERSON_MENTION_SNIPPET,
                        defaultDescription,
                        getBaseUrl(),
                        personData.getEmails().get(0).getValue(),
                        personData.getName().getGivenName()));
        Response creationResponse = ContentUtil.createContent(
                updateData,
                null);
        creationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Update.class,
                updateTag,
                creationResponse.as(Update.class));
    }

    @When("^Create status update (.*) with tag in (Japanese|Chinese|Thai) language in place (.*)$")
    public void createStatusUpdateWithTagInLanguageInPlace(String updateTag, String languageTag,
            String placeTag) throws Exception {
        Content updateData = ContentConstants.getDefaultContentDataOfType(Content.TypeEnum.UPDATE);
        val placeData = FeatureRegistryStepDefs.getPlaceData(placeTag);
        String languageWord = "";
        switch (languageTag) {
            case ContentConsts.JAPANESE_LANGUAGE_WORD:
                languageWord = ContentConsts.SOME_JAPANESE_WORD;
                break;

            case ContentConsts.CHINESE_LANGUAGE_WORD:
                languageWord = ContentConsts.SOME_CHINESE_WORD;
                break;

            case ContentConsts.THAI_LANGUAGE_WORD:
                languageWord = ContentConsts.SOME_THAI_WORD;
                break;
        }

        updateData
                .parent(CloudCommonUtils.getParentPlaceUri(placeData))
                .visibility(Content.VisibilityEnum.PLACE)
                .addTagsItem(languageWord)
                .getContent()
                .setText(String.format(ContentConsts.STATUS_UPDATE_TEXT_WITH_LANGUAGE_TAG_SNIPPET,
                        getBaseUrl(),
                        URLEncoder.encode(languageWord, StandardCharsets.UTF_8.toString()),
                        languageWord));
        Response creationResponse = ContentUtil.createContent(
                updateData,
                null);
        creationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Update.class,
                updateTag,
                creationResponse.as(Update.class));
    }

    @Then("^Status update (.*) is older than status update (.*)$")
    public void updateOneIsOlderThanUpdateTwo(String update1Tag, String update2Tag) {
        Update update1Data = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                update1Tag);
        Update update2Data = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                update2Tag);

        assertThat(String.format(ValidationConstants.UPDATE_ONE_ELDER_UPDATE_TWO_VALIDATION,
                update1Data.getSubject(),
                update2Data.getSubject()),
                Integer.parseInt(update1Data.getId()),
                lessThan(Integer.parseInt(update2Data.getId())));
    }

    @When("^Delete status update (.*)$")
    public void deleteStatusUpdate(String updateTag) {
        Update updateData = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                updateTag);

        Response deleteResponse = ContentUtil.deleteContent(
                updateData.getContentID());
        deleteResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("^Status update (.*) is deleted successfully$")
    public void updateIsDeletedSuccessfully(String updateTag) {
        Update updateData = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                updateTag);

        Response getResponse = ContentUtil.getContent(
                updateData.getContentID());

        assertEquals(String.format(ValidationConstants.STATUS_UPDATE_IS_DELETED_VALIDATION,
                updateData.getSubject()),
                getResponse.getStatusCode(), SC_NOT_FOUND);
    }
}
