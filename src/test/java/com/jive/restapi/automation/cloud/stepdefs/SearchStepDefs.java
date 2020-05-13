package com.jive.restapi.automation.cloud.stepdefs;

import com.jive.restapi.automation.cloud.data.SearchFilterConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.FilterBuilder;
import com.jive.restapi.automation.utilities.SearchUtils;
import com.jive.restapi.generated.person.models.Group;
import com.jive.restapi.generated.person.models.Person;
import com.jive.restapi.generated.person.models.PersonEntities;
import com.jive.restapi.generated.person.models.PlaceEntities;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import org.junit.Assert;

public class SearchStepDefs {
    private Response searchResponse;

    @When("User searches for person (.+) using firstname from (\\S+)( with underscore)?")
    public void searchPersonFromMention(String userTag, String origin, String withUnderscore) {
        Person person = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        String searchString = person.getName().getGivenName();
        if (withUnderscore != null) {
            searchString = person.getName().getGivenName() + "_* OR " + person.getName().getGivenName();
        }
        searchResponse = SearchUtils.searchPeople(FilterBuilder
                .builder()
                .addCondition("search", searchString)
                .buildList(), origin);
    }

    @Then("^Person (\\S+) is searched successfully$")
    public void verifyPersonSearch(String userTag) {
        Person person = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        Assert.assertTrue(SearchFilterConstants.INVALID_MESSAGE, searchResponse.as(PersonEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getName().getGivenName().equals(person.getName().getGivenName())));
    }

    @When("User searches for group (.+) using name from (\\S+)( with underscore)?")
    public void searchPlaceFromMention(String groupTag, String origin, String withUnderscore) {
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        String searchString = group.getName();
        if (withUnderscore != null) {
            searchString = group.getName() + "_* OR " + group.getName();
        }
        searchResponse = SearchUtils.searchPlace(FilterBuilder
                .builder()
                .addCondition("search", searchString)
                .buildList(), origin);
    }

    @Then("^Group (.+) is searched successfully$")
    public void verifyGroupSearch(String groupTag) {
        Group group = FeatureRegistry.getCurrentFeature().getData(Group.class, groupTag);
        Assert.assertTrue(SearchFilterConstants.INVALID_MESSAGE, searchResponse.as(PlaceEntities.class)
                .getList()
                .stream()
                .anyMatch(c -> c.getName().equals(group.getName())));
    }
}
