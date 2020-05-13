@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8156] - [E2E test scenario for Announcements]

  Background:
    Given We are running E2E JVCLD-8156
    And ADMIN logged into the system

  Scenario: API - Announcement creation in root community [23194925]
    When Admin creates a system announcement AnnouncementFoo
    Then Announcement is created successfully

  Scenario: API - Verify System Announcement visibility to Regular User [23194952]
    Given Admin creates a system announcement AnnouncementFoo
    Then Announcement is created successfully
    When User UserFoo has been created
    And User UserFoo is logged in
    Then Announcement AnnouncementFoo is visible to regular user

  Scenario: API - Precondition - As an admin creating a space [23195477]
    When Admin creates Space SpaceFoo
    Then Space SpaceFoo has been created

  @FailFixRCA
  Scenario: API - Announcement creation in a space [23195479]
    Given User UserFoo has been created
    And Admin creates a system announcement AnnouncementFoo1
    Then Announcement is created successfully
    When User UserFoo is logged in
    And User requests all notifications
    And User awaits for 60 seconds
    Then Notification of AnnouncementFoo1 is present in inbox

  Scenario: API - Verify Inbox Notification when Announcement created in root community [23200391]
    When ADMIN creates an announcement SpaceAnnouncement in SpaceFoo
    Then Announcement is created successfully

  Scenario: API - Announcement creation in a group [23201034]
    Given User requests to create a group GroupFoo in SpaceFoo
    When ADMIN creates an announcement GroupAnnouncement in GroupFoo
    Then Announcement is created successfully

  Scenario: API - Edit System announcement from Manage Announcement [23201062]
    Given Admin creates a system announcement AnnouncementFoo
    Then Announcement is created successfully
    When Logged in user updates announcement AnnouncementFoo
    Then Announcement is updated successfully

  Scenario: API - Edit announcement from the group activity page [23201079]
    Given Admin creates a system announcement AnnouncementFoo
    Then Announcement is created successfully
    When Logged in user updates announcement GroupAnnouncement
    Then Announcement is updated successfully

  Scenario: API - Expire System Announcement [23201082]
    Given Admin creates a system announcement AnnouncementFoo
    Then Announcement is created successfully
    When Logged in user expires announcement AnnouncementFoo
    Then Announcement is updated successfully
