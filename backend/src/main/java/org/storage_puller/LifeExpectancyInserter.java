package org.storage_puller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.simple.parser.ParseException;
import org.localization.Languages;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.storage_puller.insert_data_models.DataModel;
import org.storage_puller.insert_data_models.LifeExpectancyModel;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
    public void insert(List<DataModel> dms) throws IOException, ParseException {
        List<LifeExpectancyModel> dataModels = new ArrayList<>();
        dms.forEach(model->dataModels.add((LifeExpectancyModel) model));
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        JdbcTemplate jdbcTemplateObj = this.jdbcTemplateObject();
        Function<String, String> locSQL = (String loc) -> "INSERT IGNORE INTO ui_local_storage." + loc + "_statistics (statistic_id, value) VALUES ((SELECT id FROM statistical_storage.statistics WHERE table_name = 'life_statistic'), ?)";
        String statisticInfoSQL = "INSERT IGNORE INTO statistics (table_name, statistic_summary) VALUES ('life_statistic', ?)";
        String insertSQL = "INSERT IGNORE INTO life_statistic (country_id, measurenment, year_id, sex_id) VALUES (?, ?, ?, ?)";

            try {
                jdbcTemplateObj.update(statisticInfoSQL, dataModels.get(0).getMeasurementUnit());
                for (Languages l : getLocLanguages()) {
                    log.info("Translating into " + l.getCode());
                    jdbcTemplateObj.update(locSQL.apply(l.getCode()), l.translate(dataModels.get(0).getMeasurementUnit()));
                }
                jdbcTemplateObj.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        LifeExpectancyModel dataModel = dataModels.get(i);
                        try {
                            ps.setInt(1, dbHelper.getCountryId(dataModel.getCountry(), dataModel.getRegion()));
                        } catch (IOException | ParseException | NullPointerException e) {
                            log.error("Could not get Country or region");
                            e.printStackTrace();
                        }
                        ps.setDouble(2, dataModel.getMeasurenment());
                        ps.setInt(3, dbHelper.getYearId(dataModel.getYear()));
                        try {
                            ps.setInt(4, dbHelper.getSexId(dataModel.getSex()));
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public int getBatchSize() {
                        return dataModels.size();
                    }
                });
                this.transactionManager.commit(status);
            } catch (DataAccessException e) {
                log.error("Error in creating record, rolling back");
                this.transactionManager.rollback(status);
                throw e;
            }
    }
}
