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
import org.storage_puller.insert_data_models.DeathsAttributableToTheEnvironmentDataModel;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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

    @Override
    public void insert(List<DataModel> dms) throws IOException, ParseException {
        List<DeathsAttributableToTheEnvironmentDataModel> dataModels = new ArrayList<>();
        dms.forEach(model->dataModels.add((DeathsAttributableToTheEnvironmentDataModel) model));
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        JdbcTemplate jdbcTemplateObj = this.jdbcTemplateObject();
        String statisticInfoSQL = "INSERT IGNORE INTO statistics (table_name, statistic_summary) VALUES ('death_by_environment_statistics', ?)";
        Function<String, String> locSQL = (String loc) -> "INSERT IGNORE INTO ui_local_storage." + loc + "_statistics (statistic_id, value) VALUES ((SELECT id FROM statistical_storage.statistics WHERE table_name = 'death_by_environment_statistics'), ?)";

        String insertSQL = "INSERT IGNORE INTO tobacco_usage_statistic (country_id, measurenment, year_id) VALUES (?, ?, ?)";

            try {
                jdbcTemplateObj.update(statisticInfoSQL, dataModels.get(0).getMeasurementUnit());

                for (Languages l : getLocLanguages()) {
                    log.info("Translating into " + l.getCode());
                    jdbcTemplateObj.update(locSQL.apply(l.getCode()), l.translate(dataModels.get(0).getMeasurementUnit()));
                }

                jdbcTemplateObj.batchUpdate(insertSQL, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        DeathsAttributableToTheEnvironmentDataModel dataModel = dataModels.get(i);
                        try {
                            ps.setInt(1, dbHelper.getCountryId(dataModel.getCountry(), dataModel.getRegion()));
                        } catch (IOException | ParseException e) {
                            e.printStackTrace();
                        }
                        ps.setDouble(2, dataModel.getMeasurenment());
                        ps.setInt(3, dbHelper.getYearId(dataModel.getYear()));
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
