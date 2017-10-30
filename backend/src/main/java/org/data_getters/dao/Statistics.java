package org.data_getters.dao;

import java.lang.reflect.InvocationTargetException;

import static org.statistics.Statistics.*;

/**
 * Created by chmel on 26.02.17.
 */
public enum Statistics {
    LIFE_EXPECTANCY(LIFE_STATISTIC.toString(), LifeExpectancyDao.class),
    DEATH_ATTRIBUTABLE_TO_THE_ENVIRONMENT(DEATH_BY_ENVIRONMENT_STATISTICS.toString(), DeathsAttributableToTheEnvironmentDao.class),
    TOBACCO_USAGE_BY_COUNTRY(TOBACCO_USAGE_STATISTIC.toString(), TobaccoUseDataByCountryDao.class);

    private String name;
    private Class<StatisticGetterDao> statistic;

    Statistics(String name, Class statistic) {
        this.name = name;
        this.statistic = statistic;
    }

    public static StatisticGetterDao getInstance(String name) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        if (name.equals(Statistics.LIFE_EXPECTANCY.toString())) {
            return Statistics.LIFE_EXPECTANCY.statistic.getConstructor().newInstance();
        } else if (name.equals(Statistics.DEATH_ATTRIBUTABLE_TO_THE_ENVIRONMENT.toString())) {
            return Statistics.DEATH_ATTRIBUTABLE_TO_THE_ENVIRONMENT.statistic.getConstructor().newInstance();
        } else if (name.equals(Statistics.TOBACCO_USAGE_BY_COUNTRY.toString())) {
            return Statistics.TOBACCO_USAGE_BY_COUNTRY.statistic.getConstructor().newInstance();
        } else {
            throw new RuntimeException("Cannot get this statistic");
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}
