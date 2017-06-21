package org.data_getters.row_mapper;

import org.data_getters.model.UserTokenAuthentication;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by chmel on 09.03.17.
 */
public class UserTokenMapper implements RowMapper<UserTokenAuthentication> {
    @Override
    public UserTokenAuthentication mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserTokenAuthentication uta = new UserTokenAuthentication();
        uta.setUserName(rs.getString("username"));
        uta.setToken(rs.getString("cred_hash"));
        uta.setDueDate(rs.getTimestamp("due_date"));
        uta.setAdmin(rs.getBoolean("is_admin"));
        return uta;
    }
}
