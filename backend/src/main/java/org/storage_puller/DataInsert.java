package org.storage_puller;

import org.data_source.JdbcTemplateGetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.storage_puller.insert_data_models.DataModel;

/**
 * Created by chmel on 31.12.16.
 */
public interface DataInsert {
    public default PlatformTransactionManager getTransactionManager() {
        return JdbcTemplateGetter.getTransactionManager();
    }

    public default JdbcTemplate jdbcTemplateObject() {
        return JdbcTemplateGetter.getJdbcTemplate();
    }

    public void insert(DataModel dm);
}
