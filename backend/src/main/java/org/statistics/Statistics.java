package org.statistics;

import org.http.data_getter.requester.DataRequester;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Objects;

public enum Statistics {
    DEATH_BY_ENVIRONMENT_STATISTICS("death_by_environment_statistics"),
    LIFE_STATISTIC("life_statistic"),
    TOBACCO_USAGE_STATISTIC("tobacco_usage_statistic");

    private String code;

    Statistics(String code) {
        this.code = code;
    }

    public static Statistics getInstance(String code) {
        Statistics stat = null;
        for (Statistics c :
                Statistics.values()) {
            if (Objects.equals(c.code, code)) {
                stat = c;
            }
        }
        return stat;
    }

    public static DataRequester getStatRequester(String statCode) {
        ApplicationContext dataGettersContext = new ClassPathXmlApplicationContext("beans/DataGettersBeans.xml");
        switch (getInstance(statCode)) {
            case LIFE_STATISTIC:
                return (DataRequester) dataGettersContext.getBean("lifeExpectancyDataRequester");
            case TOBACCO_USAGE_STATISTIC:
                return (DataRequester) dataGettersContext.getBean("tobaccoUseDataByCountryRequester");
            case DEATH_BY_ENVIRONMENT_STATISTICS:
                return (DataRequester) dataGettersContext.getBean("deathByEnvironmentRequester");
            default:
                return null;
        }
    }

    @Override
    public String toString() {
        return this.code;
    }
}
