package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.ContentConsts;
import com.jive.restapi.automation.cloud.data.FilterParameterConstants;
import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.jive.restapi.automation.cloud.data.TimeoutConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.LoginStepdefs;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.InboxUtils;
import com.jive.restapi.automation.utilities.v3.ContentUtil;
import com.jive.restapi.automation.utilities.v3.InboxUtil;
import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.generated.person.models.Announcement;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.InboxEntryEntities;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Update;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.List;

public class InboxStepDefs {

    private InboxEntry inbox;
    private String inboxContents;
    private Response inboxResponse;

    @Given("User ([a-zA-Z0-9]+) create a direct message for ([a-zA-Z0-9]+)")
    public void userCreateDirectMessageForAnotherUser(String user1, String user2) {
        LoginStepdefs login = new LoginStepdefs();
        DirectMessageStepDefs directMessage = new DirectMessageStepDefs();
        login.loggedIntoTheSystem("ADMIN");
        login.createUser(user1);
        login.createUser(user2);
        login.switchToUser(user1);
        directMessage.createDirectMessage(user2);
        directMessage.directMessageIsCreatedSuccessfully();
    }

    @When("^User request all unread (direct messages|notifications)$")
    public void userRequestUnreadMessages(String messageType) {
        FilterBuilder filter = new FilterBuilder();

        if (SearchFilterConstants.DIRECT_MESSAGES.equals(messageType)) {
            filter.addCondition(SearchFilterConstants.typeFilterKey, SearchFilterConstants.DIRECT_MESSAGE);
        } else if (SearchFilterConstants.NOTIFICATIONS.equals(messageType)) {
            filter.addCondition(SearchFilterConstants.NOTIFICATIONS, null);
        }

        filter.addCondition(SearchFilterConstants.UNREAD, null);

        CloudCommonUtils.waitToProceed(NumberConstants.twoThousand * 3);
        inboxResponse = InboxUtils.readInboxAsResponse(filter);
        inbox = inboxResponse.as(InboxEntry.class);
    }

    @When("^User request all (unread)?(?: )?messages by author (\\S+)$")
    public void userRequestUnreadMessagesByAuthor(String unreadMessages, String userTag) {
        FilterBuilder filter = new FilterBuilder();
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        filter.addCondition(SearchFilterConstants.AUTHOR_FILTER_KEY, CloudCommonUtils.getPersonUri(user));

        if (unreadMessages != null) {
            filter.addCondition(SearchFilterConstants.UNREAD, null);
        }

        CloudCommonUtils.waitToProceed(NumberConstants.twoThousand * 3);
        inboxResponse = InboxUtils.readInboxAsResponse(filter);
        inbox = inboxResponse.as(InboxEntry.class);
        inboxResponse.then().assertThat().statusCode(SC_OK);
    }

    @When("User requests all notifications")
    public void userRequestsNotifications() throws InterruptedException {
        FilterBuilder filter = new FilterBuilder();
        filter.addCondition(SearchFilterConstants.UNREAD, null);
        //filter.addCondition(SearchFilterConstants.NOTIFICATIONS, null);
        int start = 0;
        Response res = InboxUtil.getActivity(
                Options.custom(op->op
                        .filterQuery((Object[]) new FilterBuilder()
                                .addCondition("unread",null)
                                .buildList())));
        InboxEntryEntities entryEntities = res.as(InboxEntryEntities.class);
        List<InboxEntry> entries = new ArrayList<>();
        inboxContents = InboxUtils.readInboxToReturnString(filter);
    }

    @Then("^Inbox returns (\\d+) (unread|read) item(?:s)?$")
    public void inboxReturnUnreadMessages(Integer countMessages, String messageStatus) {
        InboxEntryEntities inboxEntries = inboxResponse.as(InboxEntryEntities.class);
        int unreadMessages = inboxEntries.getUnread();
        int allMessages = inboxEntries.getList().size();

        if (messageStatus.equals(SearchFilterConstants.UNREAD)) {
            assertTrue(ValidationConstants.INBOX_UNREAD_MESSAGES, unreadMessages == countMessages);
        } else {
            assertTrue(ValidationConstants.INBOX_READ_MESSAGES, allMessages - unreadMessages == countMessages);
        }
    }

