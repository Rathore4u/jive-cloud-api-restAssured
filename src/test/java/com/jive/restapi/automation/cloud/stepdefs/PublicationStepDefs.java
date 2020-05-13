package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.junit.Assert.assertTrue;

import com.jive.restapi.automation.cloud.data.EntityConstants;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.PublicationConstants;
import com.jive.restapi.automation.utilities.PublicationUtils;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.Publication;
import com.jive.restapi.generated.person.models.Subscription;
import com.xo.restapi.automation.configs.CloudCommonUtils;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

public class PublicationStepDefs {

    private Response publicationResponse;

    @When("^User request to create a new stream (\\S+) with rule adding (\\S+) and (\\S+)$")
    public void userCreatesStreamWithGroupUser(String publicationTag, String groupTag, String userTag) {
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        Publication publication = PublicationConstants.getDefaultPublicationData();
        Subscription subscription = new Subscription();

        subscription.addSubscribersItem(CloudCommonUtils.getPersonUri(user));
        subscription.addAssociationsItem(CloudCommonUtils.getParentPlaceUri(group));
        publication.addSubscriptionsItem(subscription);
        publicationResponse = PublicationUtils.createPublication(publication);
        publication.setId(publicationResponse.as(Publication.class).getId());
        FeatureRegistry.getCurrentFeature().setData(Publication.class, publicationTag, publication);
    }

    @Then("^Stream (\\S+) is created successfully$")
    public void verifyPublicationCreatedSuccessfully(String publicationTag) {
        publicationResponse.then().assertThat().statusCode(SC_CREATED);
        Publication publication = FeatureRegistry.getCurrentFeature().getData(Publication.class, publicationTag);
        Publication publicationCreated = publicationResponse.as(Publication.class);

        assertTrue(
                ValidationConstants.getErrorMessage(EntityConstants.QUESTION, ValidationConstants.CREATED),
                publication.getName().equals(publicationCreated.getName())
        );
        assertTrue(
                ValidationConstants.getErrorMessage(EntityConstants.QUESTION, ValidationConstants.CREATED),
                publication.getSubscriptions().get(0).getAssociations().get(0)
                        .equals(publicationCreated.getSubscriptions().get(0).getAssociations().get(0))
        );
    }
}
