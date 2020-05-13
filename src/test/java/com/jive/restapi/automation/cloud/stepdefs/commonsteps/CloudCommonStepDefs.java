package com.jive.restapi.automation.cloud.stepdefs.commonsteps;

import cucumber.api.java.en.When;
import java.util.concurrent.TimeUnit;

public class CloudCommonStepDefs {
    @When("User awaits for {int} {word}")
    public void userAwaitsForMilliseconds(int milliseconds, String unit) throws InterruptedException {
        Thread.sleep(TimeUnit.valueOf(unit.toUpperCase()).toMillis(milliseconds));
    }
}
