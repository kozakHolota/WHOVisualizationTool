package org.data_getters.model.test;

import org.data_getters.model.UserTokenAuthentication;
import org.testng.Assert;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Step;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Created by chmel on 19.03.17.
 */
public class TestUserTokenAuthentication {
    private final String adminCreds = "test";
    private final String userCreds = "test1";
    private final String token = "123";
    private final Timestamp tst = Timestamp.valueOf(LocalDateTime.now());
    private final UserTokenAuthentication userAdmin = new UserTokenAuthentication(adminCreds, token, true, tst);
    private final UserTokenAuthentication userSimple = new UserTokenAuthentication(userCreds, token, false, tst);

    @Test
    @Step("Vefrify that Token Autenthication user instance is created properly")
    public void TestTokenCreation(){
        Assert.assertEquals(userAdmin.getUserName(), adminCreds, "Not correct username. Fix the model");
        Assert.assertEquals(userAdmin.getToken(), token, "Incorrect token in the model. Fix the model");
    }
}
