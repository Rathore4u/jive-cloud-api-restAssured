package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertEquals;

import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.automation.utilities.v3.ShareUtil;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.Discussion;
import com.jive.restapi.generated.person.models.Document;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Idea;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.jive.restapi.generated.person.models.Poll;
import com.jive.restapi.generated.person.models.Post;
import com.jive.restapi.generated.person.models.Share;
import com.jive.restapi.generated.person.models.ShareRequest;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;

public class ShareStepDefs {

    private Response creationResponse;

    @When("User shares place (.+) with group (\\S+)")
    public void sharePlaceWithGroup(String placeTag, String groupTag) {
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        Place group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        ShareRequest shareData = new ShareRequest();
        List<String> places = new ArrayList<>();
        places.add(CloudCommonUtils.getParentPlaceUri(group));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getParentPlaceUri(place))
                .places(places);
        creationResponse = ShareUtil.createShare(shareData);
    }

    @Then("Share is created successfully")
    public void verifyShareCreated() {
        assertEquals(ValidationConstants.SHARE_CREATION_FAILED, creationResponse.getStatusCode(), SC_CREATED);
    }

    @When("User shares idea (.*) with place (\\S+)")
    public void shareIdeaWithPlace(String ideaTag, String placeTag) {
        Content idea = FeatureRegistry.getCurrentFeature().getData(Idea.class, ideaTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        ShareRequest shareData = new ShareRequest();
        List<String> places = new ArrayList<>();
        places.add(CloudCommonUtils.getParentPlaceUri(place));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getContentUri(idea))
                .places(places);
        creationResponse = ShareUtil.createShare(shareData);
    }

    @When("User shares discussion (.*) with place (.*)")
    public void shareDiscussionWithPlace(String discussionTag, String placeTag) {
        Content discussion = FeatureRegistry.getCurrentFeature().getData(Discussion.class, discussionTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        ShareRequest shareData = new ShareRequest();
        List<String> places = new ArrayList<>();
        places.add(CloudCommonUtils.getParentPlaceUri(place));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getContentUri(discussion))
                .places(places);
        creationResponse = ShareUtil.createShare(shareData);
    }

    @When("User shares poll (.*) with place (.*)")
    public void sharePollWithPlace(String pollTag, String placeTag) {
        Content poll = FeatureRegistry.getCurrentFeature().getData(Poll.class, pollTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        ShareRequest shareData = new ShareRequest();
        List<String> places = new ArrayList<>();
        places.add(CloudCommonUtils.getParentPlaceUri(place));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getContentUri(poll))
                .places(places);
        creationResponse = ShareUtil.createShare(shareData);
    }

    @When("User shares content (.*) with place (.*)")
    public void shareContentWithPlace(String contentTag, String placeTag) {
        Content content = FeatureRegistry.getCurrentFeature().getData(Content.class, contentTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        ShareRequest shareData = new ShareRequest();
        List<String> places = new ArrayList<>();
        places.add(CloudCommonUtils.getParentPlaceUri(place));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getContentUri(content))
                .places(places);
        creationResponse = ShareUtil.createShare(shareData);
    }

    @When("User shares document (.*) with place (.*)")
    public void shareDocumentWithPlace(String documentTag, String placeTag) {
        Content content = FeatureRegistry.getCurrentFeature().getData(Document.class, documentTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Place.class, placeTag);
        ShareRequest shareData = new ShareRequest();
        List<String> places = new ArrayList<>();
        places.add(CloudCommonUtils.getParentPlaceUri(place));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getContentUri(content))
                .places(places);
        creationResponse = ShareUtil.createShare(shareData);
    }

    @When("User shares blogPost (.*) with group (.*)")
    public void shareBlogPostWithPlace(String blogPostTag, String placeTag) {
        Content content = FeatureRegistry.getCurrentFeature().getData(Post.class, blogPostTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Group.class, placeTag);
        ShareRequest shareData = new ShareRequest();
        List<String> places = new ArrayList<>();
        places.add(CloudCommonUtils.getParentPlaceUri(place));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getContentUri(content))
                .places(places);
        creationResponse = ShareUtil.createShare(shareData);
    }

    @When("Logged in user shares group (.*) with another user (.*) with response (.*)")
    public void shareGroupWithUser(String groupTag, String userTag, String response) {
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        Place group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        ShareRequest shareData = new ShareRequest();
        shareData.addParticipantsItem(CloudCommonUtils.getPersonUri(user));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getParentPlaceUri(group));
        creationResponse = ShareUtil.createShare(shareData);
        assertEquals(ValidationConstants.SHARE_CREATION_FAILED, creationResponse.getStatusCode(), SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Share.class, response, creationResponse.as(Share.class));
    }

    @Then("Share (.*) is created successfully")
    public void verifyShareCreated(String shareResponse) {
        Share shareData = FeatureRegistry.getCurrentFeature().getData(Share.class, shareResponse);
        assertEquals(ValidationConstants.SHARE_CREATION_FAILED, EntityConstants.SHARE, shareData.getType());
        assertEquals(ValidationConstants.SHARE_CREATION_FAILED, EntityConstants.PUBLISHED, shareData.getStatus());
    }

    @When("^Logged in user shares (document|discussion|blogpost|idea|poll) (.*) with user (.*)$")
    public void shareContentWithUser(String contentType, String contentTag, String userTag) {
        Content contentData = CloudCommonUtils.getContentData(contentType, contentTag);
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        ShareRequest shareData = new ShareRequest();
        List<String> participants = new ArrayList<>();
        participants.add(CloudCommonUtils.getPersonUri(user));
        shareData.content(new ContentBody().text(RandomStringUtils.randomAlphanumeric(10)))
                .shared(CloudCommonUtils.getContentUri(contentData))
                .setParticipants(participants);
        creationResponse = ShareUtil.createShare(shareData, Options.logRequest());
    }
}
