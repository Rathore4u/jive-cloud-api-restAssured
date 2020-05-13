@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8723] - [CLOUD E2E TS 037 - Content - Events]

  Background:
    Given We are running E2E JVCLD-8723
    And ADMIN logged into the system

  Scenario: API - Verify that standard user is able to create a private event [23971232]
    Given User USER1 has been created
    And User USER1 is logged in
    When Create private event EVENT1
    Then Event EVENT1 is created successfully

  Scenario: API - Verify that standard user is able to create a closed event [23971243]
    Given User USER1 is logged in
    When Create closed event EVENT2
    Then Event EVENT2 is created successfully

  Scenario: API - Verify that User A is able to invite User B to an event [23971279]
    Given User USER2 has been created
    And User USER1 is logged in
    And Logged in user requests to create event EVENT3 with tags #AutoTag1
    When User send invitation INVITE1 to user USER2 to join event EVENT3
    Then Event invite INVITE1 sent successfully

  Scenario: API - When user A invites User B to an event, User B to get event invitation notification in jive hosted instance inbox [23971361]
    When User USER2 logs in
    And User request all unread notifications
    Then Inbox returns 1 unread item

  Scenario: API - Verify that user is able to Move an event to a different place [23971395]
    Given User USER1 is logged in
    And User request to create "Open" group GROUP1
    When Move event EVENT3 to GROUP1
    Then Event EVENT3 is moved to place GROUP1 successfully

  Scenario: API - Verify that user is able to Follow/Unfollow an event [23972125]
    Given User USER2 is logged in
    When Get list STREAMSLIST1 of user USER2 Streams
    Then List STREAMSLIST1 of user streams is received successfully
    When User with streams STREAMSLIST1 follows event EVENT3
    Then List STREAMSLIST1 of user streams is received successfully
    When User unfollows event EVENT3
    Then User has unfollowed event EVENT3 successfully

  Scenario: API - Verify that the Creater/Organizer and invitee is able to add and submit comment successfully to an event and comments are visible to both of them [23972148]
    Given User USER1 is logged in
    When Add a comment COMMENT1 to the event EVENT3
    Then Comment is added to the event successfully
    When User USER2 is logged in
    And Get comments from the event EVENT3 to the list COMMENTSLIST1
    Then Verify that comments list COMMENTSLIST1 contains comment COMMENT1
    When Add a comment COMMENT2 to the event EVENT3
    Then Comment is added to the event successfully

  Scenario: API - Verify that organizer of an event can delete anyone's comment [23972212]
    Given User USER1 is logged in
    When Delete comment COMMENT1
    Then Comment deleted successfully

  Scenario: API - Verify that user is able to like/unlike any other user's comment [23972229]
    Given User USER1 is logged in
    When Like comment COMMENT2
    Then Comment is liked successfully
    When Unlike comment COMMENT2
    Then Comment is unliked successfully
