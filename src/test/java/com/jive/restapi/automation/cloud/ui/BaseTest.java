package com.jive.restapi.automation.cloud.ui;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.concurrent.ExecutionException;

public class BaseTest {
    private SuiteContext currentContext;

    @Getter
    @AllArgsConstructor
    public static class SuiteContext {
        private final String name;

        public void addTest(Test test) {

        }
    }

    interface Suite {
        void execute(SuiteContext context);
    }

    public static class TestContext {

    }

    interface Test {
        void execute(TestContext context);
    }


    public void describe(String name, Runnable suite) throws ExecutionException, InterruptedException {
        currentContext = new SuiteContext(name);
        suite.run();
    }

    public void it(String name, Runnable test) {
        //currentContext.addTest(test);
    }
}
