@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8753] - [CLOUD E2E TS 025 - Content - Binary Documents]

  Background:
    Given We are running E2E JVCLD-8753
    And ADMIN logged into the system

  Scenario: API - Verify User is able to create binary document with option "Allow specific people to edit this document" in community [23999083]
    Given User USER1 has been created
    And User USER2 has been created
    And User USER3 has been created
    When User USER1 is logged in
    And Create a file FILE1 with attach testPDF.pdf and with "Allow specific people to edit this document" option:
      |USER2|
    Then File FILE1 is created successfully
    When User USER2 is logged in
    And Update file FILE1 with new subject SUBJECT1
    Then File FILE1 is updated with subject SUBJECT1 successfully
    When User USER3 is logged in
    And Update file FILE1 with subject SUBJECTUPDATED1 by user with no edit rights
    Then File FILE1 isn't updated with subject SUBJECTUPDATED1

  Scenario: API - Verify User is able to create binary document as Hidden [23999112]
    Given User USER1 is logged in
    When Create a file FILE2 with attach testPDF.pdf and with hidden visibility
    Then File FILE2 is created successfully

  Scenario: API - Verify User is able to create binary document with "Specific People" [23999159]
    Given User USER1 is logged in
    When Create a file FILE3 with attach testPDF.pdf and with "Specific People" publish location:
      |USER2|
    Then File FILE3 is created successfully

  Scenario: API - Verify User can create binary document with Tag [23999204]
    Given User USER1 is logged in
    When Logged in user requests to create a File FILE4 with tags #Tag8753 And File Path src/test/resources/TestFiles/testPDF.pdf
    Then File FILE4 is created successfully

  Scenario: API - Verify User is able to add comment in binary document [23999235]
    Given User USER1 is logged in
    And Logged in user requests to create a File FILE5 with tags #Tag8753 And File Path src/test/resources/TestFiles/testPDF.pdf
    When User USER2 is logged in
    And Add a comment COMMENT1 to the file FILE5
    Then Comment is added to the file successfully

  Scenario: API - Verify User is able to edit binary document in community [23999274]
    Given User USER1 is logged in
    And Logged in user requests to create a File FILE6 with tags #Tag8753 And File Path src/test/resources/TestFiles/testPDF.pdf
    When Update file FILE6 with new subject SUBJECT2
    Then File FILE6 is updated with subject SUBJECT2 successfully

  Scenario: API - Verify file version is increased by 1 when file is edited [23999292]
    Given User USER1 is logged in
    When Read file FILE6
    Then File FILE6 version is equal to 2

  Scenario: API - Verify User can see file version in manage versions in community [23999332]
    Given User USER1 is logged in
    When Get file FILE6 versions to list VERSIONSLIST1
    Then File versions list VERSIONSLIST1 contains 2 versions

  Scenario: API - Verify User can edit file using option "Minor edit in a binary document" in community [23999511]
    Given User USER1 is logged in
    And Logged in user requests to create a File FILE7 with tags #Tag8753 And File Path src/test/resources/TestFiles/testPDF.pdf
    And Logged in user requests to create a File FILE8 with tags #Tag8753 And File Path src/test/resources/TestFiles/testPDF.pdf
    When Make minor update of file FILE7 with new subject SUBJECT3
    Then File FILE7 is updated with subject SUBJECT3 successfully
    When Get latest 1 activity ACTIVITY1 from all activities
    Then Activities ACTIVITY1 dont-contain content FILE7

  Scenario: API - Verify User can delete binary file in community [23999702]
    Given User USER1 is logged in
    And Logged in user requests to create a File FILE9 with tags #Tag8753 And File Path src/test/resources/TestFiles/testPDF.pdf
    When Delete file FILE9
    Then File FILE9 is deleted successfully