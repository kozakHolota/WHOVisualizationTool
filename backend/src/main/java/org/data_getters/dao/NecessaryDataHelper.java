package org.data_getters.dao;

import org.data_source.JdbcTemplateGetter;
import org.localization.Languages;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NecessaryDataHelper {
    private static JdbcTemplate jdbcTemplate;

    static {
        jdbcTemplate = JdbcTemplateGetter.getJdbcTemplate();
    }

    public static List<Integer> getYears() {
        String SQL = "SELECT year FROM year";
        return jdbcTemplate.queryForList(SQL, Integer.class);
    }

    public static List<Map<String, String>> getSexes(Languages lang) {
        String SQL = "SELECT s.sex AS sex, ls.value AS sex_localized_value FROM statistical_storage.sex s JOIN ui_local_storage." + lang.getCode() + "_sex ls ON s.id = ls.sex_id";
        List<Map<String, Object>> s = jdbcTemplate.queryForList(SQL);
        List<Map<String, String>> y = new ArrayList<>();
        s.forEach(a -> {
            Map<String, String> j = new HashMap<>();
            a.forEach((key, value) -> j.put(key, value.toString()));
            y.add(j);
        });
        return y;
    }

    public static List<Map<String, String>> getRegions(Languages lang) {
        String SQL = "SELECT r.region_code AS region, lr.value AS region_localized_value FROM statistical_storage.region r JOIN ui_local_storage." + lang.getCode() + "_region lr ON r.id = lr.region_id ";
        List<Map<String, Object>> s = jdbcTemplate.queryForList(SQL);
        List<Map<String, String>> y = new ArrayList<>();
        s.forEach(a -> {
            Map<String, String> j = new HashMap<>();
            a.forEach((key, value) -> j.put(key, value.toString()));
            y.add(j);
        });
        return y;
    }

    public static List<Map<String, String>> getCountries(Languages lang, String region) {
        String SQL = "";
        if (region.isEmpty()) {
            SQL = "SELECT c.country_code AS country, lc.value AS country_localized_value FROM statistical_storage.country c JOIN ui_local_storage." + lang.getCode() + "_country lc ON c.id = lc.country_id  ORDER BY lc.value";
        } else {
            SQL = "SELECT c.country_code AS country, lc.value AS country_localized_value FROM statistical_storage.country c JOIN ui_local_storage." + lang.getCode() + "_country lc ON c.id = lc.country_id RIGHT JOIN statistical_storage.region r ON r.id = c.region_id WHERE r.region_code='" + region + "'  ORDER BY lc.value";
        }

        List<Map<String, Object>> s = jdbcTemplate.queryForList(SQL);
        List<Map<String, String>> y = new ArrayList<>();
        s.forEach(a -> {
            Map<String, String> j = new HashMap<>();
            a.forEach((key, value) -> j.put(key, value.toString()));
            y.add(j);
        });
        return y;
    }

    public static boolean containsSex(String tableName) {
        String SQL = "SELECT COUNT(*)  \n" +
                "FROM information_schema.COLUMNS \n" +
                "WHERE \n" +
                "    TABLE_SCHEMA = 'statistical_storage' \n" +
                "AND TABLE_NAME = '" +
                tableName + "' \n" +
                "AND COLUMN_NAME = 'sex_id'";
        int columns = jdbcTemplate.queryForObject(SQL, Integer.class);
        return columns > 0;

    }

    public static List<Map<String, String>> getStatistics(Languages lang) {
        String SQL = "SELECT DISTINCT s.table_name AS table_name, s.statistic_summary AS statistic_summary, sl.value AS localized_value FROM statistical_storage.statistics s JOIN ui_local_storage." + lang.getCode() + "_statistics sl ON s.id = sl.statistic_id";
        List<Map<String, Object>> s = jdbcTemplate.queryForList(SQL);
        List<Map<String, String>> y = new ArrayList<>();
        s.forEach(a -> {
            Map<String, String> j = new HashMap<>();
            a.forEach((key, value) -> j.put(key, value.toString()));
            y.add(j);
        });
        return y;
    }

    public static Map<String, String> getLoginPageLabels(Languages lang) {
        String SQL = "SELECT m.message AS message_key, lm.label AS label " +
                "FROM ui_local_storage." + lang.getCode() + "_messages lm " +
                "JOIN ui_local_storage.messages m ON m.id=lm.message_id " +
                "WHERE m.message LIKE 'MW_%'" +
                "OR m.message LIKE 'LP_%' " +
                "OR m.message LIKE 'FP_%'";
        List<Map<String, Object>> s = jdbcTemplate.queryForList(SQL);
        Map<String, String> y = new HashMap<>();
        s.forEach(item -> y.put(item.get("message_key").toString(), item.get("label").toString()));
        return y;
    }

    public static Map<String, String> getWorkSpaceLabels(Languages lang) {
        String SQL = "SELECT m.message AS message_key, lm.label AS label " +
                "FROM ui_local_storage." + lang.getCode() + "_messages lm " +
                "JOIN ui_local_storage.messages m ON m.id=lm.message_id " +
                "WHERE m.message LIKE 'MW_%'" +
                "OR m.message LIKE 'ws_%' " +
                "OR m.message LIKE 'FP_%'";
        List<Map<String, Object>> s = jdbcTemplate.queryForList(SQL);
        Map<String, String> y = new HashMap<>();
        s.forEach(item -> y.put(item.get("message_key").toString(), item.get("label").toString()));
        return y;
    }
}
