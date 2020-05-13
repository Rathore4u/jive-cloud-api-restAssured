@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-20326] - [E2E test scenario for JVCLD-19795]

  Background:
    Given We are running E2E JVCLD-20326
    Given ADMIN logged into the system

  Scenario: [Precondition] Create a standard user by name "Florian Dietrich". (Take any unique name) - [23191288]
    When Create new user by name Florian Dietrich
    Then User has been created

  Scenario: Verify searching the user by last name and first name in Jive community displays result in spotlight search. - [23191289]
    Given New user has been created by name Florian Dietrich
    When Admin searches people by the created person's partially name query Florian Dietrich
    Then Person name Florian Dietrich is searched successfully

  Scenario: Verify searching the user by partial fisrt name in Jive community displays result in spotlight search. - [23205513]
    Given New user has been created by name Florian Dietrich
    When Admin searches people by the created person's partially name query Florian
    Then Person name Florian is searched successfully

  Scenario: Verify searching the user by partial last name in Jive community displays result in spotlight search. - [23205514]
    Given New user has been created by name Florian Dietrich
    When Admin searches people by the created person's partially name query Dietrich
    Then Person name Dietrich is searched successfully
