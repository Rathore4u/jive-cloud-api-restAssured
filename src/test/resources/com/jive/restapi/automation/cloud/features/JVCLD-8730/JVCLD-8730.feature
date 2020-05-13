@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8730] - [CLOUD E2E TS 023 - Content - Author Discussions]

  Background:
    Given We are running E2E JVCLD-8730
    And ADMIN logged into the system

  Scenario: API - (precondition) Create a document with "Specific People" location [23999728]
    Given User USER1 has been created
    And User USER2 has been created
    And User USER1 is logged in
    When Create a document DOCUMENT1 with "Specific People" publish location and authors:
      |USER2|
    Then Document DOCUMENT1 is created successfully

  Scenario: API - Add authored discussion to the document [23999719]
    Given User USER1 is logged in
    When Add an author comment COMMENT1 to the document DOCUMENT1
    Then Comment has been added to the document successfully
    When Get author comments from the document DOCUMENT1 to the list COMMENTSLIST1
    Then Verify that comments list COMMENTSLIST1 contains 1 comments

  Scenario: API - Reply to the authored comments in a published doc [24002805]
    Given User USER2 is logged in
    When Add reply REPLY1 to the comment COMMENT1
    Then Reply is added to the comment successfully

  Scenario: API - (precondition) Author discussion for a draft doc [24002889]
    Given User USER3 has been created
    And User USER1 is logged in
    When Create a draft document DOCUMENT2 with "Specific People" publish location and authors:
      |USER2|
      |USER3|
    Then Document DOCUMENT2 is created successfully
    When Add an author comment COMMENT2 to the document DOCUMENT2
    Then Comment has been added to the document successfully
    When Get author comments from the document DOCUMENT2 to the list COMMENTSLIST2
    Then Verify that comments list COMMENTSLIST2 contains 1 comments

  Scenario: API - Add authored discussion to the draft doc by a collaborative user [24002931]
    Given User USER2 is logged in
    When Add reply REPLY2 to the comment COMMENT2
    Then Reply is added to the comment successfully

  Scenario: API - Add authored discussion to the draft document by a non-collaborative user [24002959]
    Given User USER4 has been created
    And User USER4 is logged in
    When Search document DOCUMENT2 by subject from spotlight using 1 attempts
    Then Document DOCUMENT2 is not found

  Scenario: API - Add authored discussion to the document that was published-edited by a collaborative user [24003018]
    Given User USER1 is logged in
    When Update a document DOCUMENT1 by new title TITLE1 and save as a draft with "Specific People" publish location:
      |USER2|
    Then Document DOCUMENT1 is updated with title TITLE1 successfully
    When Add an author comment COMMENT3 to the document DOCUMENT1
    Then Comment has been added to the document successfully
    When User USER2 is logged in
    And Add reply REPLY3 to the comment COMMENT3
    Then Reply is added to the comment successfully

  Scenario: API - Add authored discussion to the document that was published-edited by a non-collaborative user [24003908]
    Given User USER4 has been created
    And User USER4 is logged in
    When Search document DOCUMENT1 by subject from spotlight using 1 attempts
    Then Document DOCUMENT1 is not found

  Scenario: API - (precondition) Removing a collaborator after replying [24003963]
    Given User USER2 is logged in
    When Add reply REPLY4 to the comment COMMENT1
    Then Reply is added to the comment successfully
    When User USER1 is logged in
    And Update a document DOCUMENT1 by new "Specific People" publish location list:
      |USER1|
    Then Document DOCUMENT1 doesn't contain following users:
      |USER2|

  Scenario: API - Add authored discussion to the document without collaboration permissions [24004014]
    Given User USER2 is logged in
    When Search document DOCUMENT1 by subject from spotlight using 1 attempts
    Then Document DOCUMENT1 is not found

  Scenario: API - (precondition) Adding a binary document with Author comments [24004034]
    Given User USER1 is logged in
    When Create a file FILE1 with attach testPDF.pdf and with "Specific People" publish location and authors:
      |USER2|
    Then File FILE1 is created successfully
    When Add an author comment COMMENT4 to the file FILE1
    Then Comment is added to the file successfully

  Scenario: API - Add authored discussion for a published binary doc by the collaborative user [24004075]
    Given User USER2 is logged in
    When Add reply REPLY5 to the comment COMMENT4
    Then Reply is added to the comment successfully

  Scenario: API - Add authored discussion to the published binary doc without collaborative permission [24004079]
    Given User USER3 is logged in
    When Search file FILE1 by subject from spotlight using 1 attempts
    Then File is not found
