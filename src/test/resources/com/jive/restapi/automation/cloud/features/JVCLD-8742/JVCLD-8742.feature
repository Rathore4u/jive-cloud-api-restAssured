@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8742] - [Content - Structured Outcomes - Mark as Outdated]

  Background:
    Given We are running E2E JVCLD-8742
    And ADMIN logged into the system

  Scenario: Create a Regular User [23005513]
    When Requests to create a person
    Then person is created successfully

  Scenario: API - Pre-condition Create a Discussion and Mark it as Final [24004154]
    When User UserFoo has been created
    And User UserFoo logs in
    And Logged in user requests to create a discussion TestDiscussion
    Then Discussion TestDiscussion is created successfully
    When User UserOne mark content discussion TestDiscussion as Mark for Action DiscussionOutcome
    Then Verify content discussion TestDiscussion is Mark for action DiscussionOutcome

  Scenario: API - Document: Mark as outdated [24004155]
    When User TestRegularUserDocument has been created
    And User TestRegularUserDocument logs in
    And User request to create a document UpdateDocOutdated
    Then Document UpdateDocOutdated is created successfully
    When user request to mark document UpdateDocOutdated as Outdated OutcomeOutdated
    Then outcome OutcomeOutdated should be created in UpdateDocOutdated

  Scenario: API - Discussion: Mark as outdated [24004156]
    When User TestRegularUserDiscussion has been created
    And  User TestRegularUserDiscussion logs in
    And User request to create a discussion UpdateOutdated
    Then Discussion UpdateOutdated should be created successfully
    When user request to mark discussion UpdateOutdated as Outdated OutcomeOutdatedDis
    Then Discussion outcome OutcomeOutdatedDis should be created in discussion UpdateOutdated

  Scenario: API - Check for Display of content marked as Outdated In Browse [24004200]
    When User TestRegularUserSearch has been created
    And User TestRegularUserSearch logs in
    And user Request to create a document MarkDocOutdated
    Then Content MarkDocOutdated is created successfully
    When Logged in user requests to create a discussion MarkDiscussionOutdated
    Then Discussion MarkDiscussionOutdated is created successfully
    When User request to mark document MarkDocOutdated as Outdated OutcomeOutdatedDoc
    Then Content outcome OutcomeOutdatedDoc should be created in document MarkDocOutdated
    When User request to mark discussion MarkDiscussionOutdated as Outdated OutcomeOutdatedDiscussion
    Then Content outcome OutcomeOutdatedDiscussion should be created in discussion MarkDiscussionOutdated
    When User request to search document MarkDocOutdated by subject from spotlight
    Then It should return document MarkDocOutdated
    When User request to search discussion MarkDiscussionOutdated by subject from spotlight
    Then It should return discussion MarkDiscussionOutdated

  Scenario: API - Verify Discussion Marked As Outdated when edited and Published, Marked as Outdated Label is removed [24005126]
    When User TestRegularUserDocument has been created
    And User TestRegularUserDocument logs in
    And User request to create a document UpdateDocOutdated
    Then Document UpdateDocOutdated is created successfully
    When user request to mark document UpdateDocOutdated as Outdated OutcomeOutdatedEdit
    Then outcome OutcomeOutdatedEdit should be created in UpdateDocOutdated
    When Logged in user requests to update document UpdateDocOutdated with title UpdatedTitle
    Then Document UpdateDocOutdated is updated with title UpdatedTitle successfully
    And outcome OutcomeOutdatedEdit should be removed in UpdateDocOutdated

  Scenario: API - Discussion Marked as final and then mark as outdated. On editing the discussion which is marked as outdated to its previous outcome which is final [24005160]
    When User UserFooFinal has been created
    And User UserFooFinal logs in
    And User request to create a discussion TestDiscussionFinal
    Then Discussion TestDiscussionFinal should be created successfully
    When User request to mark discussion TestDiscussionFinal as Final FinalTagDiscussion
    Then FinalTagDiscussion should be created in TestDiscussionFinal discussion
    When user request to mark discussion TestDiscussionFinal as Outdated OutdatedTagDiscussion
    Then OutdatedTagDiscussion should be created in TestDiscussionFinal discussion
    When Logged in user requests to update discussion TestDiscussionFinal with title UpdatedTitleDiscussion
    Then Discussion TestDiscussionFinal is updated with title UpdatedTitleDiscussion successfully
    And OutdatedTagDiscussion should be removed in TestDiscussionFinal discussion