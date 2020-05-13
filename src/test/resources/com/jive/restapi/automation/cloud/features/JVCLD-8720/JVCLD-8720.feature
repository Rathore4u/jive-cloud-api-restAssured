@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8720] - [CLOUD E2E TS 001 - Content - Structured Outcomes - Mark as Decision]

  Background:
    Given We are running E2E JVCLD-8720
    And ADMIN logged into the system
    And Create or update jive property:
      | outcomes.enabled | true |

  Scenario: API - [Precondition] Enable structured outcomes in Admin console [23969794]
    When Create or update jive property:
      | outcomes.enabled | true |
    Then Jive property created or updated successfully

  Scenario: API - [PreCondition] To verify standard user is able to Create discussion with reply [23969816]
    Given User USER1 has been created
    And User USER1 is logged in
    When Logged in user requests to create a discussion DISC1 with tags #Tag1
    Then Discussion DISC1 is created successfully
    When Add reply REPLY1 to the discussion DISC1
    Then Reply has been added to the discussion successfully

  Scenario: API - [PreCondition] To verify standard user is able to Create document with comment [23969832]
    Given User USER1 is logged in
    When Logged in user requests to create document DOC1 with tags #Tag2
    Then Document DOC1 is created successfully
    When Add a comment COMMENT1 to the document DOC1
    Then Comment has been added to the document successfully

  Scenario: API - [PreCondition] To verify standard user is able to Create blog post with comment [23969845]
    Given User USER1 is logged in
    When Logged in user requests to create post BLOGPOST1 with tags #Tag3
    Then Post BLOGPOST1 is created successfully
    When Add a comment COMMENT2 to the blog post BLOGPOST1
    Then Comment has been added to the blog post successfully

  Scenario: API - To Verify Author is able to Mark and Unmark a reply as decision [23969883]
    Given User USER1 is logged in
    When Mark reply REPLY1 as decision and get outcome OUTCOME9
    Then Outcome OUTCOME9 is created for reply REPLY1
    When Unmark reply REPLY1 as "Decision" using outcome OUTCOME9
    Then Reply REPLY1 is unmarked as "Decision" successfully

  Scenario: API - To Verify Author is able to Mark and Unmark a document's comment as decision [23969945]
    Given User USER1 is logged in
    When Mark comment COMMENT1 as "Decision" and get outcome OUTCOME1
    Then Comment has been marked as "Decision" successfully with creation of outcome OUTCOME1
    When Unmark comment COMMENT1 as "Decision" using outcome OUTCOME1
    Then Comment has been unmarked as "Decision" successfully using outcome OUTCOME1

  Scenario: API - To verify Author is able to Mark an Unmark a blog post's comment as decision [23970008]
    Given User USER1 is logged in
    When Mark comment COMMENT2 as "Decision" and get outcome OUTCOME2
    Then Comment has been marked as "Decision" successfully with creation of outcome OUTCOME2
    When Unmark comment COMMENT2 as "Decision" using outcome OUTCOME2
    Then Comment has been unmarked as "Decision" successfully using outcome OUTCOME2

  Scenario: API - To verify Non Contributor is able to Mark and Unmark a reply as decision [23970029]
    Given User USER2 has been created
    And User USER2 is logged in
    When Mark reply REPLY1 as decision and get outcome OUTCOME9
    Then Outcome OUTCOME9 is created for reply REPLY1
    When Unmark reply REPLY1 as "Decision" using outcome OUTCOME9
    Then Reply REPLY1 is unmarked as "Decision" successfully

  Scenario: API - To verify Non Contributor is able to Mark and Unmark a document's comment as decision [23970034]
    Given User USER2 has been created
    And User USER2 is logged in
    When Mark comment COMMENT1 as "Decision" and get outcome OUTCOME3
    Then Comment has been marked as "Decision" successfully with creation of outcome OUTCOME3
    When Unmark comment COMMENT1 as "Decision" using outcome OUTCOME3
    Then Comment has been unmarked as "Decision" successfully using outcome OUTCOME3

  Scenario: API - To verify Non Contributor is able to Mark an Unmark a blog post's comment as decision [23970853]
    Given User USER2 has been created
    And User USER2 is logged in
    When Mark comment COMMENT2 as "Decision" and get outcome OUTCOME4
    Then Comment has been marked as "Decision" successfully with creation of outcome OUTCOME4
    When Unmark comment COMMENT2 as "Decision" using outcome OUTCOME4
    Then Comment has been unmarked as "Decision" successfully using outcome OUTCOME4

  Scenario: API - Verify count of decisions made on a discussion [23970946]
    Given User USER1 is logged in
    When Mark reply REPLY1 as decision and get outcome OUTCOME9
    When User USER2 is logged in
    And Add reply REPLY2 to the discussion DISC1
    Then Reply has been added to the discussion successfully
    When Mark reply REPLY2 as decision and get outcome OUTCOME10
    Then Outcome OUTCOME10 is created for reply REPLY2
    When Get replies from discussion DISC1 to list REPLIESLIST1
    Then Verify that replies list REPLIESLIST1 contains replies with outcomes:
      | decision | 1 |

  Scenario: API - Verify count of decisions made on a document [23971014]
    Given User USER1 is logged in
    And Mark comment COMMENT1 as "Decision" and get outcome OUTCOME5
    Given User USER2 is logged in
    When Add a comment COMMENT3 to the document DOC1
    Then Comment has been added to the document successfully
    When Mark comment COMMENT3 as "Decision" and get outcome OUTCOME6
    Then Comment has been marked as "Decision" successfully with creation of outcome OUTCOME6
    When Get comments from the document DOC1 to the list COMMENTSLIST1
    Then Verify that comments list COMMENTSLIST1 contains 2 comments
    And Verify that comments list COMMENTSLIST1 contains comments with outcomes:
      | decision | 1 |

  Scenario: API - Verify count of decisions made on a blog post [23971042]
    Given User USER1 is logged in
    And Mark comment COMMENT2 as "Decision" and get outcome OUTCOME7
    Given User USER2 is logged in
    When Add a comment COMMENT4 to the blog post BLOGPOST1
    Then Comment has been added to the blog post successfully
    When Mark comment COMMENT4 as "Decision" and get outcome OUTCOME8
    Then Comment has been marked as "Decision" successfully with creation of outcome OUTCOME8
    When Get comments from the BlogPost BLOGPOST1 to the list COMMENTSLIST2
    Then Verify that comments list COMMENTSLIST2 contains 2 comments
    And Verify that comments list COMMENTSLIST2 contains comments with outcomes:
      | decision | 1 |