@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-16507] - [E2E test scenario for JVCLD-16507]

  Background:
    Given We are running E2E JVCLD-16507
    Given ADMIN logged into the system

  Scenario: API - Create a category in space [25249867]
    Given Space SpaceOne has been created
    When User creates category CategoryOne in place SpaceOne
    Then Category is created successfully

  Scenario: API - Create two documents one with category and one without category in the space [25337641]
    When User requests to create a document DocumentOneInSpace under place SpaceOne
    Then Document DocumentOneInSpace is created successfully
    When User requests to create a document DocumentTwoInSpace under place SpaceOne with category CategoryOne
    Then Document DocumentTwoInSpace is created successfully

  Scenario: API - Create document with category and tag in the space [25337643]
    When User requests to create a document DocumentThreeInSpace under place SpaceOne with category CategoryOne and tag newTag
    Then Document DocumentThreeInSpace is created successfully

  Scenario: API - Verify content count when user filter by category by selecting the category on the left hand side in the space [25338041]
    When User requests to get contents by category CategoryOne from place SpaceOne
    Then Verify count of contents by category is 2

  Scenario: API - Verify content count when user filter by tag in the space [25338044]
    When User requests to get contents by tag newTag from place SpaceOne
    Then Verify count of contents by tag is 1

  Scenario: API - Create a category in private unlisted group [25338047]
    Given User requests to create a private unlisted group GroupOne
    When User creates category CategoryTwo in place GroupOne
    Then Category is created successfully

  Scenario: API - Create two documents one with category and one without category in the private unlisted group [25338048]
    When User requests to create a document DocumentOneInGroup under place GroupOne
    Then Document DocumentOneInGroup is created successfully
    When User requests to create a document DocumentTwoInGroup under place GroupOne with category CategoryTwo
    Then Document DocumentTwoInGroup is created successfully

  Scenario: API - Create document with category and tag in the private unlisted group [25338050]
    When User requests to create a document DocumentThreeInGroup under place GroupOne with category CategoryTwo and tag newTag
    Then Document DocumentThreeInGroup is created successfully

  Scenario: API - Verify content count when user filter by category by selecting the category on the left hand side in the private unlisted group [25338051]
    When User requests to get contents by category CategoryTwo from place GroupOne
    Then Verify count of contents by category is 2

  Scenario: API - Verify content count when user filter by tag in the private unlisted group [25338054]
    When User requests to get contents by tag newTag from place GroupOne
    Then Verify count of contents by tag is 1

  Scenario: API - Create a category in project [25341575]
    Given Request to create an Project ProjectOne
    When User creates category CategoryThree in place ProjectOne
    Then Category is created successfully

  Scenario: API - Create two documents one with category and one without category in the project [25341588]
    When User requests to create a document DocumentOneInProject under place ProjectOne
    Then Document DocumentOneInProject is created successfully
    When User requests to create a document DocumentTwoInProject under place ProjectOne with category CategoryThree
    Then Document DocumentTwoInProject is created successfully

  Scenario: API - Create document with category and tag in the project [25341591]
    When User requests to create a document DocumentThreeInProject under place ProjectOne with category CategoryThree and tag newTag
    Then Document DocumentThreeInProject is created successfully

  Scenario: API - Verify content count when user filter by category by selecting the category on the left hand side in the project [25341594]
    When User requests to get contents by category CategoryThree from place ProjectOne
    Then Verify count of contents by category is 2

  Scenario: API - Verify content count when user filter by tag in the project [25341601]
    When User requests to get contents by tag newTag from place ProjectOne
    Then Verify count of contents by tag is 1
