@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-28082]

  Background:
    Given We are running E2E JVCLD-28082
    And ADMIN logged into the system
    And User USER1 has been created
    And User USER1 logs in
    And Create corrupted file on file system

  Scenario: API - Verify that correct error message is displayed when uploading an error video in Jive Community [25269971]
    When User request to upload corrupted video VIDEO1 in community
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in Group [25269972]
    When User request to upload corrupted video VIDEO1 in group
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in Group Project [25269977]
    Given User requests to create a group GROUP1
    When User request to upload corrupted video VIDEO1 in project under GROUP1
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in Space Project [25269978]
    Given ADMIN logged into the system
    And User requests to create a space SPACE1
    And User USER1 logs in
    When User request to upload corrupted video VIDEO1 in project under SPACE1
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in SubSpace [25270448]
    Given ADMIN logged into the system
    And User requests to create a space SPACE1
    And User requests to create a subSpace SUB-SPACE1 under space SPACE1
    And User USER1 logs in
    When User request to upload corrupted video VIDEO1 in sub-space SUB-SPACE1
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in SubSpace Project [25284382]
    Given ADMIN logged into the system
    And User requests to create a space SPACE1
    And User requests to create a subSpace SUB-SPACE1 under space SPACE1
    And User USER1 logs in
    When User request to upload corrupted video VIDEO1 in project under SUB-SPACE1
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in Public Restricted Group [25284384]
    Given User requests to create a public restricted group RES_GROUP1
    When User request to upload corrupted video VIDEO1 in restricted group RES_GROUP1
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in Private Group [25284385]
    Given User requests to create a private group PRIVATE_GROUP1
    When User request to upload corrupted video VIDEO1 in private group PRIVATE_GROUP1
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in Private Unlisted Group [25284386]
    Given User requests to create a private unlisted group PRIVATE_UNLISTED_GROUP1
    When User request to upload corrupted video VIDEO1 in private unlisted group PRIVATE_UNLISTED_GROUP1
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video with publish location as specific people [25284387]
    Given ADMIN logged into the system
    And User USER2 has been created
    And User USER1 logs in
    When User request to upload corrupted video VIDEO1 with publish location as specific people USER2
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in the RTE of a document [25287247]
    When User request to upload corrupted video VIDEO in RTE of a document
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in the RTE of a question [25284389]
    When User request to upload corrupted video VIDEO in RTE of a question
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in the RTE of a discussion [25286851]
    When User request to upload corrupted video VIDEO in RTE of a discussion
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API – Verify that correct error message is displayed when uploading an error video in the RTE of an Idea [25286852]
    When User request to upload corrupted video VIDEO in RTE of an idea
    Then VIDEO1 can't be upload - an error occurred while processing video

  Scenario: API - Verify that correct error message is displayed when uploading an error video in the RTE of a Blog [25286853]
    Given User requests to create a personal Blog BLOG1 for user USER1
    When User request to upload corrupted video VIDEO1 in blog BLOG1
    Then VIDEO1 can't be upload - an error occurred while processing video

