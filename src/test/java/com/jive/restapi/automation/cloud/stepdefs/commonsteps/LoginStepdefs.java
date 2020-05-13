package com.jive.restapi.automation.cloud.stepdefs.commonsteps;

import static com.xo.restapi.automation.configs.RestAssuredConfigUtils.setBaseUri;
import static org.apache.http.HttpStatus.SC_OK;

import com.aurea.automation.openapi.common.FeatureRegistry;
import com.jive.restapi.automation.cloud.configs.AppConfig;
import com.jive.restapi.automation.cloud.configs.Role;
import com.jive.restapi.automation.cloud.configs.User;
import com.jive.restapi.automation.utilities.PeopleUtils;
import com.jive.restapi.automation.utilities.v3.PersonUtil;
import com.jive.restapi.generated.person.models.Name;
import com.jive.restapi.generated.person.models.Person;
import com.xo.restapi.automation.actions.UserActionUtils;
import com.xo.restapi.automation.configs.BaseUrlUtil;
import com.xo.restapi.automation.context.UserContext;
import com.xo.restapi.automation.context.UserData;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import io.restassured.response.Response;
import java.util.concurrent.ThreadLocalRandom;
import lombok.Getter;
import org.apache.http.HttpStatus;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsEqual;

public class LoginStepdefs {

    private Response meResponse;

    public LoginStepdefs() {
    }

    public void executeAs(Role role, Runnable action) {
        com.xo.restapi.automation.types.User currentUser = UserContext.getUser();
        try {
            loggedIntoTheSystem(role.toString());
            action.run();
        } finally {
            // Restore previous user
            UserData userData = new UserData();
            userData.addUser(currentUser.getUsername(), currentUser.getPassword());
            UserContext.setUserData(userData);
            UserActionUtils.perform(currentUser.getUsername(), UserContext::getUser);
        }
    }

    public void executeAs(String userTag, Runnable action) {
        com.xo.restapi.automation.types.User currentUser = UserContext.getUser();
        try {
            switchToUser(userTag);
            action.run();
        } finally {
            // Restore previous user
            UserData userData = new UserData();
            userData.addUser(currentUser.getUsername(), currentUser.getPassword());
            UserContext.setUserData(userData);
            UserActionUtils.perform(currentUser.getUsername(), UserContext::getUser);
        }
    }

    @Given("^(.*) Log in to the system in (.*) environment$")
    public void accountManagerLogsInToSystem(String userRoleString, String environment) {
        setBaseUri(environment);
        userLogsIntoEnv(Role.valueOf(userRoleString), environment);
    }

    @Given("^(.*) logged into the system$")
    public void loggedIntoTheSystem(String userRole) {
        User userCredentials = AppConfig.getInstance().getRole(Role.valueOf(userRole));
        UserData userData = new UserData();
        userData.addUser(userCredentials.getUsername(), userCredentials.getPassword());
        UserContext.setUserData(userData);
        UserActionUtils.perform(userCredentials.getUsername(), UserContext::getUser);
    }

    @Given("^User ([a-zA-Z0-9]+) has been created")
    public void createUser(String userTag) {
        Person newPerson = PeopleUtils.newPerson();
        Response resp = PeopleUtils.createPerson(newPerson);
        resp.then().statusCode(HttpStatus.SC_CREATED);
        newPerson.setId(resp.as(Person.class).getId());
        newPerson.setThumbnailUrl(resp.as(Person.class).getThumbnailUrl());
        FeatureRegistry.getCurrentFeature().setData(Person.class, userTag, newPerson);
        UserData userData = new UserData();
        userData.addUser(newPerson.getJive().getUsername(), newPerson.getJive().getPassword());
    }

    @Given("^User ([a-zA-Z0-9]+) is logged in")
    @When("^User ([a-zA-Z0-9]+) logs in")
    public void switchToUser(String userTag) {
        Person user = FeatureRegistry.getCurrentFeature().getData(Person.class, userTag);
        UserData userData = new UserData();
        userData.addUser(user.getJive().getUsername(), user.getJive().getPassword());
        UserContext.setUserData(userData);
        UserActionUtils.perform(user.getJive().getUsername(), UserContext::getUser);

        // Check if context switched
        Person me = PersonUtil.getPerson("@me").as(Person.class);
        MatcherAssert.assertThat("Wrong logged in user?", me.getJive().getUsername().toLowerCase(), Matchers.is(user.getJive().getUsername().toLowerCase()));

    }

    private void userLogsIntoEnv(Role userRole, String environment) {
        User userCredentials = AppConfig.getInstance().getRole(userRole);
        UserData userData = new UserData();
        userData.addUser(userCredentials.getUsername(), userCredentials.getPassword(), environment);
        UserContext.setUserData(userData);
        UserActionUtils.perform(userCredentials.getUsername(), UserContext::getUser);
    }

    @When("Logged in user gets the status")
    public void loggedInUserGetsTheStatus() {
        meResponse = PeopleUtils.getMe();
    }

    @Then("Himself is returned")
    public void himselfIsReturned() {
        meResponse.then().statusCode(SC_OK).body("jive.username", IsEqual.equalTo(UserContext.getUser().getUsername().toLowerCase()));
    }

    @When("^Create new user by name (.*)")
    public void createUserByName(String username) {
        Person newPerson = PeopleUtils.newPerson();
        int userId = ThreadLocalRandom.current().nextInt(0, 2147483647);
        String givenName = username + userId;
        newPerson.setName(new Name().givenName(givenName).familyName(givenName));
        meResponse = PeopleUtils.createPerson(newPerson);
    }

    @Then("^User has been created")
    public void verifyCreatedUser() {
        meResponse.then().statusCode(HttpStatus.SC_CREATED);
    }

    @Given("^New user has been created by name ([a-zA-Z]+) ([a-zA-Z]+)")
    public void createDefaultUser(String firstName, String lastName) {
        Person newPerson = PeopleUtils.newPerson();
        newPerson.setName(new Name().givenName(firstName).familyName(lastName));
        Response resp = PeopleUtils.createPerson(newPerson);
        resp.then().statusCode(HttpStatus.SC_CREATED);
        FeatureRegistry.getCurrentFeature().setData(Person.class, firstName + lastName, resp.as(Person.class));
    }
}
