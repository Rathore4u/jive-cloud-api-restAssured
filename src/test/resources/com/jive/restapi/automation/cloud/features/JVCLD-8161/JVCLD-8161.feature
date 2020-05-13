@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8161] - [CLOUD E2E TS 047 - Content - Status Updates]

  Background:
    Given We are running E2E JVCLD-8161
    And ADMIN logged into the system

  Scenario: API - Create Status update with a link [25333545]
    Given User USER1 has been created
    And User USER1 is logged in
    When Create status update STATUSUPDATE1 with link
    Then Status update STATUSUPDATE1 is created successfully
    When Get update activities ACTIVITIES1 from the user USER1
    Then User activities ACTIVITIES1 contain update STATUSUPDATE1

  Scenario: API - Create status update with an image [25334180]
    Given User USER2 has been created
    And User USER2 is logged in
    When Create status update STATUSUPDATE2 with image TestFiles/testImage.png
    Then Status update STATUSUPDATE2 is created successfully
    When Get update activities ACTIVITIES2 from the user USER2
    Then User activities ACTIVITIES2 contain update STATUSUPDATE2

  Scenario: API - Verify that the status message with @ mention of a person's name, place or content, is posted and gets updated with the link [25334247]
    Given User USER3 has been created
    And User USER1 is logged in
    When Create status update STATUSUPDATE3 with person USER3 mention
    Then Status update STATUSUPDATE3 is created successfully
    When Get update activities ACTIVITIES3 from the user USER1
    Then User activities ACTIVITIES3 contain update STATUSUPDATE3

  Scenario: API - Creates a status update, and it shows up in the follower's Inbox [25334254]
    Given User USER1 is logged in
    When User USER1 follow a user USER2
    Then User USER1 is following by user USER2
    When User USER2 is logged in
    And Create status update STATUSUPDATE4 with link
    Then Status update STATUSUPDATE4 is created successfully
    When Get created entries ENTRIES1 from inbox
    Then Inbox entries ENTRIES1 contain update STATUSUPDATE4

  Scenario: API - Edit a status update, and it replaces the old update on the author's profile page [25334289]
    Given User USER4 has been created
    And User USER4 is logged in
    When Create status update STATUSUPDATE5 with link
    Then Status update STATUSUPDATE5 is created successfully
    When Get update activities ACTIVITIES4 from the user USER4
    Then User activities ACTIVITIES4 contain update STATUSUPDATE5
    When Create status update STATUSUPDATE6 with link
    Then Status update STATUSUPDATE6 is created successfully
    When Get update activities ACTIVITIES5 from the user USER4
    Then User activities ACTIVITIES5 contain update STATUSUPDATE5
    And Status update STATUSUPDATE6 is older than status update STATUSUPDATE5

  Scenario: API - Delete a status update [25334293]
    Given User USER1 is logged in
    When Delete status update STATUSUPDATE1
    Then Status update STATUSUPDATE1 is deleted successfully
    When Get update activities ACTIVITIES6 from the user USER1
    Then User activities ACTIVITIES6 dont-contain update STATUSUPDATE1

  Scenario: API - Create a status update with tag in Japanese language [25334306]
    Given User USER1 is logged in
    And User request to create "Open" group GROUP1
    When Create status update STATUSUPDATE7 with tag in Japanese language in place GROUP1
    Then Status update STATUSUPDATE7 is created successfully
    When Get update activities ACTIVITIES7 from the group GROUP1
    Then Group activities ACTIVITIES7 contain update STATUSUPDATE7

  Scenario: API - Create a status update with tag in Chinese language [25334539]
    Given User USER1 is logged in
    And User request to create "Open" group GROUP2
    When Create status update STATUSUPDATE8 with tag in Chinese language in place GROUP2
    Then Status update STATUSUPDATE8 is created successfully
    When Get update activities ACTIVITIES8 from the group GROUP2
    Then Group activities ACTIVITIES8 contain update STATUSUPDATE8

  Scenario: API - Create a status update with tag in Thai language [25334543]
    Given User USER1 is logged in
    And User request to create "Open" group GROUP3
    When Create status update STATUSUPDATE9 with tag in Thai language in place GROUP3
    Then Status update STATUSUPDATE9 is created successfully
    When Get update activities ACTIVITIES9 from the group GROUP3
    Then Group activities ACTIVITIES9 contain update STATUSUPDATE9
