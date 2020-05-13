@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8759] - [CLOUD E2E TS 007 - Content - Structured Outcomes - Mark for Action]

  Background:
    Given We are running E2E JVCLD-8759
    And ADMIN logged into the system

  Scenario: API - Create a Document type and mark content as "Mark for Action" [23974833]
    # Preconditions
    When User UserOne has been created
    And  User UserOne logs in
    # Step 1
    When User request to create a document TestDoc
    Then Document TestDoc is created successfully
    # Step 2
    When User UserOne mark content document TestDoc as Mark for Action DocOutcome
    Then Verify content document is Mark for action DocOutCome

  Scenario: API - Create a Discussion type and mark content as "Mark for Action" [23974834]
    # Preconditions
    When User UserOne has been created
    And  User UserOne logs in
    # Step 1
    When Logged in user requests to create a discussion TestDiscussion
    Then Discussion TestDiscussion is created successfully
    # Step 2
    When User UserOne mark content discussion TestDiscussion as Mark for Action DiscussionOutcome
    Then Verify content discussion TestDiscussion is Mark for action DiscussionOutcome

  Scenario: API - Create a Question and mark content as "Mark for Action" [23974835]
    # Preconditions
    When User UserOne has been created
    And  User UserOne logs in
    # Step 1
    When Logged in user requests to create Question TestQuestion
    Then Question TestQuestion is created successfully
    # Step 2
    When User UserOne mark content question TestQuestion as Mark for Action QuestOutcome
    Then Verify content question TestQuestion is Mark for action QuestOutcome

  Scenario: API - Create the Blog Post and mark content as "Mark for Action" [23974836]
    # Preconditions
    When User UserOne has been created
    And  User UserOne logs in
    # Step 1
    When Request to create a BlogPost TestBlogPost
    Then BlogPost TestBlogPost is created successfully
    # Step 2
    When User UserOne mark content blog post TestBlogPost as Mark for Action BlogOutcome
    Then Verify content blog post TestBlogPost is Mark for action BlogOutcome

  Scenario: API - Mark Blog Post content as "Mark for Action" created by user A [23974917]
    # Preconditions
    When User UserOne has been created
    And User UserTwo has been created
    And  User UserOne logs in
    And Request to create a BlogPost TestBlogPost
    And  User UserTwo logs in
    # Step 1
    When User UserTwo mark content blog post TestBlogPost as Mark for Action BlogOutcome
    Then Verify content blog post TestBlogPost is Mark for action BlogOutcome

  Scenario: API - Mark Document content as "Mark for Action" created by user A [23974918]
    # Preconditions
    When User UserOne has been created
    And User UserTwo has been created
    And  User UserOne logs in
    And User request to create a document TestDoc
    And  User UserTwo logs in
    # Step 1
    When User UserTwo mark content document TestDoc as Mark for Action DocOutcome
    Then Verify content document is Mark for action DocOutCome

  Scenario: API - Mark Discussion content as "Mark for Action", created by user A [23974919]
    # Preconditions
    When User UserOne has been created
    And User UserTwo has been created
    And  User UserOne logs in
    And Logged in user requests to create a discussion TestDiscussion
    And  User UserTwo logs in
    # Step 1
    When User UserTwo mark content discussion TestDiscussion as Mark for Action DiscussionOutcome
    Then Verify content discussion TestDiscussion is Mark for action DiscussionOutcome

  Scenario: API - Mark Question content as "Mark for Action" created by user A [23974921]
    # Preconditions
    When User UserOne has been created
    And User UserTwo has been created
    And  User UserOne logs in
    And Logged in user requests to create Question TestQuestion
    And  User UserTwo logs in
    # Step 1
    When User UserTwo mark content question TestQuestion as Mark for Action QuestOutcome
    Then Verify content question TestQuestion is Mark for action QuestOutcome

  Scenario: API - Mark the content as "Resolved" which is marked for "Action" by User B [23974922]
    # Preconditions
    When User UserOne has been created
    And User UserTwo has been created
    And  User UserOne logs in
    And User request to create a document TestDoc
    And  User UserTwo logs in
    And User UserTwo mark content document TestDoc as Mark for Action DocOutcome
    #Step 1
    When  User UserOne logs in
    And User UserOne mark content TestDoc resolved, which is marked for action DocOutcome
    Then Verify content is marked as resolved DocOutcome
