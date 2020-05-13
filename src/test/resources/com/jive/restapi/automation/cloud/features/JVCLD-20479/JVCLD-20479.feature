@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-20479]

  Background:
    Given We are running E2E JVCLD-20479
    Given ADMIN logged into the system

  Scenario: API - Create Umlauts Discussion content - [23204692]
    When user creates a umlauts Discussion DISFOO with tags #E2ETag01
    Then Discussion DISFOO is created successfully

  Scenario: API-  Update Discussion with Umlauts content - [23204693]
    When Logged in user requests to create a discussion DISFOR with tags #E2ETag01
    Then Discussion DISFOR is created successfully
    When user updates a Discussion DISFOR with Umlauts
    Then Discussion DISFOR is updated with umlauts successfully

  Scenario: API - Create Umlauts Document content - [23204694]
    When user creates a umlauts Document DOCFOO with tags #E2ETag01
    Then Document DOCFOO is created successfully

  Scenario: API-  Update Document with Umlauts content - [23204695]
    When Logged in user requests to create document DOCFOR with tags #E2ETag01
    Then Document DOCFOR is created successfully
    When user updates a Document DOCFOR with Umlauts
    Then Document DOCFOR is updated with umlauts successfully

  Scenario: API - Create Umlauts Blog Post - [23204696]
    When user creates a umlauts BlogPost POSTFOO with tags #E2ETag01
    Then Post POSTFOO is created successfully

  Scenario: API-  Update BlogPost with Umlauts content - [23204697]
    When Logged in user requests to create post POSTFOR with tags #E2ETag01
    Then Post POSTFOR is created successfully
    When user updates a BlogPost POSTFOR with Umlauts
    Then BlogPost POSTFOR is updated with umlauts successfully

  Scenario: API - Verify pdf preview for created discussion [23970692]
    When User requests to get pdf of discussion DISFOO and gets response PdfResponse
    Then Pdf request PdfResponse is successful

  Scenario: API - Verify pdf preview for updated discussion [23970693]
    When User requests to get pdf of discussion DISFOR and gets response PdfResponse
    Then Pdf request PdfResponse is successful

  Scenario: API - Verify pdf preview for created document [23970695]
    When User requests to get pdf of document DOCFOO and gets response PdfResponse
    Then Pdf request PdfResponse is successful

  Scenario: API - Verify pdf preview for updated document [23970697]
    When User requests to get pdf of document DOCFOR and gets response PdfResponse
    Then Pdf request PdfResponse is successful

  Scenario: API - Verify pdf preview for created blog post [23970698]
    When User requests to get pdf of blogpost POSTFOO and gets response PdfResponse
    Then Pdf request PdfResponse is successful

  Scenario: API - Verify pdf preview for updated blog post [23970699]
    When User requests to get pdf of blogpost POSTFOR and gets response PdfResponse
    Then Pdf request PdfResponse is successful
