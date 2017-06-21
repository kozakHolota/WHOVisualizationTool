package org.data_getters.dao;

/**
 * Created by chmel on 26.02.17.
 */
public enum Sex {
    MALE("male"),
    FEMALE("female"),
    BOX_SEXES("Both sexes");

    private String name;

    private Sex(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
