package com.jive.restapi.automation.cloud.stepdefs;

import static org.junit.Assert.assertNotNull;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.generated.person.models.InboxEntry;
import com.jive.restapi.generated.person.models.Post;
import cucumber.api.java.en.Then;

public class BlogPostExtendedStepDefs {
    @Then("^Notification of blog ([a-zA-Z]+) is present in inbox (.*)$")
    public void verifyNotificationHasBlog(String blogTag, String inboxTag) {
        InboxEntry inbox = FeatureRegistry.getCurrentFeature().getData(InboxEntry.class, inboxTag);
        Post blogData = FeatureRegistry.getCurrentFeature().getData(Post.class, blogTag);
        assertNotNull(blogData.getSubject(), inbox.getUnread());
    }
}
