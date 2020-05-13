package com.jive.restapi.automation.cloud.types;

import com.xo.restapi.automation.types.User;

public class UserImpl extends User {

    public UserImpl(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }
}
