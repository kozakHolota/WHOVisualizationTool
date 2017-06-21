package org.data_getters.model;

/**
 * Created by chmel on 10.03.17.
 */
public enum TokenFailureReason {
    SUCCESS("successfull"),
    NO_SUCH_USER("no_such_user"),
    INVALID_TOKEN("invalid_token"),
    EXPIRED_SESSION("expired_session"),
    ACCESS_FORBIDDEN("access_forbidden");

    private String name;

    TokenFailureReason(String name) {
        this.name = name;
    }

    @Override
    public String toString(){
        return this.name;
    }
}
