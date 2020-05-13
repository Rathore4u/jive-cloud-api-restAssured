package com.jive.restapi.automation.cloud.stepdefs;

import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertHttpResponseEquals;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.google.common.html.HtmlEscapers;
import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.BlogPostUtils;
import com.jive.restapi.automation.utilities.CommentUtils;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.ContentUtils;
import com.jive.restapi.automation.utilities.DocumentUtils;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.InboxUtils;
import com.jive.restapi.automation.utilities.PeopleUtils;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.PostUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.generated.person.models.Attachment;
import com.jive.restapi.generated.person.models.Comment;
import com.jive.restapi.generated.person.models.CommentEntities;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.DocumentEntities;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Image;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.Outcome;
import com.jive.restapi.generated.person.models.OutcomeBase;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.jive.restapi.generated.person.models.PlaceEntities;
import com.jive.restapi.generated.person.models.Post;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import com.xo.restapi.automation.context.UserData;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;

public class BlogPostStepDefs {

    private Response creationResponse;
    private Response creationBlogResponse;
    private Response BlogPostUnderPlaceCreationResponse;
    private Response searchResponse;
    private Response commentCreationResponse;
    private String umlauts = "f�r alle & jeden zum testen � � �";
    private Response contentOutcomeResponse;
    private InboxEntry inbox;
    private Response like;
    private Response pdfURlResp;

    @Getter
    private UserData userData = new UserData();