    @Then("^Notification of (\\S+) is present in inbox")
    public void verifyNotificationHasAnnouncement(String announcementTag) {
        Announcement announcement = FeatureRegistry.getCurrentFeature().getData(Announcement.class, announcementTag);
        assertTrue(inboxContents.contains("announcements/"+announcement.getId()));
    }

    @When("^User request all unread items (.*)$")
    public void userRequestForUnreadMessages(String messagesTag) throws InterruptedException {
        Response response = null;
        FilterBuilder filter = new FilterBuilder();
        filter.addCondition(SearchFilterConstants.UNREAD, null);
        for (int counter = 0; counter < NumberConstants.fifteen; counter++) {
            response = InboxUtils.readInboxAsResponse(filter);
            response.then().assertThat().statusCode(SC_OK);
            InboxEntry inboxEntry = response.as(InboxEntry.class);
            if (inboxEntry.getUnread() > 0) {
                break;
            }
            // Adding wait as the loop is to read the message from the inbox
            Thread.sleep(TimeoutConstants.XS);
        }
        FeatureRegistry.getCurrentFeature().setData(InboxEntry.class, messagesTag, response.as(InboxEntry.class));
    }

    @When("^User request to mark as read first inbox message for user (\\S+)$")
    public void userRequestMarkMessageAsRead(String userTag) throws InterruptedException {
        userRequestUnreadMessagesByAuthor(SearchFilterConstants.UNREAD, userTag);
        InboxEntryEntities inboxEntryEntities = inboxResponse.as(InboxEntryEntities.class);
        InboxEntry inboxEntry = inboxEntryEntities.getList().get(0);
        String[] contentUrl = inboxEntry.getJive().getUpdate().split("/");
        Response markAsReadResponse = ContentUtil.markAsRead(contentUrl[contentUrl.length - 2]);
        markAsReadResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^User request to mark all messages as read$")
    public void userRequestMarkAllMessagesAsRead() {
        inboxResponse = InboxUtil.markRead();
    }

    @Then("^All messages marked as read successfully$")
    public void verifyAllMessagesMarkedAsRead() {
        inboxResponse.then().assertThat().statusCode(SC_NO_CONTENT);
    }

    @When("^Get (created|modified) entries (.*) from inbox$")
    public void getInboxEntriesByVerbFromInbox(String verbTag, String activitiesTag) throws InterruptedException {
        // Static wait is used to give time for content to appear in activities
        Thread.sleep(TimeoutConstants.S);
        Response getEntriesResponse = InboxUtil.getActivity(
                Options.custom(op -> op.filterQuery(String.format(
                        FilterParameterConstants.VERB_FILTER_OPTION, verbTag))));
        getEntriesResponse.then().assertThat().statusCode(SC_OK);

        FeatureRegistry.getCurrentFeature().setData(
                InboxEntryEntities.class,
                activitiesTag,
                getEntriesResponse.as(InboxEntryEntities.class));
    }

    @Then("^Inbox entries (.*) contain update (.*)$")
    public void inboxEntriesContainUpdate(String entriesTag, String updateTag) {
        InboxEntryEntities inboxEntries = FeatureRegistry.getCurrentFeature().getData(
                InboxEntryEntities.class,
                entriesTag);
        Update updateData = FeatureRegistry.getCurrentFeature().getData(
                Update.class,
                updateTag);

        assertTrue(String.format(ValidationConstants.INBOX_ENTRIES_CONTAIN_UPDATE_VALIDATION, updateData.getSubject()),
                inboxEntries
                        .getList()
                        .stream()
                        .anyMatch(elem -> elem
                                .getObject()
                                .getDisplayName()
                                .replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING)
                                .contains(updateData.getSubject().replace(ContentConsts.ONE_SPACE_STRING, ContentConsts.EMPTY_STRING))));
    }
}
