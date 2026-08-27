package lk.edu.icbt.sunrise.config;

import com.zaxxer.hikari.*;

import javax.sql.DataSource;

public final class DatabaseConfig {
    private static HikariDataSource dataSource;
    private DatabaseConfig() {
    }

    public static synchronized DataSource initialize() {
        if (dataSource == null) {
            HikariConfig c = new HikariConfig();
            c.setJdbcUrl(value("SUNRISE_DB_URL", "jdbc:mariadb://localhost:3306/sunrise_dental"));
            c.setUsername(value("SUNRISE_DB_USER", "root"));
            c.setPassword(value("SUNRISE_DB_PASSWORD", ""));
            c.setDriverClassName("org.mariadb.jdbc.Driver");
            c.setMaximumPoolSize(10);
            c.setMinimumIdle(2);
            c.setConnectionTimeout(10000);
            c.setPoolName("SunrisePool");
            dataSource = new HikariDataSource(c);
        }
        return dataSource;
    }

    public static DataSource get() {
        return initialize();
    }

    public static synchronized void close() {
        if (dataSource != null) {
            dataSource.close();
            dataSource = null;
        }
    }

    private static String value(String key, String fallback) {
        String v = System.getenv(key);
        return v == null || v.isBlank() ? fallback : v;
    }
}
