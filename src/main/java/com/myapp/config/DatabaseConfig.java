package com.myapp.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

@ApplicationScoped  // one instance shared by the whole app
public class DatabaseConfig {

    private HikariDataSource dataSource;

    @PostConstruct  // runs automatically when the app starts
    public void init() {
        // Read values from environment variables (set in docker-compose)
        String host     = System.getenv("DB_HOST");
        String port     = System.getenv("DB_PORT");
        String db       = System.getenv("DB_NAME");
        String user     = System.getenv("DB_USER");
        String password = System.getenv("DB_PASSWORD");

        // sslmode=require is mandatory for Supabase
        String url = String.format(
            "jdbc:postgresql://%s:%s/%s?sslmode=require",
            host, port, db
        );

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setMaximumPoolSize(5); // max 5 simultaneous DB connections

        dataSource = new HikariDataSource(config);
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    @PreDestroy // runs when the app shuts down, closes connections cleanly
    public void close() {
        if (dataSource != null) dataSource.close();
    }
}
