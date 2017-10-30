package org.storage_puller;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
import org.statistics.Statistics;
import org.storage_puller.insert_data_models.DataModel;
import org.storage_puller.insert_data_models.DeathsAttributableToTheEnvironmentDataModel;
import org.storage_puller.insert_data_models.LifeExpectancyModel;
import org.storage_puller.insert_data_models.TobaccoUsageByCountryUsageModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by chmel on 02.01.17.
 */
public class DBPuller {
    private final Logger log = Logger.getLogger(DBPuller.class);

    private DataInsert getDataInserter(String arg) {
        Statistics s = Statistics.getInstance(arg);
        switch (s) {
            case DEATH_BY_ENVIRONMENT_STATISTICS:
                return new DeathsAttributableToTheEnvironmentDataInsert();
            case LIFE_STATISTIC:
                return new LifeExpectancyInserter();
            case TOBACCO_USAGE_STATISTIC:
                return new TobaccoUsageByCountryDataInserter();
        }
        return null;
    }

    public DataModel getDataModel(String arg, Hashtable<String, String> data) {
        Statistics s = Statistics.getInstance(arg);
        switch (s) {
            case DEATH_BY_ENVIRONMENT_STATISTICS:
                return new DeathsAttributableToTheEnvironmentDataModel(data);
            case LIFE_STATISTIC:
                return new LifeExpectancyModel(data);
            case TOBACCO_USAGE_STATISTIC:
                return new TobaccoUsageByCountryUsageModel(data);
        }
        return null;
    }

    public void insert(String arg, ArrayList<Hashtable<String, String>> data) throws IOException, ParseException {
        DataInsert dataInserter = this.getDataInserter(arg);
        ArrayList<DataModel> dms = new ArrayList<>();
        data.forEach((Hashtable<String, String> dat) -> {
            log.debug(dat);
            DataModel dm = this.getDataModel(arg, dat);
            try {
                //dataInserter.insert(dm);
                dms.add(dm);
            } catch (NullPointerException npe) {
                log.error("Empty Data Model :(");
                npe.printStackTrace();
            }
        });

        try {
            if (dataInserter != null) {
                dataInserter.insert(dms);
            } else {
                log.error("Nullable Data Insert Object for '" + arg + "'");
            }
        } catch (NullPointerException npe) {
            log.error("Empty Data Model :( Nothing to insert :(");
            npe.printStackTrace();
        }

    }
}
