@APISuite
Feature: [CLOUD - API Experimental Suite] - [JVCLD-9735] - [E2E test scenario for JVCLD-9735]

  Background:
    Given We are running E2E JVCLD-9735
    And ADMIN logged into the system

  Scenario: API - Precondition - Create a discussion [23991105]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Logged in user requests to create a discussion TestDiscussion
    Then Discussion TestDiscussion is created successfully

  Scenario: API - Precondition - Create a Discussion with Image [23991106]
    Given User UserOne is logged in
    When Upload an image DISCIMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create discussion DiscussionWithImage with Image DISCIMAGE
    Then Discussion DiscussionWithImage is created successfully with Image testImage.png

  Scenario: API - Precondition - Create a discussion in a Group [23991108]
    Given User UserOne is logged in
    When User requests to create a group GROUP
    And User requests to create Discussion DiscussionInGroup under place GROUP
    Then Discussion DiscussionInGroup is created successfully

  Scenario: API - Precondition - Create a discussion in a Space [23991109]
    When User requests to create a space SPACE
    And User UserOne logs in
    And User requests to create Discussion DiscussionInSpace under place SPACE
    Then Discussion DiscussionInSpace is created successfully

  Scenario: API - Reply To Discussion in Group [23991110]
    Given User UserTwo is logged in
    When Add reply REPLYGROUP to the discussion DiscussionInGroup
    Then Reply has been added to the discussion successfully
    When Logged in user search discussion DiscussionInGroup replies
    Then Verify discussion contains reply REPLYGROUP

  Scenario: API - Reply To Discussion in Space [23991111]
    Given User UserTwo is logged in
    When Add reply REPLYSPACE to the discussion DiscussionInSpace
    Then Reply has been added to the discussion successfully
    When Logged in user search discussion DiscussionInSpace replies
    Then Verify discussion contains reply REPLYSPACE

  Scenario: API - Reply To Discussion with Image [23991112]
    Given User UserTwo is logged in
    When Add reply REPLYIMAGE to the discussion DiscussionWithImage
    Then Reply has been added to the discussion successfully
    When Logged in user search discussion DiscussionWithImage replies
    Then Verify discussion contains reply REPLYIMAGE

  Scenario: API - Reply With Image [23991113]
    Given User UserTwo is logged in
    When Upload an image DISCIMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Logged in user to add reply REPLYIMAGE to the discussion TestDiscussion with image DISCIMAGE
    Then Reply has been added to the discussion successfully
    When Logged in user search discussion TestDiscussion replies
    Then Verify discussion contains reply REPLYIMAGE

  Scenario: API - Delete reply a discussion [23991114]
    Given User UserTwo is logged in
    When Logged in user to delete discussion reply REPLYIMAGE
    Then Reply is deleted successfully

  Scenario: API - Reply to a discussion [23991116]
    Given User UserTwo is logged in
    When Logged in user requests to create a discussion TestDiscussion1
    And Add reply REPLY to the discussion TestDiscussion1
    Then Reply has been added to the discussion successfully
    When Logged in user search discussion TestDiscussion1 replies
    Then Verify discussion contains reply REPLY
