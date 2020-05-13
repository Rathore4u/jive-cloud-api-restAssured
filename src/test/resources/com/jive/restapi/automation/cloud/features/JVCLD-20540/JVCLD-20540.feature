@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-20540]

  Background:
    Given We are running E2E JVCLD-20481

  Scenario: API - Verify that user is able to see comment in Question [23202620]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And User create question Q1
    When User add reply REPLY1 on question Q1
    Then Reply is created successfully
    When User search question's "Q1" replies
    Then Question contains reply REPLY1

  Scenario: API - Verify that user is able to see comment in Discussion [23202634]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And Request to create a discussion D1
    When User add reply REPLY1 on discussion D1
    Then Reply is created successfully
    When User search discussion's "D1" replies
    Then Discussion contains reply REPLY1

  Scenario: API - Verify that user is able to see comment in Blog Post [23202635]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And Request to create a BlogPost BP1
    When User add comment COMMENT1 on blog post BP1
    Then Comment is created successfully
    When User search blog post's "BP1" comments
    Then Blog Post contains comment COMMENT1

  @CREATE-FILE
  Scenario: API - Verify that user is able to see comment in File [23202650]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And Request to create file FILE1
    When User add comment COMMENT1 on file FILE1
    Then Comment is created successfully
    When User search file's "FILE1" comments
    Then File contains comment COMMENT1

  Scenario: API - Verify that user is able to see comment in Document [23202629]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And Request to create a document DOC1
    When User add comment COMMENT1 on document DOC1
    Then Comment is created successfully
    When User search document's "DOC1" comments
    Then Document contains comment COMMENT1

  Scenario: API - Verify that user is able to see comment in Poll [23204755]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And Request to create a Poll POLL1
    When User add comment COMMENT1 on poll POLL1
    Then Comment is created successfully
    When User search poll's "POLL1" comments
    Then Poll contains comment COMMENT1

  @CREATE-VIDEO
  @FailFixRCA
  Scenario: API - Verify that user is able to see comment in Video [23204778]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And Request to create video VIDEO1
    When User add comment COMMENT1 on video VIDEO1
    Then Comment is created successfully
    When User search video's "VIDEO1" comments
    Then Video contains comment COMMENT1