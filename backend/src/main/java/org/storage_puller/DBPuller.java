package org.storage_puller;

import org.apache.log4j.Logger;
import org.json.simple.parser.ParseException;
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
        switch (arg) {
            case "death_by_environment_statistics":
                return new DeathsAttributableToTheEnvironmentDataInsert();
            case "life_statistic":
                return new LifeExpectancyInserter();
            case "tobacco_usage_statistic":
                return new TobaccoUsageByCountryDataInserter();
        }
        return null;
    }

    public DataModel getDataModel(String arg, Hashtable<String, String> data) {
        switch (arg) {
            case "death_by_environment_statistics":
                return new DeathsAttributableToTheEnvironmentDataModel(data);
            case "life_statistic":
                return new LifeExpectancyModel(data);
            case "tobacco_usage_statistic":
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
