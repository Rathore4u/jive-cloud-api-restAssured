package com.jive.restapi.automation.cloud.stepdefs;

import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_NOT_FOUND;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.automation.cloud.data.ContentAuthorshipEnum;
import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.jive.restapi.automation.utilities.CommentUtils;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.DocumentConstants;
import com.jive.restapi.automation.utilities.FileConstants;
import com.jive.restapi.automation.utilities.FileUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.InboxUtils;
import com.jive.restapi.automation.utilities.ProjectUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.automation.utilities.v3.VersionUtil;
import com.jive.restapi.generated.person.models.Comment;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Content.VisibilityEnum;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.ContentVersionEntities;
import com.jive.restapi.generated.person.models.FileModel;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import com.xo.restapi.automation.context.UserContext;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;


public class FileStepDefs {

    private Response fileCreationResponse;
    private Response commentCreationResponse;
    private Response searchResponse;
    private File fileBinary;
    private static String fileName = "testing-fileBinary-" + RandomStringUtils.randomAlphanumeric(10);
    private static final String PATH_VIDEO = "src/test/resources/TestFiles/testVideo.mp4";
    private static final String PATH_CORRUPTED_VIDEO = "src/test/resources/TestFiles/testCorruptedVideo.mp4";
    private final static String DEFAULT_FILE_PATH = "src/test/resources/TestFiles/";
    private InboxEntry inbox;

    @Before("@CREATE-FILE")
    public void createFile() throws IOException {
        fileBinary = new File(fileName + ".txt");
        fileBinary.createNewFile();
    }

    @Before("@CREATE-VIDEO")
    public void createVideo() {
        fileBinary = new File(PATH_VIDEO);
    }

    @After("@DELETE-FILE")
    public void deleteFile() {
        fileBinary.delete();
    }

