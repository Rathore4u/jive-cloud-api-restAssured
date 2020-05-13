@SAMPLE
Feature: <Suite Name> - <Jira Ticket Number> - As ADMIN, I want to create a person.

  Background:
    Given We are running E2E JVARCH-0123456
    Given ADMIN logged into the system

  Scenario: As ADMIN, I want to create a person [<TC Number>]
    When Requests to create a person
    Then person is created successfully
    When admin searches people by the created person's username
    Then person is found successfully
    When admin wants to update the person
    Then person is updated

  Scenario: User context can be switched
    Given User FOO has been created
    And User BAZ has been created
    When User FOO logs in
    And Logged in user gets the status
    Then Himself is returned
    When User BAZ logs in
    And Logged in user gets the status
    Then Himself is returned

  Scenario: User can be shared between scenario (part I)
    Given User FOO has been created
    When User FOO logs in
    And Logged in user gets the status
    Then Himself is returned

  Scenario: User can be shared (part II)
    Given User FOO is logged in
    When Logged in user gets the status
    Then Himself is returned

  Scenario: As Admin, I want to create a discussion
    When Request to create a discussion D1
    Then a discussion is created successfully
    When Request to delete that discussion
    Then that discussion gets deleted

  Scenario: As ADMIN, I want to create a space
    When admin requests to create a space TEST1
    Then space is created successfully

  Scenario: As ADMIN, I want to create a project
    When admin requests to create a project PROJECT1
    Then project is created successfully

  Scenario: As ADMIN, I want to create a group
    When admin requests to create a group GROUP1
    Then group is created successfully

  Scenario: As ADMIN, I want To create a Question
    When Request to create a question
    Then Question is Created Successfully

  Scenario: As ADMIN, I want To create an Open Group
    When Request to create an Open Group
    Then Open Group is Created Successfully

  Scenario: As ADMIN, I want To create an Private Group
    When Request to create an Private Group
    Then Private Group is Created Successfully