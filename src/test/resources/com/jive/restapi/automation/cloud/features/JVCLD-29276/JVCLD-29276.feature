@APISuite
Feature: CLOUD RestAssured API Experimental Suite - JVCLD-29276

  Background:
    Given We are running E2E JVCLD-29276
    And ADMIN logged into the system

  # Auto generated by aurea-automation - util on Tue, 30 Apr 2019 18:09:25 GMT
  Scenario: API - [Pre-Condition] Create Event1 in Public Group with specific event details - [25313337]
    When Logged in user requests to create event in public group
      | eventTag  | eventDescription |
      | EVENTLOC1 | my awesome event |
    Then Event EVENTLOC1 is created successfully

  # Auto generated by aurea-automation - util on Tue, 30 Apr 2019 18:29:00 GMT
  Scenario: API - [Pre-Condition] Create Event2 in Public Group with different location - [25313338]
    When Logged in user requests to create event in public group
      | eventTag  | eventDescription |
      | EVENTLOC2 | my awesome event |
    Then Event EVENTLOC2 is created successfully

  # Auto generated by aurea-automation - util on Tue, 30 Apr 2019 19:03:01 GMT
  Scenario: API - [Pre-Condition] Create Event3 in Public Group with different city - [25313339]
    When Logged in user requests to create event in public group
      | eventTag  | eventDescription |
      | EVENTCITY | my awesome event |
    Then Event EVENTCITY is created successfully

  # Auto generated by aurea-automation - util on Tue, 30 Apr 2019 19:03:02 GMT
  Scenario: API - [Pre-Condition] Create Event4 in Public Group with different Country - [25313341]
    When Logged in user requests to create event in public group
      | eventTag     | eventDescription |
      | EVENTCOUNTRY | my awesome event |
    Then Event EVENTCOUNTRY is created successfully

  # Auto generated by aurea-automation - util on Tue, 30 Apr 2019 19:40:45 GMT
  Scenario: API - Verify that event published in Public Group with same Title, Same time but different Location are displayed - [25313343]
    When Verify that events EVENTCITY EVENTCOUNTRY EVENTLOC1 published in Public Group with same Title
    Then Verify that events EVENTCITY EVENTCOUNTRY EVENTLOC1 published in Public Group with Different Location

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 08:54:53 GMT
  Scenario: API - [Pre-Condition] Create Event1 in Space with specific event details - [25313344]
    When Logged in user requests to create event under space
      | eventTag  | eventDescription |
      | EVENTLOC1 | my awesome event |
    Then Event EVENTLOC1 is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 08:54:54 GMT
  Scenario: API - [Pre-Condition] Create Event2 in Space with different location - [25313346]
    When Logged in user requests to create event under space
      | eventTag  | eventDescription |
      | EVENTLOC2 | my awesome event |
    Then Event EVENTLOC2 is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 08:54:54 GMT
  Scenario: API - [Pre-Condition] Create Event3 in Space with different city - [25313347]
    When Logged in user requests to create event under space
      | eventTag  | eventDescription |
      | EVENTCITY | my awesome event |
    Then Event EVENTCITY is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 08:54:55 GMT
  Scenario: API - [Pre-Condition] Create Event4 in Space with different Country - [25313348]
    When Logged in user requests to create event under space
      | eventTag     | eventDescription |
      | EVENTCOUNTRY | my awesome event |
    Then Event EVENTCOUNTRY is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:01:59 GMT
  Scenario: API - [Pre-Condition] Create Event1 in Sub-Space with specific event details - [25313349]
    When Logged in user requests to create event under sub-space
      | eventTag  | eventDescription |
      | EVENTLOC1 | my awesome event |
    Then Event EVENTLOC1 is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:02:00 GMT
  Scenario: API- [Pre-Condition] Create Event2 in Sub-Space with different location - [25313350]
    When Logged in user requests to create event under sub-space
      | eventTag  | eventDescription |
      | EVENTLOC2 | my awesome event |
    Then Event EVENTLOC2 is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:02:01 GMT
  Scenario: API - [Pre-Condition] Create Event3 in Sub-Space with different city - [25313351]
    When Logged in user requests to create event under sub-space
      | eventTag  | eventDescription |
      | EVENTCITY | my awesome event |
    Then Event EVENTCITY is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:02:01 GMT
  Scenario: API - [Pre-Condition] Create Event4 in Sub-Space with different Country - [25313352]
    When Logged in user requests to create event under sub-space
      | eventTag     | eventDescription |
      | EVENTCOUNTRY | my awesome event |
    Then Event EVENTCOUNTRY is created successfully
    
  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:30:21 GMT
  Scenario: API - [Pre-Condition] Create Event1 in Group->Project with specific event details - [25313353]
    When Logged in user requests to create event in project under group
      | eventTag  | eventDescription |
      | EVENTLOC1 | my awesome event |
    Then Event EVENTLOC1 is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:30:22 GMT
  Scenario: API - [Pre-Condition] Create Event2 in Group->Project with different location - [25313354]
    When Logged in user requests to create event in project under group
      | eventTag  | eventDescription |
      | EVENTLOC2 | my awesome event |
    Then Event EVENTLOC2 is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:30:23 GMT
  Scenario: API - [Pre-Condition] Create Event3 in Group->Project with different city - [25313355]
    When Logged in user requests to create event in project under group
      | eventTag  | eventDescription |
      | EVENTCITY | my awesome event |
    Then Event EVENTCITY is created successfully

  # Auto generated by aurea-automation - util on Wed, 01 May 2019 09:30:23 GMT
  Scenario: API -[Pre-Condition] Create Event4 in Group->Project with different Country - [25313356]
    When Logged in user requests to create event in project under group
      | eventTag     | eventDescription |
      | EVENTCOUNTRY | my awesome event |
    Then Event EVENTCOUNTRY is created successfully
    