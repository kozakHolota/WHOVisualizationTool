package org.data_getters.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.data_getters.model.LoginStatus;
import org.data_getters.model.TokenFailureReason;
import org.data_getters.model.UserTokenAuthentication;
import org.data_getters.row_mapper.UserTokenMapper;
import org.data_source.JdbcTemplateGetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Timestamp;
import java.util.HashMap;

/**
 * Created by chmel on 10.03.17.
 */
public class TokenAuthDao implements LoginDao {

    private static JdbcTemplate jdbcTemplate;
    private static String SQL;

    static {
        jdbcTemplate = JdbcTemplateGetter.getAccountDbJdbcTemplate();
        SQL = "SELECT u.username, u.is_admin, ht.cred_hash, ht.due_date FROM user u \n" +
                "JOIN hash_table ht ON u.id=ht.user_id \n" +
                "WHERE u.username = ?";
    }

    private Log log = LogFactory.getLog(TokenAuthDao.class);

    private String userName;
    private String token;
    private Timestamp dueDate;
    private boolean isAdmin;
    private UserTokenAuthentication expectedUser;
    private UserTokenAuthentication actualUser;

    public TokenAuthDao(String userName, String token) {
        this.userName = userName;
        this.token = token;
        this.expectedUser = this.getUser(this.userName);
        this.actualUser = this.getUser(this.userName, this.token);
    }

    @Override
    public UserTokenAuthentication getUser(String userName) {
        UserTokenAuthentication user = jdbcTemplate.queryForObject(SQL, new Object[]{userName}, new UserTokenMapper());
        this.isAdmin = user.isAdmin();
        this.dueDate = user.getDueDate();
        return user;
    }

    @Override
    public UserTokenAuthentication getUser(String userName, String authToken) {
        UserTokenAuthentication newUser = new UserTokenAuthentication();
        newUser.setUserName(userName);
        newUser.setToken(authToken);
        newUser.setDueDate(this.dueDate);
        newUser.setAdmin(this.isAdmin);
        return newUser;
    }

    @Override
    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public Timestamp getDueDate() {
        return dueDate;
    }

    public void setDueDate(Timestamp dueDate) {
        this.dueDate = dueDate;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public HashMap<String, String> checkLogin(){
        HashMap<String, String> status = new HashMap<>();
        Integer userId = this.getUserId(this.userName);
        if (!this.actualUser.getUserName().equals(this.expectedUser.getUserName())){
            status.put("status", LoginStatus.FAILURE.toString());
            status.put("reason", TokenFailureReason.NO_SUCH_USER.toString());
        }
        else if (!this.actualUser.getToken().equals(this.expectedUser.getToken())){
            status.put("status", LoginStatus.FAILURE.toString());
            status.put("reason", TokenFailureReason.INVALID_TOKEN.toString());
        }
        else if (System.currentTimeMillis() > this.dueDate.getTime()){
            status.put("status", LoginStatus.FAILURE.toString());
            status.put("reason", TokenFailureReason.EXPIRED_SESSION.toString());
        } else {
            this.log.debug("Updating user " + this.userName + " with id " + userId);
            String sessionUpdateSQL = "UPDATE hash_table SET due_date=DATE_ADD(NOW(), INTERVAL 15 MINUTE) WHERE user_id=?";
            jdbcTemplate.update(sessionUpdateSQL, userId);
            status.put("status", LoginStatus.SUCCESS.toString());
            status.put("reason", TokenFailureReason.SUCCESS.toString());
        }
        return status;
    }

    public HashMap<String, String> checkAdminAccess(){
        HashMap<String, String> status = this.checkLogin();
        if (!this.isAdmin()) {
            status.replace("status", LoginStatus.FAILURE.toString());
            status.replace("reason", TokenFailureReason.ACCESS_FORBIDDEN.toString());
        }
        return status;
    }

    public void clearHash()  {
        SQL = "DELETE FROM hash_table WHERE user_id = " + this.getUserId(this.userName);
        jdbcTemplate.update(SQL);
    }
}
