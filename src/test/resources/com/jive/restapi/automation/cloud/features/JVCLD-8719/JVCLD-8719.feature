@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8719] - [CLOUD E2E TS 044 - Content - Polls]

  Background:
    Given We are running E2E JVCLD-8719
    And ADMIN logged into the system

  Scenario: API - Verify that Poll can be created in the project [23209879]
    When Request to create an Project TestProject
    Then Project TestProject is Created Successfully
    When User request to create an Poll TestPollInProject under place TestProject
    Then Poll TestPollInProject is created successfully

  Scenario: API - Verify that Poll can be created in space [23209885]
    When User requests to create a space TestSpace
    Then space is created successfully
    When User request to create an Poll TestPollInSpace under place TestSpace
    Then Poll TestPollInSpace is created successfully

  Scenario: API- Comment on the Poll as UserB, created by the UserA [23209890]
    When User TestUserOne has been created
    And User TestUserTwo has been created
    And User TestUserOne logs in
    And User request to create an Poll TestPollComment with extended author TestUserTwo
    Then Poll TestPollComment is created successfully
    When User TestUserTwo logs in
    And Add a comment CommentTag to the poll TestPollComment
    Then Comment has been added to the poll successfully

  Scenario: API - Verify that Poll can be visible with Specific Person (User B) [23209887]
    When User TestUserPollOne has been created
    And User TestUserPollTwo has been created
    And User TestUserPollOne logs in
    And User request to create an Poll PollShare with extended author TestUserPollTwo
    Then Poll PollShare is created successfully
    When User TestUserPollTwo logs in
    And User request to search poll PollShare by pollSearch
    Then It should returns poll pollSearch
    