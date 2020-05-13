package com.jive.restapi.automation.cloud.stepdefs;

import static org.apache.http.HttpStatus.SC_CREATED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_NO_CONTENT;
import static org.apache.http.HttpStatus.SC_CONFLICT;
import static org.junit.Assert.assertTrue;

import com.aurea.automation.openapi.common.Options;
import com.jive.restapi.automation.cloud.data.ValidationConstants;
import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.v3.AdminUtil;
import com.jive.restapi.generated.person.models.JivePropertyEntity;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.cucumber.datatable.DataTable;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import lombok.val;

public class SystemPropertyStepDefs {

    Response adminResponse;
    private static final String TAG_PROPERTY_NAME = "tagPropertyName";
    private static final String PROPERTY_NAME = "propertyName";
    private static final String PROPERTY_VALUE = "propertyValue";
    private static final String JIVE_PROPERTY = "jive property";
    private static final String CREATED = "created";
    private static final String DELETED = "deleted";

    @When("^Admin user request to set jive property$")
    public void userRequestCreateJiveProperty(DataTable properties) {
        List<Map<String, String>> data = properties.asMaps(String.class, String.class);
        JivePropertyEntity jiveProperty = new JivePropertyEntity();
        String tagName = data.get(0).get(TAG_PROPERTY_NAME);
        String propertyName = data.get(0).get(PROPERTY_NAME);
        String propertyValue = data.get(0).get(PROPERTY_VALUE);
        jiveProperty.setName(propertyName);
        jiveProperty.setValue(propertyValue);

        deleteJiveProperty(properties);
        adminResponse = AdminUtil.createProperty(jiveProperty);
        FeatureRegistry.getCurrentFeature().setData(JivePropertyEntity.class, tagName, jiveProperty);
    }

    @Then("^Jive property (\\S+) is (created|updated|deleted) successfully$")
    public void verifyResponseCreateJiveProperty(String jivePropertyTag, String expectedCode) {
        int statusCode;
        switch (expectedCode) {
            case CREATED:
                statusCode = SC_CREATED;
                break;
            case DELETED:
                statusCode = SC_NO_CONTENT;
                break;
            default:
                statusCode = SC_OK;
                break;
        }
        int responseStatusCode = adminResponse.getStatusCode();
        if (statusCode == SC_CREATED) {
            assertTrue(
                    ValidationConstants.getErrorMessage(JIVE_PROPERTY, ValidationConstants.CREATED),
                    responseStatusCode == SC_CREATED || responseStatusCode == SC_CONFLICT
            );
        } else {
            adminResponse.then().assertThat().statusCode(statusCode);
        }
    }

    @Given("^Delete jive property$")
    public void deleteJiveProperty(DataTable properties) {
        List<Map<String, String>> data = properties.asMaps(String.class, String.class);
        adminResponse = AdminUtil.deleteProperty(data.get(0).get(PROPERTY_NAME));
    }

    @When("^Create or update jive property:$")
    public void createOrUpdateJiveProperty(DataTable propertyData) {
        List<String> propertyList = propertyData.asList(String.class);
        val jiveProperty = new JivePropertyEntity()
                .name(propertyList.get(0))
                .value(propertyList.get(1));

        adminResponse = AdminUtil.createProperty(jiveProperty);
        if (adminResponse.statusCode() == SC_CONFLICT) {
            adminResponse = AdminUtil.updateProperty(jiveProperty.getName(), jiveProperty);
            adminResponse.then().assertThat().statusCode(SC_OK);
        }
    }

    @Then("^Jive property created or updated successfully$")
    public void jivePropertyCreatedOrUpdatedSuccessfully() {
        int statusCode = -1;
        int responseStatusCode = adminResponse.getStatusCode();

        switch (responseStatusCode) {
            case SC_CREATED:
                statusCode = SC_CREATED;
                break;
            case SC_OK:
                statusCode = SC_OK;
                break;
        }

        adminResponse.then().assertThat().statusCode(statusCode);
    }
}
