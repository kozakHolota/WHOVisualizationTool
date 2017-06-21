package org.data_readers;

import java.util.Hashtable;

/**
 * Created by chmel on 26.12.16.
 */
public interface CSVReader {
    public default String getDelimiter() {
        return ",";
    }

    public Hashtable<String, String> getData();
}
