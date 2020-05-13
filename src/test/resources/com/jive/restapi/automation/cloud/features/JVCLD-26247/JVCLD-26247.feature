@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-26247]

  Background:
    Given We are running E2E JVCLD-26247
    And ADMIN logged into the system
    And User USER1 has been created
    And User USER2 has been created
    And User USER1 logs in
    And User USER1 follow a user USER2
    And User USER2 logs in
    And User request to create multiple contents: BlogPost, Document, Question, Discussion
    And User USER1 logs in

  Scenario: API - To verify correct notification count appears for User's inbox notification [25324426]
    When User request all unread messages by author USER2
    Then Inbox returns 4 unread items

  Scenario: API - To verify unread count is correctly reflected when some notifications are read [25324428]
    When User request all unread messages by author USER2
    Then Inbox returns 4 unread items
    When User request to mark as read first inbox message for user USER2
    And  User request all messages by author USER2
    Then Inbox returns 3 unread items
    And Inbox returns 1 read item

  Scenario: API - To verify User is able to check Unread checkbox and only unread items are displayed [25324452]
    When User request all unread messages by author USER2
    Then Inbox returns 4 unread items
    When User request to mark as read first inbox message for user USER2
    And  User request all unread messages by author USER2
    Then Inbox returns 3 unread items

  Scenario: API - To verify correct count is reflected when one of the message is read from the unread items with Unread only checkbox selected [25324443]
    When User request to mark as read first inbox message for user USER2
    And  User request all unread messages by author USER2
    Then Inbox returns 3 unread items

  Scenario: API - To verify clicking on "Mark all read link" makes all the items in the inbox as read [25324444]
    When User request to mark all messages as read
    Then All messages marked as read successfully

  Scenario: API - To verify unread only checkbox remains checked with no notification messages displayed (as marked all items as read)  when user navigates to Your view then back to Inbox [25324446]
    When User request to mark all messages as read
    Then All messages marked as read successfully
    When User request all unread messages by author USER2
    Then Inbox returns 0 unread items
