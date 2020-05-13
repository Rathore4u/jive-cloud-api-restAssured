@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-18905] - [E2E test scenario for JVCLD-831]

  Background:
    Given We are running E2E JVCLD-18905
    Given ADMIN logged into the system

  Scenario: API - Create a task (Task1) with a future due date [23175548]
    When Request to create an Project PROJECTFOO
    Then Project PROJECTFOO is Created Successfully
    When Request to create a task TASKFOO Under project PROJECTFOO with due date 2 days from today
    Then Task TASKFOO is Created Successfully
    When Enable Overview tab visibility for project PROJECTFOO

  Scenario: API- Create a task (Task2) with due date as second day after the first task's due date [23175863]
    When Request to create a task TASKBAR Under project PROJECTFOO with due date 4 days from today
    Then Task TASKBAR is Created Successfully