    @When("^Logged in user requests to create a File (.*) with tags (.*) And File Path (.*)$")
    public void userCreatesAFile(String fileTag, String tag, String filePath) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        FileModel fileData = ContentConstants.getDefaultFileData();
        fileData.setTags(tagList);
        fileData.setContent(new ContentBody().text(RandomStringUtils.randomAlphabetic(10)));
        fileData.setSubject("regular-" + RandomStringUtils.randomAlphanumeric(8));
        fileCreationResponse = FileUtils.createFileWithBinary(fileData, new java.io.File(filePath));
        FeatureRegistry.getCurrentFeature().setData(FileModel.class, fileTag, fileCreationResponse.as(FileModel.class));
        FeatureRegistry.getCurrentFeature().setData(Content.class, fileTag, fileCreationResponse.as(Content.class));
    }

    @When("^Request to create (file|video) (.*)$")
    public void userCreatesAFile(String type, String fileTag) {
        Content.TypeEnum typeFile;

        if ("file".equals(type)) {
            typeFile = Content.TypeEnum.FILE;
        } else {
            typeFile = Content.TypeEnum.VIDEO;
        }

        FileModel content = (FileModel) new FileModel().type(typeFile).subject(fileName).visibility(VisibilityEnum.ALL);
        content.setName(fileName);
        content.setContent((new ContentBody()).text(fileName));
        int times = 0;
        do {
            fileCreationResponse = FileUtils.createFileWithBinary(content, fileBinary);
            times++;
        } while (times <= NumberConstants.one && fileCreationResponse.statusCode() != SC_CREATED);

        fileCreationResponse.then().assertThat().statusCode(SC_CREATED);

        // Force an http GET on video page to force it indexed
        if (typeFile == Content.TypeEnum.VIDEO) {
            given()
                    .auth()
                    .basic(UserContext.getUser().getUsername(), UserContext.getUser().getPassword())
                    .log().all()
                    .get(fileCreationResponse.as(FileModel.class).getResources().get("html").getRef())
                    .then().assertThat().statusCode(SC_OK).log().all();
        }

        FeatureRegistry.getCurrentFeature().setData(FileModel.class, fileTag, fileCreationResponse.as(FileModel.class));
    }

    @Then("^File (.*) is created successfully$")
    public void fileIsCreatedSuccessfully(String fileTag) {
        fileCreationResponse.then().assertThat().statusCode(SC_CREATED);
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, fileTag);
        Response createFileResponse = FileUtils.getResponseOfFileById(fileData.getContentID());
        createFileResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Logged in user requests to create a File (.*) with File Path (.*) And Description (.*) under place$")
    public void userCreatesAFileWithDescriptionUnderPlace(String fileTag, String filePath, String description) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        FileModel fileData = ContentConstants.getDefaultFileData();
        fileData.setContent((new ContentBody()).text(description));
        fileData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        fileData.setVisibility(VisibilityEnum.PLACE);
        fileCreationResponse = FileUtils.createFileWithBinary(fileData, new java.io.File(filePath));
        FeatureRegistry.getCurrentFeature().setData(FileModel.class, fileTag, fileCreationResponse.as(FileModel.class));
    }

    @When("^Logged in user requests to create a File (.*) with File Path (.*) And Description (.*) in community$")
    public void userCreatesAFileWithDescription(String fileTag, String filePath, String description) {
        FileModel fileData = ContentConstants.getDefaultFileData();
        fileData.setContent((new ContentBody()).text(description));
        fileCreationResponse = FileUtils.createFileWithBinary(fileData, new java.io.File(filePath));
        FeatureRegistry.getCurrentFeature().setData(FileModel.class, fileTag, fileCreationResponse.as(FileModel.class));
    }

    @When("Logged in user Like file (.*)")
    public void likeFile(String fileTag) throws InterruptedException {
        Content fileData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, fileTag);
        Response like = ContentUtil.contentLikes(fileData.getContentID(), Options.logResponse(), Options.logRequest());
        int maxCount = TimeoutConstants.Times.TWO_HUNDRED_TIMES;
        while (like.statusCode() != SC_NO_CONTENT && maxCount > 0) {
            Thread.sleep(TimeoutConstants.M);
            like = ContentUtil.contentLikes(fileData.getContentID(), Options.logResponse(), Options.logRequest());
            maxCount--;
        }
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("Logged in user remove Like added on file (.*)")
    public void removeLikeOnFile(String fileTag) {
        Content fileData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, fileTag);
        Response like = ContentUtil.deleteContentLike(fileData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("User request all unread messages for File")
    public void userRequestUnreadMessages() {
        Response response = null;

        FilterBuilder filter = new FilterBuilder();
        filter.addCondition("unread", null);

        for (int counter = 0; counter < 15; counter++) {
            response = InboxUtils.readInboxAsResponse(filter);
            response.then().assertThat().statusCode(SC_OK);
            InboxEntry inboxEntry = response.as(InboxEntry.class);
            if (inboxEntry.getUnread() > 0) {
                break;
            }

            try {
                Thread.sleep(1500); // Sleep is require to give some time to message to be received before doing a retry
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        inbox = response.as(InboxEntry.class);
    }

    @Then("^Notification of File ([a-zA-Z]+) is present in inbox")
    public void verifyNotificationHasDiscussion(String fileTag) {
        Content fileData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, fileTag);
        assertNotNull(fileData.getSubject(), inbox.getUnread());
    }

    @When("^Add a comment (.*) to the file (.*)$")
    public void addCommentToFile(String commentTag, String fileTag) {
        FileModel file = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        commentCreationResponse = CommentUtils.createComment(
                ContentConstants.getDefaultCommentData(),
                file);

        FeatureRegistry.getCurrentFeature().setData(
                Comment.class,
                commentTag,
                commentCreationResponse.as(Comment.class));
    }

    @Then("^Comment is added to the file successfully$")
    public void verifyCommentIsAddedSuccessfully() {
        commentCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^User request to upload corrupted video (\\S+) in (community|group|project|sub-space|restricted group|private group|private unlisted group|blog)(?: )?(?:under)?(?: )?(.*)?$")
    public void userCreatesACorruptedVideoInPlace(String videoTag, String placeType, String underPlaceTag) {
        Place parentPlace = new Place();
        FileModel content = (FileModel) new FileModel().type(Content.TypeEnum.VIDEO).subject(fileName);
        content.setName(fileName);
        content.setContent((new ContentBody()).text(fileName));

        if (underPlaceTag != null) {
            parentPlace = FeatureRegistry.getCurrentFeature().getData(Place.class, underPlaceTag);
        }

        parentPlace = createPlace(placeType, parentPlace);

        if (parentPlace != null) {
            content.setParent(CloudCommonUtils.getParentPlaceUri(parentPlace));
            content.setVisibility(VisibilityEnum.PLACE);
        }

        fileCreationResponse = FileUtils.createFileWithBinary(content, fileBinary);
        if (fileCreationResponse.statusCode() == SC_CREATED) {
            FeatureRegistry.getCurrentFeature().setData(FileModel.class, videoTag, fileCreationResponse.as(FileModel.class));
        } else {
            FeatureRegistry.getCurrentFeature().setData(FileModel.class, videoTag, null);
        }
    }

    @When("^User request to upload corrupted video (\\S+) with publish location as specific people (\\S+)$")
    public void userCreatesACorruptedVideoSpecificPeople(String videoTag, String userTag) {
        Person user2 = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        FileModel content = (FileModel) new FileModel().type(Content.TypeEnum.VIDEO).subject(fileName);
        content.setName(fileName);
        content.setContent((new ContentBody()).text(fileName));
        content.addUsersItem(user2);
        content.setVisibility(Content.VisibilityEnum.PEOPLE);
        fileCreationResponse = FileUtils.createFileWithBinary(content, fileBinary);
        if (fileCreationResponse.statusCode() == SC_CREATED) {
            FeatureRegistry.getCurrentFeature().setData(FileModel.class, videoTag, fileCreationResponse.as(FileModel.class));
        } else {
            FeatureRegistry.getCurrentFeature().setData(FileModel.class, videoTag, null);
        }
    }

    @When("^User request to upload corrupted video (\\S+) in RTE of a(?:n)? (document|discussion|idea|question|blog)$")
    public void userCreatesACorruptedVideoInContent(String videoTag, String contentType) {
        Response contentResponse;
        FileModel videoContent = (FileModel) new FileModel().type(Content.TypeEnum.VIDEO).subject(fileName);
        videoContent.setName(fileName);
        videoContent.setContent((new ContentBody()).text(fileName));
        Content contentRte;

        switch (contentType.toLowerCase()) {
            case EntityConstants.DOCUMENT:
                contentRte = ContentConstants.getDefaultDocumentData();
                break;
            case EntityConstants.DISCUSSION:
                contentRte = ContentConstants.getDefaultDiscussionData();
                break;
            case EntityConstants.QUESTION:
                contentRte = ContentConstants.getDefaultContentDataForQuestion();
                break;
            case EntityConstants.BLOG:
                contentRte = ContentConstants.getDefaultContentDataForQuestion();
                break;
            default:
                contentRte = ContentConstants.getDefaultIdeaData();
                break;
        }
        contentRte.setParent(CloudCommonUtils.getParentPlaceUri(GroupUtils.createOpenGroup().as(Place.class)));
        contentRte.setVisibility(VisibilityEnum.PLACE);
        contentResponse = ContentUtil.createContent(contentRte, fileBinary);
        contentRte.setParent(contentResponse.as(Place.class).getParent());
        fileCreationResponse = ContentUtil.createContent(videoContent, fileBinary);

        if (fileCreationResponse.statusCode() == SC_CREATED) {
            FeatureRegistry.getCurrentFeature()
                    .setData(FileModel.class, videoTag, fileCreationResponse.as(FileModel.class));
        } else {
            FeatureRegistry.getCurrentFeature().setData(FileModel.class, videoTag, null);
        }
    }

    @Then("^(\\S+) can't be upload - an error occurred while processing video$")
    public void verifyVideoCantBeUpload(String videoTag) {
        FileModel videoData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, videoTag);
        fileCreationResponse.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
        assertNull(ValidationConstants.VIDEO_ERROR, videoData);
    }

    @Then("^Create corrupted file on file system$")
    public void createCorruptedVideo() {
        fileBinary = new File(PATH_CORRUPTED_VIDEO);
    }

    private static Place createPlace(String placeType, Place parentPlace) {
        Place place;
        switch (placeType.toLowerCase()) {
            case EntityConstants.GROUP:
                place = GroupUtils.createOpenGroup().as(Place.class);
                break;
            case EntityConstants.PROJECT:
                place = ProjectUtils.createProject(parentPlace).as(Place.class);
                break;
            default:
                place = parentPlace;
                break;
        }
        return place;
    }

    @When("^Create a file (.*) with attach (.*) and with \"Specific People\" publish location:$")
    public void createFileWithSpecificPeople(String fileTag, String attachTag, List<String> usersTags) {
        val users = usersTags
                .stream()
                .map(userTag -> FeatureRegistry.getCurrentFeature().getData(Person.class, userTag))
                .collect(Collectors.toList());

        fileCreationResponse = ContentUtil.createContent(
                FileConstants.getDefaultFileDataWithSpecificPeople(users),
                new File(DEFAULT_FILE_PATH + attachTag));
        fileCreationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                FileModel.class,
                fileTag,
                fileCreationResponse.as(FileModel.class));
    }

    @When("^Create a file (.*) with attach (.*) and with \"Specific People\" publish location and authors:$")
    public void createFileWithSpecificPeopleAndAuthors(String fileTag, String attachTag, List<String> usersTags) {
        val users = usersTags
                .stream()
                .map(userTag -> FeatureRegistry.getCurrentFeature().getData(Person.class, userTag))
                .collect(Collectors.toList());

        fileCreationResponse = ContentUtil.createContent(
                FileConstants.getDefaultFileDataWithSpecificPeopleAndAuthors(users), null,
                Options.attachResource("file", attachTag, "TestFiles/" + attachTag, "application/pdf"));
        fileCreationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                FileModel.class,
                fileTag,
                fileCreationResponse.as(FileModel.class));
    }

    @When("^Add an author comment (.*) to the file (.*)$")
    public void addAuthorCommentToFile(String commentTag, String fileTag) {
        FileModel file = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        commentCreationResponse = ContentUtil.createComment(
                file.getContentID(),
                ContentConstants.getDefaultCommentData(),
                Options.custom(op -> op.authorQuery("true")));

        FeatureRegistry.getCurrentFeature().setData(
                Comment.class,
                commentTag,
                commentCreationResponse.as(Comment.class));
    }

    @When("Search file (.*) by subject from (.*) using (.*) attempts")
    public void searchFileBySubjectUsingAttempts(String fileTag, String origin, String attemptsQty) throws InterruptedException {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        for (int counter = 0; counter < Integer.parseInt(attemptsQty); counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, fileData.getSubject())
                            .buildList(), origin);
            if (CollectionUtils.isNotEmpty(searchResponse.as(ContentEntities.class).getList())) {
                break;
            }
            // Static wait is used to give time between this search query and the next in case content is not indexed
            Thread.sleep(ContentConsts.TWO_THOUSAND);
        }
    }

    @Then("^File is not found$")
    public void fileNotFound() {
        searchResponse.then().assertThat().statusCode(SC_OK);
        assertTrue(CollectionUtils.isEmpty(searchResponse.as(ContentEntities.class).getList()));
    }

    @Then("^Notification of file ([a-zA-Z]+) is present in inbox (.*)$")
    public void verifyNotificationHasDocument(String fileTag, String inboxTag) {
        InboxEntry inbox = FeatureRegistry.getCurrentFeature().getData(InboxEntry.class, inboxTag);
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(FileModel.class, fileTag);
        assertNotNull(fileData.getSubject(), inbox.getUnread());
    }

    @When("^User request to upload File (\\S+) in (community|group|project|sub-space|restricted group|private group|private unlisted group|blog|space)(?: )?(?:under)?(?: )?(.*)? And File Path (.*)$")
    public void userCreatesFileInPlace(String fileTag, String placeType, String underPlaceTag, String filePath) {
        Place parentPlace = new Place();
        FileModel content = (FileModel) new FileModel().type(Content.TypeEnum.FILE).subject(fileName);
        content.setName(fileName);
        content.setContent((new ContentBody()).text(fileName));
        if (underPlaceTag != null) {
            parentPlace = FeatureRegistry.getCurrentFeature().getData(Place.class, underPlaceTag);
        }
        parentPlace = createPlace(placeType, parentPlace);
        if (parentPlace != null) {
            content.setParent(CloudCommonUtils.getParentPlaceUri(parentPlace));
            content.setVisibility(VisibilityEnum.PLACE);
        }
        fileCreationResponse = FileUtils.createFileWithBinary(content, new java.io.File(filePath));
        FeatureRegistry.getCurrentFeature().setData(FileModel.class, fileTag, fileCreationResponse.as(FileModel.class));
    }

    @When("^Create a file (.*) with attach (.*) and with \"Allow specific people to edit this document\" option:$")
    public void createFileWithAllowSpecificPeopleToEdit(String fileTag, String attachTag, List<String> usersTags) {
        String defaultDescription = "regularDescription-" + RandomStringUtils.randomAlphanumeric(NumberConstants.fifteen);
        val users = usersTags
                .stream()
                .map(userTag -> FeatureRegistry.getCurrentFeature().getData(Person.class, userTag))
                .collect(Collectors.toList());

        Content fileData = ContentConstants
                .getDefaultFileData()
                .authorship(ContentAuthorshipEnum.LIMITED.getName())
                .authors(users)
                .content(new ContentBody().text((defaultDescription)));

        fileCreationResponse = ContentUtil.createContent(
                fileData,
                new File(DEFAULT_FILE_PATH + attachTag));
        fileCreationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                FileModel.class,
                fileTag,
                fileCreationResponse.as(FileModel.class));
    }

    @When("^Update file (.*) with new subject (.*)$")
    public void updateFileWithSubject(String fileTag, String subjectTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        String newTitle = fileData.getSubject() + DocumentConstants.UPDATED_SUBJECT_POSTFIX;
        fileData.subject(newTitle);

        fileCreationResponse = ContentUtil.updateContent(
                fileData.getContentID(),
                fileData);
        fileCreationResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                String.class,
                subjectTag,
                newTitle);
        FeatureRegistry.getCurrentFeature().setData(
                FileModel.class,
                fileTag,
                fileCreationResponse.as(FileModel.class));
    }

    @Then("^File (.*) is updated with subject (.*) successfully$")
    public void fileIsUpdatedWithSubjectSuccessfully(String fileTag, String subjectTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);
        String updatedSubject = FeatureRegistry.getCurrentFeature().getData(
                String.class,
                subjectTag);

        assertTrue(String.format(ValidationConstants.FILE_SUBJECT_IS_UPDATED_VALIDATION, updatedSubject),
                fileData.getSubject().contains(updatedSubject));
    }


    @When("^Update file (.*) with subject (.*) by user with no edit rights$")
    public void updateFileWithSubjectByUserWithNoRights(String fileTag, String subjectTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        String oldSubject = fileData.getSubject();
        String newSubject = oldSubject + subjectTag;
        fileData.subject(newSubject);

        fileCreationResponse = ContentUtil.updateContent(
                fileData.getContentID(),
                fileData);
        fileCreationResponse.then().assertThat().statusCode(SC_FORBIDDEN);

        fileData.subject(oldSubject);
        FeatureRegistry.getCurrentFeature().setData(
                String.class,
                subjectTag,
                newSubject);
    }

    @Then("^File (.*) isn't updated with subject (.*)$")
    public void fileIsntUpdatedWithSubject(String fileTag, String subjectTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);
        String updatedSubject = FeatureRegistry.getCurrentFeature().getData(
                String.class,
                subjectTag);

        assertFalse(String.format(ValidationConstants.FILE_SUBJECT_ISNT_UPDATED_VALIDATION, updatedSubject),
                fileData.getSubject().contains(subjectTag));
    }

    @When("^Create a file (.*) with attach (.*) and with (.*) visibility$")
    public void createFileWithSpecificVisibility(String fileTag, String attachTag, String visibilityTag) {
        String defaultDescription = "regularDescription-" + RandomStringUtils.randomAlphanumeric(NumberConstants.fifteen);

        Content.VisibilityEnum visibilityType = VisibilityEnum.ALL;
        if (visibilityTag.toLowerCase().equals(VisibilityEnum.HIDDEN.getValue())) {
            visibilityType = VisibilityEnum.HIDDEN;
        }
        if (visibilityTag.toLowerCase().equals(VisibilityEnum.PEOPLE.getValue())) {
            visibilityType = VisibilityEnum.PEOPLE;
        }
        Content fileData = ContentConstants
                .getDefaultFileData()
                .visibility(visibilityType)
                .content(new ContentBody().text((defaultDescription)));

        fileCreationResponse = ContentUtil.createContent(
                fileData,
                new File(DEFAULT_FILE_PATH + attachTag));
        fileCreationResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                FileModel.class,
                fileTag,
                fileCreationResponse.as(FileModel.class));
    }

    @When("^Read file (.*)$")
    public void getFile(String fileTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        Response getFileResponse = ContentUtil.getContent(fileData.getContentID());
        getFileResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                FileModel.class,
                fileTag,
                getFileResponse.as(FileModel.class));
    }

    @Then("^File (.*) version is equal to (.*)$")
    public void verifyFileVersionNumber(String fileTag, String versionTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        assertThat(String.format(ValidationConstants.FILE_VERSION_VALIDATION, versionTag),
                fileData.getVersion(),
                equalTo(Integer.parseInt(versionTag)));
    }

    @When("^Get file (.*) versions to list (.*)$")
    public void getFileVersionsToList(String fileTag, String listTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        Response getVersionsResponse = VersionUtil.getVersions(fileData.getContentID());
        getVersionsResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                ContentVersionEntities.class,
                listTag,
                getVersionsResponse.as(ContentVersionEntities.class));
    }

    @Then("^File versions list (.*) contains (.*) versions$")
    public void verifyVersionsListSize(String listTag, String sizeTag) {
        ContentVersionEntities listData = FeatureRegistry.getCurrentFeature().getData(
                ContentVersionEntities.class,
                listTag);

        assertThat(String.format(ValidationConstants.FILE_VERSIONS_LIST_SIZE_VALIDATION, sizeTag),
                listData.getList(),
                hasSize(Integer.parseInt(sizeTag)));
    }

    @When("^Make minor update of file (.*) with new subject (.*)$")
    public void makeMinorUpdateOfFileWithSubject(String fileTag, String subjectTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        String newTitle = fileData.getSubject() + DocumentConstants.UPDATED_SUBJECT_POSTFIX;
        fileData.subject(newTitle);

        fileCreationResponse = ContentUtil.updateContent(
                fileData.getContentID(),
                fileData,
                Options.custom(op -> op.minorQuery("true")));
        fileCreationResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                String.class,
                subjectTag,
                newTitle);
        FeatureRegistry.getCurrentFeature().setData(
                FileModel.class,
                fileTag,
                fileCreationResponse.as(FileModel.class));
    }

    @When("^Delete file (.*)$")
    public void deleteFile(String fileTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        Response deleteFileResponse = ContentUtil.deleteContent(fileData.getContentID());
        deleteFileResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("^File (.*) is deleted successfully$")
    public void fileIsDeletedSuccessfully(String fileTag) {
        FileModel fileData = FeatureRegistry.getCurrentFeature().getData(
                FileModel.class,
                fileTag);

        Response getFileResponse = ContentUtil.getContent(fileData.getContentID());

        assertEquals(String.format(ValidationConstants.CONTENT_IS_DELETED_VALIDATION, fileData.getSubject()),
                getFileResponse.getStatusCode(), SC_NOT_FOUND);
    }
}
