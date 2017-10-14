package org.storage_puller;

import org.data_source.JdbcTemplateGetter;
import org.json.simple.parser.ParseException;
import org.localization.Languages;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.storage_puller.insert_data_models.DataModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by chmel on 31.12.16.
 */
public interface DataInsert {
    default PlatformTransactionManager getTransactionManager() {
        return JdbcTemplateGetter.getTransactionManager();
    }

    default PlatformTransactionManager getUiTransactionManager() {
        return JdbcTemplateGetter.getUILocDbTransactionManager();
    }

    default JdbcTemplate jdbcTemplateObject() {
        return JdbcTemplateGetter.getJdbcTemplate();
    }

    /**
     * default JdbcTemplate uiJdbcTemplateObject() {
     * <p>
     * return JdbcTemplateGetter.getLocUIJdbcTemplate();
     * }
     **/

    default Languages[] getLocLanguages() {
        return Languages.values();
    }

    void insert(List<DataModel> dms) throws IOException, ParseException;
}
