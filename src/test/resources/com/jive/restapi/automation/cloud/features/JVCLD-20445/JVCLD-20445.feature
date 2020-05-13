@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-20445]

  Background:
    Given We are running E2E JVCLD-20445
    And ADMIN logged into the system
    And User USER1 has been created
    And User USER2 has been created

  Scenario: API - Precondition - Create User1 with department as "Sales" [25312214]
    When User requests to update person USER1
      |jiveLabel |jiveValue |
      |Department|Sales     |
    Then Person USER1 is updated successfully

  Scenario: API - Precondition - Create User2 with department as "Marketing1" [25312223]
    When User requests to update person USER2
      |jiveLabel |jiveValue |
      |Department|Marketing|
    Then Person USER2 is updated successfully

  Scenario: API – Precondition - Deactivate User2 with department as "Marketing1" [25312232]
    When User requests to update person USER2
      |enabled|jiveLabel |jiveValue |
      |false  |Department|Marketing1|
    Then Person USER2 is updated successfully

  Scenario: API – Verify Deactivated user does not show by default on People page [25312234]
    Given User requests to update person USER2
      |enabled|
      |false  |
    When User search person USER2
    Then Search results not include person USER2

  Scenario: API – Verify Deactivated user shown as grey when 'Include Deactivated Users' is selected on people page [25312241]
    Given User requests to update person USER2
      |enabled|
      |false  |
    When User search person USER2 - include deactivated person
    Then Search results include person USER2

  Scenario: API – Verify Active user does show not as grey when 'Include Deactivated Users' is selected on people page [25312247]
    Given User requests to update person USER2
      |enabled|
      |false  |
    When User search person USER1 - include deactivated person
    Then Search results include person USER1

  Scenario: API – Verify department value of Deactivated user does not show in filter [25312245]
    Given User requests to update person USER2
      |enabled|jiveLabel |jiveValue |
      |false  |Department|Marketing1|
    When User search all person
    Then Search results not include department Marketing1

  Scenario: API - Verify department value of Deactivated user is shown in the filter when 'Include Deactivated Users' is selected on people page [25312251]
    Given User requests to update person USER2
      |enabled|jiveLabel |jiveValue |
      |false  |Department|Marketing1|
    When User search all person - include deactivated person
    Then Search results include department Marketing1

  Scenario: API - Verify Deactivated user is searchable by Department value when 'Include Deactivated Users' is selected on people page [25312257]
    Given User requests to update person USER2
      |enabled|jiveLabel |jiveValue |
      |false  |Department|Marketing1|
    When User search all person by department Marketing1 - include deactivated person
    Then Search results include department Marketing1

  Scenario: API - Verify Active user is searchable by Department value when 'Include Deactivated Users' is selected on people page [25312271]
    Given User requests to update person USER2
      |enabled|jiveLabel |jiveValue |
      |false  |Department|Marketing1|
    And User requests to update person USER1
      |enabled|jiveLabel |jiveValue|
      |true   |Department|Sales    |
    When User search all person by department Sales - include deactivated person
    Then Search results include department Sales

  Scenario: API - Precondition - Add Skill value as "Skill-1" for user4 [25312273]
    Given User USER4 has been created
    And User USER4 logs in
    When User requests to add skill for person USER4
      |skill  |
      |Skill-1|
    Then Skill is added successfully for user USER4

  Scenario: API - Precondition - Add Skill value as "Skill-2" for user5 [25312276]
    Given User USER5 has been created
    And User USER5 logs in
    When User requests to add skill for person USER5
      |skill  |
      |Skill-2|
    Then Skill is added successfully for user USER5

  Scenario: API - Precondition - Deactivate User5 with skill value as "Skill-2" [25312277]
    Given User USER5 has been created
    And Add Skill for user USER5
      |skill  |
      |Skill-3|
    And ADMIN logged into the system
    When User requests to update person USER5
      |enabled|
      |false  |
    Then Person USER5 is updated successfully

  Scenario: API - Verify Deactivated user does not show when searched with Skill [25312278]
    Given User USER5 has been created
    And Add Skill for user USER5
      |skill  |
      |Skill-4|
    And User requests to update person USER5
      |enabled|
      |false  |
    And User USER1 logs in
    When User search all person by skill Skill-4
    Then Search results not include skill Skill-4

  Scenario: API - Verify Deactivated user is shown in result when searched with Skill and 'Include Deactivated Users' selected [25312281]
    Given User USER5 has been created
    And Add Skill for user USER5
      |skill  |
      |Skill-5|
    And User requests to update person USER5
      |enabled|
      |false  |
    And User USER1 logs in
    When User search all person by skill Skill-5 - include deactivated person
    Then Search results include skill Skill-5
