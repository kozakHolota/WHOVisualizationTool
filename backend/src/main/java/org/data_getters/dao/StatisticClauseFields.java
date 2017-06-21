package org.data_getters.dao;

/**
 * Created by chmel on 26.02.17.
 */
public enum StatisticClauseFields {
    YEAR("year"),
    COUNTRY("country_code"),
    REGION("region_code"),
    SEX("sex");

    private final String name;

    private StatisticClauseFields(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
