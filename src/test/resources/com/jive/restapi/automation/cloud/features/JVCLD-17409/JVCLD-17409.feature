@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-17409]

  Background:
    Given We are running E2E JVCLD-17409
    When ADMIN logged into the system
    And User USER1 has been created
    Then User USER1 is logged in

  Scenario: API - Verify that user1 is following his own created document by default - [25501672]
    When Logged in user requests to create document DOCUMENT1 with tags tagone
    Then Verify User USER1 is following DOCUMENT1 by default

  Scenario: API - Verify that user1 is following his own created discussion by default - [25501673]
    When Logged in user requests to create a discussion DISCUSSION1 with tags tagone,tagtwo
    Then Verify User USER1 is following DISCUSSION1 by default

  Scenario: API - Verify that user1 is following his own created poll by default - [25501674]
    When Logged in user requests to create a Poll POLL with tags tagone,tagtwo
    Then Verify User USER1 is following POLL by default

  Scenario: API -Verify that user1 is following his own created blog post by default - [25501675]
    When Logged in user requests to create BlogPost BLOGPOST1 with description BLOGTESTDESC1 in community
    Then Verify User USER1 is following BLOGPOST1 by default

  Scenario: API - Verify that user1 is following his own created question by default - [25501676]
    When Logged in user requests to create question QUESTION1 with tags tagone,tagtwo
    Then Verify User USER1 is following QUESTION1 by default

  Scenario: API - Verify that user2 by default isn't following blog post created by user1 - [25501677]
    Given ADMIN logged into the system
    When User USER2 has been created
    Then Verify User USER2 is NOT following BLOGPOST1 by default

  Scenario: API - Verify that user2 by default isn't following document created by user1 - [25501678]
    Given ADMIN logged into the system
    When User USER2 has been created
    Then Verify User USER2 is NOT following DOCUMENT1 by default

  Scenario: API - Verify that user2 by default isn't following discussion created by user1 - [25501679]
    Given ADMIN logged into the system
    When User USER2 has been created
    Then Verify User USER2 is NOT following DISCUSSION1 by default
