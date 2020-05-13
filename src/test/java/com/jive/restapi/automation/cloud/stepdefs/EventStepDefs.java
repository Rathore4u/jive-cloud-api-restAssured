package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.data.GroupTypes;
import com.jive.restapi.automation.cloud.data.GroupTypesNew;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.FeatureRegistryStepDefs;
import com.jive.restapi.automation.utilities.*;
import com.jive.restapi.automation.utilities.EventConstants.EventAccessEnum;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.automation.utilities.v3.InviteEventUtil;
import com.jive.restapi.generated.person.models.*;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;
import lombok.val;
import org.apache.commons.lang.RandomStringUtils;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class EventStepDefs {

    private Response createEventResponse;
    private Response searchResponse;
    private Response followResponse;
    private Response commentResponse;
    private InboxEntry inbox;
    private Content event;
    private static final String EVENT_DESCRIPTION = "eventDescription";
    private static final String EVENT_TAG = "eventTag";
    private static final String SUB_SPACE = "sub-space";
    private static final String SPACE = "space";
    private static final String UNLISTED = "unlisted";
    private static final String EXTERNAL_ACCESIBLE = "external accessible";
    private static final String MEMBER_ONLY = "member only";
    private static final String PRIVATE = "private";
    private static final String EVENT = "event";
    private static final String GROUP = "group";

    @When("^Logged in user requests to create event (.*) with tags (.*)$")
    public void userCreatesAEventWithTags(String eventTag, String tag) {
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        Event eventData = ContentConstants.getDefaultEventData();
        eventData.setTags(tagList);
        eventData.startDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));
        eventData.endDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(4));
        eventData.eventAccess("open");
        eventData.eventAccessID(0);
        eventData.location("testlocation");
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Create private event (.*)$")
    public void createPrivateEvent(String eventTag) {
        createEventResponse = EventUtils.createDefaultEventWithVisibility(EventAccessEnum.PRIVATE);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Create closed event (.*)$")
    public void createClosedEvent(String eventTag) {
        Event eventData = getEventData();
        eventData.setVisibility(Content.VisibilityEnum.HIDDEN);
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @Then("^Event (.*) is created successfully$")
    public void eventIsCreatedSuccessfully(String eventTag) {
        createEventResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Logged in user requests to update event (.*) with tags (.*)$")
    public void userUpdatesAEvent(String eventTag, String tag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        eventData.setTags(tagList);
        createEventResponse = EventUtils.updateEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @Then("^Event (.*) is updated with tags (.*) successfully$")
    public void eventIsUpdatedSuccessfully(String eventTag, String tag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        List<String> tagList = new ArrayList<>(1);
        tagList.addAll(Arrays.asList(tag.split(",")));
        assertTrue(eventData.getTags().contains(tag));
    }

    @When("User request to search event (.*) by subject from (.*)")
    public void searchByEventSubject(String eventTag, String origin) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        searchResponse = SearchUtils.searchContent(
                FilterBuilder
                        .builder()
                        .addCondition(SearchFilterConstants.searchFilterKey, eventData.getSubject())
                        .buildList(), origin);
    }

    @Then("^Event (.*) is searched successfully$")
    public void eventIsSearched(String eventTag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        assertTrue(searchResponse.as(ContentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(eventData.getSubject())));
    }

    @Then("Event is not found")
    public void eventIsNotFound() {
        assertTrue(searchResponse.as(ContentEntities.class)
                .getList()
                .isEmpty());
    }

    @When("User request to delete event (.*)")
    public void deleteEvent(String eventTag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        searchResponse = EventUtils.deleteEvent(eventData.getContentID());
    }

    @Then("^Event (.*) is deleted successfully$")
    public void eventIsDeleted(String eventTag) {
        FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        searchResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^Logged in user requests to create event (.*) with description (.*) under place$")
    public void userCreatesAEventWithDescriptionUnderPlace(String eventTag, String description) {
        Response openGroupCreationResponse = GroupUtils.createOpenGroup();
        Event eventData = getEventData();
        eventData.location("testlocation");
        eventData.content((new ContentBody()).text(description));
        eventData.setParent(CloudCommonUtils.getParentPlaceUri(openGroupCreationResponse.as(Place.class)));
        eventData.setVisibility(Content.VisibilityEnum.PLACE);
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Logged in user requests to create event under (sub-space|space)$")
    public void userCreatesAEventUnderSpace(String placeType, DataTable dataValues) {
        List<Map<String, String>> rows = dataValues.asMaps(String.class, String.class);
        String eventDescription = rows.get(0).get(EVENT_DESCRIPTION);
        String eventTag = rows.get(0).get(EVENT_TAG);
        Response spaceResponse = PlacesUtils.createPlace(PlaceConstants.getDefaultSpaceData());
        spaceResponse.then().assertThat().statusCode(SC_CREATED);

        if (SUB_SPACE.equals(placeType)) {
            Place subSpace = PlaceConstants.getDefaultSpaceData();
            subSpace.setParent(CloudCommonUtils.getParentPlaceUri(spaceResponse.as(Place.class)));
            spaceResponse = PlacesUtils.createPlace(subSpace);
            spaceResponse.then().assertThat().statusCode(SC_CREATED);
        }

        Event eventData = getEventData();
        eventData.content((new ContentBody()).text(eventDescription));
        eventData.setParent(CloudCommonUtils.getParentPlaceUri(spaceResponse.as(Place.class)));
        eventData.setVisibility(Content.VisibilityEnum.PLACE);
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Logged in user requests to create event in project under (space|group)$")
    public void userCreatesAEventUnderProject(String placeType, DataTable dataValues) {
        List<Map<String, String>> rows = dataValues.asMaps(String.class, String.class);
        String eventDescription = rows.get(0).get(EVENT_DESCRIPTION);
        String eventTag = rows.get(0).get(EVENT_TAG);
        Response placeResponse;

        if (SPACE.equals(placeType)) {
            placeResponse = PlacesUtils.createPlace(PlaceConstants.getDefaultSpaceData());
        } else {
            placeResponse = GroupUtils.createGroup(GroupTypes.OPEN.toString());
        }

        placeResponse.then().assertThat().statusCode(SC_CREATED);
        placeResponse = ProjectUtils.createProject(placeResponse.as(Place.class));
        placeResponse.then().assertThat().statusCode(SC_CREATED);

        Event eventData = getEventData();
        eventData.content((new ContentBody()).text(eventDescription));
        eventData.setParent(CloudCommonUtils.getParentPlaceUri(placeResponse.as(Place.class)));
        eventData.setVisibility(Content.VisibilityEnum.PLACE);
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Logged in user requests to create event (.*) with description (.*) in community$")
    public void userCreatesAEventWithDescription(String eventTag, String description) {
        Event eventData = getEventData();
        eventData.content((new ContentBody()).text(description));
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Logged in user requests to create event in (public|member only|private|unlisted|external accessible) group$")
    public void userCreatesAEventInGroup(String groupType, DataTable dataValues) {
        String displayName = "regular-" + RandomStringUtils.randomAlphanumeric(8);
        List<Map<String, String>> rows = dataValues.asMaps(String.class, String.class);
        String eventDescription = rows.get(0).get(EVENT_DESCRIPTION);
        String eventTag = rows.get(0).get(EVENT_TAG);
        Response groupResponse;
        Group group = new Group();
        group.setName(displayName);
        group.setDescription(displayName);
        group.setDisplayName(displayName);
        group.setType(PlaceTypes.group.toString());

        switch (groupType) {
            case UNLISTED:
                group.setGroupType(GroupTypes.PRIVATE.toString());
                group.setGroupTypeV2(GroupTypesNew.PRIVATE_UNLISTED.toString());
                break;
            case EXTERNAL_ACCESIBLE:
                group.setGroupType(GroupTypes.PRIVATE.toString());
                group.setGroupTypeV2(GroupTypesNew.PUBLIC_RESTRICTED.toString());
                break;
            case MEMBER_ONLY:
                group.setGroupType(GroupTypes.MEMBER_ONLY.toString());
            case PRIVATE:
                group.setGroupType(GroupTypes.PRIVATE.toString());
            default:
                group.setGroupType(GroupTypes.OPEN.toString());
                break;
        }
        groupResponse = PlacesUtils.createPlace(group);
        groupResponse.then().assertThat().statusCode(SC_CREATED);

        Event eventData = getEventData();
        eventData.content((new ContentBody()).text(eventDescription));
        eventData.setParent(CloudCommonUtils.getParentPlaceUri(groupResponse.as(Place.class)));
        eventData.setVisibility(Content.VisibilityEnum.PLACE);
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Logged in user requests to create (open|private) event (\\S+)( as hidden)? with description \"([^\"]+)\"(?: in )?(community|group)?$")
    public void userCreatesAEventWithDescription(String eventAccess, String eventTag, String eventHidden, String description, String placeType) {
        String randomSeed = RandomStringUtils.randomAlphabetic(10);
        Event eventData = getEventData();

        if (eventAccess.toUpperCase().equals(EventAccessEnum.PRIVATE.toString())) {
            eventData.eventAccessID(EventAccessEnum.PRIVATE.getEventAccessID());
        }

        if (placeType != null && placeType.equals(GROUP)) {
            Group group = GroupUtils.createGroup(GroupTypes.OPEN.toString()).as(Group.class);
            eventData.setParent(CloudCommonUtils.getParentPlaceUri(group));
            eventData.setVisibility(Content.VisibilityEnum.PLACE);
        }

        if (eventHidden != null) {
            eventData.setVisibility(Content.VisibilityEnum.HIDDEN);
        }

        eventData.setContent((new ContentBody()).text(description + randomSeed));
        eventData.setSubject(description + randomSeed);
        createEventResponse = EventUtils.createEventToReturnResponse(eventData);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("Logged in user Like event (.*)")
    public void likeEvent(String eventTag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        Response like = ContentUtil.contentLikes(eventData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("Logged in user remove Like added on event (.*)")
    public void removeLikeOnEvent(String eventTag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        Response like = ContentUtil.deleteContentLike(eventData.getContentID());
        like.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("User request all unread messages for Event")
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
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        inbox = response.as(InboxEntry.class);
    }

    @Then("^Notification of Event ([a-zA-Z]+) is present in inbox")
    public void verifyNotificationHasDiscussion(String eventTag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        assertNotNull(eventData.getSubject(), inbox.getUnread());
    }

    @When("^User send invitation (.*) to user (.*) to join event (.*)$")
    public void userSendInvitationAnotherUserJoinEvent(String inviteTag, String user2Tag, String eventTag) {
        Person user2 = FeatureRegistry.getCurrentFeature().getData(Person.class, user2Tag);
        Event event = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        EventInviteRequest inviteEventRequest = new EventInviteRequest();
        inviteEventRequest.setBody("Please come join my awesome event.");
        inviteEventRequest.addInviteesItem(user2.getJive().getUsername());
        createEventResponse = InviteEventUtil.createInvites(event.getContentID(), inviteEventRequest);
        createEventResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(EventInviteRequest.class, inviteTag, inviteEventRequest);
    }

    @Then("^Event invite (\\S+) sent successfully$")
    public void eventInviteSentSuccessfully(String inviteTag) {
        EventInviteEntities eventInvities = createEventResponse.as(EventInviteEntities.class);
        EventInvite eventInvite = eventInvities.getList().get(0);
        EventInviteRequest eventInviteRequest = FeatureRegistry.getCurrentFeature().getData(EventInviteRequest.class, inviteTag);
        assertEquals(ValidationConstants.getErrorMessage(EVENT, ValidationConstants.CREATED),
                eventInvite.getInvitee().getJive().getUsername().toLowerCase(),
                eventInviteRequest.getInvitees().get(0).toLowerCase());
    }

    @When("^User request private event (\\S+)$")
    public void userRequestPrivateEvent(String eventTag) {
        Event storedEvent = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        event = EventUtils.getEventById(storedEvent.getContentID());
    }

    @When("^User can view private event (\\S+) details$")
    public void verifyUserCanViewPrivateEvent(String eventTag) {
        Event storedEvent = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        assertTrue(storedEvent.getSubject().equals(event.getSubject()));
    }

    private static Event getEventData() {
        String seed = RandomStringUtils.randomAlphabetic(10);
        Event eventData = ContentConstants.getDefaultEventData();
        eventData.startDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(1));
        eventData.endDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(4));
        eventData.eventAccess(EventAccessEnum.OPEN.toString());
        eventData.eventAccessID(EventAccessEnum.OPEN.getEventAccessID());
        eventData.location("event-location-" + seed);
        eventData.city("event-city-" + seed);
        eventData.content((new ContentBody()).text("event-description-" + seed));
        eventData.setVisibility(Content.VisibilityEnum.ALL);
        return eventData;
    }

    @When("^User requests to create a draft Event (\\S+)$")
    public void createDraftEvent(String eventTag) {
        Event event = getEventData();
        event.setStatus(Content.StatusEnum.INCOMPLETE);
        createEventResponse = EventUtils.createEvent(event);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @When("^Move event (.*) to (.*)$")
    public void moveEventToPlace(String eventTag, String placeTag) {
        Event event = FeatureRegistry.getCurrentFeature().getData(
                Event.class,
                eventTag);
        val placeData = FeatureRegistryStepDefs.getPlaceData(placeTag);

        event.setVisibility(Content.VisibilityEnum.PLACE);
        event.setParent(CloudCommonUtils.getParentPlaceUri(placeData));
        createEventResponse = ContentUtil.updateContent(
                event.getContentID(),
                event);
        createEventResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                Event.class,
                eventTag,
                createEventResponse.as(Event.class));
    }

    @Then("^Event (.*) is moved to place (.*) successfully$")
    public void eventIsMovedToPlaceSuccessfully(String eventTag, String placeTag) {
        Event event = FeatureRegistry.getCurrentFeature().getData(
                Event.class,
                eventTag);
        val place = FeatureRegistryStepDefs.getPlaceData(placeTag);

        assertTrue(event
                .getParent()
                .contains(place.getPlaceID()));
    }

    @When("^User with streams (.*) follows event (.*)$")
    public void followEvent(String listTag, String eventTag) {
        StreamEntities streamsList = FeatureRegistry.getCurrentFeature().getData(
                StreamEntities.class,
                listTag);
        Event event = FeatureRegistry.getCurrentFeature().getData(
                Event.class,
                eventTag);

        followResponse = ContentUtil.putContentFollowingIn(
                event.getContentID(),
                streamsList.getList());
        followResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("^User unfollows event (.*)$")
    public void unfollowEvent(String eventTag) {
        Event event = FeatureRegistry.getCurrentFeature().getData(
                Event.class,
                eventTag);
        val streamsList = new ArrayList<Stream>();

        followResponse = ContentUtil.putContentFollowingIn(
                event.getContentID(),
                streamsList);
        followResponse.then().assertThat().statusCode(SC_OK);
    }

    @Then("^User has unfollowed event (.*) successfully$")
    public void verifyUnfollowedEventSuccessfully(String eventTag) {
        Event event = FeatureRegistry.getCurrentFeature().getData(
                Event.class,
                eventTag);

        followResponse = ContentUtil.getContentFollowingIn(
                event.getContentID());

        assertTrue(followResponse.as(StreamEntities.class).getList().isEmpty());
    }

    @When("^Add a comment (.*) to the event (.*)$")
    public void addCommentToEvent(String commentTag, String eventTag) {
        Event event = FeatureRegistry.getCurrentFeature().getData(
                Event.class,
                eventTag);

        commentResponse = ContentUtil.createComment(
                event.getContentID(),
                ContentConstants.getDefaultCommentData());
        commentResponse.then().assertThat().statusCode(SC_CREATED);

        FeatureRegistry.getCurrentFeature().setData(
                Comment.class,
                commentTag,
                commentResponse.as(Comment.class)
        );
    }

    @Then("^Comment is added to the event successfully$")
    public void commentAddedSuccessfully() {
        commentResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("^Get comments from the event (.*) to the list (.*)$")
    public void getCommentsFromEvent(String eventTag, String listTag) {
        Event event = FeatureRegistry.getCurrentFeature().getData(
                Event.class,
                eventTag);

        Response commentsResponse = ContentUtil.comments(
                event.getContentID());

        FeatureRegistry.getCurrentFeature().setData(
                CommentEntities.class,
                listTag,
                commentsResponse.as(CommentEntities.class));
    }

    @Then("^Update Event (.*) EndDate$")
    public void updatedEventStartDate(String eventTag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        eventData.endDate(OffsetDateTime.now(ZoneOffset.UTC).plusDays(7));
        createEventResponse = EventUtils.updateEventToReturnResponse(eventData);
        createEventResponse.then().assertThat().statusCode(SC_OK);
        FeatureRegistry.getCurrentFeature().setData(Event.class, eventTag, createEventResponse.as(Event.class));
    }

    @Then("^Event (.*) StartDate Not Changed$")
    public void updatedEventStartDateSuccessfully(String eventTag) {
        Event eventData = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTag);
        int today = eventData.getStartDate().getDayOfMonth();
        int dayOfMonth = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1).getDayOfMonth();
        assertEquals(ValidationConstants.EVENT_STARTDATE_NOT_EQUAL, dayOfMonth, today);
    }
}
