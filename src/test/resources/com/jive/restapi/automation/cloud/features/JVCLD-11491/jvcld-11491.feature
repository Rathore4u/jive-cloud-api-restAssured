@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-11491] - [E2E test scenario for JVCLD-11491]

  Background:
    Given We are running E2E JVCLD-11491
    When ADMIN logged into the system

  Scenario: API - Admin creates new sub space - [25466691]
    Given admin requests to create a space SpaceOne
    When Admin requests to create a subSpace SubSpace under space SpaceOne
    Then SubSpace SubSpace has SpaceOne as Parent

  Scenario: API - Admin edits a subspace - [25466690]
    When Admin Updates SubSpace SubSpace
    Then SubSpace SubSpace Updated Successfully

  Scenario:API - Admin deletes a subspace - [25466689]
    When Admin Deletes SubSpace SubSpace
    Then SubSpace is deleted successfully

  Scenario: API - Invite people to Private Unlisted Group. [25466686]
    Given User UserOne has been created
    When User UserTwo has been created
    Then User UserOne is logged in
    When Logged in user to create "private unlisted" group GroupUnlisted
    Then Verify "private unlisted" group GroupUnlisted is created successfully
    When User UserOne is logged in
    Then Logged in user to search the created group GroupUnlisted by name in spotlight search with response GROUPRESP
    When Verify group GroupUnlisted search response GROUPRESP is searched successfully
    And Logged in user to invite user UserTwo to group GroupUnlisted created with response INVITERESP
    Then Group GroupUnlisted invite having response INVITERESP sent successfully

  Scenario: API - Invite people to Public Restricted group - [25466692]
    Given User UserOne has been created
    When User UserTwo has been created
    Then User UserOne is logged in
    When Logged in user to create "public restricted" group GroupUnlisted
    Then Verify "public restricted" group GroupUnlisted is created successfully
    When User UserOne is logged in
    Then Logged in user to search the created group GroupUnlisted by name in spotlight search with response GROUPRESP
    When Verify group GroupUnlisted search response GROUPRESP is searched successfully
    And Logged in user to invite user UserTwo to group GroupUnlisted created with response INVITERESP
    Then Group GroupUnlisted invite having response INVITERESP sent successfully

  Scenario: API - Invite people to Private Group - [25466687]
    Given User UserOne has been created
    When User UserTwo has been created
    Then User UserOne is logged in
    When Logged in user to create "private" group GroupUnlisted
    Then Verify "private" group GroupUnlisted is created successfully
    When User UserOne is logged in
    Then Logged in user to search the created group GroupUnlisted by name in spotlight search with response GROUPRESP
    When Verify group GroupUnlisted search response GROUPRESP is searched successfully
    And Logged in user to invite user UserTwo to group GroupUnlisted created with response INVITERESP
    Then Group GroupUnlisted invite having response INVITERESP sent successfully
