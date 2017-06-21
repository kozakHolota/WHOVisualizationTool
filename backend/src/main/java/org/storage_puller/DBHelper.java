package org.storage_puller;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.function.Supplier;

/**
 * Created by chmel on 02.01.17.
 */
public class DBHelper {
    private final JdbcTemplate jdbcTemplateObject;

    public DBHelper(JdbcTemplate jdbcTemplateObject) {
        this.jdbcTemplateObject = jdbcTemplateObject;
    }

    ;

    public Integer getYearId(Integer year) {
        final Integer effectiveYear = (year == null) ? 0 : year;
        String selectQuery = "SELECT id FROM year WHERE year=?";
        String CreateYearQuery = "INSERT INTO year (year) VALUES (?)";
        Supplier<Integer> selectQueryResult = () -> (Integer) this.jdbcTemplateObject.queryForObject(selectQuery, Integer.class, effectiveYear);

        Integer result = null;
        try {
            result = selectQueryResult.get();
        } catch (EmptyResultDataAccessException erde) {
            this.jdbcTemplateObject.update(CreateYearQuery, year);
            result = selectQueryResult.get();
        }

        return result;
    }

    ;

    public Integer getRegionId(String regionName) {
        String selectQuery = "SELECT id FROM region WHERE region_code=?";
        String CreateRegionQuery = "INSERT INTO region (region_code) VALUES (?)";
        Supplier<Integer> selectQueryResult = () -> (Integer) this.jdbcTemplateObject.queryForObject(selectQuery, Integer.class, regionName);

        Integer result = null;
        try {
            result = selectQueryResult.get();
        } catch (EmptyResultDataAccessException erde) {
            this.jdbcTemplateObject.update(CreateRegionQuery, regionName);
            result = selectQueryResult.get();
        }

        return result;
    }

    public Integer getCountryId(String countryName, String regionName) {
        String selectQuery = "SELECT id FROM country WHERE country_code =?";
        String CreateCountryQuery = "INSERT INTO country (country_code, region_id) VALUES (?, ?)";
        Supplier<Integer> selectQueryResult = () -> (Integer) this.jdbcTemplateObject.queryForObject(selectQuery, Integer.class, countryName);
        Integer regionId = this.getRegionId(regionName);

        Integer result = null;
        try {
            result = selectQueryResult.get();
        } catch (EmptyResultDataAccessException erde) {
            this.jdbcTemplateObject.update(CreateCountryQuery, countryName, regionId);
            result = selectQueryResult.get();
        }

        return result;
    }

    public Integer getSexId(String sex) {
        String selectQuery = "SELECT id FROM sex WHERE sex =?";
        String CreateSexQuery = "INSERT INTO sex (sex) VALUES (?)";
        Supplier<Integer> selectQueryResult = () -> (Integer) this.jdbcTemplateObject.queryForObject(selectQuery, Integer.class, sex);

        Integer result = null;
        try {
            result = selectQueryResult.get();
        } catch (EmptyResultDataAccessException erde) {
            this.jdbcTemplateObject.update(CreateSexQuery, sex);
            result = selectQueryResult.get();
        }

        return result;
    }
}
