package org.data_getters.model;

/**
 * Created by chmel on 26.02.17.
 */
public enum LoginStatus {
    SUCCESS("Success"),
    FAILURE("Failure"),
    LOGOUT("Logout");

    private String name;

    private LoginStatus(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
