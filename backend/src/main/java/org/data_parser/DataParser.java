package org.data_parser;

import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by chmel on 25.12.16.
 */
public interface DataParser {
    public default Hashtable<String, String> getDataTemplate() {
        Hashtable<String, String> rest_t = new Hashtable<>();
        rest_t.put("Value", "");
        rest_t.put("Measurement Unit", "");
        rest_t.put("Country", "");
        rest_t.put("Region", "");
        rest_t.put("Year", "");
        return rest_t;
    }

    ;

    Hashtable<String, String> getDataRow(Object ob);

    public ArrayList<Hashtable<String, String>> receiveStatistic() throws ParseException;
}
