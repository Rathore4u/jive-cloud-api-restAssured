@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-27711] - [E2E test scenario for JVCLD-27711]

  Background:
    Given We are running E2E JVCLD-27711

  Scenario: Precondition - Create a standard user and login
    Given ADMIN logged into the system
    And admin requests to create a space SpaceOne
    When Admin requests to create a subSpace SubSpace under space SpaceOne
    And User UserOne has been created
    Then User UserOne is logged in

  Scenario: API - Mark document created in Group as 'Mark for Action' [24022773]
    Given Logged in user requests to create Document DocumentInOpenGroup with description Foo under place
    When User UserOne mark content document DocumentInOpenGroup as Mark for Action DocOutcome
    Then Verify content document is Mark for action DocOutCome

  Scenario: API - Create a blog post in Project in Space [24022964]
    Given User requests to create a project ProjectInSpace under place SpaceOne
    When User requests to create BlogPost BlogInProjectInSpace under place ProjectInSpace
    Then BlogPost BlogInProjectInSpace is created successfully

  Scenario: API - Mark blog post created in Project in Space as 'Mark as Success' [24022793]
    When User request to mark blogpost BlogInProjectInSpace as Success outcome
    Then Verify content blog post BlogInProjectInSpace is marked as success outcome outcome

  Scenario: API - Mark discussion created in Private Group as 'Mark as Outdated' [24022830]
    Given User requests to create a private group PrivateGroup
    And User requests to create Discussion DiscussionInPrivateGroup under place PrivateGroup
    When user request to mark discussion DiscussionInPrivateGroup as Outdated outcome
    Then Discussion outcome outcome should be created in discussion DiscussionInPrivateGroup

  Scenario: API - [Precondition] Create a Question in SubSpace [24022859]
    When User requests to create Question QuestionInSubSpace under place SubSpace
    Then Question QuestionInSubSpace is created successfully

  Scenario: API - Mark Question created in SubSpace as 'Mark as Official' [24022868]
    When User requests to mark question QuestionInSubSpace as Mark for Official outcome
    Then Verify question is marked as Official outcome

  Scenario: API - Share a blog post created in Project in Space with Group [24023004]
    Given User request to create "Open" group GroupOne
    When User shares place ProjectInSpace with group GroupOne
    Then Share is created successfully

  Scenario: API - Verify standard user is able to create public restricted group. [24027056]
    When User requests to create a public restricted group PublicRestrictedGroup
    Then group is created successfully

  Scenario: API - [Precondition] Create an idea in Space [24027060]
    When Request to create an idea IdeaInSpace Under Place SpaceOne
    Then Idea is Created Successfully

  Scenario: API - Share an idea created in Space with Public Restricted Group [24027062]
    When User shares idea IdeaInSpace with place PublicRestrictedGroup
    Then Share is created successfully

  Scenario: API - Share a discussion created in Private Group with SubSpace [24027169]
    When User shares discussion DiscussionInPrivateGroup with place SubSpace
    Then Share is created successfully

  Scenario: API - Share a poll created in Project in Group with Project in Space. [24027176]
    Given Request to create an Project ProjectInGroup
    When User request to create an Poll PollInProjectInGroup under place ProjectInGroup
    Then Poll is Created Successfully Under Place
    When User shares poll PollInProjectInGroup with place ProjectInSpace
    Then Share is created successfully

  Scenario: API - Share a Question created in SubSpace with Private Group [24027193]
    When User shares content QuestionInSubSpace with place PrivateGroup
    Then Share is created successfully

  Scenario: API - [Precondition] Add comment to document created in Group and mark comment as 'Mark as Success' [24030120]
    Given Add a comment CommentOne to the document DocumentInOpenGroup
    When User request to mark comment CommentOne as Success and get outcome outcome
    Then Comment has been marked as outcomeType successfully with creation of outcome outcome

  Scenario: API - Verify document is displayed on Space content page when filtered by action 'Has action items' [24030140]
    When User shares document DocumentInOpenGroup with place SpaceOne
    Then Share is created successfully
    When User requests to get document DocumentInOpenGroup by outcome PENDING from place SpaceOne
    Then Document DocumentInOpenGroup is searched successfully

  Scenario: API - Verify document is displayed on Space content page when filtered by action 'Has Successes' [24030200]
    When User requests to get document DocumentInOpenGroup by outcome SUCCESS from place SpaceOne
    Then Document DocumentInOpenGroup is searched successfully

  Scenario: API - [Precondition] Add comment to Blog post created in Project in Space and mark comment as 'Mark as Decision' [24030206]
    Given Add a comment CommentTwo to the blog post BlogInProjectInSpace
    When Mark comment CommentTwo as "Decision" and get outcome outcome
    Then Comment has been marked as "Decision" successfully with creation of outcome outcome

  Scenario: API - Verify Blog post is displayed on Group content page when filtered by action 'Has successes' [24030235]
    When User shares blogPost BlogInProjectInSpace with group GroupOne
    Then Share is created successfully
    When User requests to get blogPost BlogInProjectInSpace by outcome SUCCESS from group GroupOne
    Then Post BlogInProjectInSpace is returned successfully

  Scenario: API - Verify Blog post is displayed on Group content page when filtered by action 'Has Decisions' [24030245]
    When User requests to get blogPost BlogInProjectInSpace by outcome PENDING from group GroupOne
    Then Post BlogInProjectInSpace is returned successfully

  Scenario: API - [Precondition] Add comment to discussion created in Private Group and mark comment as 'Mark for Action' [24030371]
    When Add reply SomeReply to the discussion DiscussionInPrivateGroup
    Then Reply has been added to the discussion successfully
    When Mark reply SomeReply for action and get outcome outcomeOne
    Then Outcome outcomeOne is created for reply SomeReply

  Scenario: API - Verify discussion is displayed on SubSpace content page when filtered by action 'Marked as Outdated' [24030373]
    When User requests to get discussion DiscussionInPrivateGroup by outcome OUTDATED from place SubSpace
    Then Discussion DiscussionInPrivateGroup is searched successfully

  Scenario: API - Verify discussion is displayed on SubSpace content page when filtered by action 'Has Action Items' [24030398]
    When User requests to get discussion DiscussionInPrivateGroup by outcome PENDING from place SubSpace
    Then Discussion DiscussionInPrivateGroup is searched successfully

  Scenario: API - [Precondition] Add comment to Question created in SubSpace and mark comment as 'Mark as Decision' [24031490]
    When Add a comment CommentTwo to question QuestionInSubSpace
    Then Comment has been added to the question successfully
    When Mark reply CommentTwo as decision and get outcome outcomeOne
    Then Outcome outcomeOne is created for reply CommentTwo

  Scenario: API - Verify Question is displayed on Private Group content page when filtered by action 'Marked as Official' [24031502]
    When User requests to get question QuestionInSubSpace by outcome PENDING from place PrivateGroup
    Then Question QuestionInSubSpace is searched successfully

  Scenario: API - Verify Question is displayed on Private Group content page when filtered by action 'Has decisions' [24031525]
    When User requests to get question QuestionInSubSpace by outcome DECISION from place PrivateGroup
    Then Question QuestionInSubSpace is searched successfully
