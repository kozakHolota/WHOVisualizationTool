package org.data_getters.dao;

import org.data_source.JdbcTemplateGetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class NecessaryDataHelper {
    private static JdbcTemplate jdbcTemplate;

    static {
        jdbcTemplate = JdbcTemplateGetter.getJdbcTemplate();
    }

    public static List<Integer> getYears() {
        String SQL = "SELECT year FROM year";
        return jdbcTemplate.queryForList(SQL, Integer.class);
    }

    public static List<String> getSexes(){
        String SQL = "SELECT sex FROM sex";
        return jdbcTemplate.queryForList(SQL, String.class);
    }

    public static List<String> getRegions(){
        String SQL = "SELECT region_code FROM region";
        return jdbcTemplate.queryForList(SQL, String.class);
    }

    public static List<String> getCountries(String region) {
        String SQL = "";
        if (region.isEmpty()) {
            SQL = "SELECT country_code FROM country";
        } else {
            SQL = "SELECT country_code FROM country c RIGHT JOIN region r ON r.id = c.region_id WHERE r.region_code='" + region + "'";
        }

        return jdbcTemplate.queryForList(SQL, String.class);
    }
}
