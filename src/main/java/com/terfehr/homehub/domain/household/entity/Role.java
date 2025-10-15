package com.terfehr.homehub.domain.household.entity;

import lombok.Getter;

@Getter
public enum Role {

    ROLE_USER("user"),
    ROLE_ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    /**
     * Determines if the given String represents a valid role.
     *
     * @param role The role to check.
     * @return true, if the role is valid. false otherwise.
     */
    public static boolean isValidRole(String role) {
        for (Role r : Role.values()) {
            if (r.getValue().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
