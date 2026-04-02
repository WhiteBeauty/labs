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

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));

        String poolSize = properties.getProperty("db.pool.size", "5");
        hikariConfig.setMaximumPoolSize(Integer.parseInt(poolSize));
        hikariConfig.setPoolName("building-app-pool");

        System.out.println("Инициализация пула соединений HikariCP...");
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);

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

        try (InputStream inputStream = ConnectionConfig.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE)) {

            if (inputStream == null) {
                throw new RuntimeException("Файл конфигурации не найден: " + PROPERTIES_FILE);
            }

            properties.load(inputStream);
            System.out.println("Настройки подключения загружены из: " + PROPERTIES_FILE);

        } catch (IOException e) {
            throw new RuntimeException("Ошибка при загрузке конфигурации: " + e.getMessage(), e);
        }

        return properties;
    }
}