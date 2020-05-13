@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-16746] - [E2E test scenario for JVCLD-1534]

  Background:
    Given We are running E2E JVCLD-16746
    And ADMIN logged into the system
    And User FOO has been created
    And User FOO logs in
    And Logged in user requests to create document DOCFOO with tags test\upgrade
    Then Document DOCFOO is created successfully

  Scenario: Verify that user is able to create/edit "\" tag in a document [23006381]
    When Logged in user requests to update document DOCFOO with tags test\upgrade,NewTag
    Then Document DOCFOO is updated with tags test\upgrade successfully

  Scenario: API - Verify that user is able to edit/delete document with "\" tag [23162771]
    Given Search document DOCFOO by subject from spotlight using 3 attempts
    Then Document DOCFOO is searched successfully
    When User request to delete document DOCFOO
    Then Document DOCFOO is deleted successfully

  Scenario: API - Verify that user is able to create/edit "\" tag in a Discussion [23162804]
    Given Logged in user requests to create a discussion DISFOO with tags test\upgrade
    Then Discussion DISFOO is created successfully
    When Logged in user requests to update a discussion DISFOO with tags test\upgrade,NewTag
    Then Discussion DISFOO is updated with tags test\upgrade successfully

  Scenario: API - Verify that user is able to edit/delete Discussion with "\" tag [23162815]
    Given Logged in user requests to create a discussion DISFOO with tags test\upgrade
    Then Discussion DISFOO is created successfully
    When User request to search discussion DISFOO by subject from spotlight
    Then Discussion DISFOO is searched successfully
    When User request to delete discussion DISFOO
    Then Discussion DISFOO is deleted successfully

  Scenario: API - Verify that user is able to create/edit "\" tag in a Blog Post [23162860]
    Given Logged in user requests to create post POSTFOO with tags test\upgrade
    Then Post POSTFOO is created successfully
    When Logged in user requests to update post POSTFOO with tags test\upgrade,NewTag
    Then Post POSTFOO is updated with tags test\upgrade successfully

  Scenario: API- Verify that user is able to edit/delete Blog Post with "\" tag [23162863]
    Given Logged in user requests to create post POSTFOO with tags test\upgrade
    Then Post POSTFOO is created successfully
    When User request to search post POSTFOO by subject from spotlight
    Then Post POSTFOO is searched successfully
    When User request to delete post POSTFOO
    Then Post POSTFOO is deleted successfully

  Scenario: API - Verify that user is able to create/edit "\" tag in a Event [23162909]
    When Logged in user requests to create event EVENTFOO with tags test\upgrade
    Then Event EVENTFOO is created successfully
    When Logged in user requests to update event EVENTFOO with tags test\upgrade,NewTag
    Then Event EVENTFOO is updated with tags test\upgrade successfully

  Scenario: API - Verify that user is able to edit/delete Event with "\" tag [23162929]
    Given Logged in user requests to create event EVENTFOO with tags test\upgrade
    Then Event EVENTFOO is created successfully
    When User request to search event EVENTFOO by subject from spotlight
    Then Event EVENTFOO is searched successfully
    When User request to delete event EVENTFOO
    Then Event EVENTFOO is deleted successfully

  Scenario: API - Verify that user is able to create/edit "\" tag in a Idea [23162952]
    When Logged in user requests to create idea IDEAFOO with tags test\upgrade
    Then Idea IDEAFOO is created successfully
    When Logged in user requests to update idea IDEAFOO with tags test\upgrade,NewTag
    Then Idea IDEAFOO is updated with tags test\upgrade successfully

  Scenario: API - Verify that user is able to edit/delete Idea with "\" tag [23162958]
    Given Logged in user requests to create idea IDEAFOO with tags test\upgrade
    Then Idea IDEAFOO is created successfully
    When User request to search idea IDEAFOO by subject from spotlight
    Then Idea IDEAFOO is searched successfully
    When User request to delete idea IDEAFOO
    Then Idea IDEAFOO is deleted successfully

  Scenario: API - Verify that user is able to create/edit "\" tag in a Question [23162850]
    When Logged in user requests to create question QUESTIONFOO with tags test\upgrade
    Then Question QUESTIONFOO is created successfully
    When Logged in user requests to update question QUESTIONFOO with tags test\upgrade,NewTag
    Then Question QUESTIONFOO is updated with tags test\upgrade successfully

  Scenario: API - Verify that user is able to edit/delete Question with "\" tag [23164253]
    Given Logged in user requests to create question QUESTIONFOO with tags test\upgrade
    Then Question QUESTIONFOO is created successfully
    When User request to search question QUESTIONFOO by subject from spotlight
    Then Question QUESTIONFOO is searched successfully
    When User request to delete question QUESTIONFOO
    Then Question QUESTIONFOO is deleted successfully
