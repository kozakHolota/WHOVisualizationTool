package org.data_getters;

import com.google.common.collect.Iterables;
import org.apache.log4j.Logger;
import org.data_getters.dao.StatisticClauseFields;
import org.data_getters.dao.StatisticGetterDao;
import org.data_getters.dao.Statistics;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by chmel on 27.02.17.
 */
public class DataGetter {
    private final Logger log = Logger.getLogger(DataGetter.class);
    private StatisticGetterDao mainChart, secondChart;

    public DataGetter(String mainChartName, String secondChart) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        this.mainChart = Statistics.getInstance(mainChartName);
        this.secondChart = Statistics.getInstance(secondChart);
    }

    public DataGetter addClause(StatisticClauseFields scf, Object value) {
        if (!mainChart.getFields().contains(scf) && !secondChart.getFields().contains(scf)) {
            throw new RuntimeException("Clause " + scf.toString() + " is not in all metrics list");
        }
        mainChart.addClause(scf, value);
        secondChart.addClause(scf, value);

        return this;
    }

    private Double getCorrellationCoefficient(JSONArray mainMetrics, JSONArray secondMetrics) {
        double coefficient;
        int sz = mainMetrics.size();
        double x_value = 0.0;
        double y_value = 0.0;
        double xy = 0.0;
        double x2 = 0.0;
        double y2 = 0.0;

        for (int i = 0; i < sz; i++) {
            Double x = (Double) mainMetrics.get(i);
            Double y = (Double) secondMetrics.get(i);

            if (x == null && y == null) {
                this.log.error("First or second value is null");
                throw new NullPointerException();
            }
            x_value += x;
            y_value += y;
            xy += x * y;
            x2 += Math.pow(x, 2.0);
            y2 += Math.pow(y, 2.0);
        }

        coefficient = (sz * xy - x_value * y_value) / Math.sqrt((sz * x2 - 2.0 * x_value) * (sz * y2 - 2.0 * y_value));

        return coefficient;
    }

    private List<HashMap<Integer, ArrayList<Double>>> getNoramlizedData() {
        List<Integer> mainChartYears = new ArrayList<>();
        List<Integer> secondChartYears = new ArrayList<>();
        List<HashMap<Integer, ArrayList<Double>>> noramlizedData = new ArrayList<>();
        HashMap<Integer, Double> mainChartMetrics = new HashMap<>();
        HashMap<Integer, Double> secondChartMetrics = new HashMap<>();
        try {
            mainChartMetrics = this.mainChart.getMetrics();
            this.log.debug("Got main chart metrics: " + mainChartMetrics);
        } catch (NullPointerException npe) {
            if (this.mainChart == null) {
                log.error("Main Chart initialization returns null!");
            } else {
                log.error("Main Chart returns no metrics!");
            }

            throw new RuntimeException(npe.toString());
        }

        try {
            secondChartMetrics = this.secondChart.getMetrics();
            this.log.debug("Got second chart metrics: " + secondChartMetrics);
        } catch (NullPointerException npe) {
            if (this.secondChart == null) {
                log.error("Second Chart initialization returns null!");
            } else {
                log.error("Second Chart returns no metrics!");
            }

            throw new RuntimeException(npe.toString());
        }

        try {
            mainChartYears.addAll(mainChartMetrics.keySet());
            Collections.sort(mainChartYears);
        } catch (NullPointerException npe) {
            log.error("Main Chart years " + this.mainChart.getClass().getSimpleName() + " is empty");
        }

        try {
            secondChartYears.addAll(secondChartMetrics.keySet());
            Collections.sort(secondChartYears);
        } catch (NullPointerException npe) {
            log.error("Main Chart years " + this.secondChart.getClass().getSimpleName() + " is empty");
        }
        if (mainChartYears.isEmpty()) return noramlizedData;
        int firstYear = (mainChartYears.get(0) > secondChartYears.get(0)) ? mainChartYears.get(0) : secondChartYears.get(0);
        int lastYear = (Iterables.getLast(mainChartYears) > Iterables.getLast(secondChartYears)) ? Iterables.getLast(secondChartYears) : Iterables.getLast(mainChartYears);
        this.log.info("Searching data between " + firstYear + " and " + lastYear + " years");

        for (int i = firstYear; i <= lastYear; i++) {
            this.log.info("Getting year: " + i);
            HashMap<Integer, ArrayList<Double>> g = new HashMap<>();
            ArrayList<Double> n = new ArrayList<>();
            if (!mainChartMetrics.containsKey(i) || mainChartMetrics.get(i) == null) {
                for (int k = i - 1; k <= firstYear; k--) {
                    if (mainChartMetrics.get(k) != null) {
                        this.log.info("Getting data: " + mainChartMetrics.get(k));
                        n.add(mainChartMetrics.get(k));
                        break;
                    }
                }
                if (n.isEmpty()) n.add(0.0);
            } else {
                this.log.info("Getting data: " + mainChartMetrics.get(i));
                n.add(mainChartMetrics.get(i));
            }

            if (!secondChartMetrics.containsKey(i) || (secondChartMetrics.get(i) == null)) {
                for (int k = i - 1; k <= firstYear; k--) {
                    if (secondChartMetrics.get(k) != null) {
                        this.log.info("Getting data: " + secondChartMetrics.get(k));
                        n.add(secondChartMetrics.get(k));
                        break;
                    }
                }
                if (n.size() < 2) n.add(0.0);
            } else {
                n.add(secondChartMetrics.get(i));
            }

            g.put(i, n);
            noramlizedData.add(g);
        }

        return noramlizedData;
    }

    public JSONObject getJsonRepr() {
        JSONObject jo = new JSONObject();
        List<HashMap<Integer, ArrayList<Double>>> normalizedData = this.getNoramlizedData();
        JSONArray years = new JSONArray();
        JSONArray mainChart = new JSONArray();
        JSONArray secondChart = new JSONArray();
        normalizedData.forEach(v -> {
                    this.log.debug("Got normalized data: " + v);
                    Integer year = Integer.parseInt(v.keySet().toArray()[0].toString());
                    years.add(year);
                    mainChart.add(v.get(year).get(0));
                    secondChart.add(v.get(year).get(1));
                }
        );

        this.log.debug("X axis view: " + years);
        this.log.debug("Y1 axis view: " + mainChart);
        this.log.debug("Y2 axis view: " + secondChart);
        Double coefficient = this.getCorrellationCoefficient(mainChart, secondChart);
        this.log.debug("Correlation coefficient is " + coefficient);

        jo.put("years", years);
        jo.put("main_chart_points", mainChart);
        jo.put("second_chart_points", secondChart);
        jo.put("correlation_coefficient", coefficient);

        return jo;
    }
}