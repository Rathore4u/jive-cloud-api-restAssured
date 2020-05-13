@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-19474]

  Background:
    Given We are running E2E JVCLD-19474
    And ADMIN logged into the system


  Scenario: API - [Precondition] Create an announcement in Group with link to Blog 1 [24024243]
    Given User USER1 has been created
    And User USER1 logs in
    And Request to create a BlogPost BP1 under Group GROUP1
    When User add an announcement ANNOUN1 in group GROUP1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    Then Announcement is created successfully

  Scenario: API - Verify user is redirected to Blog 1 page on clicking link in announcement on Group Activity page [24024232]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in group GROUP1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    When User request to search announcement ANNOUN1 from spotlight
    Then Search results include announcement ANNOUN1
      |description                         |
      |http://www.testexample.com/blogPost1|


  Scenario: API - [Precondition] Edit announcement from Group Activity page and add link of Blog 2 in description [24024244]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in group GROUP1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    And Request to create a BlogPost BP2 under Group GROUP1
    When User request to update announcement ANNOUN1
      |description                                                              |
      |http://www.testexample.com/blogPost1 http://www.testexample.com/blogPost2|
    Then Announcement is updated successfully

  Scenario: API - [Precondition] Edit announcement from Project Activity page and add link of Blog 2 in description [24024248]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in project PROJECT1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    And Request to create a BlogPost BP2 under Project GROUP1
    When User request to update announcement ANNOUN1
      |description                                                              |
      |http://www.testexample.com/blogPost1 http://www.testexample.com/blogPost2|
    Then Announcement is updated successfully

  Scenario: API - [Precondition] Create an announcement in Space with link to Blog 1 [24024250]
    And User requests to create a space SPACE1
    Given User USER1 logs in
    And Request to create a BlogPost BP1 under Space SPACE1
    When User add an announcement ANNOUN1 in space SPACE1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    Then Announcement is created successfully

  Scenario: API - [Precondition] Create an announcement in Project in Space with link to Blog 1 [24024247]
    Given User requests to create a space SPACE1
    Given User USER1 logs in
    And User requests to create a Project PROJECT1 under space SPACE1
    And Request to create a BlogPost BP1 under Project PROJECT1
    When User add an announcement ANNOUN1 in project PROJECT1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    Then Announcement is created successfully

  Scenario: API - Verify user is redirected to Blog 1 page on clicking link in announcement on Project Activity page [24024239]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in project PROJECT1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    When User request to search announcement ANNOUN1 from spotlight
    Then Search results include announcement ANNOUN1
      |description                         |
      |http://www.testexample.com/blogPost1|

  Scenario: API - Verify user is redirected to Blog 1 and Blog 2 from links in announcement on Group Activity page [24024237]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in group GROUP1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    And Request to create a BlogPost BP2 under Group GROUP1
    And User request to update announcement ANNOUN1
      |description                                                              |
      |http://www.testexample.com/blogPost1 http://www.testexample.com/blogPost2|
    When User request to search announcement ANNOUN1 from spotlight
    Then Search results include announcement ANNOUN1
      |description                         |
      |http://www.testexample.com/blogPost1|
      |http://www.testexample.com/blogPost2|

  Scenario: API - Verify user is redirected to Blog 1 and Blog 2 from links in announcement on Project Activity page [24024240]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in project PROJECT1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost3|
    And Request to create a BlogPost BP4 under Group GROUP1
    And User request to update announcement ANNOUN1
      |description                                                              |
      |http://www.testexample.com/blogPost3 http://www.testexample.com/blogPost4|
    When User request to search announcement ANNOUN1 from spotlight
    Then Search results include announcement ANNOUN1
      |description                         |
      |http://www.testexample.com/blogPost3|
      |http://www.testexample.com/blogPost4|

  Scenario: API - [Precondition] Edit announcement from Space Activity page and add link of Blog 2 in description [24024251]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in space SPACE1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost1|http://www.testexample.com/blogPost1|
    And Request to create a BlogPost BP2 under Space SPACE1
    When User request to update announcement ANNOUN1
      |description                                                              |
      |http://www.testexample.com/blogPost1 http://www.testexample.com/blogPost2|
    Then Announcement is updated successfully

  Scenario: API - Verify user is redirected to Blog 1 page on clicking link in announcement on Space Activity page [24024241]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in space SPACE1 with link to Blog BLOG1
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost5|http://www.testexample.com/blogPost5|
    When User request to search announcement ANNOUN1 from spotlight
    Then Search results include announcement ANNOUN1
      |description                         |
      |http://www.testexample.com/blogPost5|

  Scenario: API - Verify user is redirected to Blog 1 and Blog 2 from links in announcement on Space Activity page [24024242]
    Given User USER1 logs in
    Given Create an announcement ANNOUN1 in space SPACE1 with link to Blog BLOG6
      |blogLink                            |description                         |
      |http://www.testexample.com/blogPost6|http://www.testexample.com/blogPost6|
    And Request to create a BlogPost BP7 under Space SPACE1
    And User request to update announcement ANNOUN1
      |description                                                              |
      |http://www.testexample.com/blogPost6 http://www.testexample.com/blogPost7|
    When User request to search announcement ANNOUN1 from spotlight
    Then Search results include announcement ANNOUN1
      |description                         |
      |http://www.testexample.com/blogPost6|
      |http://www.testexample.com/blogPost7|