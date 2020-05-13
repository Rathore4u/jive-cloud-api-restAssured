@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-18159]

  Background:
    Given We are running E2E JVCLD-18159
    When ADMIN logged into the system
    And User FOO has been created
    Then Admin creates Space SPACE

  Scenario: API - Verify that Question with plain text in description published in a space can be viewed as PDF by author - [25353112]
    Given User FOO is logged in
    When Logged in user requests to create Question QUESTION Under Space SPACE
    Then Question is Created Successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with plain text in description published in a space can be viewed as PDF by non author - [25353113]
    Given User BZU has been created
    When User FOO is logged in
    And  Logged in user requests to create Question QUESTION Under Space SPACE
    Then Question is Created Successfully
    When User BZU is logged in
    And View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with image inserted from RTE posted in a space can be viewed as PDF by author - [25353114]
    Given User FOO is logged in
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create Content QUESTION with Image IMAGE in RTE In Space SPACE
    Then Content QUESTION is created successfully with Image testImage.png In RTE

  Scenario: API - Verify that Question with image inserted from RTE posted in a space can be viewed as PDF by non author - [25353115]
    Given User BZU has been created
    When User FOO is logged in
    And Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create Content QUESTION with Image IMAGE in RTE In Space SPACE
    Then Content QUESTION is created successfully with Image testImage.png In RTE
    When User BZU is logged in
    And View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with replies posted in a space can be viewed as PDF by author - [25353116]
    Given User FOO is logged in
    When Create Content QUESTION with Image IMAGE in RTE In Space SPACE
    And Add Reply REPLY to the Content QUESTION
    And User awaits for 60 seconds
    Then Reply REPLY is added to the QUESTION successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with replies posted in a space can be viewed as PDF by non author - [25353117]
    Given User BZU has been created
    When User FOO is logged in
    And Create Content QUESTION with Image IMAGE in RTE In Space SPACE
    Then Question is Created Successfully
    When User BZU is logged in
    And Add Reply REPLY to the Content QUESTION
    And User awaits for 60 seconds
    Then Reply REPLY is added to the QUESTION successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with pdf attachment posted in a space can be viewed as PDF by author - [25353118]
    Given User FOO is logged in
    When Request To Create Content QUESTION In Space SPACE with attachment file TestFiles/testPDF.pdf with Type application/pdf
    Then Question QUESTION Attachment testPDF.pdf is attached successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with excel attachment posted in a space can be viewed as PDF by author - [25353119]
    Given User FOO is logged in
    When  Request To Create Content QUESTION In Space SPACE with attachment file TestFiles/sample.xls with Type application/vnd.ms-excel
    Then Question QUESTION Attachment sample.xls is attached successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with image as attachment posted in a space can be viewed as PDF by author - [25353120]
    Given User FOO is logged in
    When Request To Create Content QUESTION In Space SPACE with attachment file TestFiles/testImage.png with Type image/png
    Then Question QUESTION Attachment testImage.png is attached successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with tags posted in a space can be viewed as PDF by author - [25353121]
    Given User FOO is logged in
    When Request To Create QUESTION In Space SPACE with tags tagone,tagtwo
    Then Question is Created Successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with tags posted in a space can be viewed as PDF by non author - [25353122]
    Given User BZU has been created
    When User FOO is logged in
    And Request To Create QUESTION In Space SPACE with tags tagone,tagtwo
    Then Question is Created Successfully
    When User BZU is logged in
    And View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully
