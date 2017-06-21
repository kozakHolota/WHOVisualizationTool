package org.data_source;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by chmel on 16.02.17.
 */
public class JdbcTemplateGetter {
    private static ApplicationContext context;
    private static JdbcTemplate jdbcTemplate;
    private static JdbcTemplate AccountJdbcTemplate;
    private static PlatformTransactionManager transtactionManager;
    private static PlatformTransactionManager accountDbTranstactionManager;

    static {
        context = new ClassPathXmlApplicationContext("beans/DataSource.xml");
    }

    private static DataSource getDataSource() {
        return (DataSource) context.getBean("dataSource");
    }

    private static DataSource getAccountDataSource() {
        return (DataSource) context.getBean("accountDbDataSource");
    }

    public static PlatformTransactionManager getTransactionManager() {
        if (transtactionManager == null) {
            transtactionManager = (PlatformTransactionManager) context.getBean("transactionManager");
        }
        return transtactionManager;
    }

    public static PlatformTransactionManager getAccountDbTransactionManager() {
        if (accountDbTranstactionManager == null) {
            accountDbTranstactionManager = (PlatformTransactionManager) context.getBean("accountDbTransactionManager");
        }
        return accountDbTranstactionManager;
    }

    public static JdbcTemplate getJdbcTemplate() {
        if (jdbcTemplate == null) {
            jdbcTemplate = new JdbcTemplate(getDataSource());
        }
        return jdbcTemplate;
    }

    public static JdbcTemplate getAccountDbJdbcTemplate() {
        if (AccountJdbcTemplate == null) {
            AccountJdbcTemplate = new JdbcTemplate(getAccountDataSource());
        }
        return AccountJdbcTemplate;
    }

}
