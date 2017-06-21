package org.storage_puller.insert_data_models;

import java.util.Hashtable;

/**
 * Created by chmel on 22.01.17.
 */
public class TobaccoUsageByCountryUsageModel implements DataModel {
    private String country;
    private String region;
    private String measurementUnit;
    private Integer year;
    private Double measurenment;

    public TobaccoUsageByCountryUsageModel(Hashtable<String, String> dataRow) {
        this(dataRow.get("Country"), dataRow.get("Region"), dataRow.get("Measurement Unit"), Integer.parseInt(dataRow.get("Year").trim()), DataModel.getValue(dataRow.get("Value")));
    }

    private TobaccoUsageByCountryUsageModel(String country, String region, String measurementUnit, Integer year, Double measurement) {
        this.country = country;
        this.region = region;
        this.measurementUnit = measurementUnit;
        this.year = year;
        this.measurenment = measurement;
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
