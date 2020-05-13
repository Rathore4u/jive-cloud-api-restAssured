@APISuite
@PRECONDITIONS
Feature: <CLOUD - API Experimental Suite> - <Precondition> - As ADMIN, I want to create Preconditions.

  Background:
    Given We are running E2E JVCLD-123
    Given ADMIN logged into the system

  Scenario: API - As a first user create question [23007425]
    When Request to create a question
    Then Question is Created Successfully

  Scenario: API - Create a private group as first user [23007377]
    When Request to create an Private Group
    Then Private Group is Created Successfully

  Scenario: API - Verify standard user is able to create a discussion in Community [23007462]
    When Request to create a Discussion
    Then Discussion is created successfully

  Scenario: API - Verify standard user is able to create a blog post in Community [23007461]
    Given User USER1 has been created
    And User USER1 logs in
    When Request to create a BlogPost BP1
    Then BlogPost BP1 is created successfully

  Scenario: API - As a first user create question under group [23007431]
    When Request to create a question Under Place
    Then Question is Created Successfully Under Place

  Scenario: API - User 1 creates a BlogPost under group [23007406]
    When Request to create a BlogPost BP1 under Group GROUP1
    Then BlogPost is created successfully under Place

  Scenario: API - User 1 creates a poll in group [23007388]
    When Request to create an Poll Under Place
    Then Poll is Created Successfully Under Place

  Scenario: API - Verify standard user is able to create a poll in Community [23007474]
    When Request to create a Poll POLL1
    Then Poll is Created Successfully

  Scenario: API - User 1 creates a Project in group [23007492]
    When Request to create an Project PROJECTFOO
    When Project PROJECTFOO is Created Successfully

  Scenario: API - User 1 creates a discussion in group [23007404]
    When Request to create a Discussion DISCUSSION1 Under Place
    Then Discussion is Created Successfully Under Place

  Scenario: API - As a first user create a document Under Group [23007372]
    When Request to create a document FOO under place
    Then Document FOO is created successfully

  Scenario: API - User 1 creates a Idea in group [23007393]
    When Request to create an "TestIdea" idea
    Then Idea is Created Successfully

  Scenario: API - Verify standard user is able to create an idea in Community [23007473]
    When Request to create an idea "TestIdea" Under Place
    Then Idea is Created Successfully Under Place


    Scenario: API - Verify standard user is able to create a document in Community [23007457]
    When Request to create a document FOO
    Then Document FOO is created successfully

  @CREATE-FILE
  Scenario: API - As a regular user create file [23202651]
    When Request to create file FILE1
    Then File FILE1 is created successfully

  @CREATE-VIDEO
  Scenario: API - As a regular user create video [23195883]
    When Request to create video VIDEO1
    Then File VIDEO1 is created successfully

  Scenario: API - User creates a Project under Space
    Given Space SpaceOne has been created
    When Create Project PROJECT1 In Place SpaceOne
    When Project PROJECT1 is Created Successfully

  Scenario: API - As Admin user set event properties to 'false' [25238393]
    When Admin user request to set jive property
      |tagPropertyName|propertyName                         |propertyValue|
      |JIVEPROP1      |jive.event.eventAccess.closed.enabled|false        |
    Then Jive property JIVEPROP1 is created successfully

  Scenario: API - As User create a private event in community [25239047]
    When Logged in user requests to create private event EVENT1 with description "testing-private-event" in community
    Then Event EVENT1 is created successfully

  Scenario: API - As User1 Create a private event in Group [25238951]
    When Logged in user requests to create private event EVENT1 with description "testing-private-event" in group
    Then Event EVENT1 is created successfully

  Scenario: API - Invite User2 to private event in community [25238945]
    Given User USER1 has been created
    And User USER2 has been created
    And User USER1 logs in
    When Logged in user requests to create private event EVENT1 with description "testing-private-event" in community
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    Then Event invite INVITE1 sent successfully

  Scenario: API - Invite User2 to private event in Group [25238952]
    Given User USER1 has been created
    And User USER2 has been created
    And User USER1 logs in
    When Logged in user requests to create private event EVENT1 with description "testing-private-event" in group
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    Then Event invite INVITE1 sent successfully

  Scenario: API - As User1 Create a private event as Hidden [25238958]
    When Logged in user requests to create private event EVENT1 as hidden with description "testing-private-event"
    Then Event EVENT1 is created successfully

  Scenario: API - Invite User2 to Hidden private event [25238959]
    Given User USER1 has been created
    And User USER2 has been created
    And User USER1 logs in
    When Logged in user requests to create private event EVENT1 as hidden with description "testing-private-event"
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    Then Event invite INVITE1 sent successfully

  Scenario: API - [Precondition] Follow the User for their content activities [25324462]
    Given User USER1 has been created
    And User USER2 has been created
    And User USER1 logs in
    When User USER1 follow a user USER2
    Then User USER1 is following by user USER2

  Scenario: API - Precondition - Create a news stream with rule adding GroupB and UserA [25324445]
    Given User USER1 has been created
    And User USER1 logs in
    And User request to create "Open" group GROUP1
    And Request to create a BlogPost BLOGPOST1 under Group GROUP1
    And ADMIN logged into the system
    When User request to create a new stream STREAM1 with rule adding GROUP1 and USER1
    Then Stream STREAM1 is created successfully
    And Admin delete stream STREAM1

  Scenario: API - Precondition - Move Blog Post-1 from GroupA to GroupB with selected option "show location change in streams" [25324476]
    Given User USER1 has been created
    And User USER1 logs in
    And User request to create "Open" group GROUPA
    And User request to create "Open" group GROUPB
    And Request to create a BlogPost BLOGPOST1 under Group GROUPA
    And Request to create a BlogPost BLOGPOST2 under Group GROUPA
    And ADMIN logged into the system
    And User request to create a new stream STREAM1 with rule adding GROUPB and USER1
    And User USER1 logs in
    When User move BlogPost BLOGPOST1 to group GROUPB
    Then BlogPost BLOGPOST1 moved successfully
    And Admin delete stream STREAM1

  Scenario: API - Precondition - Move Blog Post-2 from GroupA to GroupB with selected option "show location change in streams" [25324478]
    Given User USER1 has been created
    And User USER1 logs in
    And User request to create "Open" group GROUPA
    And User request to create "Open" group GROUPB
    And Request to create a BlogPost BLOGPOST1 under Group GROUPA
    And Request to create a BlogPost BLOGPOST2 under Group GROUPA
    And ADMIN logged into the system
    And User request to create a new stream STREAM1 with rule adding GROUPB and USER1
    And User USER1 logs in
    When User move BlogPost BLOGPOST2 to group GROUPB
    Then BlogPost BLOGPOST2 moved successfully
    And Admin delete stream STREAM1

  Scenario: API - Precondition - Move Document-1 from GroupA to GroupB with selected option "show location change in streams" [25324480]
    Given User USER1 has been created
    And User USER1 logs in
    And User request to create "Open" group GROUPA
    And User request to create "Open" group GROUPB
    And Request to create a document DOC1 under place GROUPA
    And ADMIN logged into the system
    And User request to create a new stream STREAM1 with rule adding GROUPB and USER1
    And User USER1 logs in
    When User move document DOC1 to group GROUPB
    Then Document DOC1 moved successfully
    And Admin delete stream STREAM1

  Scenario: API – Precondition - Move Discussion-1 from GroupA to GroupB with selected option "show location change in streams" [25345466]
    Given User USER1 has been created
    And User USER1 logs in
    And User request to create "Open" group GROUPA
    And User request to create "Open" group GROUPB
    And Request to create a Discussion DISCUSSION Under Place GROUPA
    And ADMIN logged into the system
    And User request to create a new stream STREAM1 with rule adding GROUPB and USER1
    And User USER1 logs in
    When User move discussion DISCUSSION to group GROUPB
    Then Discussion DISCUSSION moved successfully
    And Admin delete stream STREAM1

  Scenario: API – Precondition - Move Question-1 from GroupA to GroupB with selected option "show location change in streams" [25345516]
    Given User USER1 has been created
    And User USER1 logs in
    And User request to create "Open" group GROUPA
    And User request to create "Open" group GROUPB
    And User requests to create Question QUESTION1 under place GROUPA
    And ADMIN logged into the system
    And User request to create a new stream STREAM1 with rule adding GROUPB and USER1
    And User USER1 logs in
    When User move question QUESTION1 to group GROUPB
    Then Question QUESTION1 moved successfully
    And Admin delete stream STREAM1

  Scenario: API - (Precondition) Enable structured outcomes in Admin console [23969794]
    When Create or update jive property:
      | jive.social_sharing.enabled | true |
    Then Jive property created or updated successfully

  Scenario: API - Verify that Guest Settings can be disabled [23982933]
    When Create or update jive property:
      | jive.auth.disallowGuest | true |
    Then Jive property created or updated successfully

  Scenario: API - Verify that Guest Settings can be enabled [23982893]
    When Create or update jive property:
      | jive.auth.disallowGuest | false |
    Then Jive property created or updated successfully

  Scenario: API - Setting Max uploaded document size (KB) in Admin Console [23982962]
    When Create or update jive property:
      | docbody.maxBodySize | 2048 |
    Then Jive property created or updated successfully

  Scenario: API - Enable Abuse Reporting [23982980]
    When Create or update jive property:
      | abuse.enabled | true |
    Then Jive property created or updated successfully
