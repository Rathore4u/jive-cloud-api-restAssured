package com.jive.restapi.automation.cloud.stepdefs;

import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.NumberConstants;
import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.BlogPostUtils;
import com.jive.restapi.automation.utilities.ContentConstants;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.PeopleUtils;
import com.jive.restapi.automation.utilities.PlacesUtils;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.ContentBody;
import com.jive.restapi.generated.person.models.ContentEntities;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Image;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Place;
import com.jive.restapi.generated.person.models.Post;
import com.xo.restapi.automation.context.UserData;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import java.net.URL;
import lombok.Getter;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpStatus;

public class BlogPostExtendStepDefs {

    private Response creationResponse;
    private Response searchResponse;

    @Getter
    private UserData userData = new UserData();

    @When("^Logged in user requests to create BlogPost (.*) with user in body And Inline Image$")
    public void userCreatesABlogPostWithMultipleUsersInBodyAndInlineImage(String blogPostTag) {
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
        content.setIconCss(RandomStringUtils.randomAlphanumeric(10));
        creationResponse = BlogPostUtils.createPostToReturnResponse(content);
        FeatureRegistry.getCurrentFeature().setData(Post.class, blogPostTag, creationResponse.as(Post.class));
    }

    @When("User requests to get blogPost (.*) by outcome (.*) from group (.*)")
    public void getPostByOutcomeFromPlace(String postTag, String outcomeTag, String groupTag) throws
            InterruptedException {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        Place place = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        for (int counter = NumberConstants.zero; counter < NumberConstants.fifteen; counter++) {
            searchResponse = PlacesUtils.getContentWithFilters(place.getPlaceID(),
                    FilterBuilder
                            .builder()
                            .addCondition(SearchFilterConstants.searchFilterKey, postData.getSubject())
                            .addCondition(SearchFilterConstants.outcomeTypeFilterKey, "\"" + outcomeTag + "\"")
                            .buildList());
            if (searchResponse.as(ContentEntities.class).getList().size() != NumberConstants.zero) {
                break;
            }
            // Added 2 sec sleep for content to index
            Thread.sleep(NumberConstants.twoThousand);
        }
    }

    @Then("^Post (.*) is returned successfully$")
    public void verifyPostReturned(String postTag) {
        Post postData = FeatureRegistry.getCurrentFeature().getData(Post.class, postTag);
        assertTrue(SearchFilterConstants.INVALID_MESSAGE, searchResponse.as(ContentEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getSubject().equals(postData.getSubject())));
    }

    @When("User requests to get pdf of blogpost (.*) and gets response (.*)")
    public void getPdf(String blogPostTag, String response) throws Throwable {
        Content blogPost = FeatureRegistry.getCurrentFeature().getData(Post.class, blogPostTag);
        String pdfUrl = blogPost.getResources().get("pdf").getRef();
        Response pdfURlResp = RestAssured.get(new URL(pdfUrl));
        FeatureRegistry.getCurrentFeature().setData(int.class, response, pdfURlResp.getStatusCode());
    }
}
