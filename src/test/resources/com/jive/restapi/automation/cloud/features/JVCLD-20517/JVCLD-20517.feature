@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-20517]

  Background:
    Given We are running E2E JVCLD-20517

  Scenario: Verify that user can create a direct message [23185533]
    Given ADMIN logged into the system
    And User USER1 has been created
    And User USER2 has been created
    When User USER1 logs in
    And Create a direct message to USER2
    Then Direct message created successfully
