@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-18833]

  Background:
    Given We are running E2E JVCLD-18833
    And ADMIN logged into the system
    And Admin user request to set jive property
      |tagPropertyName|propertyName                          |propertyValue|
      |JIVEPROP1      |jive.event.eventAccess.closed.enabled |false        |
    Then Jive property JIVEPROP1 is created successfully

  Scenario: API - Verify if events can be created in community when event properties are set to false [25238412]
    When Logged in user requests to create event EVENT1 with description testing event in community
    Then Event EVENT1 is created successfully

  Scenario: API – Verify if events can be created in space when event properties are set to false [25238415]
    When Logged in user requests to create event under space
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API – Verify if events can be created in sub-space when event properties are set to false [25238416]
    When Logged in user requests to create event under sub-space
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API - Verify if events can be created in project under space when event properties are set to false [25238418]
    When Logged in user requests to create event in project under space
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API - Verify if events can be created in public group when event properties are set to false [25238421]
    When Logged in user requests to create event in public group
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API - Verify if events can be created in project under a group when event properties are set to false [25238422]
    When Logged in user requests to create event in project under group
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API - Verify if events can be created in Member only Group when event properties are set to false [25238424]
    When Logged in user requests to create event in member only group
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API - Verify if events can be created in private Group when event properties are set to false [25238425]
    When Logged in user requests to create event in private group
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API - Verify if events can be created in unlisted Group when event properties are set to false [25238426]
    When Logged in user requests to create event in unlisted group
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully

  Scenario: API – Verify if events can be created in external accessible group when event properties are set to false [25238427]
    When Logged in user requests to create event in external accessible group
      |eventTag|eventDescription|
      |EVENT1  |my awesome event|
    Then Event EVENT1 is created successfully
