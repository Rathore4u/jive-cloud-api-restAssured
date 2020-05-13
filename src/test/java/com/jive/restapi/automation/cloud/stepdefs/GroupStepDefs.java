package com.jive.restapi.automation.cloud.stepdefs;

import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertHttpResponseEquals;
import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertKeyEquals;
import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertKeyFalse;
import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertKeyNotNull;
import static com.jive.restapi.automation.cloud.data.ExpectationValidator.assertKeyTrue;
import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.FilterParameterConstants;
import com.jive.restapi.automation.cloud.data.GroupTypes;
import com.jive.restapi.automation.cloud.data.GroupTypesNew;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.UrlConsts;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloudutils.data.DataConstants;
import com.jive.restapi.automation.cloudutils.data.InviteStateEnum;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.GroupUtils;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.automation.utilities.v3.InviteUtil;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.automation.utilities.v3.PlacesUtil;
import com.jive.restapi.generated.person.models.ActivityEntities;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.Invite;
import com.jive.restapi.generated.person.models.InviteEntities;
import com.jive.restapi.generated.person.models.InviteRequest;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.PlaceEntities;
import com.jive.restapi.generated.person.models.Update;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.lang.RandomStringUtils;

public class GroupStepDefs {

    private Response creationResponse;
    private Response privateGroupCreationResponse;
    private static final String UNLISTED = "private unlisted";
    private static final String EXTERNAL_ACCESIBLE = "external accessible";
    private static final String PUBLIC_RESTRICTED = "public restricted";
    private static final String MEMBER_ONLY = "member only";
    private static final String PRIVATE = "private";
    private static final String SEARCH = "search";
    private static final String ORIGIN = "atmention";

    @When("Request to create an Open Group")
    public void adminRequestToCreateOpenGroup() {
        creationResponse = GroupUtils.createOpenGroup();
    }

    @Then("Open Group is Created Successfully")
    public void adminVerifyOpenGroupCreated() {
        creationResponse.then().assertThat().statusCode(SC_CREATED);
    }

    @When("Request to create an Private Group")
    public void adminRequestToCreatePrivateGroup() {
        privateGroupCreationResponse = GroupUtils.createPrivateGroup();
    }

    @Then("Private Group is Created Successfully")
    public void adminVerifyPrivateGroupCreated() {
        assertHttpResponseEquals(SC_CREATED, privateGroupCreationResponse.getStatusCode());
    }

    @When("^User request to create \"(Member only|Private|Secret|Open)\" group (.*)$")
    public void userCreateGroup(String group, String groupTag) {
        creationResponse = GroupUtils.createGroup(group);
        creationResponse.then().assertThat().statusCode(SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Group.class, groupTag, creationResponse.as(Group.class));
    }

    @Then("^\"(?:Member only|Private|Secret|Open)\" group is created successfully$")
    public void userVerifyGroupCreated() {
        assertHttpResponseEquals(SC_CREATED, creationResponse.getStatusCode());
    }

    @Then("^Verify \"(public|member only|private|private unlisted|external accessible|public restricted)\" group (.*) is created successfully$")
    public void userVerifyDifferentGroupCreated(String groupType, String groupTag) {
        assertHttpResponseEquals(SC_CREATED, creationResponse.getStatusCode());
        Group groupData = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        switch (groupType) {
            case UNLISTED:
                assertKeyEquals(Group.SERIALIZED_NAME_GROUP_TYPE, GroupTypes.SECRET.toString(),
                        groupData.getGroupType());
                assertKeyEquals(Group.SERIALIZED_NAME_GROUP_TYPE_V2, GroupTypesNew.PRIVATE_UNLISTED.toString(),
                        groupData.getGroupTypeV2());
                break;
            case EXTERNAL_ACCESIBLE:
                break;
            case MEMBER_ONLY:
                break;
            case PUBLIC_RESTRICTED:
                assertKeyEquals(Group.SERIALIZED_NAME_GROUP_TYPE, GroupTypes.MEMBER_ONLY.toString(),
                        groupData.getGroupType());
                assertKeyEquals(Group.SERIALIZED_NAME_GROUP_TYPE_V2, GroupTypesNew.PUBLIC_RESTRICTED.toString(),
                        groupData.getGroupTypeV2());
                assertKeyFalse(Group.SERIALIZED_NAME_EXTENDED_AUTHORS_ENABLED, groupData.getExtendedAuthorsEnabled());
                assertKeyFalse(Group.SERIALIZED_NAME_VISIBLE_TO_EXTERNAL_CONTRIBUTORS,
                        groupData.getVisibleToExternalContributors());
                break;
            case PRIVATE:
                assertKeyEquals(Group.SERIALIZED_NAME_GROUP_TYPE, GroupTypes.PRIVATE.toString(),
                        groupData.getGroupType());
                assertKeyEquals(Group.SERIALIZED_NAME_GROUP_TYPE_V2, GroupTypesNew.PRIVATE_LISTED.toString(),
                        groupData.getGroupTypeV2());
                assertKeyTrue(Group.SERIALIZED_NAME_EXTENDED_AUTHORS_ENABLED,
                        groupData.getExtendedAuthorsEnabled());
                assertKeyTrue(Group.SERIALIZED_NAME_EXTENDED_AUTHORS_ENABLED, groupData.getExtendedAuthorsEnabled());
                break;
            default:
                break;
        }
    }

