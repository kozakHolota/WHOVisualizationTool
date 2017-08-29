package org.storage_puller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.storage_puller.insert_data_models.DataModel;
import org.storage_puller.insert_data_models.LifeExpectancyModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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
    public void insert(List<DataModel> dms) {
        List<LifeExpectancyModel> dataModels = new ArrayList<>();
        dms.forEach(model->dataModels.add((LifeExpectancyModel) model));
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        JdbcTemplate jdbcTemplateObj = this.jdbcTemplateObject();
        String insertSQL = "INSERT IGNORE INTO life_statistic (country_id, measurenment, year_id, sex_id) VALUES (?, ?, ?, ?)";

            try {
                jdbcTemplateObj.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        LifeExpectancyModel dataModel = dataModels.get(i);
                        ps.setInt(1, dbHelper.getCountryId(dataModel.getCountry(), dataModel.getRegion()));
                        ps.setDouble(2, dataModel.getMeasurenment());
                        ps.setInt(3, dbHelper.getYearId(dataModel.getYear()));
                        ps.setInt(4, dbHelper.getSexId(dataModel.getSex()));
                    }

                    @Override
                    public int getBatchSize() {
                        return dataModels.size();
                    }
                });
                this.transactionManager.commit(status);
            } catch (DataAccessException e) {
                log.error("Error in creating record, rolling back");
                transactionManager.rollback(status);
                throw e;
            }
    }
}
