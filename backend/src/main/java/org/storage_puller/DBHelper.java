package org.storage_puller;

import org.json.simple.parser.ParseException;
import org.localization.Languages;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.io.IOException;
import java.util.EnumSet;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by chmel on 02.01.17.
 */
public class DBHelper {
    private final JdbcTemplate jdbcTemplateObject;
    private final Languages[] lngs;

    public DBHelper(JdbcTemplate jdbcTemplateObject) {

        this.jdbcTemplateObject = jdbcTemplateObject;
        this.lngs = Languages.values();
    }


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

    public Integer getRegionId(String regionName) throws IOException, ParseException {
        String selectQuery = "SELECT id FROM region WHERE region_code=?";
        String CreateRegionQuery = "INSERT INTO region (region_code) VALUES (?)";
        Function<String, String> translateRegion = (String loc) -> "INSERT INTO ui_local_storage." + loc + "_region (region_id, value) VALUES ((SELECT id FROM statistical_storage.region WHERE region_code = ?), ?)";
        Supplier<Integer> selectQueryResult = () -> (Integer) this.jdbcTemplateObject.queryForObject(selectQuery, Integer.class, regionName);

        Integer result = null;
        try {
            result = selectQueryResult.get();
        } catch (EmptyResultDataAccessException erde) {
            this.jdbcTemplateObject.update(CreateRegionQuery, regionName);
            for (Languages l : EnumSet.allOf(Languages.class)) {
                System.out.println("Language to translate: " + l);
                System.out.println("Region: " + l.translate(regionName));
                this.jdbcTemplateObject.update(translateRegion.apply(l.getCode()), regionName, l.translate(regionName));
            }
            result = selectQueryResult.get();
        }

        return result;
    }

    public Integer getCountryId(String countryName, String regionName) throws IOException, ParseException {
        String selectQuery = "SELECT id FROM country WHERE country_code =?";
        String CreateCountryQuery = "INSERT INTO country (country_code, region_id) VALUES (?, ?)";
        Supplier<Integer> selectQueryResult = () -> (Integer) this.jdbcTemplateObject.queryForObject(selectQuery, Integer.class, countryName);
        Integer regionId = this.getRegionId(regionName);
        Function<String, String> translateCountry = (String loc) -> "INSERT INTO ui_local_storage." + loc + "_country (country_id, value) VALUES ((SELECT id FROM statistical_storage.country WHERE country_code = ?), ?)";

        Integer result = null;
        try {
            result = selectQueryResult.get();
        } catch (EmptyResultDataAccessException erde) {
            this.jdbcTemplateObject.update(CreateCountryQuery, countryName, regionId);
            for (Languages l : EnumSet.allOf(Languages.class)) {
                System.out.println("Language to translate: " + l);
                System.out.println("Country: " + l.translate(countryName));
                this.jdbcTemplateObject.update(translateCountry.apply(l.getCode()), countryName, l.translate(countryName));
            }
            result = selectQueryResult.get();
        }

        return result;
    }

    public Integer getSexId(String sex) throws IOException, ParseException {
        String selectQuery = "SELECT id FROM sex WHERE sex =?";
        String CreateSexQuery = "INSERT INTO sex (sex) VALUES (?)";
        Supplier<Integer> selectQueryResult = () -> (Integer) this.jdbcTemplateObject.queryForObject(selectQuery, Integer.class, sex);
        Function<String, String> translateSex = (String loc) -> "INSERT INTO ui_local_storage." + loc + "_sex (sex_id, value) VALUES ((SELECT id FROM statistical_storage.sex WHERE sex = ?), ?)";

        Integer result = null;
        try {
            result = selectQueryResult.get();
        } catch (EmptyResultDataAccessException erde) {
            this.jdbcTemplateObject.update(CreateSexQuery, sex);
            for (Languages l : EnumSet.allOf(Languages.class)) {
                System.out.println("Language to translate: " + l);
                System.out.println("Sex: " + l.translate(sex));
                this.jdbcTemplateObject.update(translateSex.apply(l.getCode()), sex, l.translate(sex));
            }
            result = selectQueryResult.get();
        }

        return result;
    }
}
