@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-19263]

  Background:
    Given We are running E2E JVCLD-19263

  Scenario: API - As an Admin user search a space [23195880]
    Given ADMIN logged into the system
    When admin requests to create a space TEST1
    And admin request to search space TEST1
    Then It should returns space TEST1

  Scenario: API - As an Admin user search a group [23195882]
    Given ADMIN logged into the system
    When admin requests to create a group GROUP1
    And admin request to search group GROUP1
    Then It should returns group GROUP1

  Scenario: API - As an Admin user search a project [23195884]
    Given ADMIN logged into the system
    When admin requests to create a project PROJECT1
    And admin request to search project PROJECT1
    Then It should returns project PROJECT1

  Scenario: API - As an Admin user search a content [23195886]
    Given ADMIN logged into the system
    When admin Request to create a document DOC1
    And admin request to search document DOC1
    Then It should returns document DOC1
