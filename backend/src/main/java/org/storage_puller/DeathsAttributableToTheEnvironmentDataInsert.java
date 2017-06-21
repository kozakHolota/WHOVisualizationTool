package org.storage_puller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.storage_puller.insert_data_models.DataModel;
import org.storage_puller.insert_data_models.DeathsAttributableToTheEnvironmentDataModel;

import java.util.function.Supplier;

/**
 * Created by chmel on 01.01.17.
 */
public class DeathsAttributableToTheEnvironmentDataInsert implements DataInsert {
    private final DBHelper dbHelper;
    private final PlatformTransactionManager transactionManager;
    private Log log = LogFactory.getLog(DeathsAttributableToTheEnvironmentDataInsert.class);

    public DeathsAttributableToTheEnvironmentDataInsert() {
        this.transactionManager = this.getTransactionManager();
        this.dbHelper = new DBHelper(this.jdbcTemplateObject());

    }

    ;

    @Override
    public JdbcTemplate jdbcTemplateObject() {
        return DataInsert.super.jdbcTemplateObject();
    }

    @Override
    public void insert(DataModel dm) {
        DeathsAttributableToTheEnvironmentDataModel dataModel = (DeathsAttributableToTheEnvironmentDataModel) dm;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        JdbcTemplate jdbcTemplateObj = this.jdbcTemplateObject();
        Integer year_id = this.dbHelper.getYearId(dataModel.getYear());
        Integer country_id = this.dbHelper.getCountryId(dataModel.getCountry(), dataModel.getRegion());
        Supplier<Integer> getDataId = () -> (Integer) jdbcTemplateObj.queryForObject("SELECT id FROM tobacco_usage_statistic WHERE year_id=? AND country_id = ? AND measurenment=?", Integer.class, year_id, country_id, dataModel.getMeasurenment());
        String insertSQL = "INSERT INTO tobacco_usage_statistic (country_id, measurenment, year_id) VALUES (?, ?, ?)";
        try {
            getDataId.get();
        } catch (EmptyResultDataAccessException eae) {
            try {
                jdbcTemplateObj.update(insertSQL, country_id, dataModel.getMeasurenment(), year_id);
                this.transactionManager.commit(status);
            } catch (DataAccessException e) {
                log.error("Error in creating record, rolling back");
                transactionManager.rollback(status);
                throw e;
            }
        }


    }
}
