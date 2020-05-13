@APISuite
Feature: [CLOUD API Experimental Suite] - [JVCLD-8733] - [CLOUD E2E TS 006 - Content - Structured Outcomes - Mark as Success]

  Background:
    Given We are running E2E JVCLD-8733
    And ADMIN logged into the system

  Scenario: API - Mark a Document as Success [23971932]
    When User UserFoo has been created
    And User UserFoo logs in
    And user Request to create a document TestDoc
    Then Document TestDoc is created successfully
    When User request to mark document TestDoc as Success SuccessTag
    Then Content outcome SuccessTag should be created in document TestDoc

  Scenario: API - Email received by user when Document is marked as Success [23971935]
    When User UserDocumentEmail has been created
    And User UserDocumentEmail logs in
    And user Request to create a document TestDocEmail
    Then Document TestDocEmail is created successfully
    When User request to mark document TestDocEmail as Success SuccessTagDocumentEmail
    Then Content outcome SuccessTagDocumentEmail should be created in document TestDocEmail
    When User request all unread items inboxDocument
    Then Notification of document TestDocEmail is present in inbox inboxDocument

  Scenario: API - Search for a Document marked as Success [23971933]
    When User UserFooSearch has been created
    And User UserFooSearch logs in
    And user Request to create a document TestDocSearch
    Then Document TestDocSearch is created successfully
    When User request to mark document TestDocSearch as Success SuccessTagSearch
    Then Content outcome SuccessTagSearch should be created in document TestDocSearch
    When User request to search document TestDocSearch by subject from spotlight
    Then It should return document TestDocSearch

  Scenario: API - Filter documents by content marked as Success [23971934]
    When User UserFooFilterDoc has been created
    And User UserFooFilterDoc logs in
    And user Request to create a document TestDocFilter
    Then Document TestDocFilter is created successfully
    When User request to mark document TestDocFilter as Success SuccessTagFilter
    Then Content outcome SuccessTagFilter should be created in document TestDocFilter
    When User request to search document TestDocFilter by outcome success from searchpage
    Then Document TestDocFilter is searched successfully

  Scenario: API - Edit a document marked as Success [23971936]
    When User UserFooEdit has been created
    And User UserFooEdit logs in
    And User request to create a document TestDocEdit
    Then Document TestDocEdit is created successfully
    When User request to mark document TestDocEdit as Success SuccessTagEdit
    Then outcome SuccessTagEdit should be created in TestDocEdit
    When Logged in user requests to update document TestDocEdit with title UpdatedTitle
    Then Document TestDocEdit is updated with title UpdatedTitle successfully
    And outcome SuccessTagEdit should be removed in TestDocEdit

  Scenario: API - Mark a Discussion as Success [23972416]
    When User UserFooDiscussion has been created
    And User UserFooDiscussion logs in
    And User request to create a discussion TestDiscussion
    Then Discussion TestDiscussion should be created successfully
    When User request to mark discussion TestDiscussion as Success SuccessTagDiscussion
    Then Discussion outcome SuccessTagDiscussion should be created in discussion TestDiscussion

  Scenario: API - Email received by user when Discussion is marked as Success [23972419]
    When User UserDiscussionEmail has been created
    And User UserDiscussionEmail logs in
    And User request to create a discussion TestDiscussionEmail
    Then Discussion TestDiscussionEmail should be created successfully
    When User request to mark discussion TestDiscussionEmail as Success SuccessTagDiscussionEmail
    Then Discussion outcome SuccessTagDiscussionEmail should be created in discussion TestDiscussionEmail
    When User request all unread messages for Discussion
    Then Notification of Discussion TestDiscussionEmail is present in inbox

  Scenario: API - Search for a Discussion marked as Success [23972417]
    When User UserFooDiscussionSearch has been created
    And User UserFooDiscussionSearch logs in
    And User request to create a discussion TestDiscussionSearch
    Then Discussion TestDiscussionSearch should be created successfully
    When User request to mark discussion TestDiscussionSearch as Success SuccessTagSearchDiscussion
    Then Discussion outcome SuccessTagSearchDiscussion should be created in discussion TestDiscussionSearch
    When User request to search discussion TestDiscussionSearch by subject from spotlight
    Then Discussion TestDiscussionSearch is searched successfully

  Scenario: API - Filter discussions by content marked as Success [23972418]
    When User UserFooFilterDiscussion has been created
    And User UserFooFilterDiscussion logs in
    And User request to create a discussion TestDiscussionFilter
    Then Discussion TestDiscussionFilter should be created successfully
    When User request to mark discussion TestDiscussionFilter as Success SuccessTagFilterDiscussion
    Then Discussion outcome SuccessTagFilterDiscussion should be created in discussion TestDiscussionFilter
    When User request to search discussion TestDiscussionFilter by outcome success from searchpage
    Then Discussion TestDiscussionFilter is searched successfully in main search page


  Scenario: API - Edit a discussion marked as Success [23972420]
    When User UserFooEditDiscussion has been created
    And User UserFooEditDiscussion logs in
    And User request to create a discussion EditDiscussion
    Then Discussion EditDiscussion should be created successfully
    When User request to mark discussion EditDiscussion as Success successOutcomeDiscussion
    Then successOutcomeDiscussion should be created in EditDiscussion discussion
    When Logged in user requests to update discussion EditDiscussion with title UpdatedTitleDiscussion
    Then Discussion EditDiscussion is updated with title UpdatedTitleDiscussion successfully
    And successOutcomeDiscussion should be removed in EditDiscussion discussion

  Scenario: API - Mark a Blog post as Success [23999348]
    When User UserFooBlog has been created
    And User UserFooBlog logs in
    And Request to create a BlogPost BlogPostCreate
    Then BlogPost BlogPostCreate is created successfully
    When User request to mark blog post BlogPostCreate as Success SuccessTagBlog
    Then Content outcome SuccessTagBlog should be created in blog post BlogPostCreate

  Scenario: API - Email received by user when Blog post is marked as Success [23999351]
    When User UserBlogEmail has been created
    And User UserBlogEmail logs in
    And Request to create a BlogPost BlogPostEmail
    Then BlogPost BlogPostEmail is created successfully
    When User request to mark blog post BlogPostEmail as Success SuccessTagBlogEmail
    Then Content outcome SuccessTagBlogEmail should be created in blog post BlogPostEmail
    When User request all unread items inboxBlog
    Then Notification of blog BlogPostEmail is present in inbox inboxBlog

  Scenario: API - Search for a Blog post marked as Success [23999349]
    When User UserFooBlogSearch has been created
    And User UserFooBlogSearch logs in
    And Request to create a BlogPost BlogPostSearch
    Then BlogPost BlogPostSearch is created successfully
    When User request to mark blog post BlogPostSearch as Success SuccessTagBlogSearch
    Then Content outcome SuccessTagBlogSearch should be created in blog post BlogPostSearch
    When User request to search blog post BlogPostSearch by subject from spotlight
    Then Blog Post BlogPostSearch is searched successfully in search page

  Scenario: API - Filter blogs by content marked as Success [23999350]
    When User UserFooBlogFilter has been created
    And User UserFooBlogFilter logs in
    And Request to create a BlogPost BlogPostFilter
    Then BlogPost BlogPostFilter is created successfully
    When User request to mark blog post BlogPostFilter as Success SuccessTagBlogFilter
    Then Content outcome SuccessTagBlogFilter should be created in blog post BlogPostFilter
    When User request to search blog post BlogPostFilter by outcome success from searchpage
    Then Blog Post BlogPostFilter is searched successfully in search page

  Scenario: API - Edit a blog post marked as Success [23999352]
    When User UserFooBlogEdit has been created
    And User UserFooBlogEdit logs in
    And Logged in user requests to create BlogPost BlogPostEdit with description BlogPostDescription in community
    Then Post BlogPostEdit is created successfully
    When User request to mark blogpost BlogPostEdit as Success SuccessTagBlogEdit
    Then Verify content blog post BlogPostEdit is marked as success outcome SuccessTagBlogEdit
    When Logged in user requests to update blogpost BlogPostEdit with title updatedTitle
    Then BlogPost BlogPostEdit is updated with title updatedTitle successfully
    And Outcome SuccessTagBlogEdit should removed in blog BlogPostEdit

  Scenario: API - Mark a comment in a Blog post as Success [23999341]
    When User UserCommentTest has been created
    And User UserCommentTest logs in
    And Request to create a BlogPost BlogPostContent
    And User add comment CommentBlog on blog post BlogPostContent
    Then Comment is created successfully
    When User request to mark comment CommentBlog as Success and get outcome OutcomeTagBlogPost
    Then Comment has been marked as outcomeType successfully with creation of outcome OutcomeTagBlogPost

  Scenario: API - Mark a comment in a Document as Success [23999342]
    When User UserCommentDocument has been created
    And User UserCommentDocument logs in
    And user Request to create a document TestDocComment
    Then Document TestDocComment is created successfully
    When User add comment CommentDocument on document TestDocComment
    Then Comment is created successfully
    When User request to mark comment CommentDocument as Success and get outcome OutcomeTagDocument
    Then Comment has been marked as outcomeType successfully with creation of outcome OutcomeTagDocument

  @CREATE-FILE
  Scenario: API - Mark an uploaded file as Success [23972740]
    When User UserFooFile has been created
    And User UserFooFile logs in
    And Request to create file TestFile
    Then File TestFile is created successfully
    When User request to mark file TestFile as Success FileSuccess
    Then FileSuccess outcome should be created in TestFile file

  @CREATE-FILE
  Scenario: API - Email received by user when File is marked as Success [23999344]
    When User UserFileEmail has been created
    And User UserFileEmail logs in
    And Request to create file TestFileEmail
    Then File TestFileEmail is created successfully
    When User request to mark file TestFileEmail as Success FileSuccessEmail
    Then FileSuccessEmail outcome should be created in TestFileEmail file
    When User request all unread items inboxFile
    Then Notification of file TestFileEmail is present in inbox inboxFile

  @CREATE-FILE
  Scenario: API - Search for a File marked as Success [23972741]
    When User TestUserFileSearch has been created
    And User TestUserFileSearch logs in
    And Request to create file TestFileSearch
    Then File TestFileSearch is created successfully
    When User request to mark file TestFileSearch as Success SuccessTagSearchFile
    Then SuccessTagSearchFile outcome should be created in TestFileSearch file
    When User request to search file TestFileSearch by subject from spotlight
    Then It should return file TestFileSearch

  @CREATE-FILE
  Scenario: API - Filter files by content marked as Success [23999343]
    When User TestUserFileFilter has been created
    And User TestUserFileFilter logs in
    And Request to create file TestFileContentFilter
    Then File TestFileContentFilter is created successfully
    When User request to mark file TestFileContentFilter as Success SuccessTagFilterFile
    Then SuccessTagFilterFile outcome should be created in TestFileContentFilter file
    When User request to search file TestFileContentFilter by outcome success from searchpage
    Then File TestFileContentFilter is searched successfully in search page

  @CREATE-FILE
  Scenario: API - Edit a file marked as Success [23999345]
    When User RegularUserFile has been created
    And User RegularUserFile logs in
    And Request to create file EditFileContent
    Then File EditFileContent is created successfully
    When User request to mark file EditFileContent as Success SuccessTagEditFile
    Then SuccessTagEditFile outcome should be created in EditFileContent file
    When Logged in user requests to update file EditFileContent with title UpdatedTitleFile
    Then File EditFileContent is updated with title UpdatedTitleFile successfully

  @CREATE-FILE
  Scenario: API - Mark a comment in a File as Success [24002905]
    When User RegularUserFile has been created
    And User RegularUserFile logs in
    And Request to create file CommentFileContent
    Then File CommentFileContent is created successfully
    When User add comment CommentFile on file CommentFileContent
    Then Comment is created successfully
    When User request to mark comment CommentFile as Success and get outcome OutcomeTagFile
    Then Comment has been marked as outcomeType successfully with creation of outcome OutcomeTagFile

  Scenario: API - Search for comment marked as Success [24003719]
    When User UserFooBlogCommentSearch has been created
    And User UserFooBlogCommentSearch logs in
    And Request to create a BlogPost BlogPostSearch
    Then BlogPost BlogPostSearch is created successfully
    When User add comment CommentBlog on blog post BlogPostSearch
    Then Comment is created successfully
    When User request to mark comment CommentBlog as Success and get outcome OutcomeTagBlogPost
    Then Comment has been marked as outcomeType successfully with creation of outcome OutcomeTagBlogPost
    When User request to search comment CommentBlog by subject from spotlight
    Then It should return blog post BlogPostSearch

  Scenario: API - Filter comments marked as Success [24003729]
    When User UserFooFilterDocument has been created
    And User UserFooFilterDocument logs in
    And user Request to create a document TestDocumentFilter
    Then Document TestDocumentFilter is created successfully
    When Request to create a BlogPost BlogPostSearchFilter
    Then BlogPost BlogPostSearchFilter is created successfully
    When User add comment CommentBlogPost on blog post BlogPostSearchFilter
    Then Comment is created successfully
    When User add comment CommentDocument on document TestDocumentFilter
    Then Comment is created successfully
    When User request to mark comment CommentBlogPost as Success and get outcome OutcomeTagBlogPostFilter
    Then Comment has been marked as outcomeType successfully with creation of outcome OutcomeTagBlogPostFilter
    When User request to mark comment CommentDocument as Success and get outcome OutcomeTagDoc
    Then Comment has been marked as outcomeType successfully with creation of outcome OutcomeTagDoc
    When User request to search comment CommentDocument by subject from searchpage
    Then It should return document TestDocumentFilter
    When User request to search comment CommentBlogPost by subject from searchpage
    Then It should return blog post BlogPostSearchFilter

  Scenario: API - Unmark a document as Success [24024082]
    When User UserFooDocSuccess has been created
    And User UserFooDocSuccess logs in
    And user Request to create a document TestDocUnmarkSuccess
    Then Document TestDocUnmarkSuccess is created successfully
    When User request to mark document TestDocUnmarkSuccess as Success SuccessTagDocument
    Then Content outcome SuccessTagDocument should be created in document TestDocUnmarkSuccess
    When Unmark document TestDocUnmarkSuccess using outcome SuccessTagDocument
    Then Content has been unmarked successfully using outcome SuccessTagDocument

  Scenario: API - Unmark a discussion as Success [24024083]
    When User UserFooDiscussionSuccess has been created
    And User UserFooDiscussionSuccess logs in
    And User request to create a discussion TestDiscussionUnmarkSuccess
    Then Discussion TestDiscussionUnmarkSuccess is created successfully
    When User request to mark discussion TestDiscussionUnmarkSuccess as Success SuccessTagDiscussionUnmark
    Then Content outcome SuccessTagDiscussionUnmark should be created in discussion TestDiscussionUnmarkSuccess
    When Unmark discussion TestDiscussionUnmarkSuccess using outcome SuccessTagDiscussionUnmark
    Then Content has been unmarked successfully using outcome SuccessTagDiscussionUnmark

  @CREATE-FILE
  Scenario: API - Unmark a file as Success [24024084]
    When User UserFooFileSuccess has been created
    And User UserFooFileSuccess logs in
    And Request to create file TestFileUnmarkSuccess
    Then File TestFileUnmarkSuccess is created successfully
    When User request to mark file TestFileUnmarkSuccess as Success SuccessTagFileUnmark
    Then SuccessTagFileUnmark outcome should be created in TestFileUnmarkSuccess file
    When Unmark file TestFileUnmarkSuccess using outcome SuccessTagFileUnmark
    Then Content has been unmarked successfully using outcome SuccessTagFileUnmark

  Scenario: API - Unmark a blog post as Success [24024085]
    When User UserFooFileSuccess has been created
    And User UserFooFileSuccess logs in
    And Request to create a BlogPost BlogPostSuccessUnmark
    Then BlogPost BlogPostSuccessUnmark is created successfully
    When User request to mark blog post BlogPostSuccessUnmark as Success SuccessTagBlogPostUnmark
    Then SuccessTagBlogPostUnmark outcome should be created in BlogPostSuccessUnmark blog post
    When Unmark blog post BlogPostSuccessUnmark using outcome SuccessTagBlogPostUnmark
    Then Content has been unmarked successfully using outcome SuccessTagBlogPostUnmark

  Scenario: API - Mark a reply in a Discussion as Success [24002909]
    When User DiscussionReplyUser has been created
    And User DiscussionReplyUser is logged in
    And Logged in user requests to create a discussion TestDiscussionReply with tags TestTag
    And Add reply AddReplyData to the discussion TestDiscussionReply
    Then Reply has been added to the discussion successfully
    When Mark reply AddReplyData as success and get outcome OutcomeTagDiscussionReply
    Then Outcome OutcomeTagDiscussionReply is created for reply AddReplyData
