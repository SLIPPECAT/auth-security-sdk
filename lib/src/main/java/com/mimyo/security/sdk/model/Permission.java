package com.mimyo.security.sdk.model;

public enum Permission {
    READ_OWN_PROFILE("read:own_profile"),
    READ_ANY_PROFILE("read:any_profile"),
    UPDATE_ANY_PROFILE("update:any_profile"),
    DELETE_ANY_PROFILE("delete:any_profile"),
    ALL("*");

    private final String name;

    Permission(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}