package org.data_getters.row_mapper;

import org.data_getters.model.UserPasswordAuthentication;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by chmel on 25.02.17.
 */
public class LoginPasswordMapper implements RowMapper<UserPasswordAuthentication> {
    @Override
    public UserPasswordAuthentication mapRow(ResultSet resultSet, int i) throws SQLException {
        UserPasswordAuthentication upa = new UserPasswordAuthentication();
        upa.setUserName(resultSet.getString("username"));
        upa.setPassword(resultSet.getString("password"));
        if (resultSet.getInt("is_admin") == 0) {
            upa.setAdminState(true);
        }
        return upa;
    }
}
