package com.jive.restapi.automation.cloud.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class Users {
    private User ADMIN;
    private User MANAGER;

    User get(Role role) {
        switch (role) {
            case ADMIN:
                return ADMIN;
            case MANAGER:
                return MANAGER;
            default:
                return null;
        }
    }

}
