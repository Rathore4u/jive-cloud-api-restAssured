@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8726] - [CLOUD E2E TS 041 - Content - Ideas Part A]

  Background:
    Given We are running E2E JVCLD-8726
    And ADMIN logged into the system

  Scenario: API - Edit Idea in the Group [23213457]
    When User UserEditIdea has been created
    And User UserEditIdea is logged in
    And User request to create an TestIdea idea in the OpenGroup group
    And User request to update idea TestIdea title as TestTitle
    Then Idea TestIdea should be updated with title TestTitle

  Scenario: API - Delete Idea in the Project [23213459]
    When Request to create an Project TestProject
    Then Project TestProject is Created Successfully
    When User UserDeleteIdea has been created
    And User UserDeleteIdea is logged in
    And User request to create an TestDeleteIdea idea in the TestProject group
    And User request to delete idea TestDeleteIdea
    Then Idea TestDeleteIdea is deleted successfully

  Scenario: API - Add tag to the idea in edit mode and in published mode [23213456]
    When User UserEditIdea has been created
    And User UserEditIdea is logged in
    And User requests to create idea TestIdeaTag with tags TagOne
    Then Idea TestIdeaTag is Created Successfully with TagOne
    When User adds a tag TagTwo to the existing Idea TestIdeaTag
    Then Idea TestIdeaTag is Created Successfully with TagTwo

  Scenario: API - Create idea with user container (specific people) [23213460]
    When User UserSharePersonIdea has been created
    And User UserCreateIdea has been created
    And User UserCreateIdea is logged in
    And User request to create an TestIdeaShare idea with specific person UserSharePersonIdea
    Then Idea is Created Successfully

  Scenario: API - Create idea with an image from the web in a group [25320436]
    When admin requests to create a space SpaceOne
    And User UserCreateIdeaImageWeb has been created
    And User UserCreateIdeaImageWeb is logged in
    And User request to create an IdeaTitle idea with image from Web Under Place SpaceOne
    Then Idea is Created Successfully

  Scenario: API - Create idea with an image from your computer in a space [25320437]
    When admin requests to create a space SPACE1
    And User UserCreateIdeaImage has been created
    And User UserCreateIdeaImage is logged in
    And Upload an image IMAGE1 using file TestFiles/testImage.png
    Then Image is uploaded successfully
    When Create Idea IDEA1 with an image IMAGE1 in RTE under place SPACE1
    Then Idea IDEA1 is created successfully

  Scenario: API - Create idea with video from the web in a project [25320438]
    When admin requests to create a space SpaceTwo
    And User requests to create a project ProjectInSpace under place SpaceTwo
    And User UserCreateIdeaImageWeb has been created
    And User UserCreateIdeaImageWeb is logged in
    And User request to create an IdeaTitle idea with Video from Web Under Place ProjectInSpace
    Then Idea is Created Successfully
