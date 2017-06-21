package org.storage_puller;

import org.apache.log4j.Logger;
import org.storage_puller.insert_data_models.DataModel;
import org.storage_puller.insert_data_models.DeathsAttributableToTheEnvironmentDataModel;
import org.storage_puller.insert_data_models.LifeExpectancyModel;
import org.storage_puller.insert_data_models.TobaccoUsageByCountryUsageModel;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by chmel on 02.01.17.
 */
public class DBPuller {
    private final Logger log = Logger.getLogger(DBPuller.class);

    private DataInsert getDataInserter(String arg) {
        switch (arg) {
            case "death_by_environment":
                return new DeathsAttributableToTheEnvironmentDataInsert();
            case "life_expectancy":
                return new LifeExpectancyInserter();
            case "tobacco_usage":
                return new TobaccoUsageByCountryDataInserter();
        }
        return null;
    }

    ;

    public DataModel getDataModel(String arg, Hashtable<String, String> data) {
        switch (arg) {
            case "death_by_environment":
                return new DeathsAttributableToTheEnvironmentDataModel(data);
            case "life_expectancy":
                return new LifeExpectancyModel(data);
            case "tobacco_usage":
                return new TobaccoUsageByCountryUsageModel(data);
        }
        return null;
    }

    ;

    public void insert(String arg, ArrayList<Hashtable<String, String>> data) {
        DataInsert dataInserter = this.getDataInserter(arg);
        data.forEach((Hashtable<String, String> dat) -> {
            log.debug(dat);
            DataModel dm = this.getDataModel(arg, dat);
            try {
                dataInserter.insert(dm);
            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        });
    }
}