    @When("^Request to create a BlogPost (\\S+)$")
    public void adminRequestToCreateBlogPost(String blogPostTag) {
        creationResponse = BlogPostUtils.createBlogPost(ContentConstants.getDefaultPostData());
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @Then("^BlogPost (.*) is created successfully$")
    public void adminVerifyBlogPostCreated(String blogPostTag) {
        Content contentData = FeatureRegistry.getCurrentFeature().getData(Post.class, blogPostTag);
        creationResponse = BlogPostUtils.getResponseOfPostById(contentData.getContentID());
        creationResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Request to create a BlogPost (\\S+) under (?:Project|Group|Space) (.*)$")
    public void adminRequestToCreateBlogPostUnderPlace(String blogTag, String groupTag) {
        creationResponse = GroupUtils.createGroup("open");
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        Group group = creationResponse.as(Group.class);
        FeatureRegistry.getCurrentFeature().setData(Group.class, groupTag, group);
        Response placeResponse = PlacesUtils.getPlaces(group.getPlaceID());
        PlaceEntities placeEntities = placeResponse.as(PlaceEntities.class);
        Place groupDefaultBlog = placeEntities.getList().get(0);
        Content blogPost = ContentConstants.getDefaultPostData();
        blogPost.setParent(CloudCommonUtils.getParentPlaceUri(groupDefaultBlog));
        blogPost.setVisibility(Content.VisibilityEnum.PLACE);
        Response blogResponse = ContentUtils.createContent(blogPost);

        FeatureRegistry.getCurrentFeature().setData(Post.class, blogTag, blogResponse.as(Post.class));
    }

    @Then("BlogPost is created successfully under Place")
    public void adminVerifyBlogPostCreatedUnderPlace() {
        creationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Logged in user requests to create post (.*) with tags (.*)$")
    public void userCreatesAPostWithTags(String postTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Post postData = ContentConstants.getDefaultPostData();
        postData.setSubject(postData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        postData.setTags(tagList);
        creationResponse = BlogPostUtils.createPostToReturnResponse(postData);
        FeatureRegistry.getCurrentFeature().setData(Post.class, postTag, creationResponse.as(Post.class));
    }

    @Then("^Post (.*) is created successfully$")
    public void postIsCreatedSuccessfully(String postTag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        creationResponse = BlogPostUtils.getResponseOfPostById(postData.getContentID());
        creationResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^Logged in user requests to update post (.*) with tags (.*)$")
    public void userUpdatesADocument(String postTag, String tag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        postData.setTags(tagList);
        creationResponse = BlogPostUtils.updatePostToReturnResponse(postData);
        FeatureRegistry.getCurrentFeature().setData(Post.class, postTag, creationResponse.as(Post.class));
    }

    @Then("^Post (.*) is updated with tags (.*) successfully$")
    public void postIsUpdatedSuccessfully(String postTag, String tag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        Assert.assertTrue(postData.getTags().contains(tag));
    }

    @When("User request to search post (.*) by subject from (.*)")
    public void searchByPostSubject(String postTag, String origin) throws InterruptedException {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        for (int counter = 0; counter < 15; counter++) {
            searchResponse = SearchUtils.searchContent(
                    FilterBuilder
                            .builder()
                            .addCondition("search", postData.getSubject())
                            .buildList(), origin);
            if (searchResponse.as(DocumentEntities.class).getList().size() != 0) {
                break;
            }
            Thread.sleep(2000);
        }
    }

    @Then("^Post (.*) is searched successfully$")
    public void postIsSearched(String postTag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        Assert.assertTrue(searchResponse.as(DocumentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(postData.getSubject())));
    }

    @When("User request to delete post (.*)")
    public void deletePost(String postTag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        searchResponse = PostUtils.deletePost(postData.getContentID());
    }

    @Then("^Post (.*) is deleted successfully$")
    public void postIsDeleted(String postTag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        searchResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^user creates a umlauts BlogPost (.*) with tags (.*)$")
    public void userCreatesUmlautsBlogPost(String post, String tag) {
        String subject = umlauts + RandomStringUtils.randomAlphanumeric(10);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Post postData = ContentConstants.getDefaultPostData();
        postData.setSubject(postData.getSubject() + RandomStringUtils.randomAlphanumeric(10));
        postData.content(new ContentBody().text(HtmlEscapers.htmlEscaper().escape(umlauts))).subject(umlauts);
        postData.setSubject(subject);
        postData.setTags(tagList);
        creationResponse = BlogPostUtils.createPostToReturnResponse(postData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Post.class, post, creationResponse.as(Post.class));
    }

    @When("^user updates a BlogPost (.*) with Umlauts$")
    public void userUpdatesBlogPostWithUmlauts(String post) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, post);
        postData.content(new ContentBody().text(HtmlEscapers.htmlEscaper().escape(umlauts))).subject(umlauts);
        creationResponse = BlogPostUtils.updatePostToReturnResponse(postData);
        FeatureRegistry.getCurrentFeature().setData(Post.class, post, creationResponse.as(Post.class));
    }

    @Then("^BlogPost (.*) is updated with umlauts successfully$")
    public void blogPostIsUpdatedWithUmlautsSuccessfully(String post) {
        String umlautsedit = "jeden zum testen";
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, post);
        Assert.assertTrue(postData.getSubject().contains(umlautsedit));
        Assert.assertTrue(postData.getContent().getText().contains(umlautsedit));
    }

    @When("^Add a comment (.*) to the blog post (.*)$")
    public void addCommentToBlogPost(String commentTag, String blogpostTag) {
        Post blogpost = FeatureRegistry.getCurrentFeature().getData(Post.class, blogpostTag);
        commentCreationResponse = CommentUtils.createComment(ContentConstants.getDefaultCommentData(), blogpost);
        FeatureRegistry.getCurrentFeature()
                .setData(Comment.class, commentTag, commentCreationResponse.as(Comment.class));
    }

    @Then("^Comment has been added to the blog post successfully$")
    public void commentAddedSuccessfully() {
        commentCreationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Logged in user requests to create BlogPost (.*) with description (.*) under place$")
    public void userCreatesABlogPostWithDescriptionUnderPlace(String blogPostTag, String description) {
        creationResponse = GroupUtils.createGroup("open");
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        Group group = creationResponse.as(Group.class);
        Response placeResponse = PlacesUtils.getPlaces(group.getPlaceID());
        PlaceEntities placeEntities = placeResponse.as(PlaceEntities.class);
        Place blog = placeEntities.getList().get(0);
        Post postData = ContentConstants.getDefaultPostData();
        postData.content((new ContentBody()).text(description));
        postData.setParent(CloudCommonUtils.getParentPlaceUri(blog));
        postData.setVisibility(Content.VisibilityEnum.PLACE);
        creationResponse = BlogPostUtils.createPostToReturnResponse(postData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^Logged in user requests to create BlogPost (.*) with description (.*) in community$")
    public void userCreatesABlogPostWithDescription(String blogPostTag, String description) {
        Post postData = ContentConstants.getDefaultPostData();
        postData.content((new ContentBody()).text(description));
        creationResponse = BlogPostUtils.createPostToReturnResponse(postData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
        FeatureRegistry.getCurrentFeature().setData(Content.class, blogPostTag, creationResponse.as(Content.class));
    }

    @When("^User (.*) mark content blog post (.*) as Mark for Action (.*)$")
    public void userMarkForAction(String userName, String blogPostTag, String outcomeName) {
        Content blogPost = FeatureRegistry.getCurrentFeature().getData(Post.class, blogPostTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.PENDING.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(blogPost.getContentID(), outcomeData);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outcomeName, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("Verify content blog post (.*) is Mark for action (.*)$")
    public void verifyUserMarkForAction(String blogPostTag, String outcomeName) {
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        Assert.assertEquals(contentOutcomeResponse.as(Outcome.class).getOutcomeType().getName(),
                StandardOutcomeTypes.PENDING.getOutcomeType().getName());
    }

    @When("User request to mark blogpost (.*) as Success (.*)")
    public void markContentAsSuccessOutcome(String contentTag, String outdatedTag) {
        Post blogPostData = FeatureRegistry.getCurrentFeature().getData(Post.class, contentTag);
        Outcome outcomeData = (Outcome) new Outcome()
                .outcomeType(StandardOutcomeTypes.SUCCESS.getOutcomeType());
        contentOutcomeResponse = ContentUtil.createOutcome(blogPostData.getContentID(), outcomeData);
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature()
                .setData(Outcome.class, outdatedTag, contentOutcomeResponse.as(Outcome.class));
    }

    @Then("Verify content blog post (.*) is marked as success outcome (.*)$")
    public void verifyUserMarkForSuccess(String blogPostTag, String outcomeName) {
        contentOutcomeResponse.then().assertThat().statusCode(SC_CREATED);
        assertEquals(ValidationConstants.successOutcomeCreation,
                contentOutcomeResponse.as(Outcome.class).getOutcomeType().getName(),
                StandardOutcomeTypes.SUCCESS.getOutcomeType().getName());
    }

    @When("Logged in user requests to update blogpost (.*) with title (.*)")
    public void userUpdatesABlogPostTitle(String blogTag, String titleTag) {
        String title = RandomStringUtils.randomAlphanumeric(10);
        FeatureRegistry.getCurrentFeature().setData(String.class, titleTag, title);
        Post blogPostData = FeatureRegistry.getCurrentFeature().getData(Post.class, blogTag);
        blogPostData.setSubject(title);
        contentOutcomeResponse = DocumentUtils.updateDocumentToReturnResponse(blogPostData);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogTag, contentOutcomeResponse.as(Post.class));
    }

    @Then("BlogPost (.*) is updated with title (.*) successfully")
    public void blogIsUpdatedWithTitleSuccessfully(String blogTag, String tag) throws InterruptedException {
        Post blogData = FeatureRegistry.getCurrentFeature().getData(Post.class, blogTag);
        String updatedTitle = FeatureRegistry.getCurrentFeature().getData(String.class, tag);
        assertTrue(ValidationConstants.contentUpdatedWithTitle, blogData.getSubject().contains(updatedTitle));
        // Adding static wait for 2 seconds as next action is to read the above updated data
        Thread.sleep(TimeoutConstants.XS);
    }

    @Then("Outcome (.*) should removed in blog (.*)")
    public void outcomeShouldBeRemovedInBlog(String officialTag, String documentTag) {
        Outcome officialData = FeatureRegistry.getCurrentFeature().getData(Outcome.class, officialTag);
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, documentTag);
        assertFalse(ValidationConstants.outcomeNonDisplay,
                postData.getOutcomeTypes().contains(officialData.getOutcomeType()));
    }

    @When("Logged in user Like blog (.*)")
    public void likeDiscussion(String blogTag) {
        Post blogData = FeatureRegistry.getCurrentFeature().getData(Post.class, blogTag);
        like = ContentUtil.contentLikes(blogData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("Logged in user remove Like added on blog (.*)")
    public void removeLikeOnDiscussion(String blogTag) {
        Post blogData = FeatureRegistry.getCurrentFeature().getData(Post.class, blogTag);
        like = ContentUtil.deleteContentLike(blogData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @Then("Like added on blog Successfully")
    public void likeAddedSuccessful() {
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("User request all unread messages for blog")
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
                Thread.sleep(
                        1500);  // Sleep is require to give some time to message to be received before doing a retry
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        inbox = response.as(InboxEntry.class);
    }

    @Then("^Notification of blog ([a-zA-Z]+) is present in inbox")
    public void verifyNotificationHasDiscussion(String postTag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        assertNotNull(postData.getSubject(), inbox.getUnread());
    }

    @Given("^User requests to create BlogPost (.*) under place (.*)$")
    public void createBlogPostUnderProject(String blogPostTag, String placeTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Post postData = ContentConstants.getDefaultPostData();
        Response placeResponse = PlacesUtils.getPlaces(place.getPlaceID());
        PlaceEntities placeEntities = placeResponse.as(PlaceEntities.class);
        Place blog = placeEntities.getList().get(0);
        postData.setParent(CloudCommonUtils.getParentPlaceUri(blog));
        postData.setVisibility(Content.VisibilityEnum.PLACE);
        creationResponse = BlogPostUtils.createPostToReturnResponse(postData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^Logged in user requests to create BlogPost (.*) with user in body in community$")
    public void userCreatesABlogPostWithMultipleUsersInBody(String blogPostTag) {
        Person newPerson = PeopleUtils.newPerson();
        Response resp = PeopleUtils.createPerson(newPerson);
        resp.then().assertThat().statusCode(HttpStatus.SC_CREATED);
        newPerson.setId(resp.as(Person.class).getId());
        userData.addUser(newPerson.getJive().getUsername(), newPerson.getJive().getPassword());
        Post postData = ContentConstants.getDefaultPostData();
        postData.content((new ContentBody()).text("@" + resp.as(Person.class).getDisplayName()));
        String title = RandomStringUtils.randomAlphanumeric(10);
        postData.setSubject(title);
        creationResponse = BlogPostUtils.createPostToReturnResponse(postData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^Logged in user requests to create BlogPost (.*) with user in body under Place$")
    public void userCreatesABlogPostWithMultipleUsersInBodyUnderPlace(String blogPostTag) {
        Person newPerson = PeopleUtils.newPerson();
        Response resp = PeopleUtils.createPerson(newPerson);
        resp.then().assertThat().statusCode(HttpStatus.SC_CREATED);
        newPerson.setId(resp.as(Person.class).getId());
        userData.addUser(newPerson.getJive().getUsername(), newPerson.getJive().getPassword());
        Post postData = ContentConstants.getDefaultPostData();
        postData.content((new ContentBody()).text("@" + resp.as(Person.class).getDisplayName()));
        String title = RandomStringUtils.randomAlphanumeric(10);
        postData.setSubject(title);
        creationResponse = GroupUtils.createGroup("open");
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        Group group = creationResponse.as(Group.class);
        Response placeResponse = PlacesUtils.getPlaces(group.getPlaceID());
        PlaceEntities placeEntities = placeResponse.as(PlaceEntities.class);
        Place blog = placeEntities.getList().get(0);
        postData.setParent(CloudCommonUtils.getParentPlaceUri(blog));
        postData.setVisibility(Content.VisibilityEnum.PLACE);
        creationResponse = BlogPostUtils.createPostToReturnResponse(postData);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^Logged in user requests to create BlogPost (.*) with user in body And Attachment$")
    public void userCreatesABlogPostWithMultipleUsersInBodyAndAttachment(String blogPostTag) {
        Person newPerson = PeopleUtils.newPerson();
        Response resp = PeopleUtils.createPerson(newPerson);
        resp.then().assertThat().statusCode(HttpStatus.SC_CREATED);
        newPerson.setId(resp.as(Person.class).getId());
        userData.addUser(newPerson.getJive().getUsername(), newPerson.getJive().getPassword());
        Content content = ContentConstants.getDefaultContentDataOfType(Content.TypeEnum.POST);
        content.content((new ContentBody()).text("@" + resp.as(Person.class).getDisplayName()));
        Attachment attachment = new Attachment();
        content.addAttachmentsItem(attachment);
        String title = RandomStringUtils.randomAlphanumeric(10);
        content.setSubject(title);
        creationResponse = BlogPostUtils.createPostToReturnResponse(content);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^Schedule Future BlogPost (.*)$")
    public void scheduleBlogPost(String blogPostTag) {
        Post postData = ContentConstants.getDefaultPostData();
        postData.setPublishDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(5));
        creationResponse = BlogPostUtils.createBlogPost(postData);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^Restrict Comment On BlogPost (.*)$")
    public void restrictCommentBlogPost(String blogPostTag) {
        Post postData = ContentConstants.getDefaultPostData();
        postData.restrictComments(true);
        postData.setSubject("regular-" + RandomStringUtils.randomAlphanumeric(8));
        creationResponse = BlogPostUtils.createBlogPost(postData);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @Then("^BlogPost is created successfully with Restrict$")
    public void adminVerifyBlogPostCreatedRestrict() {
        creationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @Then("^Comment has been Restricted to the blog post successfully$")
    public void restrictCommentAddedSuccessfully() {
        commentCreationResponse.then().assertThat().statusCode(SC_CONFLICT);
    }

    @When("Logged in user Like Schedule blog (.*)")
    public void likeSchedule(String blogTag) {
        Post blogData = FeatureRegistry.getCurrentFeature().getData(Post.class, blogTag);
        like = ContentUtil.contentLikes(blogData.getContentID());
        like.then().assertThat().statusCode(SC_FORBIDDEN);
    }

    @Then("Verify Like Cannot be added on Schedule Post")
    public void likeNoAddedOnSchedulePost() {
        like.then().assertThat().statusCode(SC_FORBIDDEN);
    }

    @And("^View Content (.*) as PDF$")
    public void viewContentAsType(String contentTag) throws Throwable {
        Content content = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        creationResponse = ContentUtil.getContent(content.getContentID());
        String htmlUrl = creationResponse.as(Content.class).getResources().get("html").getRef();
        String pdfUrl = htmlUrl + ".pdf";
        URL pdfURL = new URL(pdfUrl);
        pdfURlResp = RestAssured.get(pdfURL);
    }

    @When("^View BlogPost (.*) as PDF$")
    public void viewBlogPostAsPdf(String contentTag) throws Throwable {
        Post content = FeatureRegistry.getCurrentFeature().getData(Post.class, contentTag);
        creationResponse = ContentUtil.getContent(content.getContentID());
        String htmlUrl = creationResponse.as(Content.class).getResources().get("html").getRef();
        String pdfUrl = htmlUrl + ".pdf";
        URL pdfUrlTest = new URL(pdfUrl);
        pdfURlResp = RestAssured.get(pdfUrlTest);
    }

    @Then("^Content Viewed As PDF Successfully$")
    public void viewContentAsTypeSuccessfully() {
        pdfURlResp.then().assertThat().statusCode(HttpStatus.SC_OK);
    }

    @When("^User requests to create a draft BlogPost (\\S+)$")
    public void createDraftBlogPost(String blogPostTag) {
        Post post = ContentConstants.getDefaultPostData();
        post.subject(RandomStringUtils.randomAlphanumeric(8)).setStatus(Content.StatusEnum.INCOMPLETE);
        creationResponse = BlogPostUtils.createBlogPost(post);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^Get comments from the BlogPost (.*) to the list (.*)$")
    public void getCommentsFromBlogPost(String postTag, String listTag) {
        Post post = FeatureRegistry.getCurrentFeature().getData(
                Post.class,
                postTag);

        Response commentsResponse = ContentUtil.comments(
                post.getContentID());

        FeatureRegistry.getCurrentFeature().setData(
                CommentEntities.class,
                listTag,
                commentsResponse.as(CommentEntities.class));
    }

    @When("^Logged in user requests to update post (.*) with new place$")
    public void userUpdatesADocument(String postTag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        creationResponse = GroupUtils.createGroup("open");
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        Group group = creationResponse.as(Group.class);
        Response placeResponse = PlacesUtils.getPlaces(group.getPlaceID());
        PlaceEntities placeEntities = placeResponse.as(PlaceEntities.class);
        Place blog = placeEntities.getList().get(0);
        postData.setParent(CloudCommonUtils.getParentPlaceUri(blog));
        postData.setVisibility(Content.VisibilityEnum.PLACE);
        creationResponse = BlogPostUtils.updatePostToReturnResponse(postData);
        FeatureRegistry.getCurrentFeature().setData(Post.class, postTag, creationResponse.as(Post.class));
    }

    @When("^Logged in user requests to create BlogPost (.*) with user in body And Banner Image$")
    public void userCreatesABlogPostWithMultipleUsersInBodyAndBanner(String blogPostTag) {
        Person newPerson = PeopleUtils.newPerson();
        Response resp = PeopleUtils.createPerson(newPerson);
        resp.then().assertThat().statusCode(HttpStatus.SC_CREATED);
        newPerson.setId(resp.as(Person.class).getId());
        userData.addUser(newPerson.getJive().getUsername(), newPerson.getJive().getPassword());
        Content content = ContentConstants.getDefaultContentDataOfType(Content.TypeEnum.POST);
        content.content((new ContentBody()).text("@" + resp.as(Person.class).getDisplayName()));
        String title = RandomStringUtils.randomAlphanumeric(10);
        content.setSubject(title);
        Image image = new Image();
        content.addContentImagesItem(image);
        creationResponse = BlogPostUtils.createPostToReturnResponse(content);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("^User move BlogPost (\\S+) to group (\\S+)$")
    public void userMoveBlogPostToGroup(String blogPostTag, String groupTag) {
        Post blogPost = FeatureRegistry.getCurrentFeature().getData(Post.class, blogPostTag);
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        Place blog = PlacesUtils.getPlaceById(CloudCommonUtils.getParentId(blogPost.getParent()));

        blog.setParent(CloudCommonUtils.getParentPlaceUri(group));
        creationResponse = ContentUtil.updateContent(blogPost.getContentID(), blogPost);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, blogPost);
    }

    @Then("^BlogPost (\\S+) moved successfully$")
    public void verifyBlogPostMovedSuccessfully(String blogPostTag) {
        creationResponse.then().assertThat().statusCode(SC_OK);
        Post blogPost = FeatureRegistry.getCurrentFeature().getData(Post.class, blogPostTag);
        Post blogPostUpdated = creationResponse.as(Post.class);
        assertTrue(
                ValidationConstants.getErrorMessage(EntityConstants.BLOG_POST, ValidationConstants.UPDATED),
                blogPost.getParent().equals(blogPostUpdated.getParent())
        );
    }

    @When("Logged in user to mark blog content (.*) as Mark for Action to another user (.*) with response (.*)")
    public void userMarkForAction1(String contentTag, String userName, String outcomeName) {
        Post content = FeatureRegistry.getCurrentFeature().getData(Post.class, contentTag);
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userName);
        OutcomeBase outcomeData = new Outcome()
                .outcomeType(StandardOutcomeTypes.PENDING.getOutcomeType());
        outcomeData.setUser(user);
        outcomeData.setNote(SearchFilterConstants.MESSAGES);
        contentOutcomeResponse = ContentUtil.createOutcome(content.getContentID(),
                outcomeData);
        FeatureRegistry.getCurrentFeature().setData(OutcomeBase.class, outcomeName,
                contentOutcomeResponse.as(OutcomeBase.class));
        assertHttpResponseEquals(SC_CREATED, contentOutcomeResponse.statusCode());
    }
}
