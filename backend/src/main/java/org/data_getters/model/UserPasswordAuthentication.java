package org.data_getters.model;

/**
 * Created by chmel on 26.02.17.
 */
public class UserPasswordAuthentication implements UserAuthentication {
    private String username;
    private String password;
    private boolean isAdmin;

    public UserPasswordAuthentication() {
    }

    public UserPasswordAuthentication(String username, String password, boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    @Override
    public String getUserName() {
        return this.username;
    }

    @Override
    public void setUserName(String userName) {
        this.username = userName;
    }

    @Override
    public boolean isAdmin() {
        return this.isAdmin;
    }

    public String getPassword() { return this.password; }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setAdminState(boolean state) {
        this.isAdmin = state;
    }

    @Override
    public boolean equals(Object s) {
        UserPasswordAuthentication r = (UserPasswordAuthentication) s;
        if (s == null) return false;
        return (
                (this.username).equals(r.username)
                        &&
                        (this.password).equals(r.password)
                        &&
                        (this.isAdmin == r.isAdmin));
    }
}
