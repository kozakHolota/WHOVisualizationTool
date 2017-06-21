package org.data_getters.dao;

/**
 * Created by chmel on 26.02.17.
 */
public enum Regions {
    EUROPE("Europe"),
    AMERICAS("Americas"),
    AFRICA("Africa"),
    SOUTH_EAST_ASIA("South-East Asia"),
    WESTERN_PACIFIC("Western Pacific"),
    EASTERN_MEDITERRANEAN("Eastern Mediterranean");

    private String name;

    private Regions(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
