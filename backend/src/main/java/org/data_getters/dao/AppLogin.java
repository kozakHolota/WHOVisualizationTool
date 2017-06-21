package org.data_getters.dao;

import org.data_getters.model.LoginStatus;
import org.data_getters.model.UserAuthentication;
import org.data_getters.model.UserPasswordAuthentication;
import org.data_getters.row_mapper.LoginPasswordMapper;
import org.data_source.JdbcTemplateGetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

/**
 * Created by chmel on 25.02.17.
 */
public class AppLogin implements LoginDao {
    private static JdbcTemplate jdbcTemplate;
    private static String SQL;

    static {
        jdbcTemplate = JdbcTemplateGetter.getAccountDbJdbcTemplate();
        SQL = "SELECT username, password, is_admin FROM user WHERE username=?";
    }

    private String userName;
    private String password;
    private boolean isAdmin;
    private UserAuthentication expectedUser;
    private UserAuthentication actualUser;

    public AppLogin(String userName, String password) {
        this.userName = userName;
        this.password = this.getEncryptedPassword(password);
        actualUser = this.getUser(this.userName);
        expectedUser = this.getUser(this.userName, this.password);
    }

    private String getEncryptedPassword(String passwd) {
        String sql = "SELECT PASSWORD(?)";
        return jdbcTemplate.queryForObject(sql, String.class, passwd);
    }

    @Override
    public UserAuthentication getUser(String userName) {
        UserAuthentication ua = jdbcTemplate.queryForObject(SQL, new Object[]{userName}, new LoginPasswordMapper());
        this.isAdmin = ua.isAdmin();
        return ua;
    }

    @Override
    public UserAuthentication getUser(String userName, String authToken) {
        return new UserPasswordAuthentication(userName, authToken, this.isAdmin);
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public HashMap<String, String> checkLogin() {
        SecureRandom random = new SecureRandom();
        String hashCode = new BigInteger(255, random).toString(32);
        String verificationSQL = "SELECT COUNT(*) FROM hash_table ht \n" +
                "JOIN user u ON u.id = ht.user_id \n" +
                "WHERE u.username=?";
        String SQL = "";
        HashMap<String, String> status = new HashMap<>();
        if (expectedUser.equals(actualUser)) {
            int res = jdbcTemplate.queryForObject(verificationSQL, Integer.class, this.userName);
            Integer userId = this.getUserId(this.userName);
            if (res == 0 ) {
                SQL = "INSERT INTO hash_table (user_id, cred_hash, due_date) VALUES (?, ?, DATE_ADD(NOW(), INTERVAL 15 MINUTE))";
                jdbcTemplate.update(SQL, userId, hashCode);
            } else {
                SQL = "UPDATE hash_table SET cred_hash=?, due_date=DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE user_id=?";
                jdbcTemplate.update(SQL, hashCode, userId);
            }
            status.put("status", LoginStatus.SUCCESS.toString());
            status.put("hash_code", hashCode);
        } else {
            status.put("status", LoginStatus.FAILURE.toString());
            status.put("hash_code", "");
        }

        return status;

    }
}
