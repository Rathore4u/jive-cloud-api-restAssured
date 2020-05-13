@APISuite
@TestMe
Feature: [CLOUD API Experimental Suite] - [JVCLD-8153] - [CLOUD E2E TS 048 - Content - Tasks]

  Background:
    Given We are running E2E JVCLD-8153
    And ADMIN logged into the system
    
  Scenario: API - Verify user is able to edit project task [23189214]
    When Request to create an Project ProjectTask
    Then Project ProjectTask is Created Successfully
    When Request to create a task TaskEditTitle Under project ProjectTask with due date 2 days from today
    Then Task TaskEditTitle is Created Successfully
    When User requests to update task TaskEditTitle with title UpdatedTitle
    Then Task TaskEditTitle is updated with title UpdatedTitle successfully

  Scenario: API - Verify user is able to add 'Tags' in the advanced option [23191071]
    When Request to create an Project ProjectTaskTags
    Then Project ProjectTaskTags is Created Successfully
    When User request to create a task TaskTags under project ProjectTaskTags with tags TestTag
    Then Task TaskTags is Created Successfully with tags TestTag

  Scenario: API - Verify user is able to Delete task from project calendar [23191074]
    When Request to create an Project ProjectTaskDelete
    Then Project ProjectTaskDelete is Created Successfully
    When Request to create a task TaskDeleteTitle Under project ProjectTaskDelete with due date 2 days from today
    Then Task TaskDeleteTitle is Created Successfully
    When User request to delete the task TaskDeleteTitle
    Then Task should be deleted successfully

  Scenario: API - Verify user is able to add 'Notes' in the advanced option - [23191070]
    When Request to create an Project PROJECT
    Then Project PROJECT is Created Successfully
    When Request to create a task TASK Under project PROJECT
    Then Verify Notes is added to the task TASK