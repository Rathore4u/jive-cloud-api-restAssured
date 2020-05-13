package com.jive.restapi.automation.cloud.configs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class ClientConfig {
    public Users users;

    User get(Role role) {
        return users.get(role);
    }

}
