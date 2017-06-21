package org.storage_puller.insert_data_models;

import java.util.Hashtable;

/**
 * Created by chmel on 10.01.17.
 */
public class LifeExpectancyModel implements DataModel {
    private String country;
    private String region;
    private String measurementUnit;
    private Integer year;
    private Double measurenment;
    private String sex;

    private LifeExpectancyModel(String country, String region, String measurementUnit, Integer year, Double measurenment, String sex) {
        this.country = country;
        this.region = region;
        this.measurementUnit = measurementUnit;
        this.year = year;
        this.measurenment = measurenment;
        this.sex = sex;
    }

    public LifeExpectancyModel(Hashtable<String, String> dataRow) {
        this(dataRow.get("Country"), dataRow.get("Region"), dataRow.get("Measurement Unit"), Integer.parseInt(dataRow.get("Year")), DataModel.getValue(dataRow.get("Value")), dataRow.get("Sex"));
    }

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

    public String getSex() {
        return sex;
    }
}
