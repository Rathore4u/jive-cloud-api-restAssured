@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-9789] - [CLOUD E2E TS 007 - Content - Structured Outcomes - Mark for Action - Part B]

  Background:
    Given We are running E2E JVCLD-9789
    And ADMIN logged into the system

  Scenario: API - Precondition - Create document as User1 and add comment as User2 [23968377]
    Given User USER1 has been created
    And User USER2 has been created
    And User USER1 is logged in
    When Logged in user requests to create document DOCUMENT1 with tags #Tag2
    Then Document DOCUMENT1 is created successfully
    When User USER2 is logged in
    And Add a comment COMMENT1 to the document DOCUMENT1
    Then Comment has been added to the document successfully

  Scenario: API - Verify User2 is able to select action "Mark for Action" for comment for document [23968398]
    Given User USER2 is logged in
    When Mark comment COMMENT1 for "Action" and get outcome OUTCOME1
    Then Comment has been marked as outcomeType successfully with creation of outcome OUTCOME1

  Scenario: API - Verify User2 is able to resolve action item for document [23968420]
    Given User USER2 is logged in
    When Resolve action item OUTCOME1 and get outcome OUTCOME2
    Then Verify that comment COMMENT1 contains outcomes:
      | resolved | 1 |

  Scenario: API - Verify User2 is able to "Mark for Action" for User3 using "Bring in others to resolve this action item" checkbox for document [23968439]
    Given User USER3 has been created
    And User USER1 is logged in
    And Logged in user requests to create document DOCUMENT2 with tags #Tag2
    And User USER2 is logged in
    And Add a comment COMMENT3 to the document DOCUMENT2
    When Mark comment COMMENT3 for "Action" with "Bring in others to resolve this action item" option and get outcome OUTCOME7:
      | USER2 |
      | USER3 |
    Then Comment has been marked as outcomeType successfully with creation of outcome OUTCOME7

  Scenario: API - Verify User2 is able to "Mark for Action" from before adding comment for document [23968653]
    Given User USER2 is logged in
    And Add a comment COMMENT2 to the document DOCUMENT1
    Then Comment has been added to the document successfully
    When Mark comment COMMENT2 for "Action" and get outcome OUTCOME3
    Then Comment has been marked as outcomeType successfully with creation of outcome OUTCOME3

  Scenario: API - Precondition - Create discussion as User1 and add reply as User2 [23968676]
    Given User USER1 is logged in
    When Logged in user requests to create a discussion DISCUSSION1 with tags #Tag1
    Then Discussion DISCUSSION1 is created successfully
    When User USER2 is logged in
    And Add reply REPLY1 to the discussion DISCUSSION1
    Then Reply has been added to the discussion successfully

  Scenario: API - Verify User2 is able to select action "Mark for Action" for reply for discussion [23968690]
    Given User USER2 is logged in
    When Mark reply REPLY1 for action and get outcome OUTCOME4
    Then Outcome OUTCOME4 is created for reply REPLY1

  Scenario: API - Verify User2 is able to resolve action item for discussion [23968742]
    Given User USER2 is logged in
    When Resolve action item OUTCOME4 and get outcome OUTCOME5
    Then Verify that reply REPLY1 contains outcomes:
      | resolved | 1 |

  Scenario: API - Verify User2 is able to "Mark for Action" for User3 using "Bring in others to resolve this action item" checkbox for discussion [23968768]
    Given User USER1 is logged in
    And Logged in user requests to create a discussion DISCUSSION2 with tags #Tag1
    And User USER2 is logged in
    And Add reply REPLY3 to the discussion DISCUSSION2
    And Mark reply REPLY3 for "Action" with "Bring in others to resolve this action item" option and get outcome OUTCOME8:
      | USER2 |
      | USER3 |
    Then Outcome OUTCOME8 is created for reply REPLY3

  Scenario: API - Verify User2 is able to "Mark for Action" from before adding comment for discussion [23968889]
    Given User USER2 is logged in
    And Add reply REPLY2 to the discussion DISCUSSION1
    Then Reply has been added to the discussion successfully
    When Mark reply REPLY2 for action and get outcome OUTCOME6
    Then Outcome OUTCOME6 is created for reply REPLY2
