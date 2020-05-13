package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.stepdefs.commonsteps.LoginStepdefs;
import com.jive.restapi.automation.configs.Role;
import com.jive.restapi.automation.utilities.PublicationUtils;
import com.jive.restapi.automation.utilities.v3.PersonUtil;
import com.jive.restapi.automation.utilities.v3.StreamUtil;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.StreamEntities;
import com.jive.restapi.generated.person.models.Content;
import com.jive.restapi.generated.person.models.Stream;
import com.jive.restapi.generated.person.models.GenericEntities;
import com.jive.restapi.generated.person.models.Publication;

import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

public class StreamStepDefs {

    private Response streamResponse;
    private static final String PLACE_ID = "placeID";
    private static final String CONNECTIONS = "connections";

    @When("^User (\\S+) request streams$")
    public void userGetStreams(String userTag) {
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        streamResponse =  PersonUtil.getStreams(user.getId());
    }

    @Then("^Streams contain (Blog Post|Document|Discussion|Question) (\\S+)$")
    public void verifyStreamAssociations(String contentType, String contentTag){
        streamResponse.then().assertThat().statusCode(SC_OK);
        StreamEntities streams = streamResponse.as(StreamEntities.class);
        String compareKey;

        Content content = CloudCommonUtils.getContentData(contentType.toLowerCase(), contentTag);
        compareKey = content.getParent();

        Stream connection = streams.getList().stream()
                .filter(
                        item -> item.getSource().toString().equals(CONNECTIONS)
                )
                .findAny().orElse(null);
        Response resp = StreamUtil.getAssociations(connection.getId());
        GenericEntities entities = resp.as(GenericEntities.class);

        assertTrue(
                ValidationConstants.STREAM_NOT_CONTAIN_OBJECT,
                entities.getList().stream().anyMatch(
                item -> compareKey.contains(
                        item.get(PLACE_ID).toString())
                )
        );
    }

    @Then("^Admin delete stream (\\S+)$")
    public void adminDeletePublication(String publicationTag){
        LoginStepdefs login = new LoginStepdefs();
        login.loggedIntoTheSystem(Role.ADMIN.toString());
        Publication publication = FeatureRegistry.getCurrentFeature().getData(Publication.class, publicationTag);

        // Publication should be deleted because limit of streams is 10
        Response resp = PublicationUtils.deletePublication(publication);
        resp.then().assertThat().statusCode(SC_NO_CONTENT);
    }
}
