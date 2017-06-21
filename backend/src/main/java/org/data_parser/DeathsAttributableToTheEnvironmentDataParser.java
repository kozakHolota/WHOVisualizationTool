package org.data_parser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * Created by chmel on 25.12.16.
 */
public class DeathsAttributableToTheEnvironmentDataParser implements DataParser {
    private String data;

    public DeathsAttributableToTheEnvironmentDataParser(String data) {
        this.data = data;
    }

    ;

    @Override
    public Hashtable<String, String> getDataTemplate() {
        return DataParser.super.getDataTemplate();
    }

    @Override
    public Hashtable<String, String> getDataRow(Object ob) {
        Hashtable<String, String> dataTemplate = (Hashtable<String, String>) this.getDataTemplate().clone();
        JSONObject obj = (JSONObject) ob;
        JSONObject dimensions = (JSONObject) obj.get("dim");
        String measurement = (String) obj.get("Value");
        String region = (String) dimensions.get("REGION");
        String country = (String) dimensions.get("COUNTRY");
        String measurementUnit = (String) dimensions.get("GHO");
        String year = (String) dimensions.get("YEAR");
        dataTemplate.replace("Value", measurement);
        dataTemplate.replace("Measurement Unit", measurementUnit);
        dataTemplate.replace("Country", country);
        dataTemplate.replace("Region", region);
        dataTemplate.replace("Year", year);
        return dataTemplate;
    }


    @Override
    public ArrayList<Hashtable<String, String>> receiveStatistic() throws ParseException {
        ArrayList<Hashtable<String, String>> statistics = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(this.data);
        JSONArray result = (JSONArray) obj.get("fact");
        result.forEach(row -> statistics.add(this.getDataRow((Object) row)));
        return statistics;
    }
}
