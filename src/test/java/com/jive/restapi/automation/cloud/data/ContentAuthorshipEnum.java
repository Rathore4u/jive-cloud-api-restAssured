package com.jive.restapi.automation.cloud.data;

import lombok.Getter;

public enum ContentAuthorshipEnum {
    OPEN("open"),
    AUTHOR("author"),
    LIMITED("limited");

    ContentAuthorshipEnum(String authorshipName) {
        this.name = authorshipName;
    }

    @Getter
    private final String name;
}
