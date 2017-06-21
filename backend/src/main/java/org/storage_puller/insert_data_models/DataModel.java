package org.storage_puller.insert_data_models;

/**
 * Created by chmel on 31.12.16.
 */
public interface DataModel {
    public static Double getValue(String value) {
        Double res = 0.0;
        try {
            res = Double.parseDouble(value);
        } catch (NumberFormatException nfe) {
            return res;
        }
        return res;
    }
}