    @When("^Logged in user to create \"(public|member only|private|private unlisted|external accessible|public restricted)\" group (.*)$")
    public void userCreatePrivateUnlistedGroup(String groupType, String groupTag) {
        String displayName = EntityConstants.GROUP + RandomStringUtils.randomAlphanumeric(8);
        Group group = new Group();
        group.setName(displayName);
        group.setDescription(displayName);
        group.setDisplayName(displayName);
        group.setType(PlaceTypes.group.toString());
        switch (groupType) {
            case UNLISTED:
                group.setGroupType(GroupTypes.SECRET.toString());
                group.setGroupTypeV2(GroupTypesNew.PRIVATE_UNLISTED.toString());
                group.setVisibleToExternalContributors(false);
                break;
            case EXTERNAL_ACCESIBLE:
                group.setGroupType(GroupTypes.PRIVATE.toString());
                group.setGroupTypeV2(GroupTypesNew.PUBLIC_RESTRICTED.toString());
                break;
            case MEMBER_ONLY:
                group.setGroupType(GroupTypes.MEMBER_ONLY.toString());
                break;
            case PUBLIC_RESTRICTED:
                group.setGroupType(GroupTypes.MEMBER_ONLY.toString());
                group.setGroupTypeV2(GroupTypesNew.PUBLIC_RESTRICTED.toString());
                group.setVisibleToExternalContributors(false);
                group.setExtendedAuthorsEnabled(false);
                break;
            case PRIVATE:
                group.setGroupType(GroupTypes.PRIVATE.toString());
                group.setGroupTypeV2(GroupTypesNew.PRIVATE_UNLISTED.toString());
                group.setVisibleToExternalContributors(false);
                group.setExtendedAuthorsEnabled(true);
                break;
            default:
                group.setGroupType(GroupTypes.OPEN.toString());
                break;
        }
        creationResponse = PlacesUtils.createPlace(group);
        assertHttpResponseEquals(SC_CREATED, creationResponse.getStatusCode());
        FeatureRegistry.getCurrentFeature().setData(Group.class, groupTag, creationResponse.as(Group.class));
    }

    @When("Logged in user to search the created group (.*) by name in spotlight search with response (.*)")
    public void searchGroupSpotlightSearch(String groupTag, String response) {
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        creationResponse = SearchUtils.searchPlace(FilterBuilder
                .builder()
                .addCondition(SEARCH, group.getName())
                .buildList(), ORIGIN);
        assertHttpResponseEquals(SC_OK, creationResponse.getStatusCode());
        FeatureRegistry.getCurrentFeature()
                .setData(PlaceEntities.class, response, creationResponse.as(PlaceEntities.class));
    }

    @Then("Verify group (.*) search response (.*) is searched successfully$")
    public void verifyGroupSearch(String groupTag, String response) {
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        PlaceEntities placeData = FeatureRegistry.getCurrentFeature().getData(PlaceEntities.class, response);
        assertKeyTrue(Group.SERIALIZED_NAME_NAME, placeData
                .getList()
                .stream()
                .anyMatch(c -> c.getName().equals(group.getName())));
    }

    @When("Logged in user to invite user (.*) to group (.*) created with response (.*)")
    public void createGroupInvite(String userData, String groupTag, String response) {
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        Person user2 = FeatureRegistry.getCurrentFeature().getData(Person.class, userData);
        InviteRequest inviteEventRequest = new InviteRequest();
        inviteEventRequest.setBody(DataConstants.GROUP_INVITE);
        List<String> invitees = new ArrayList<>(NumberConstants.one);
        invitees.addAll(Arrays.asList(user2.getThumbnailUrl().replace(UrlConsts.AVATAR_LINK, DataConstants.BLANK)));
        inviteEventRequest.setInvitees(invitees);
        creationResponse = InviteUtil.createInvite(group.getPlaceID(), inviteEventRequest);
        assertHttpResponseEquals(SC_CREATED, creationResponse.getStatusCode());
        getInviteStatus(creationResponse.as(InviteEntities.class).getList().get(NumberConstants.zero).getId());
        FeatureRegistry.getCurrentFeature()
                .setData(InviteEntities.class, response, creationResponse.as(InviteEntities.class));
    }

