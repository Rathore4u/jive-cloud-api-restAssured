package com.jive.restapi.automation.cloud.stepdefs.commonsteps;

import static com.jive.restapi.automation.cloud.configs.AppConfig.DEFAULT;
import static com.xo.restapi.automation.configs.RestAssuredConfigUtils.setBaseUri;

import com.jive.restapi.automation.cloud.configs.AppConfig;
import com.jive.restapi.automation.cloud.factories.XOUserFactory;
import com.xo.restapi.automation.context.UserContext;
import cucumber.api.java.Before;

public class Hooks {

    @Before
    public static void beforeScenario() {
        AppConfig.init(DEFAULT);
        UserContext.setFactory(new XOUserFactory());
        setBaseUri(DEFAULT);
    }

}