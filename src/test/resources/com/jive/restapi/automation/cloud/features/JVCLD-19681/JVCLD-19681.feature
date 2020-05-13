@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-19681]

  Background:
    Given We are running E2E JVCLD-19681
    When ADMIN logged into the system
    Then User FOO is created

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a public group can be created.  - [25495265]
    Given User FOO is logged in
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When User requests to create a open public group Public
    Then group is created successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place Public
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a public restricted group can be created. - [25495266]
    Given User FOO is logged in
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When User requests to create a public restricted group PublicRestricted
    Then group is created successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place PublicRestricted
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a private unlisted group can be created. - [25495267]
    Given User FOO is logged in
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When User requests to create a private unlisted group PrivateUnlisted
    Then group is created successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place PrivateUnlisted
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a externally accessible group can be created. - [25495268]
    Given User FOO is logged in
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When User requests to create a externally accessible group ExternallyAccessible
    Then group is created successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place ExternallyAccessible
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a sub space can be created. - [25495270]
    Given Admin creates Space SPACE
    When Admin requests to create a subSpace SUBSPACE under space SPACE
    Then SubSpace SUBSPACE has SPACE as Parent
    When User FOO is logged in
    And  Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place SUBSPACE
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a space can be created. - [25495271]
    Given Admin creates Space SPACE
    When  User FOO is logged in
    And Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place SPACE
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a group project can be created. - [25495272]
    Given User FOO is logged in
    When Request to create an Project PROJECT
    And Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place PROJECT
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a space project can be created. - [25495273]
    Given Admin creates Space SPACE
    When  User FOO is logged in
    Then Create Project PROJECT In Place SPACE
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place PROJECT
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a community publish location can be created. - [25495897]
    Given User FOO is logged in
    When Request to create an Project PROJECT
    And Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create Content DOCUMENT with Image IMAGE in RTE In Community
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a specific people publish location can be created. - [25495893]
    Given User USER3 has been created
    When User USER2 has been created
    Then User FOO is logged in
    When Request to create an Project PROJECT
    And Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create a draft document DOCUMENT2 with "Specific People" publish location:
      | USER2 |
      | USER3 |
    Then Document DOCUMENT2 is created successfully
    When Update a document DOCUMENT2 with Image IMAGE In RTE
    Then Content DOCUMENT2 is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a hidden publish location can be created. - [25495473]
    Given User FOO is logged in
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create Content DOCUMENT with Image IMAGE in Hidden Publish Location
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE

  Scenario: API - Verify Create binary document along with an image inserted in RTE from web in a sub space project can be created. - [25495274]
    Given Admin creates Space SPACE
    When Admin requests to create a subSpace SUBSPACE under space SPACE
    And  User FOO is logged in
    Then Create Project PROJECT In Place SUBSPACE
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create DOCUMENT with Image IMAGE in RTE In Place PROJECT
    Then Content DOCUMENT is created successfully with Image testImage.png In RTE
