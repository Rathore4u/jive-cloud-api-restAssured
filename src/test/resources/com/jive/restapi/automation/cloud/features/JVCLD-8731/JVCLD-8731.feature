@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8731] - [CLOUD E2E TS 004 - Content - Structured Outcomes - Mark as Official]

  Background:
    Given We are running E2E JVCLD-8731
    And ADMIN logged into the system

  Scenario: API - Mark a document as Official [23968740]
    When User UserFoo has been created
    And User UserFoo logs in
    And User request to create a document TestDoc
    Then Document TestDoc is created successfully
    When user request to mark document TestDoc as Official OfficialTag
    Then outcome OfficialTag should be created in TestDoc

  Scenario: API - Email received by user when a document is marked as Official [23968979]
    When User UserDocumentEmail has been created
    And User UserDocumentEmail logs in
    And User request to create a document TestDocEmail
    Then Document TestDocEmail is created successfully
    When user request to mark document TestDocEmail as Official OfficialTagEmail
    Then outcome OfficialTagEmail should be created in TestDocEmail
    When User request all unread items inboxDocument
    Then Notification of document TestDocEmail is present in inbox inboxDocument

  Scenario: API - Search for a document marked as Official [23968925]
    When User TestUser has been created
    And User TestUser logs in
    And User request to create a document SearchOfficialDoc
    Then Document SearchOfficialDoc is created successfully
    When user request to mark document SearchOfficialDoc as Official OfficialOutcomeTag
    Then outcome OfficialOutcomeTag should be created in SearchOfficialDoc
    When user request to search document SearchOfficialDoc
    Then It should returns document SearchOfficialDoc

  Scenario: API - Edit a document marked as Official [23968984]
    When User RegularUser has been created
    And User RegularUser logs in
    And User request to create a document EditDocument
    Then Document EditDocument is created successfully
    When user request to mark document EditDocument as Official OfficialOutcome
    Then outcome OfficialOutcome should be created in EditDocument
    When Logged in user requests to update document EditDocument with title UpdatedTitle
    Then Document EditDocument is updated with title UpdatedTitle successfully
    And outcome OfficialOutcome should be removed in EditDocument

  Scenario: API - Mark Official document as Outdated [23968999]
    When User TestRegularUser has been created
    And User TestRegularUser logs in
    And User request to create a document UpdateDocOutdated
    Then Document UpdateDocOutdated is created successfully
    When user request to mark document UpdateDocOutdated as Official OutcomeOfficial
    Then outcome OutcomeOfficial should be created in UpdateDocOutdated
    When user request to mark document UpdateDocOutdated as Outdated OutcomeOutdated
    Then outcome OutcomeOutdated should be created in UpdateDocOutdated

  Scenario: API - Filter documents by content marked as Official [23969606]
    When User UserTestSearchFilter has been created
    And User UserTestSearchFilter logs in
    And User request to create a document SearchOfficialDoc
    Then Document SearchOfficialDoc is created successfully
    When user request to mark document SearchOfficialDoc as Official OfficialTagMarked
    Then outcome OfficialTagMarked should be created in SearchOfficialDoc
    When User request to search document SearchOfficialDoc by outcome official from searchpage
    Then Document SearchOfficialDoc is searched successfully

  Scenario: API - Mark a discussion as Official [23969599]
    When User UserFooDiscussion has been created
    And User UserFooDiscussion logs in
    And User request to create a discussion TestDiscussion
    Then Discussion TestDiscussion should be created successfully
    When user request to mark discussion TestDiscussion as Official DiscussionOfficialTag
    Then DiscussionOfficialTag should be created in TestDiscussion discussion

  Scenario: API - Email received by user when a discussion is marked as Official [23969621]
    When User UserDiscussionEmail has been created
    And User UserDiscussionEmail logs in
    And User request to create a discussion TestDiscussionEmail
    Then Discussion TestDiscussionEmail should be created successfully
    When user request to mark discussion TestDiscussionEmail as Official DiscussionOfficialTagEmail
    Then DiscussionOfficialTagEmail should be created in TestDiscussionEmail discussion
    When User request all unread messages for Discussion
    Then Notification of Discussion TestDiscussionEmail is present in inbox

  Scenario: API - Search a discussion marked as Official  [23969601]
    When User TestUserDiscussion has been created
    And  User TestUserDiscussion logs in
    And User request to create a discussion SearchOfficialDiscussion
    Then Discussion SearchOfficialDiscussion should be created successfully
    When user request to mark discussion SearchOfficialDiscussion as Official DiscussionOfficialOutcomeTag
    Then DiscussionOfficialOutcomeTag should be created in SearchOfficialDiscussion discussion
    When User request to search discussion SearchOfficialDiscussion by subject from spotlight
    Then Discussion SearchOfficialDiscussion is searched successfully

  Scenario: API - Filter discussions by content marked as Official [23969619]
    When User UserTestSearchFilterDis has been created
    And User UserTestSearchFilterDis logs in
    And User request to create a discussion SearchOfficialDis
    Then Discussion SearchOfficialDis should be created successfully
    When user request to mark discussion SearchOfficialDis as Official DiscussionOfficialTagMarked
    Then DiscussionOfficialTagMarked should be created in SearchOfficialDis discussion
    When User request to search discussion SearchOfficialDis by outcome official from searchpage
    Then Discussion SearchOfficialDis is searched successfully in main search page

  Scenario: API - Edit a discussion marked as Official  [23969622]
    When User RegularUserDis has been created
    And User RegularUserDis logs in
    And User request to create a discussion EditDiscussion
    Then Discussion EditDiscussion should be created successfully
    When user request to mark discussion EditDiscussion as Official OfficialOutcomeDiscussion
    Then OfficialOutcomeDiscussion should be created in EditDiscussion discussion
    When Logged in user requests to update discussion EditDiscussion with title UpdatedTitleDiscussion
    Then Discussion EditDiscussion is updated with title UpdatedTitleDiscussion successfully
    And OfficialOutcomeDiscussion should be removed in EditDiscussion discussion

  Scenario: API - Mark Official discussion as Outdated [23969623]
    When User TestRegularUserDiscussion has been created
    And  User TestRegularUserDiscussion logs in
    And User request to create a discussion UpdateOutdated
    Then Discussion UpdateOutdated should be created successfully
    When user request to mark discussion UpdateOutdated as Official OfficialTagMarkedDis
    Then OfficialTagMarkedDis should be created in UpdateOutdated discussion
    When user request to mark discussion UpdateOutdated as Outdated OutcomeOutdatedDis
    Then Discussion outcome OutcomeOutdatedDis should be created in discussion UpdateOutdated

  @CREATE-FILE
  Scenario: API - Mark an uploaded file as Official [23969624]
    Given User UserFooFile has been created
    And User UserFooFile logs in
    When Request to create file TestFile
    Then File TestFile is created successfully
    When User request to mark file TestFile as official official
    Then official outcome should be created in TestFile file

  @CREATE-FILE
  Scenario: API - Email received by user when a file is marked as Official [23970921]
    When User UserFileEmail has been created
    And User UserFileEmail logs in
    And Request to create file TestFileEmail
    Then File TestFileEmail is created successfully
    When User request to mark file TestFileEmail as official officialEmail
    Then officialEmail outcome should be created in TestFileEmail file
    When User request all unread items inboxFile
    Then Notification of file TestFileEmail is present in inbox inboxFile

  @CREATE-FILE
  Scenario: API - Search an uploaded file marked as Official [23970918]
    Given User TestUserFile has been created
    And User TestUserFile logs in
    When Request to create file TestFileSearch
    Then File TestFileSearch is created successfully
    When User request to mark file TestFileSearch as official OfficialTagSearchFile
    Then OfficialTagSearchFile outcome should be created in TestFileSearch file
    When User request to search file TestFileSearch by subject from spotlight
    Then It should return file TestFileSearch

  @CREATE-FILE
  Scenario: API - Filters files by content marked as Official [23970920]
    Given User TestUserFile has been created
    And User TestUserFile logs in
    When Request to create file TestFileContent
    Then File TestFileContent is created successfully
    When User request to mark file TestFileContent as official official
    Then official outcome should be created in TestFileContent file
    When User request to search file TestFileContent by outcome official from searchpage
    Then File TestFileContent is searched successfully in search page

  @CREATE-FILE
  Scenario: API - Edit an uploaded file marked as Official [23970924]
    Given User RegularUserFile has been created
    And User RegularUserFile logs in
    When Request to create file EditFileContent
    Then File EditFileContent is created successfully
    When User request to mark file EditFileContent as official OfficialTagEditFile
    Then OfficialTagEditFile outcome should be created in EditFileContent file
    When Logged in user requests to update file EditFileContent with title UpdatedTitleFile
    Then File EditFileContent is updated with title UpdatedTitleFile successfully

  @CREATE-FILE
  Scenario: API - Mark Official file as Outdated [23970925]
    Given User TestRegularUserFile has been created
    And User TestRegularUserFile logs in
    When Request to create file EditFileContentOutdated
    Then File EditFileContentOutdated is created successfully
    When User request to mark file EditFileContentOutdated as official OfficialFileEditOutcome
    Then OfficialFileEditOutcome outcome should be created in EditFileContentOutdated file
    When User request to mark file EditFileContentOutdated as Outdated OutcomeOutdatedFile
    Then Content outcome OutcomeOutdatedFile should be created in file EditFileContentOutdated

  Scenario: API - Unmark a document as Official [24024079]
    When User UserFooDocOfficial has been created
    And User UserFooDocOfficial logs in
    And user Request to create a document TestDocUnmarkOfficial
    Then Document TestDocUnmarkOfficial is created successfully
    When User request to mark document TestDocUnmarkOfficial as official OfficialTagDocument
    Then Content outcome OfficialTagDocument should be created in document TestDocUnmarkOfficial
    When Unmark document TestDocUnmarkOfficial using outcome OfficialTagDocument
    Then Content has been unmarked successfully using outcome OfficialTagDocument

  Scenario: API - Unmark a discussion as Official [24024080]
    When User UserFooDiscussionOfficial has been created
    And User UserFooDiscussionOfficial logs in
    And User request to create a discussion TestDiscussionUnmarkOfficial
    Then Discussion TestDiscussionUnmarkOfficial is created successfully
    When User request to mark discussion TestDiscussionUnmarkOfficial as official OfficialTagDiscussion
    Then Content outcome OfficialTagDiscussion should be created in discussion TestDiscussionUnmarkOfficial
    When Unmark discussion TestDiscussionUnmarkOfficial using outcome OfficialTagDiscussion
    Then Content has been unmarked successfully using outcome OfficialTagDiscussion

  @CREATE-FILE
  Scenario: API - Unmark a file as Official [24024081]
    When User UserFooFileOfficial has been created
    And User UserFooFileOfficial logs in
    And Request to create file TestFileUnmarkOfficial
    Then File TestFileUnmarkOfficial is created successfully
    When User request to mark file TestFileUnmarkOfficial as official OfficialTagFile
    Then OfficialTagFile outcome should be created in TestFileUnmarkOfficial file
    When Unmark file TestFileUnmarkOfficial using outcome OfficialTagFile
    Then Content has been unmarked successfully using outcome OfficialTagFile