    @Then("^Group (.*) invite having response (.*) sent successfully$")
    public void groupInviteSentSuccessfully(String groupTag, String inviteTag) {
        InviteEntities groupInvite = FeatureRegistry.getCurrentFeature().getData(InviteEntities.class, inviteTag);
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        assertKeyEquals(Invite.SERIALIZED_NAME_ID,
                groupInvite.getList().get(NumberConstants.zero).getInviter().getId(),
                group.getCreator().getId());
        assertKeyFalse(Invite.SERIALIZED_NAME_TYPE,
                groupInvite.getList().get(NumberConstants.zero).getType().isEmpty());
        assertKeyFalse(Invite.SERIALIZED_NAME_STATE,
                groupInvite.getList().get(NumberConstants.zero).getState().isEmpty());
    }

    @When("Logged in user to accept invite (.*) with response (.*)")
    public void createGroupInvite(String inviteData, String response) {
        InviteEntities invite = FeatureRegistry.getCurrentFeature().getData(InviteEntities.class, inviteData);
        Invite inviteState = new Invite();
        inviteState.setState(DataConstants.STATE_ACCEPTED);
        creationResponse = InviteUtil.updateInvite(invite.getList().get(NumberConstants.zero).getId(), inviteState);
        assertHttpResponseEquals(SC_OK, creationResponse.getStatusCode());
        FeatureRegistry.getCurrentFeature().setData(Invite.class, response, creationResponse.as(Invite.class));
    }

    @Then("^Verify Group invite having response (.*) accepted successfully$")
    public void groupInviteAcceptedSuccessfully(String inviteTag) {
        Invite groupInvite = FeatureRegistry.getCurrentFeature().getData(Invite.class, inviteTag);
        assertKeyEquals(Invite.SERIALIZED_NAME_STATE,
                groupInvite.getState(), InviteStateEnum.DELETED.toString().toLowerCase());
        assertKeyFalse(Invite.SERIALIZED_NAME_TYPE, groupInvite.getType().isEmpty());
    }

    public static void getInviteStatus(String inviteData) {
        for (int counter = 0; counter < 15; counter++) {
            Invite resp = InviteUtil.getInvite(inviteData).as(Invite.class);
            if (resp.getState().equals(InviteStateEnum.SENT.toString().toLowerCase())) {
                break;
            }
            try {
                Thread.sleep(NumberConstants.fiftyThousand);  // Sleep is require to give some time to process invite
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    @Then("^Group invite notification of (.*) is present in inbox (.*)")
    public void verifyGroupInviteNotification(String inviteTag, String inboxData) {
        InviteEntities groupInvite = FeatureRegistry.getCurrentFeature().getData(InviteEntities.class, inviteTag);
        InboxEntry inbox = FeatureRegistry.getCurrentFeature().getData(InboxEntry.class, inboxData);
        assertKeyNotNull(groupInvite.getList().get(NumberConstants.zero).getBody(), inbox.getUnread());
    }

    @When("^Get (document|update) activities (.*) from the group (.*)$")
    public void getActivitiesByTypeFromGroup(String typeTag, String activitiesTag, String groupTag) throws InterruptedException {
        Group groupData = FeatureRegistry.getCurrentFeature().getData(
                Group.class,
                groupTag);

        // Static wait is used to give time for content to appear in activities
        Thread.sleep(TimeoutConstants.M);
        Response getActivitiesResponse = PlacesUtil.getActivity(
                groupData.getPlaceID(),
                Options.custom(op -> op.filterQuery(String.format(
                        FilterParameterConstants.TYPE_FILTER_OPTION, typeTag))));
        getActivitiesResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                ActivityEntities.class,
                activitiesTag,
                getActivitiesResponse.as(ActivityEntities.class));
    }

    @Then("^Group activities (.*) contain update (.*)$")
    public void groupActivitiesContainUpdate(String activitiesTag, String updateTag) {
        ActivityEntities activities = FeatureRegistry.getCurrentFeature().getData(
                ActivityEntities.class,
                activitiesTag);
        Update updateData = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                updateTag);

        assertTrue(
                String.format(ValidationConstants.USER_ACTIVITIES_CONTAIN_UPDATE_VALIDATION, updateData.getSubject()),
                activities
                        .getList()
                        .stream()
                        .anyMatch(elem -> elem
                                .getObject()
                                .getDisplayName()
                                .replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING)
                                .contains(updateData.getSubject()
                                        .replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING))));
    }
}

