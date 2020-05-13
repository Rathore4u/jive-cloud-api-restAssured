@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8725] - [CLOUD E2E TS 010 - Content - Structured Outcomes - Roll Up Comments]

  Background:
    Given We are running E2E JVCLD-8725
    And ADMIN logged into the system

  Scenario: API - [Precondition] Add Replies to a discussion [23998732]
    Given User USER1 has been created
    And User USER1 is logged in
    And Logged in user requests to create a discussion DISCUSSION1 with tags #Tag1
    When Add reply REPLY1 to the discussion DISCUSSION1
    Then Reply has been added to the discussion successfully
    When Add reply REPLY2 to the discussion DISCUSSION1
    Then Reply has been added to the discussion successfully

  Scenario: API - Mark a discussion as Final [23998743]
    Given User USER1 is logged in
    When Mark discussion DISCUSSION1 as "Final" and get outcome OUTCOME1
    Then Outcome OUTCOME1 is created for discussion DISCUSSION1

  Scenario: API - Add Reply to finalized discussion [23998763]
    Given User USER1 is logged in
    When Add reply REPLY3 to the discussion DISCUSSION1
    Then Reply has been added to the discussion successfully

  Scenario: API - [Precondition] Add comments to the document [23998771]
    Given User USER1 is logged in
    And Logged in user requests to create document DOCUMENT1 with tags #Tag2
    When Add a comment COMMENT1 to the document DOCUMENT1
    Then Comment has been added to the document successfully
    When Add a comment COMMENT2 to the document DOCUMENT1
    Then Comment has been added to the document successfully

  Scenario: API - Mark a document as Final [23998776]
    Given User USER1 is logged in
    When Mark document DOCUMENT1 as "Final" and get outcome OUTCOME2
    Then Outcome OUTCOME2 is created for document DOCUMENT1

  Scenario: API - Add a comment to finalized document [23998803]
    Given User USER1 is logged in
    When Add a comment COMMENT3 to the document DOCUMENT1
    Then Comment has been added to the document successfully

  @CREATE-FILE
  Scenario: API - [Precondition] Add comments to binary document [23998813]
    Given User USER1 is logged in
    And Request to create file FILE1
    When Add a comment COMMENT4 to the file FILE1
    Then Comment is added to the file successfully
    When Add a comment COMMENT4 to the file FILE1
    Then Comment is added to the file successfully