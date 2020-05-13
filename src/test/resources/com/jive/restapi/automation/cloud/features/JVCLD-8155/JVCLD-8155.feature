@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8155] - [E2E test scenario for JVCLD-8155]

  Background:
    Given We are running E2E JVCLD-8155
    And ADMIN logged into the system

  Scenario: API - Create Document with external Link [23206078]
    When Logged in user requests to create Document DocumentOne with description http://www.google.com in community
    Then Document DocumentOne is created successfully

  Scenario: API - Create Discussion with external Link [23206079]
    When Logged in user requests to create Discussion DiscussionOne with description http://www.google.com in community
    Then Discussion DiscussionOne is created successfully

  Scenario: API - Create Blog Post with external Link [23206083]
    When Logged in user requests to create BlogPost BlogPostOne with description http://www.google.com in community
    Then BlogPost BlogPostOne is created successfully

  Scenario: API - Create Poll with external Link [23206085]
    When Request to create an Poll With Description http://www.google.com
    Then Poll is Created Successfully

  Scenario: API - Create Idea with external Link [23206087]
    When User requests to create an Idea IdeaOne with description http://www.google.com
    Then Idea IdeaOne is created successfully

  Scenario: API - Create Direct Message with external Link [23209090]
    Given User UserOne has been created
    When Create a direct message to UserOne with description http://www.google.com
    Then Direct message created successfully
