@APISuite
Feature: [CLOUD - API Experimental Suite] - [JVCLD-18900] - [E2E test scenario for JVCLD-18900]

  Background:
    Given We are running E2E JVCLD-18900
    And ADMIN logged into the system

  Scenario: API - Verify standard user is able to create private unlisted group. [25288860]
    #Precondition - Run C14783002 - Create a standard user
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Logged in user to create "private unlisted" group GroupUnlisted
    Then Verify "private unlisted" group GroupUnlisted is created successfully

  Scenario: API - Verify that user is added successfully when being invited to join a Private unlisted group [25288947]
    Given User UserOne is logged in
    When Logged in user to search the created group GroupUnlisted by name in spotlight search with response GROUPRESP
    Then Verify group GroupUnlisted search response GROUPRESP is searched successfully
    When Logged in user to invite user UserTwo to group GroupUnlisted created with response INVITERESP
    Then Group GroupUnlisted invite having response INVITERESP sent successfully
    When User UserTwo logs in
    And User request all unread items INBOXGROUP
    Then Group invite notification of INVITERESP is present in inbox INBOXGROUP
    When Logged in user to accept invite INVITERESP with response ACCEPTINVITE
    Then Verify Group invite having response ACCEPTINVITE accepted successfully

  Scenario: API - Verify standard User1 is able to create Private group with Content editing by non members. [25290241]
    #Precondition - Run C14783002 - Create a standard user
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Logged in user to create "private" group GroupPrivate
    Then Verify "private" group GroupPrivate is created successfully

  Scenario: API - Verify that user is added successfully when being invited to join a Private group with Content Editing by Non-Members [25290272]
    Given User UserOne is logged in
    When Logged in user to search the created group GroupPrivate by name in spotlight search with response GROUPRESP
    Then Verify group GroupPrivate search response GROUPRESP is searched successfully
    When Logged in user to invite user UserTwo to group GroupPrivate created with response INVITERESP
    Then Group GroupPrivate invite having response INVITERESP sent successfully
    When User UserTwo logs in
    And User request all unread items INBOXGROUP
    Then Group invite notification of INVITERESP is present in inbox INBOXGROUP
    When Logged in user to accept invite INVITERESP with response ACCEPTINVITE
    Then Verify Group invite having response ACCEPTINVITE accepted successfully

  Scenario: API - Verify standard user is able to create public restricted group. [25290311]
    #Precondition - Run C14783002 - Create a standard user
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Logged in user to create "public restricted" group GroupPublicRestricted
    Then Verify "public restricted" group GroupPublicRestricted is created successfully

  Scenario: API - Verify that user is added successfully when being invited to join a Private group with Content Editing by Non-Members [25290337]
    Given User UserOne is logged in
    When Logged in user to search the created group GroupPublicRestricted by name in spotlight search with response GROUPRESP
    Then Verify group GroupPublicRestricted search response GROUPRESP is searched successfully
    When Logged in user to invite user UserTwo to group GroupPublicRestricted created with response INVITERESP
    Then Group GroupPublicRestricted invite having response INVITERESP sent successfully
    When User UserTwo logs in
    And User request all unread items INBOXGROUP
    Then Group invite notification of INVITERESP is present in inbox INBOXGROUP
    When Logged in user to accept invite INVITERESP with response ACCEPTINVITE
    Then Verify Group invite having response ACCEPTINVITE accepted successfully

  Scenario: API - Verify that user is added successfully without any error/warning a Public group is shared [25290517]
    #Precondition - Run C14783002 - Create a standard user
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Logged in user to create "public" group PUBLICGROUP
    And Logged in user to search the created group PUBLICGROUP by name in spotlight search with response GROUPRESP
    Then Verify group PUBLICGROUP search response GROUPRESP is searched successfully
    When Logged in user shares group PUBLICGROUP with another user UserTwo with response SHARERESP
    Then Share SHARERESP is created successfully
    When User request all unread items INBOXGROUP
    Then Group invite notification of INVITERESP is present in inbox INBOXGROUP

  Scenario: API - Verify that a document can be shared successfully with a user without any error or warning. [25341604]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And User request to create a document TestDoc
    Then Document TestDoc is created successfully
    When Logged in user shares document TestDoc with user UserTwo
    Then Share is created successfully

  Scenario: API - Verify that a discussion can be shared successfully with a user without any error or warning. [25341605]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Logged in user requests to create a discussion TestDiscussion
    Then Discussion TestDiscussion is created successfully
    When Logged in user shares discussion TestDiscussion with user UserTwo
    Then Share is created successfully

  Scenario: API - Verify that an idea can be shared successfully with a user without any error or warning. [25341606]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Request to create an TestIdea idea
    Then Verify Idea is created successfully
    When Logged in user shares idea TestIdea with user UserTwo
    Then Share is created successfully

  Scenario: API - Verify that a Poll can be shared successfully with a user without any error or warning. [25341607]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Request to create a Poll TestPoll
    Then Poll is Created Successfully
    When Logged in user shares poll TestPoll with user UserTwo
    Then Share is created successfully

  Scenario: API - Verify that user name can be successfully added without error/warning, when a Blog Post is marked for action. [25341608]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Request to create a BlogPost TestBlog
    Then BlogPost TestBlog is created successfully
    When Logged in user to mark blog content TestBlog as Mark for Action to another user UserTwo with response RESP
    Then Content has been marked for action to another user successfully RESP

  Scenario: API - Verify that user name can be successfully added without error/warning, when a Question s marked for action. [25341609]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And Logged in user requests to create Question TestQuestion
    Then Question TestQuestion is created successfully
    When Logged in user to mark content TestQuestion as Mark for Action to another user UserTwo with response RESP
    Then Content has been marked for action to another user successfully RESP

  Scenario: API - Verify that user name can be successfully added without error/warning, when a direct message is posted from User1 to User2 [25341610]
    When User UserOne has been created
    And User UserTwo has been created
    And User UserOne is logged in
    And User send Direct Message TestMessage to user UserTwo
    Then Direct message "TestMessage" sent successfully

