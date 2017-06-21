package org.data_getters.dao;

import org.data_source.JdbcTemplateGetter;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chmel on 26.02.17.
 */
public class TobaccoUseDataByCountryDao implements StatisticGetterDao {
    private static JdbcTemplate jdbcTemplate;

    static {
        jdbcTemplate = JdbcTemplateGetter.getJdbcTemplate();
    }

    private final ArrayList<StatisticClauseFields> fields = new ArrayList<>();
    private StringBuilder SQL = new StringBuilder();
    private HashMap<StatisticClauseFields, Object> clauseFields = new HashMap<>();

    public TobaccoUseDataByCountryDao() {
        this.fields.add(StatisticClauseFields.YEAR);
        this.fields.add(StatisticClauseFields.COUNTRY);
        this.fields.add(StatisticClauseFields.REGION);
        this.SQL.append("SELECT y.year, SUM(tus.measurenment) AS measurenment FROM tobacco_usage_statistic tus \n" +
                "JOIN year y ON tus.year_id = y.id \n" +
                "JOIN country c ON tus.country_id = c.id \n" +
                "JOIN region r ON c.region_id = r.id");
    }

    private String getCompleteSql() {
        if (!clauseFields.isEmpty()) this.SQL.append(" WHERE");
        clauseFields.forEach(
                (key, value) -> {
                    switch (key) {
                        case COUNTRY:
                        case REGION:
                        case SEX:
                            this.SQL.append(" ").append(key).append(" = '").append(value).append("' ");
                            break;
                        case YEAR:
                            List<Integer> years = (ArrayList<Integer>) value;
                            Integer from = years.get(0);
                            Integer to = years.get(0);
                            if (years.size() > 1) {
                                to = years.get(1);
                            }
                            this.SQL.append(" ").append(key).append(" BETWEEN ").append(from).append(" AND ").append(to).append(" ");
                            break;
                    }
                    this.SQL.append("AND");
                }
        );
        if (!clauseFields.isEmpty()) this.SQL.delete(this.SQL.length() - 3, this.SQL.length());
        this.SQL.append(" GROUP BY y.year");
        return this.SQL.toString();
    }

    @Override
    public TobaccoUseDataByCountryDao addClause(StatisticClauseFields scf, Object value) {
        this.clauseFields.put(scf, value);
        return this;
    }

    @Override
    public StatisticGetterDao clearClauses() {
        this.clauseFields.clear();
        return this;
    }

    @Override
    public List<StatisticClauseFields> getFields() {
        return this.fields;
    }

    @Override
    public Map<StatisticClauseFields, Object> getClauseList() {
        return this.clauseFields;
    }

    @Override
    public HashMap<Integer, Double> getMetrics() {
        HashMap<Integer, Double> res = new HashMap<>();
        List<Map<String, Object>> p = jdbcTemplate.queryForList(this.getCompleteSql());
        p.forEach(s -> {
            res.put(Integer.parseInt(s.get("year").toString()),
                    Double.parseDouble(s.get("measurenment").toString()));
        });
        return res;
    }
}
