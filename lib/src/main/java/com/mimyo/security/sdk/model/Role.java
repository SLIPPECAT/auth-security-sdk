package com.mimyo.security.sdk.model;

import java.util.Set;

public enum Role {
    USER(Set.of(
            Permission.READ_OWN_PROFILE
    )),
    ADMIN(Set.of(
            Permission.READ_OWN_PROFILE,
            Permission.READ_ANY_PROFILE,
            Permission.UPDATE_ANY_PROFILE,
            Permission.DELETE_ANY_PROFILE
    )),
    SYSTEM(Set.of(
            Permission.ALL
    ));

    private final Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }
}