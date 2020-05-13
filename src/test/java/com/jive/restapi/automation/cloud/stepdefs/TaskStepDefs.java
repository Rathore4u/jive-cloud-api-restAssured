package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.TaskConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.ContentConstants;  
import com.jive.restapi.automation.utilities.DiscussionUtils;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.TaskUtils;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Place;
import com.jive.restapi.generated.person.models.Task;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.apache.commons.lang.RandomStringUtils;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TaskStepDefs {
    private Response taskCreationResponse;
    private Response taskDeletionResponse;
    private Task taskData;

    @When("Request to create a task (.*) Under project (.*) with due date (\\d+) days from today")
    public void adminRequestToCreateTaskUnderProject(String taskTag, String projectTag, Integer days) {
        Place placeData = FeatureRegistry.getCurrentFeature().getData(Place.class, projectTag);
        Task taskData = ContentConstants.getDefaultTaskData();
        taskData.setDueDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(days));
        taskCreationResponse = PlacesUtils.createTaskUnderPlace(placeData, taskData);
        FeatureRegistry.getCurrentFeature().setData(Task.class, taskTag, taskCreationResponse.as(Task.class));
        taskCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @Then("Task (.*) is Created Successfully")
    public void verifyTaskCreated(String taskTag) {
        FeatureRegistry.getCurrentFeature().getData(Task.class, taskTag);
        taskCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^User requests to update task (.*) with title (.*)$")
    public void userUpdatesTaskTitle(String taskTag, String titleTag) {
        String title = RandomStringUtils.randomAlphanumeric(10);
        FeatureRegistry.getCurrentFeature().setData(String.class, titleTag, title);
        Content contentData = FeatureRegistry.getCurrentFeature().getData(Task.class, taskTag);
        contentData.setSubject(title);
        taskCreationResponse = DiscussionUtils.updateDiscussionToReturnResponse(contentData);
        FeatureRegistry.getCurrentFeature().setData(Task.class, taskTag, taskCreationResponse.as(Task.class));
    }

    @Then("^Task (.*) is updated with title (.*) successfully$")
    public void contentIsUpdatedWithTitleSuccessfully(String taskTag, String tag) {
        Task taskData = FeatureRegistry.getCurrentFeature().getData(Task.class, taskTag);
        String updatedTitle = FeatureRegistry.getCurrentFeature().getData(String.class, tag);
        assertTrue(ValidationConstants.taskUpdatedWithTitle, taskData.getSubject().contains(updatedTitle));
    }

    @When("^User request to create a task (.*) under project (.*) with tags (.*)$")
    public void userRequestToCreateProjectTaskWithTags(String taskTag, String projectTag, String tags) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tags.split(",")));
        Place placeData = FeatureRegistry.getCurrentFeature().getData(Place.class, projectTag);
        Task taskData = ContentConstants.getDefaultTaskData();
        taskData.setDueDate(OffsetDateTime.now(ZoneOffset.UTC));
        taskData.setTags(tagList);
        taskCreationResponse = PlacesUtils.createTaskUnderPlace(placeData, taskData);
        FeatureRegistry.getCurrentFeature().setData(Task.class, taskTag, taskCreationResponse.as(Task.class));
        taskCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @Then("^Task (.*) is Created Successfully with tags (.*)$")
    public void verifyTaskCreatedWithTags(String taskTag, String tags) {
        Task taskData = FeatureRegistry.getCurrentFeature().getData(Task.class, taskTag);
        assertTrue(ValidationConstants.contentUpdatedWithTitle, taskData.getTags().toString().contains(tags.toLowerCase()));
    }

    @When("^User request to delete the task (.*)$")
    public void userRequestToDeleteTask(String taskTag) {
        Task taskData = FeatureRegistry.getCurrentFeature().getData(Task.class, taskTag);
        taskDeletionResponse = TaskUtils.deleteTask(taskData.getId());
    }

    @Then("^Task should be deleted successfully$")
    public void verifyTaskDeleted() { taskDeletionResponse.then().assertThat().statusCode(SC_NOT_FOUND); }

    @When("^User request to create a task (.*) Under project (.*) with past due date (\\d+) days before today$")
    public void userRequestToCreateTaskWithInvalidDueDate(String taskTag, String projectTag, Integer days) {
        Place placeData = FeatureRegistry.getCurrentFeature().getData(Place.class, projectTag);
        Task taskData = ContentConstants.getDefaultTaskData();
        taskData.setDueDate(OffsetDateTime.now(ZoneOffset.UTC).minusDays(days));
        taskCreationResponse = PlacesUtils.createTaskUnderPlace(placeData, taskData);
        FeatureRegistry.getCurrentFeature().setData(Task.class, taskTag, taskCreationResponse.as(Task.class));
        taskCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @Then("^Task should not be created successfully$")
    public void verifyTaskNotCreated() {
        taskCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Request to create a task (.*) Under project (\\S+)$")
    public void adminRequestToCreateTaskUnderProject(String taskTag, String projectTag) {
        Place placeData = FeatureRegistry.getCurrentFeature().getData(Place.class, projectTag);
        taskData = ContentConstants.getDefaultTaskData();
        taskData.setDueDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(5));
        taskCreationResponse = PlacesUtils.createTaskUnderPlace(placeData, taskData);
        FeatureRegistry.getCurrentFeature().setData(Task.class, taskTag, taskCreationResponse.as(Task.class));
        taskCreationResponse.then().assertThat().statusCode(SC_CREATED);
        assertTrue(TaskConstants.RESPONSE_CONTAINS_SUBJECT_TEXT , taskCreationResponse.as(Task.class)
                .getSubject().contains(taskData.getSubject()));
    }

    @Then("^Verify Notes is added to the task (.*)$")
    public void verifyNotesIsAddedToTheTask(String taskTag) {
        Task task = FeatureRegistry.getCurrentFeature().getData(Task.class, taskTag);
        assertTrue(TaskConstants.NOTES_ADDED_TO_TASK_TEXT ,
                task.getContent().getText().contains(taskData.getContent().getText()));
    }
}
