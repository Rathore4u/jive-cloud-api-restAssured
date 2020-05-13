package com.jive.restapi.automation.cloud.stepdefs;

import static org.junit.Assert.assertNotNull;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.DocumentUtils;
import com.jive.restapi.generated.person.models.Category;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Document;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.Place;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;

public class DocumentExtendedStepDefs {

    @When("User requests to get pdf of document (.*) and gets response (.*)")
    public void getPdf(String documentTag, String response) throws Throwable {
        Content document = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        String pdfUrl = document.getResources().get("pdf").getRef();
        Response pdfURlResp = RestAssured.get(new URL(pdfUrl));
        FeatureRegistry.getCurrentFeature().setData(int.class, response, pdfURlResp.getStatusCode());
    }

    @Then("^Notification of document ([a-zA-Z]+) is present in inbox (.*)$")
    public void verifyNotificationHasDocument(String documentTag, String inboxTag) {
        InboxEntry inbox = FeatureRegistry.getCurrentFeature().getData(InboxEntry.class, inboxTag);
        Document documentData = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        assertNotNull(documentData.getSubject(), inbox.getUnread());
    }

    @When("User requests to create a document (.*) under place (\\S+)")
    public void createDocumentUnderPlace(String documentTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Document documentData = ContentConstants.getDefaultDocumentDataUnderPlace(place);
        Response createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature().setData(Document.class, documentTag,
                createDocumentResponse.as(Document.class));
    }

    @When("User requests to create a document (.*) under place (.*) with category (\\S+)")
    public void createDocumentUnderPlaceWithCategory(String documentTag, String placeTag, String categoryTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Category category = FeatureRegistry.getCurrentFeature().getData(Category.class, categoryTag);
        Document documentData = ContentConstants.getDefaultDocumentDataUnderPlace(place);
        List<String> categoryList = new ArrayList<>(1);
        categoryList.add(category.getName());
        documentData.categories(categoryList).subject(RandomStringUtils.randomAlphanumeric(8));
        Response createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature().setData(Document.class, documentTag,
                createDocumentResponse.as(Document.class));
    }

    @When("User requests to create a document (.*) under place (.*) with category (.*) and tag (.*)")
    public void createDocumentUnderPlaceWithCategoryAndTag(String documentTag,
            String placeTag, String categoryTag, String tag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Category category = FeatureRegistry.getCurrentFeature().getData(Category.class, categoryTag);
        Document documentData = ContentConstants.getDefaultDocumentDataUnderPlace(place);
        List<String> categoryList = new ArrayList<>(1);
        categoryList.add(category.getName());
        List<String> tagList = new ArrayList<>(1);
        tagList.add(tag);
        documentData.categories(categoryList).subject(RandomStringUtils.randomAlphanumeric(8)).tags(tagList);
        Response createDocumentResponse = DocumentUtils.createDocumentToReturnResponse(documentData);
        FeatureRegistry.getCurrentFeature().setData(Document.class, documentTag,
                createDocumentResponse.as(Document.class));
    }
}
