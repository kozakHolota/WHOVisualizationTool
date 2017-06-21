package org.data_getters.dao;

import org.data_getters.model.UserAuthentication;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by chmel on 25.02.17.
 */
public interface LoginDao {
    UserAuthentication getUser(String userName);

    UserAuthentication getUser(String userName, String authToken);

    JdbcTemplate getJdbcTemplate();

    default Integer getUserId(String userName){
        String SQL = "SELECT id FROM user WHERE username=?";
        return this.getJdbcTemplate().queryForObject(SQL, Integer.class, userName);
    }
}
