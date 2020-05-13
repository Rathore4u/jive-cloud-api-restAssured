package com.jive.restapi.automation.cloud.stepdefs;

import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.generated.person.models.Event;
import cucumber.api.java.en.Then;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EventExtendedStepDefs {

    @Then("^Verify that events (.*) (.*) (.*) published in Public Group with same Title$")
    public void eventsPublishedWithSameTitle(String eventTagCity, String eventTagCountry, String eventTagLocation) {
        Event eventDataCity = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTagCity);
        Event eventDataCountry = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTagCountry);
        Event eventDataLocation = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTagLocation);
        assertEquals(ValidationConstants.EVENT_SUBJECT, eventDataCountry.getSubject(), eventDataLocation.getSubject());
        assertEquals(ValidationConstants.EVENT_SUBJECT, eventDataCountry.getSubject(), eventDataCity.getSubject());
    }

    @Then("^Verify that events (.*) (.*) (.*) published in Public Group with Different Location$")
    public void eventsPublishedWithDifferentLocation(String eventTagCity, String eventTagCountry, String eventTagLocation) {
        Event eventDataCity = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTagCity);
        Event eventDataCountry = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTagCountry);
        Event eventDataLocation = FeatureRegistry.getCurrentFeature().getData(Event.class, eventTagLocation);
        assertNotEquals(ValidationConstants.EVENT_LOCATION, eventDataCountry.getLocation(), eventDataLocation.getLocation());
        assertNotEquals(ValidationConstants.EVENT_LOCATION, eventDataCountry.getLocation(), eventDataCity.getLocation());
    }
}
