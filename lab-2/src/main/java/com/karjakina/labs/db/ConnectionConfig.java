package com.karjakina.labs.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.sql.Connection;
import java.sql.SQLException;
public class ConnectionConfig {

    private static final String PROPERTIES_FILE = "db.properties";

    public static DataSource createDataSource() {
        Properties properties = loadProperties();

        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("db.url"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(
                Integer.parseInt(properties.getProperty("db.pool.size", "5"))
        );
        hikariConfig.setPoolName("building-app-pool");

        System.out.println("Инициализация пула соединений HikariCP...");

       /* HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        //  инициализация Liquibase
        try {
            initializeLiquibase(dataSource);
            System.out.println("Liquibase: таблицы созданы успешно.");
        } catch (Exception e) {
            System.out.println("Ошибка Liquibase: " + e.getMessage());
            e.printStackTrace();
        }
        return dataSource;
        */

        return new HikariDataSource(hikariConfig);
    }

/*
    private static void initializeLiquibase(DataSource dataSource) throws Exception {
        Connection connection = dataSource.getConnection();
        try {
            Database database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));

            Liquibase liquibase = new Liquibase(
                    "db/changelog/db.changelog-master.xml",  // путь к файлу миграции
                    new ClassLoaderResourceAccessor(),        // доступ к ресурсам
                    database                                   // подключение к БД
            );

            liquibase.update();  //
        } finally {
            connection.close();  // обязательно закрываем соединение
        }
    }
    */
    private static Properties loadProperties() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = ConnectionConfig.class.getClassLoader().getResourceAsStream(PROPERTIES_FILE);
            if (inputStream == null) {
                System.out.println("Файл " + PROPERTIES_FILE + " не найден!");
                return properties;
            }
            properties.load(inputStream);
        } catch (IOException e) {
            System.out.println("Ошибка чтения файла: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {

                }
            }
        }
        return properties;
    }
}
