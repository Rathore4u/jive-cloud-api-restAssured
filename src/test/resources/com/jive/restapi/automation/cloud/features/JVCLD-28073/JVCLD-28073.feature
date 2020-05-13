@APISuite
Feature: [CLOUD - API Experimental Suite] - [JVCLD-28073] - [E2E test scenario for JVCLD-28073]

  Background:
    Given We are running E2E JVCLD-28073

  Scenario: Precondition - Create two standard users and a group
    Given ADMIN logged into the system
    When User UserOne has been created
    And User UserTwo has been created
    Then User UserOne is logged in
    When User request to create "Open" group GroupOne
    Then "Open" group is created successfully

  Scenario: API - Verify '@mention + space' for people and place in Blog post disappears the context menu [25264606]
    When User requests to create a draft BlogPost BlogPostOne
    Then BlogPost BlogPostOne is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + space' for people and place in Document disappears the context menu [25264613]
    When User requests to create a draft Document DocumentOne
    Then Document DocumentOne is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + space' for people and place in Discussion disappears the context menu [25266791]
    When User requests to create a draft Discussion DiscussionOne
    Then Discussion is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + space' for people and place in Question disappears the context menu [25281948]
    When User requests to create a draft Question QuestionOne
    Then Question QuestionOne is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + space' for people and place in Poll disappears the context menu [25281963]
    When User requests to create a draft Poll PollOne
    Then Poll is Created Successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + space' for people and place in Event disappears the context menu [25282005]
    When User requests to create a draft Event EventOne
    Then Event EventOne is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + space' for people and place in Idea disappears the context menu [25282015]
    When User requests to create a draft Idea IdeaOne
    Then Idea is Created Successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + '_'' for people and place in Blog post doesnt disappear the context menu [25292771]
    When User requests to create a draft BlogPost BlogPostTwo
    Then BlogPost BlogPostTwo is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for person UserTwo using firstname from atmention with underscore
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully
    When User searches for group GroupOne using name from atmention with underscore
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + '_'' for people and place in Document doesnt disappear the context menu [25292795]
    When User requests to create a draft Document DocumentTwo
    Then Document DocumentTwo is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for person UserTwo using firstname from atmention with underscore
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully
    When User searches for group GroupOne using name from atmention with underscore
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + '_'' for people and place in Discussion doesnt disappear the context menu [25292806]
    When User requests to create a draft Discussion DiscussionTwo
    Then Discussion is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for person UserTwo using firstname from atmention with underscore
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully
    When User searches for group GroupOne using name from atmention with underscore
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + '_'' for people and place in Question doesnt disappear the context menu [25292832]
    When User requests to create a draft Question QuestionTwo
    Then Question QuestionTwo is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for person UserTwo using firstname from atmention with underscore
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully
    When User searches for group GroupOne using name from atmention with underscore
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + '_'' for people and place in Poll doesnt disappear the context menu [25292848]
    When User requests to create a draft Poll PollTwo
    Then Poll is Created Successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for person UserTwo using firstname from atmention with underscore
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully
    When User searches for group GroupOne using name from atmention with underscore
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + '_'' for people and place in Event doesnt disappear the context menu [25292849]
    When User requests to create a draft Event EventTwo
    Then Event EventTwo is created successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for person UserTwo using firstname from atmention with underscore
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully
    When User searches for group GroupOne using name from atmention with underscore
    Then Group GroupOne is searched successfully

  Scenario: API - Verify '@mention + '_'' for people and place in Idea doesnt disappear the context menu [25292854]
    When User requests to create a draft Idea IdeaTwo
    Then Idea is Created Successfully
    When User searches for person UserTwo using firstname from atmention
    Then Person UserTwo is searched successfully
    When User searches for person UserTwo using firstname from atmention with underscore
    Then Person UserTwo is searched successfully
    When User searches for group GroupOne using name from atmention
    Then Group GroupOne is searched successfully
    When User searches for group GroupOne using name from atmention with underscore
    Then Group GroupOne is searched successfully

  Scenario: API - Verify that the group and person can be selected from the context menu [25292907]
    When User requests to create a Document DocumentThree with GroupOne mention and UserTwo mention
    Then Document DocumentThree is created successfully
