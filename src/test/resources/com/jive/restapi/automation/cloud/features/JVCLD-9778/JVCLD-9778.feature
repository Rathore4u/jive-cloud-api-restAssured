@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-9778] - [CLOUD E2E TS 005 - File - Structured Outcomes - Mark as Reserved]

  Background:
    Given We are running E2E JVCLD-9778
    And ADMIN logged into the system

  @CREATE-FILE
  Scenario: API - Verify content owner can able to mark content as Reserved [24032878]
    When User UserFooReserved has been created
    And User UserFooReserved logs in
    And Request to create file TestFile
    Then File TestFile is created successfully
    When User request to mark file TestFile as Reserved FileReserved
    Then FileReserved outcome should be created in TestFile file

  @CREATE-FILE
  Scenario: API - Verify "Mark as Reserved" option should not be available for other members [24032961]
    When User UserOneReserved has been created
    And User UserTwoReserved has been created
    And User UserOneReserved logs in
    And Request to create file TestFileReserved
    Then File TestFileReserved is created successfully
    When User UserTwoReserved logs in
    And Invalid user request to mark file TestFileReserved as Reserved
    Then Content should not be marked with outcome

  @CREATE-FILE
  Scenario: API - Verify user can mark content as Reserved for content marked as final [24032962]
    When User UserFooFileReserved has been created
    And User UserFooFileReserved logs in
    And Request to create file TestFileMarkFinal
    Then File TestFileMarkFinal is created successfully
    When User request to mark file TestFileMarkFinal as Final FileFinal
    Then FileFinal outcome should be created in TestFileMarkFinal file
    When User request to mark file TestFileMarkFinal as Reserved FileFinalReserved
    Then FileFinalReserved outcome should be created in TestFileMarkFinal file

  @CREATE-FILE
  Scenario: API - Verify user can mark content as Reserved for content marked as Outdated [24032964]
    When User UserFooFileOutdatedReserved has been created
    And User UserFooFileOutdatedReserved logs in
    And Request to create file TestFileMarkFinalOutdated
    Then File TestFileMarkFinalOutdated is created successfully
    When User request to mark file TestFileMarkFinalOutdated as Outdated FileOutdated
    Then FileOutdated outcome should be created in TestFileMarkFinalOutdated file
    When User request to mark file TestFileMarkFinalOutdated as Reserved FileOutdatedReserved
    Then FileOutdatedReserved outcome should be created in TestFileMarkFinalOutdated file

  @CREATE-FILE
  Scenario: API - Verify user can mark content as Reserved for content marked as Success [24032965]
    When User UserFooFileSuccessReserved has been created
    And User UserFooFileSuccessReserved logs in
    And Request to create file TestFileMarkSuccessReserved
    Then File TestFileMarkSuccessReserved is created successfully
    When User request to mark file TestFileMarkSuccessReserved as Success FileSuccess
    Then FileSuccess outcome should be created in TestFileMarkSuccessReserved file
    When User request to mark file TestFileMarkSuccessReserved as Reserved FileSuccessReserved
    Then FileSuccessReserved outcome should be created in TestFileMarkSuccessReserved file

  @CREATE-FILE
  Scenario: API - Verify user can mark content as Reserved for content marked as Official [24032966]
    When User UserFooFileOfficialReserved has been created
    And User UserFooFileOfficialReserved logs in
    And Request to create file TestFileMarkOfficialReserved
    Then File TestFileMarkOfficialReserved is created successfully
    When User request to mark file TestFileMarkOfficialReserved as official FileOfficial
    Then FileOfficial outcome should be created in TestFileMarkOfficialReserved file
    When User request to mark file TestFileMarkOfficialReserved as Reserved FileOfficialReserved
    Then FileOfficialReserved outcome should be created in TestFileMarkOfficialReserved file

  @CREATE-FILE
  Scenario: API - Verify user can unmarked content as Reserved [24032967]
    When User UserFooFile has been created
    And User UserFooFile logs in
    And Request to create file TestFileUnmarkReserved
    Then File TestFileUnmarkReserved is created successfully
    When User request to mark file TestFileUnmarkReserved as Reserved ReservedTagFileUnmark
    Then ReservedTagFileUnmark outcome should be created in TestFileUnmarkReserved file
    When Unmark file TestFileUnmarkReserved using outcome ReservedTagFileUnmark
    Then Content has been unmarked successfully using outcome ReservedTagFileUnmark

  @CREATE-FILE
  Scenario: API - Verify Filter Uploaded File as Reserved as Viewer User [24032968]
    When User TestUserFileFilter has been created
    And User TestUserFileFilter logs in
    And Request to create file TestFileContentFilter
    Then File TestFileContentFilter is created successfully
    When User request to mark file TestFileContentFilter as Reserved ReservedTagFilterFile
    Then ReservedTagFilterFile outcome should be created in TestFileContentFilter file
    When User request to search file TestFileContentFilter by outcome wip from searchpage
    Then File TestFileContentFilter is searched successfully in search page