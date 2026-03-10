package com.karjakina.labs.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class ConnectionConfig {

    private static final String PROPERTIES_FILE = "db.properties";
    public static DataSource createDataSource() {
        Properties properties = loadProperties();

        // настраиваем пул соединений HikariCP
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));

        String poolSize = properties.getProperty("db.pool.size", "5");
        hikariConfig.setMaximumPoolSize(Integer.parseInt(poolSize));
        hikariConfig.setPoolName("building-app-pool");

        System.out.println("Инициализация пула соединений HikariCP...");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

        // попытка инициализировать Liquibase для создания таблиц
        try {
            initializeLiquibase(dataSource);
            System.out.println("Liquibase: таблицы созданы успешно.");
        } catch (Exception e) {
            System.out.println("Ошибка Liquibase: " + e.getMessage());
            e.printStackTrace();
        }

        return dataSource;
    }

    private static void initializeLiquibase(DataSource dataSource) throws Exception {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();

            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",
                    new ClassLoaderResourceAccessor(),
                    database
            );

            liquibase.update();

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Не удалось закрыть соединение: " + e.getMessage());
                }
            }
        }
    }

    private static Properties loadProperties() {
        Properties properties = new Properties();

        // из файла отказывался читать
        properties.setProperty("db.url", "jdbc:postgresql://localhost:5432/postgres");
        properties.setProperty("db.username", "postgres");
        properties.setProperty("db.password", "123");
        properties.setProperty("db.pool.size", "5");

        System.out.println("Настройки подключения загружены из кода.");

        return properties;
    }
}