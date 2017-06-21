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
import org.storage_puller.insert_data_models.LifeExpectancyModel;

import java.util.function.Supplier;

/**
 * Created by chmel on 10.01.17.
 */
public class LifeExpectancyInserter implements DataInsert {
    private final DBHelper dbHelper;
    private final PlatformTransactionManager transactionManager;
    private Log log = LogFactory.getLog(LifeExpectancyInserter.class);

    public LifeExpectancyInserter() {
        this.transactionManager = this.getTransactionManager();
        this.dbHelper = new DBHelper(this.jdbcTemplateObject());
    }

    @Override
    public JdbcTemplate jdbcTemplateObject() {
        return DataInsert.super.jdbcTemplateObject();
    }

    @Override
    public void insert(DataModel dm) {
        LifeExpectancyModel dataModel = (LifeExpectancyModel) dm;
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        JdbcTemplate jdbcTemplateObj = this.jdbcTemplateObject();
        Integer year_id = this.dbHelper.getYearId(dataModel.getYear());
        Integer country_id = this.dbHelper.getCountryId(dataModel.getCountry(), dataModel.getRegion());
        Integer sex_id = this.dbHelper.getSexId(dataModel.getSex());
        Supplier<Integer> getDataId = () -> (Integer) jdbcTemplateObj.queryForObject("SELECT id FROM life_statistic WHERE year_id=? AND country_id = ? AND measurenment=? AND sex_id=?", Integer.class, year_id, country_id, dataModel.getMeasurenment(), sex_id);
        String insertSQL = "INSERT INTO life_statistic (country_id, measurenment, year_id, sex_id) VALUES (?, ?, ?, ?)";
        try {
            getDataId.get();
        } catch (EmptyResultDataAccessException eae) {
            try {
                jdbcTemplateObj.update(insertSQL, country_id, dataModel.getMeasurenment(), year_id, sex_id);
            } catch (DataAccessException e) {
                log.error("Error in creating record, rolling back");
                transactionManager.rollback(status);
                throw e;
            }
        }
    }
}
