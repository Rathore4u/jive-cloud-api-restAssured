package com.jive.restapi.automation.cloud.stepdefs;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.utilities.OutcomeConstants;
import com.jive.restapi.automation.utilities.StandardOutcomeTypes;
import com.jive.restapi.automation.utilities.v3.OutcomesUtil;
import com.jive.restapi.generated.person.models.Outcome;
import cucumber.api.java.en.When;
import io.restassured.response.Response;

public class OutcomeStepDefs {

    @When("^Resolve action item (.*) and get outcome (.*)$")
    public void resolveActionItem(String actionItemTag, String outcomeTag) {
        Outcome actionItemData = FeatureRegistry.getCurrentFeature().getData(
                Outcome.class,
                actionItemTag);

        Response createOutcomeResponse = OutcomesUtil.createOutcome(
                actionItemData.getId(),
                OutcomeConstants.getDefaultOutcomeDataWithType(StandardOutcomeTypes.RESOLVED)
        );

        FeatureRegistry.getCurrentFeature().setData(
                Outcome.class,
                outcomeTag,
                createOutcomeResponse.as(Outcome.class)
        );
    }
}
