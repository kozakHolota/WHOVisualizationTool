package org.data_getters.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created by chmel on 09.03.17.
 */
public class UserTokenAuthentication implements UserAuthentication {
    private String username;
    private String token;
    private boolean isAdmin;
    private Timestamp dueDate;

    public UserTokenAuthentication(){}

    public UserTokenAuthentication(String username, String token, boolean isAdmin, Timestamp dueDate) {
        this.username = username;
        this.token = token;
        this.isAdmin = isAdmin;
        this.dueDate = dueDate;
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public void setUserName(String userName) {
        this.username = userName;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }
}
