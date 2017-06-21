package org.storage_puller.insert_data_models;

import java.util.Hashtable;

/**
 * Created by chmel on 01.01.17.
 */
public class DeathsAttributableToTheEnvironmentDataModel implements DataModel {

    private String country;
    private String region;
    private String measurementUnit;
    private Integer year;
    private Double measurenment;

    public DeathsAttributableToTheEnvironmentDataModel(Hashtable<String, String> dataRow) {
        this(dataRow.get("Country"), dataRow.get("Region"), dataRow.get("Measurement Unit"), Integer.parseInt(dataRow.get("Year")), DataModel.getValue(dataRow.get("Value")));
    }

    private DeathsAttributableToTheEnvironmentDataModel(String country, String region, String measurementUnit, Integer year, Double measurenment) {
        this.country = country;
        this.region = region;
        this.measurementUnit = measurementUnit;
        this.year = year;
        this.measurenment = measurenment;
    }

    ;


    public String getCountry() {
        return country;
    }

    public String getRegion() {
        return region;
    }

    public String getMeasurementUnit() {
        return measurementUnit;
    }

    public Integer getYear() {
        return year;
    }

    public Double getMeasurenment() {
        return measurenment;
    }
}
