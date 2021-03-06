@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8721] - [CLOUD E2E TS 026 - Content - Blog Posts]

  Background:
    Given We are running E2E JVCLD-8721
    Then ADMIN logged into the system
    And User BZU is created

    # Auto generated by chrome-extension on Tue, 16 Apr 2019 10:57:58 GMT
  Scenario: API - Create a blog post with mentions others - [25283607]
    When Logged in user requests to create BlogPost BLOGFOO with user in body in community
    Then Post BLOGFOO is created successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 07:52:51 GMT
  Scenario: API- Like a blog post with mentions - [25287619]
    When User BZU logs in
    And Logged in user Like blog BLOGFOO
    Then Like added on blog Successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 08:08:46 GMT
  Scenario: API - Comment on a blog post with mentions - [25287620]
    When Add a comment TESTCOMMENT to the blog post BLOGFOO
    Then Comment has been added to the blog post successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 08:21:15 GMT
  Scenario: API - Edit a blog post - [25287621]
    When Logged in user requests to update blogpost BLOGFOO with title TITLEFOO
    Then BlogPost BLOGFOO is updated with title TITLEFOO successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 08:27:37 GMT
  Scenario: API - Create a blog post in a user"s Personal blog with mentions - [25287622]
    When Logged in user requests to create BlogPost BLOGPLACE with user in body under Place
    Then BlogPost BLOGPLACE is created successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 09:02:24 GMT
  Scenario: API - Create a blog post with attached file with mentions - [25287623]
    When Logged in user requests to create BlogPost BLOGPATTACHMENT with user in body And Attachment
    Then BlogPost BLOGPATTACHMENT is created successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 14:10:03 GMT
  Scenario: API - Schedule when the blog post gets published - [25287624]
    When Schedule Future BlogPost FUTUREBLOG
    Then BlogPost FUTUREBLOG is created successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 14:22:57 GMT
  Scenario: API - Like a blog post Scheduled - [25287626]
    When User BZU logs in
    And Logged in user Like Schedule blog FUTUREBLOG
    Then Verify Like Cannot be added on Schedule Post

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 08:03:22 GMT
  Scenario: API - Edit Scheduled a blog post - [25287630]
    When Logged in user requests to update post FUTUREBLOG with new place
    Then BlogPost FUTUREBLOG is created successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 14:19:41 GMT
  Scenario: API - Restrict comments in a blog post - [25287625]
    When Restrict Comment On BlogPost RESTRICTBLOG
    Then BlogPost is created successfully with Restrict

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 14:23:52 GMT
  Scenario: API - Comment on a blog post Restricted - [25287627]
    When Add a comment TESTCOMMENT to the blog post RESTRICTBLOG
    Then Comment has been Restricted to the blog post successfully

    # Auto generated by chrome-extension on Wed, 17 Apr 2019 14:23:52 GMT
  Scenario: API- View blog post as PDF - [25290583]
    When View BlogPost RESTRICTBLOG as PDF
    Then Content Viewed As PDF Successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 07:37:36 GMT
  Scenario: API - move a blog post - [25287628]
    When Logged in user requests to update post RESTRICTBLOG with new place
    Then BlogPost RESTRICTBLOG is created successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 08:00:19 GMT
  Scenario: API - Delete a blog post - [25287629]
    When User request to delete post RESTRICTBLOG
    Then Post RESTRICTBLOG is deleted successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 08:15:29 GMT
  Scenario: API - Create a blog post with video(s) - [25287631]
    When Logged in user requests to create BlogPost BLOG with user in body And Attachment
    Then BlogPost BLOG is created successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 08:21:38 GMT
  Scenario: API  - Create a blog post with a banner image - [25287633]
    When Logged in user requests to create BlogPost BLOG with user in body And Banner Image
    Then BlogPost BLOG is created successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 17:17:22 GM
  Scenario: API - Create a blog post and save it as a Draft - [25287634]
    When User requests to create a draft BlogPost BLOG
    Then BlogPost BLOG is created successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 17:22:16 GMT
  Scenario: API - Create a blog post in a place"s blog - [25287635]
    When Logged in user requests to create BlogPost BLOGPLACE with user in body under Place
    Then BlogPost BLOGPLACE is created successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 17:22:35 GMT
  Scenario: API - Create a blog post in a user"s Personal blog - [25287636]
    When Logged in user requests to create BlogPost BLOGFOO with user in body in community
    Then Post BLOGFOO is created successfully

    # Auto generated by chrome-extension on Thu, 18 Apr 2019 17:23:00 GMT
  Scenario: API - Create a blog post with inline images - [25287632]
    When Logged in user requests to create BlogPost BLOGFOO with user in body And Inline Image
    Then Post BLOGFOO is created successfully
