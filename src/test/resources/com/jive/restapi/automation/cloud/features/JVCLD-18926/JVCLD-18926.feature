@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-18926] - [E2E test scenario for JVCLD-14577]

  Background:
    Given We are running E2E JVCLD-18926
    And ADMIN logged into the system

  Scenario: API-Create a subspace within a space as an admin user [23196039]
    When admin requests to create a space SpaceProjEditOne
    And Admin requests to create a subSpace SubSpaceProjectEditOne under space SpaceProjEditOne
    Then space is created successfully

  Scenario: API-As a regular user create subspace project [23196042]
    When User RegularUserOne has been created
    And admin requests to create a space SpaceTwo
    And Admin requests to create a subSpace SubSpaceProjectTwo under space SpaceTwo
    And User RegularUserOne logs in
    And User requests to create a project ProjectSubSpaceTestTwo under place SubSpaceProjectTwo
    Then space is created successfully

  Scenario: API-As standard user, Create a Project with overview page in space [23196044]
    When User RegularUserTwo has been created
    And admin requests to create a space SpaceThree
    And User RegularUserTwo logs in
    And User requests to create a project ProjectSpaceTestTwo under place SpaceThree
    Then space is created successfully

  Scenario: API-Create a group and verify that the default view should show as overview page [23195978]
    When admin requests to create a group GroupOne
    Then group is created successfully
    When Enable Overview tab visibility for project GroupOne
    Then Verify place GroupOne default view as overview tab

  Scenario: API-Create a project in group and verify that the default view should show as overview page [23196026]
    When admin requests to create a group GroupProject
    And User requests to create a project ProjectOne under place GroupProject
    Then project is created successfully
    When Enable Overview tab visibility for project ProjectOne
    Then Verify place ProjectOne default view as overview tab

  Scenario: API-Create a space and verify that the default view should show as overview page [23196032]
    When admin requests to create a space SpaceOne
    Then space is created successfully
    When Enable Overview tab visibility for project SpaceOne
    Then Verify place SpaceOne default view as overview tab

  Scenario: API-Create a project in space and verify that the default view should show as overview page [23196035]
    When admin requests to create a space SpaceProject
    And User requests to create a project ProjectTest under place SpaceProject
    Then project is created successfully
    When Enable Overview tab visibility for project ProjectTest
    Then Verify place ProjectTest default view as overview tab

  Scenario: API-Create a project in Subspace and verify that the default view should show as overview page [23196041]
    When admin requests to create a space SpaceProj
    And Admin requests to create a subSpace SubSpaceProject under space SpaceProj
    And User requests to create a project ProjectSubSpaceTest under place SubSpaceProject
    Then project is created successfully
    When Enable Overview tab visibility for project ProjectSubSpaceTest
    Then Verify place ProjectSubSpaceTest default view as overview tab

  Scenario: API-Edit group to have overview page and check the default view [23195983]
    When admin requests to create a group GroupEdit
    Then group is created successfully
    When Enable Overview tab visibility for project GroupEdit
    Then Verify place GroupEdit default view as overview tab

  Scenario: API-Edit project in group to have overview page and check the default view [23196031]
    When admin requests to create a group GroupProjectEdit
    And User requests to create a project ProjectOneEdit under place GroupProjectEdit
    Then project is created successfully
    When Enable Overview tab visibility for project ProjectOneEdit
    Then Verify place ProjectOneEdit default view as overview tab

  Scenario: API-Edit space to have overview page and check the default view [23196034]
    When admin requests to create a space SpaceEdit
    Then space is created successfully
    When Enable Overview tab visibility for project SpaceEdit
    Then Verify place SpaceEdit default view as overview tab

  Scenario: API-Edit project in Space to have overview page and check the default view [23196036]
    When admin requests to create a space SpaceProjectEdit
    And User requests to create a project ProjectTestEdit under place SpaceProjectEdit
    Then project is created successfully
    When Enable Overview tab visibility for project ProjectTestEdit
    Then Verify place ProjectTestEdit default view as overview tab

  Scenario: API-Edit project in Subspace to have overview page and check the default view [23196043]
    When admin requests to create a space SpaceProjEdit
    And Admin requests to create a subSpace SubSpaceProjectEdit under space SpaceProjEdit
    And User requests to create a project ProjectSubSpaceTestEdit under place SubSpaceProjectEdit
    Then project is created successfully
    When Enable Overview tab visibility for project ProjectSubSpaceTestEdit
    Then Verify place ProjectSubSpaceTestEdit default view as overview tab
