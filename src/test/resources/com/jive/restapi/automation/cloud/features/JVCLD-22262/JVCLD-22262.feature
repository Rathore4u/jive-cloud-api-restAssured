@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-22262] - [E2E test scenario for Document Permission]

  Background:
    Given ADMIN logged into the system
    And We are running E2E JVCLD-22262
    And User BZU is created

  Scenario: API - Create document Allowing 2 specific to edit -[23205035]
    Given User FOO has been created
    When  User FOO logs in
    Then Logged In User Add BZU User to Document TestDoc

  Scenario: API - To verify User 2 can modify the document created by user1 after having edit permission -[23205056]
    When Logged In User Add BZU User to Document TestDoc
    Then Logged in user requests to update document TestDoc with tags test\upgrade,NewTag
