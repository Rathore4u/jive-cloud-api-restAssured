package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.FeatureRegistryStepDefs;
import com.jive.restapi.automation.utilities.v3.ActivitiesUtil;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.generated.person.models.Activity;
import com.jive.restapi.generated.person.models.ActivityEntities;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.function.Predicate;
import lombok.val;

public class ActivityStepDefs {
    @When("^Get latest (.*) activit(?:y|ies) (.*) from all activities$")
    public void getLatestActivities(String quantityTag, String activitiesTag) throws InterruptedException {
        // Static wait is used to give time for content to appear in activities
        Thread.sleep(TimeoutConstants.S);
        Response getActivitiesResponse = ActivitiesUtil.getActivity(
                Options.custom(op -> op.countQuery(Integer.parseInt(quantityTag))));
        getActivitiesResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                ActivityEntities.class,
                activitiesTag,
                getActivitiesResponse.as(ActivityEntities.class));
    }

    @Then("^Activities (.*) (dont-contain|contain) content (.*)$")
    public void verifyActivitiesContainContent(String activitiesTag, String equalityTag, String contentTag) {
        ActivityEntities activities = FeatureRegistry.getCurrentFeature().getData(
                ActivityEntities.class,
                activitiesTag);
        val contentData = FeatureRegistryStepDefs.getContentData(contentTag);

        Predicate<Activity> predicate = activity ->
                activity
                        .getObject()
                        .getDisplayName()
                        .replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING)
                        .contains(contentData.getSubject()
                                .replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING));

        val activityList = activities.getList();
        val errorMessage = String.format(equalityTag.contains(ContentConsts.DONT_WORD)
                ? ValidationConstants.ACTIVITIES_DONT_CONTAIN_CONTENT_VALIDATION
                : ValidationConstants.ACTIVITIES_CONTAIN_CONTENT_VALIDATION,
                contentData.getSubject());

        assertTrue(
                errorMessage,
                equalityTag.contains(ContentConsts.DONT_WORD)
                        ? activityList.stream().noneMatch(predicate)
                        : activityList.stream().anyMatch(predicate));
    }
}
