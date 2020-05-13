@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-20478]

  Background:
    Given We are running E2E JVCLD-20478
    And ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And User request to create "Open" group GROUP1
    And Request to create a BlogPost BLOGPOST1 under Group GROUP1
    And ADMIN logged into the system
    And User request to create a new stream STREAM1 with rule adding GROUP1 and USER1
    And User USER1 logs in

  Scenario: API - Verify added News stream is visible to UserA [25324475]
    When User USER1 request streams
    Then Streams contain Blog Post BLOGPOST1
    And Admin delete stream STREAM1

  Scenario: API – Verify moved Blog Post-1 is visible to UserA in added custom news stream [25324477]
    Given User request to create "Open" group GROUP2
    And Request to create a BlogPost BLOGPOST1 under Group GROUP1
    And User move BlogPost BLOGPOST1 to group GROUP2
    When User USER1 request streams
    Then Streams contain Blog Post BLOGPOST1
    And Admin delete stream STREAM1

  Scenario: API – Verify moved Blog Post-2 is visible to UserA in All activity stream [25324479]
    Given User request to create "Open" group GROUP2
    And Request to create a BlogPost BLOGPOST2 under Group GROUP1
    And User move BlogPost BLOGPOST2 to group GROUP2
    When User USER1 request streams
    Then Streams contain Blog Post BLOGPOST2
    And Admin delete stream STREAM1

  Scenario: API – Verify moved Document-1 is visible to UserA in added custom news stream [25324481]
    Given User request to create "Open" group GROUP2
    And Request to create a document DOC1 under place GROUP2
    And User move document DOC1 to group GROUP1
    When User USER1 request streams
    Then Streams contain Document DOC1
    And Admin delete stream STREAM1

  Scenario: API – Verify moved Blog Post-2 is visible to UserA in added custom news stream [25345461]
    Given User request to create "Open" group GROUP2
    And Request to create a BlogPost BLOGPOST2 under Group GROUP1
    And User move BlogPost BLOGPOST2 to group GROUP2
    When User USER1 request streams
    Then Streams contain Blog Post BLOGPOST2
    And Admin delete stream STREAM1

  Scenario: API – Verify moved Discussion-1 is visible to UserA in added custom news stream [25345482]
    Given User request to create "Open" group GROUP2
    And Request to create a Discussion DISCUSSION Under Place GROUP1
    And User move discussion DISCUSSION to group GROUP2
    When User USER1 request streams
    Then Streams contain Discussion DISCUSSION
    And Admin delete stream STREAM1

  Scenario: API – Verify moved Question-1 is visible to UserA in added custom news stream [25345520]
    Given User request to create "Open" group GROUP2
    And User requests to create Question QUESTION1 under place GROUP2
    When User move question QUESTION1 to group GROUP2
    When User USER1 request streams
    Then Streams contain Question QUESTION1
    And Admin delete stream STREAM1
