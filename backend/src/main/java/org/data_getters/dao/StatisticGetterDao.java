package org.data_getters.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chmel on 26.02.17.
 */
public interface StatisticGetterDao {
    StatisticGetterDao addClause(StatisticClauseFields scf, Object value);

    StatisticGetterDao clearClauses();

    List<StatisticClauseFields> getFields();

    Map<StatisticClauseFields, Object> getClauseList();

    HashMap<Integer, Double> getMetrics();
}
