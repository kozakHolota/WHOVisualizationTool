package org.data_getters.model.test;

import org.data_getters.model.UserPasswordAuthentication;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Step;

/**
 * Created by chmel on 18.03.17.
 */
public class TestUserPasswordAuthentication {
    private final String adminCreds = "test";
    private final String userCreds = "test1";
    private final UserPasswordAuthentication upa_admin  = new UserPasswordAuthentication(adminCreds, adminCreds, true);
    private final UserPasswordAuthentication upa_user = new UserPasswordAuthentication(userCreds, userCreds, false);

    @Test
    @Step("Verify that UserPasswordAuthentication class instance is created as expected for non-admin user")
    public void testCredsGetUser(){
        Assert.assertEquals(upa_user.getUserName(), userCreds, "Incorrect username are got from the model. Is: " + upa_user.getUserName() + ". Should be: " + userCreds);
        Assert.assertEquals(upa_user.getPassword(), userCreds, "Incorrect password are got from the model. Is: " + upa_user.getPassword() + ". Should be: " + userCreds);
        Assert.assertFalse(upa_user.isAdmin(), "Not admin user is represented as admin. Need to fix the model");
    }

    @Test
    @Step("Verify that UserPasswordAuthentication class instance is created as expected for admin user")
    public void testCredsGetAdmin(){
        Assert.assertEquals(upa_admin.getUserName(), adminCreds, "Incorrect username are got from the model. Is: " + upa_admin.getUserName() + ". Should be: " + adminCreds);
        Assert.assertEquals(upa_admin.getPassword(), adminCreds, "Incorrect password are got from the model. Is: " + upa_admin.getPassword() + ". Should be: " + adminCreds);
        Assert.assertTrue(upa_admin.isAdmin(), "Admin user is represented as non-admin. Need to fix the model");
    }

    @Test
    @Step("Verify ability to set values in the user model")
    public void testSetValues(){
        String changedCreds = upa_user + "==";
        upa_user.setUserName(changedCreds);
        Assert.assertEquals(upa_user.getUserName(), changedCreds, "Incorrect username are got from the model. Is: " + upa_user.getUserName() + ". Should be: " + changedCreds);
        upa_user.setPassword(changedCreds);
        Assert.assertEquals(upa_user.getPassword(), changedCreds, "Incorrect password are got from the model. Is: " + upa_user.getPassword() + ". Should be: " + changedCreds);
        upa_user.setAdminState(true);
        Assert.assertTrue(upa_user.isAdmin(), "Admin user is represented as non-admin. Need to fix the model");
    }
}
