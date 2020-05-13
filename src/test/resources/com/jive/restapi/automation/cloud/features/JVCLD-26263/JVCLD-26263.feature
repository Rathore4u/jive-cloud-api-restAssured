@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-27456]

  Background:
    Given We are running E2E JVCLD-27456
    And ADMIN logged into the system
    Then User requests to create a group GROUP1

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:11:04 GMT
  Scenario: API - Create new file in subspace and attach pdf document of file name like ".Test" - [25338061]
    When User request to upload File FILEFOO in sub-space under GROUP1 And File Path src/test/resources/TestFiles/testPDF.pdf
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:14:14 GMT
  Scenario: API - Create new file in subspace project and attach pdf document of file name like ".Test" - [25338062]
    Given Request to create an Project PROJECTFOO
    When User request to upload File FILEFOO in sub-space under PROJECTFOO And File Path src/test/resources/TestFiles/testPDF.pdf
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:16:20 GMT
  Scenario: API - Create new file in space project and attach pdf document of file name like ".Test" - [25338063]
    Given Request to create an Project PROJECTFOO
    When User request to upload File FILEFOO in space under PROJECTFOO And File Path src/test/resources/TestFiles/testPDF.pdf
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:17:41 GMT
  Scenario: API - Create new file in group and attach pdf document of file name like ".Test" - [25338064]
    When User request to upload File FILEFOO in group under GROUP1 And File Path src/test/resources/TestFiles/testPDF.pdf
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:17:42 GMT
  Scenario: API - Create new file in community and attach pdf document of file name like ".Test" - [25338065]
    When User request to upload File FILEFOO in community under GROUP1 And File Path src/test/resources/TestFiles/testPDF.pdf
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:17:43 GMT
  Scenario: API - Create new file in subspace and attach word document of file name like ".Test" - [25338066]
    When User request to upload File FILEFOO in sub-space under GROUP1 And File Path src/test/resources/TestFiles/testWord.doc
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:17:44 GMT
  Scenario: API - Create new file in space project and attach word document of file name like ".Test" - [25338067]
    Given Request to create an Project PROJECTFOO
    When User request to upload File FILEFOO in space under PROJECTFOO And File Path src/test/resources/TestFiles/testWord.doc
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:17:45 GMT
  Scenario: API - Create new file in subspace project and attach word document of file name like ".Test" - [25338068]
    Given Request to create an Project PROJECTFOO
    When User request to upload File FILEFOO in sub-space under PROJECTFOO And File Path src/test/resources/TestFiles/testWord.doc
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:17:46 GMT
  Scenario: API - Create new file in group and attach word document of file name like ".Test" - [25338069]
    When User request to upload File FILEFOO in group under GROUP1 And File Path src/test/resources/TestFiles/testWord.doc
    Then File FILEFOO is created successfully

  # Auto generated by aurea-automation - util on Thu, 02 May 2019 08:17:47 GMT
  Scenario: API - Create new file in community and attach word document of file name like ".Test" - [25338070]
    When User request to upload File FILEFOO in community under GROUP1 And File Path src/test/resources/TestFiles/testWord.doc
    Then File FILEFOO is created successfully

  Scenario: API - Create new file in subspace and attach excel csv document of file name like ".Test" - [25338071]
    When User request to upload File FILEFOO in sub-space under GROUP1 And File Path src/test/resources/TestFiles/test.csv
    Then File FILEFOO is created successfully

  Scenario: API - Create new file in subspace project and attach excel csv document of file name like ".Test" - [25338073]
    Given Request to create an Project PROJECTFOO
    When User request to upload File FILEFOO in sub-space under PROJECTFOO And File Path src/test/resources/TestFiles/test.csv
    Then File FILEFOO is created successfully

  Scenario: API - Create new file in space project and attach excel csv document of file name like ".Test" - [25338074]
    Given Request to create an Project PROJECTFOO
    When User request to upload File FILEFOO in space under PROJECTFOO And File Path src/test/resources/TestFiles/test.csv
    Then File FILEFOO is created successfully

  Scenario: API - Create new file in group and attach excel csv document of file name like ".Test" - [25338075]
    When User request to upload File FILEFOO in group under GROUP1 And File Path src/test/resources/TestFiles/test.csv
    Then File FILEFOO is created successfully

  Scenario: API - Create new file in community and attach word document of file name like ".Test" - [25338076]
    When User request to upload File FILEFOO in community under GROUP1 And File Path src/test/resources/TestFiles/test.csv
    Then File FILEFOO is created successfully