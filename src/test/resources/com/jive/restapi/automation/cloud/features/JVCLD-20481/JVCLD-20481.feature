@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-20481]

  Background:
    Given We are running E2E JVCLD-20481

  Scenario: Verify the inbox unread items count [23188690]
    Given User USER1 create a direct message for USER2
    And User USER2 logs in
    When User request all unread direct messages
    Then Inbox returns 1 unread item
