@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-9779] - [CLOUD E2E TS 005 - Document - Structured Outcomes - Mark as Reserved]

  Background:
    Given We are running E2E JVCLD-9779
    And ADMIN logged into the system

  Scenario: API - Document: Mark as Reserved [24023282]
    When User UserDocumentReserved has been created
    And User UserDocumentReserved logs in
    And user Request to create a document TestDoc
    Then Document TestDoc is created successfully
    When User request to mark document TestDoc as Reserved DocumentReserved
    Then DocumentReserved outcome should be created in TestDoc document

  Scenario: API - Check for Display of content marked as Reserved In Browse [24023283]
    When User UserDocReserved has been created
    And User UserDocReserved logs in
    And user Request to create a document TestDocumentReserved
    Then Document TestDocumentReserved is created successfully
    When User request to mark document TestDocumentReserved as Reserved DocReserved
    Then DocReserved outcome should be created in TestDocumentReserved document
    When User request to search document TestDocumentReserved by subject from spotlight
    Then It should return document TestDocumentReserved