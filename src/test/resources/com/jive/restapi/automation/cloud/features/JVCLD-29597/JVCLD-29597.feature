@APISuite
Feature: [CLOUD API Experimental Suite] - [E2E test scenario for JVCLD-29597]

  Background:
    Given We are running E2E JVCLD-29597
    And ADMIN logged into the system
    And User USER1 has been created
    And User USER2 has been created
    And User USER1 logs in

  Scenario: API - Verify User2 gets inbox notification for invite to private event in community [25238946]
    Given Logged in user requests to create private event EVENT1 with description "testing-private-event" in community
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    Then Event invite INVITE1 sent successfully
    When User USER2 logs in
    And User request all unread notifications
    Then Inbox returns 1 unread item

  Scenario: API - Verify User2 can view Private event in community in search results when invited [25238947]
    Given Logged in user requests to create private event EVENT1 with description "testing-private-event" in community
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    And User USER2 logs in
    When User request to search event EVENT1 by subject from search
    Then Event EVENT1 is searched successfully

  Scenario: API - Verify non invited user does not get inbox notification for invite to private event in community [25238949]
    Given ADMIN logged into the system
    And User USER3 has been created
    And User USER1 logs in
    And Logged in user requests to create private event EVENT1 with description "testing-private-event" in community
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    When User USER3 logs in
    And User request all unread notifications
    Then Inbox returns 0 unread item

  Scenario: API - Verify non invited user can not view private event in community in search results [25238950]
    Given ADMIN logged into the system
    And User USER3 has been created
    And User USER1 logs in
    And Logged in user requests to create private event EVENT1 with description "testing-private-event" in community
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    When User USER3 logs in
    And User request to search event EVENT1 by subject from search
    Then Event is not found

  Scenario: API - Verify User2 gets inbox notification for invite to private event in Group [25238953]
    Given Logged in user requests to create private event EVENT1 with description "testing-private-event" in group
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    Then Event invite INVITE1 sent successfully
    When User USER2 logs in
    And User request all unread notifications
    Then Inbox returns 1 unread item

  Scenario: API - Verify User2 can view private event in Group in search results when invited [25238955]
    Given Logged in user requests to create private event EVENT1 with description "testing-private-event" in group
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    And User USER2 logs in
    When User request to search event EVENT1 by subject from search
    Then Event EVENT1 is searched successfully

  Scenario: API - Verify non invited user does not get inbox notification for invite to private event in Group [25238956]
    Given ADMIN logged into the system
    And User USER3 has been created
    And User USER1 logs in
    And Logged in user requests to create private event EVENT1 with description "testing-private-event" in group
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    When User USER3 logs in
    And User request to search event EVENT1 by subject from search
    Then Event is not found

  Scenario: API - Verify non invited user can not view private event in Group in search results [25238957]
    Given ADMIN logged into the system
    And User USER3 has been created
    And User USER1 logs in
    And Logged in user requests to create private event EVENT1 with description "testing-private-event" in group
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    When User USER3 logs in
    And User request to search event EVENT1 by subject from search
    Then Event is not found

  Scenario: API - Verify User2 gets inbox notification for invite to Hidden private event [25238960]
    Given Logged in user requests to create private event EVENT1 as hidden with description "testing-private-event"
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    Then Event invite INVITE1 sent successfully
    When User USER2 logs in
    And User request all unread notifications
    Then Inbox returns 1 unread item

  Scenario: API - Verify User2 can view Hidden private event in search results when invited [25238961]
    Given Logged in user requests to create private event EVENT1 as hidden with description "testing-private-event"
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    And User USER2 logs in
    When User request to search event EVENT1 by subject from search
    Then Event EVENT1 is searched successfully

  Scenario: API - Verify non invited user can not view Hidden private event in search results [25238962]
    Given ADMIN logged into the system
    And User USER3 has been created
    And User USER1 logs in
    And Logged in user requests to create private event EVENT1 as hidden with description "testing-private-event"
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    When User USER3 logs in
    And User request to search event EVENT1 by subject from search
    Then Event is not found

  Scenario: API - Verify User2 can view private event in community when invited [25345545]
    Given User USER1 logs in
    And Logged in user requests to create private event EVENT1 with description "testing-private-event" in community
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    And User USER2 logs in
    When User request private event EVENT1
    Then User can view private event EVENT1 details

  Scenario: API – Verify User2 can view detail of private event in Group when invited [25345578]
    Given User USER1 logs in
    And Logged in user requests to create private event EVENT1 with description "testing-private-event" in group
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    And User USER2 logs in
    When User request private event EVENT1
    Then User can view private event EVENT1 details

  Scenario: API - Verify User2 can view detail of Hidden private event when invited [25345579]
    Given User USER1 logs in
    And Logged in user requests to create private event EVENT1 as hidden with description "testing-private-event"
    And User send invitation INVITE1 to user USER2 to join event EVENT1
    And User USER2 logs in
    When User request private event EVENT1
    Then User can view private event EVENT1 details
