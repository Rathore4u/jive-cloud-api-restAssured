package com.jive.restapi.automation.cloud.data;

import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationConstants {

    public static String searchResults = "Search content should be displayed";
    public static String officialOutcomeCreation = "Official outcome should be created";
    public static String contentCreation = "Content should be created successfully";
    public static String officialOutcomeNonDisplay = "Official outcome should be removed";
    public static String contentUpdatedWithTitle = "Content should be updated with title";
    public static String successOutcomeCreation = "Success outcome should be created";
    public static String outcomeNonDisplay = "Outcome should be removed in the content";
    public static String taskUpdatedWithTitle = "Task should be updated with title";
    public static String taskDeletedText = "Task should be deleted successfully";
    public static final String ERROR_MESSAGE = " couldn't be ";
    public static final String INBOX_UNREAD_MESSAGES = "Inbox should return unread messages";
    public static final String INBOX_READ_MESSAGES = "Inbox should return read messages";
    public final String COMMENT_CONTAINS_OUTCOME_VALIDATION = "Comment should contain outcome '%s'";
    public static final String VIDEO_ERROR = "An error occurred while processing video";
    public final String DOCUMENT_NOT_FOUND_VALIDATION = "Document '%s' shouldn't be found";
    public static final String EVENT_STARTDATE_NOT_EQUAL = "Event Start date is not equal";
    public static final String EVENT_SUBJECT = "Event Subjects are equal";
    public static final String EVENT_LOCATION = "Event Location are not equal";
    public final String DOCUMENT_DOESNT_CONTAIN_USERS_VALIDATION = "Document '%s' shouldn't contain users";
    public static final String ERROR_SEARCHING = "Error searching ";
    public static final String CREATED = "created";
    public static final String UPDATED = "updated";
    public static final String DELETED = "deleted";
    public static final String SHARE_CREATION_FAILED = "Share creation failed!";
    public static final String OUTCOME_CREATION_FAILED = "Outcome creation failed!";
    public static final String PDF_NOT_FOUND = "Pdf not found!";
    public static final String CONTENT_IMAGE = "content creation response should have  image name '%s'";
    public static final String ATTACHMENT_VALIDATION_MESSAGE = "Attachment is uploaded with name '%s'";
    public static final String DISCUSSION_CONTAIN_REPLY_VALIDATION = "Discussion '$s' should contain reply '%s'";
    public static final String FOLLOW_USER_FAILED = "Follow user failed";
    public static final String CONTENT_COUNT_MISMATCH = "Expected count of contents is not present.";
    public static final String STREAM_NOT_CONTAIN_OBJECT = "Element is not present in Stream.";
    public final String REPLY_DOESNT_CONTAIN_OUTCOMES_VALIDATION = "Reply shouldn't contain outcomes";
    public final String REPLY_CONTAINS_OUTCOME_VALIDATION = "Reply should contain outcome '%s'";
    public static final String SUB_SPACE_NAME = "sub-space name should be '%s'";
    public static final String SUB_SPACE_PARENT = "sub-space parent name should be '%s'";
    public final String STATUS_UPDATE_IS_CREATED_VALIDATION = "Status update '%s' should be created";
    public final String STATUS_UPDATE_IS_DELETED_VALIDATION = "Status update '%s' should be deleted";
    public final String USER_ACTIVITIES_CONTAIN_UPDATE_VALIDATION = "User activities should contain update '%s'";
    public final String INBOX_ENTRIES_CONTAIN_UPDATE_VALIDATION = "Inbox entries should contain update '%s'";
    public final String UPDATE_ONE_ELDER_UPDATE_TWO_VALIDATION = "Status update '%s' should be elder than status update '%s'";
    public final String FILE_SUBJECT_IS_UPDATED_VALIDATION = "File subject should be updated to '%s' value";
    public final String FILE_SUBJECT_ISNT_UPDATED_VALIDATION = "File subject shouldn't be updated to '%s' value";
    public final String FILE_VERSION_VALIDATION = "File version should be equal to '%s'";
    public final String FILE_VERSIONS_LIST_SIZE_VALIDATION = "File versions list size should be equal to '%s'";
    public final String ACTIVITIES_CONTAIN_CONTENT_VALIDATION = "Activities should contain content '%s'";
    public final String ACTIVITIES_DONT_CONTAIN_CONTENT_VALIDATION = "Activities shouldn't contain content '%s'";
    public final String CONTENT_IS_DELETED_VALIDATION = "Content '%s' should be deleted";
    public static final String PLACE_PARENT = "Place has Parent '%s'";
    public static final String GROUP_DISPLAY_NAME = "Group displayName is not null";
    public static final String FOLLOW_USER = "Followed user flag is set to True";
    public static final String NOT_FOLLOW_USER = "Followed user flag is set to False";

    public static String getErrorMessage(String entityName, String option) {
        return entityName.toUpperCase() + ERROR_MESSAGE + option;
    }

    public static String booleanFalseMessage(String keyName) {
        return "Expected " + keyName + ": Not to be FALSE";
    }
}
