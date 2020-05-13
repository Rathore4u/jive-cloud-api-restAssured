@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-18160]

  Background:
    Given We are running E2E JVCLD-18160
    When ADMIN logged into the system
    Then User FOO has been created

  Scenario: API - Verify that Question with plain text in description posted in a group can be viewed as PDF by author - [25338099]
    Given User FOO is logged in
    When Logged in user requests to create Question QUESTION with description QUESTION DESCRIPTION under place
    Then Question is Created Successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with plain text in description posted in a group can be viewed as PDF by non author - [25338100]
    Given User BZU has been created
    When User FOO is logged in
    And Logged in user requests to create Question QUESTION with description QUESTION DESCRIPTION under place
    Then Question is Created Successfully
    When User BZU is logged in
    And View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with replies posted in a group can be viewed as PDF by author - [25338101]
    Given User FOO is logged in
    When Logged in user requests to create Question QUESTION with description QUESTION DESCRIPTION under place
    And Add Reply REPLY to the Content QUESTION
    Then Reply REPLY is added to the QUESTION successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with replies posted in a group can be viewed as PDF by non author - [25338102]
    Given User BZU has been created
    When User FOO is logged in
    And Logged in user requests to create Question QUESTION with description QUESTION DESCRIPTION under place
    Then Question is Created Successfully
    When User BZU is logged in
    And Add Reply REPLY to the Content QUESTION
    Then Reply REPLY is added to the QUESTION successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with pdf attachment posted in a group can be viewed as PDF by author - [25338103]
    Given User FOO is logged in
    When Create Content QUESTION with attachment file TestFiles/testPDF.pdf with Type application/pdf
    Then Question QUESTION Attachment testPDF.pdf is attached successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: Verify that Question with excel attachment posted in a group can be viewed as PDF by author - [25338104]
    Given User FOO is logged in
    When Create Content QUESTION with attachment file TestFiles/sample.xls with Type application/vnd.ms-excel
    Then Question QUESTION Attachment sample.xls is attached successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with image as attachment posted in a group can be viewed as PDF by author - [25338105]
    Given User FOO is logged in
    When Create Content QUESTION with attachment file TestFiles/testImage.png with Type image/png
    Then Question QUESTION Attachment testImage.png is attached successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with image inserted from RTE posted in a group can be viewed as PDF by author - [25338106]
    Given User FOO is logged in
    When Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create Content QUESTION with Image IMAGE in RTE In Place
    Then Content QUESTION is created successfully with Image testImage.png In RTE

  Scenario: API - Verify that Question with image inserted from RTE posted in a group can be viewed as PDF by non author - [25338108]
    Given User BZU has been created
    When User FOO is logged in
    And Upload an image IMAGE using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create Content QUESTION with Image IMAGE in RTE In Place
    Then Content QUESTION is created successfully with Image testImage.png In RTE
    When User BZU is logged in
    And View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with tags posted in a group can be viewed as PDF by author - [25338109]
    Given User FOO is logged in
    When Create question QUESTION with tags tagone,tagtwo Under Place
    Then Question is Created Successfully
    When View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully

  Scenario: API - Verify that Question with tags posted in a group can be viewed as PDF by non author - [25338112]
    Given User BZU has been created
    When User FOO is logged in
    And Create question QUESTION with tags tagone,tagtwo Under Place
    Then Question is Created Successfully
    When User BZU is logged in
    And View Content QUESTION as PDF
    Then Content Viewed As PDF Successfully
