@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-9561] - [CLOUD E2E TS 002 - Content - Structured Outcomes - Mark as Final - Part C]

  Background:
    Given We are running E2E JVCLD-9561
    And ADMIN logged into the system

  @CREATE-FILE
  Scenario: API - Create File and verify that Mark as Final Link is available [25238272]
    When User UserFooReserved has been created
    And User UserFooReserved logs in
    And Request to create file TestFile
    Then File TestFile is created successfully
    When User request to mark file TestFile as Final FileFinal
    Then FileFinal outcome should be created in TestFile file

  @CREATE-FILE
  Scenario: API - Verify that Mark as Final Link is not available for other users (user other than the author) [25238274]
    When User UserOneFinal has been created
    And User UserTwoFinal has been created
    And User UserOneFinal logs in
    And Request to create file TestFileFinal
    Then File TestFileFinal is created successfully
    When User UserTwoFinal logs in
    And Invalid user request to mark file TestFileFinal as Final
    Then Content should not be marked with outcome

  @CREATE-FILE
  Scenario: API - Verify that File can be marked as Final [25238275]
    When User UserFooFileFinal has been created
    And User UserFooFileFinal logs in
    And Request to create file TestFileMarkFinal
    Then File TestFileMarkFinal is created successfully
    When User request to mark file TestFileMarkFinal as Final FileFinal
    Then FileFinal outcome should be created in TestFileMarkFinal file

  @CREATE-FILE
  Scenario: API - Verify that File can be unmarked as Final [25238277]
    When User UserFooFile has been created
    And User UserFooFile logs in
    And Request to create file TestFileUnmarkFinal
    Then File TestFileUnmarkFinal is created successfully
    When User request to mark file TestFileUnmarkFinal as Final FinalTagFileUnmark
    Then FinalTagFileUnmark outcome should be created in TestFileUnmarkFinal file
    When Unmark file TestFileUnmarkFinal using outcome FinalTagFileUnmark
    Then Content has been unmarked successfully using outcome FinalTagFileUnmark

  @CREATE-FILE
  Scenario: API - Verify that editing File will undo 'Mark as Final' [25238279]
    When User RegularUserFile has been created
    And User RegularUserFile logs in
    And Request to create file EditFileContent
    Then File EditFileContent is created successfully
    When User request to mark file EditFileContent as Final FinalTagEditFile
    Then FinalTagEditFile outcome should be created in EditFileContent file
    When Logged in user requests to update file EditFileContent with title UpdatedTitleFile
    Then File EditFileContent is updated with title UpdatedTitleFile successfully

  @CREATE-FILE
  Scenario: API - Verify that user can mark edited File as 'Mark as Final' [25238280]
    When User RegularUserFileFinal has been created
    And User RegularUserFileFinal logs in
    And Request to create file EditFileContentFinal
    Then File EditFileContentFinal is created successfully
    When Logged in user requests to update file EditFileContentFinal with title TitleFileUpdated
    Then File EditFileContentFinal is updated with title TitleFileUpdated successfully
    When User request to mark file EditFileContentFinal as Final TagEditFileFinal
    Then TagEditFileFinal outcome should be created in EditFileContentFinal file

  @CREATE-FILE
  Scenario: API - Verify that user can Unmark edited File [25238281]
    When User UserFileFinal has been created
    And User UserFileFinal logs in
    And Request to create file TestFileUnmark
    Then File TestFileUnmark is created successfully
    When Logged in user requests to update file TestFileUnmark with title TitleFileUpdated
    Then File TestFileUnmark is updated with title TitleFileUpdated successfully
    When User request to mark file TestFileUnmark as Final FinalTagFileUnmark
    Then FinalTagFileUnmark outcome should be created in TestFileUnmark file
    When Unmark file TestFileUnmark using outcome FinalTagFileUnmark
    Then Content has been unmarked successfully using outcome FinalTagFileUnmark
