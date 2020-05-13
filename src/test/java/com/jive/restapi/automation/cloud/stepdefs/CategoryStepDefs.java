package com.jive.restapi.automation.cloud.stepdefs;

import static org.junit.Assert.assertEquals;

import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.generated.person.models.Category;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.Place;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;

public class CategoryStepDefs {
    private Response creationResponse;
    private Response searchResponse;

    @When("User creates category (.*) in place (\\S+)")
    public void createCategoryInPlace(String categoryTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        creationResponse = PlacesUtils.createCategory(place.getPlaceID(),
                "Category" + RandomStringUtils.randomAlphanumeric(8));
        FeatureRegistry.getCurrentFeature().setData(Category.class, categoryTag, creationResponse.as(Category.class));
    }

    @Then("Category is created successfully")
    public void verifyCategoryCreated() {
        assertEquals(creationResponse.getStatusLine(), HttpStatus.SC_CREATED, creationResponse.getStatusCode());
    }

    @When("User requests to get contents by category (.*) from place (.*)")
    public void getDiscussionByOutcomeFromPlace(String categoryTag, String placeTag)
            throws InterruptedException {
        Category category = FeatureRegistry.getCurrentFeature().getData(Category.class, categoryTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = PlacesUtils.getContentWithFilters(place.getPlaceID(),
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.CATEGORY_FILTER_KEY, category.getId())
                            .buildList());
            if (searchResponse.as(ContentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^Verify count of contents by category is (\\d+)$")
    public void verifyContentByCategoryCount(int expectedCount) {
        assertEquals(ValidationConstants.CONTENT_COUNT_MISMATCH,
                searchResponse.as(ContentEntities.class).getList().size(), expectedCount);
    }
}
