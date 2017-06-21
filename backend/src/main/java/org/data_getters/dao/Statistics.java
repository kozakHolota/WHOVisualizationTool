package org.data_getters.dao;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by chmel on 26.02.17.
 */
public enum Statistics {
    LIFE_EXPECTANCY("life_expectancy", LifeExpectancyDao.class),
    DEATH_ATTRIBUTABLE_TO_THE_ENVIRONMENT("death_attributable_to_the_environment", DeathsAttributableToTheEnvironmentDao.class),
    TOBACCO_USAGE_BY_COUNTRY("tobacco_usage", TobaccoUseDataByCountryDao.class);

    private String name;
    private Class<StatisticGetterDao> statistic;

    private Statistics(String name, Class statistic) {
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
