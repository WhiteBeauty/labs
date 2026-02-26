package com.karjakina.labs.dao;

import com.karjakina.labs.entity.BuildingEntity;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BuildingRepositoryJdbc implements BuildingRepository {

    private static final String SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS building (" +
                    "id SERIAL PRIMARY KEY, " +
                    "address VARCHAR(255) NOT NULL, " +
                    "floors INTEGER NOT NULL" +
                    ")";

    private static final String SQL_INSERT =
            "INSERT INTO building (address, floors) VALUES (?, ?) RETURNING id";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, address, floors FROM building WHERE id = ?";

    private static final String SQL_FIND_ALL =
            "SELECT id, address, floors FROM building";

    private static final String SQL_UPDATE =
            "UPDATE building SET address = ?, floors = ? WHERE id = ?";

    private static final String SQL_DELETE_BY_ID =
            "DELETE FROM building WHERE id = ?";

    private final DataSource dataSource;

    public BuildingRepositoryJdbc(DataSource dataSource) {
        this.dataSource = dataSource;
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        Connection connection = null;
        Statement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement();
            statement.execute(SQL_CREATE_TABLE);
            System.out.println("Таблица 'building' проверена/создана успешно.");
        } catch (SQLException e) {
            System.out.println("Ошибка при создании таблицы: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {

            }
        }
    }

    @Override
    public int save(BuildingEntity building) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        int id = -1;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_INSERT);
            statement.setString(1, building.getAddress());
            statement.setInt(2, building.getFloors());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {

            }
        }
        return id;
    }

    @Override
    public BuildingEntity findById(int id) {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        BuildingEntity building = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_FIND_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                building = new BuildingEntity();
                building.setId(resultSet.getInt("id"));
                building.setAddress(resultSet.getString("address"));
                building.setFloors(resultSet.getInt("floors"));
            }
        } catch (SQLException e) {
            System.out.println("Ошибка поиска id=" + id + ": " + e.getMessage());
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {

            }
        }
        return building;
    }

    @Override
    public List<BuildingEntity> findAll() {
        List<BuildingEntity> buildings = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_FIND_ALL);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                BuildingEntity building = new BuildingEntity();
                building.setId(resultSet.getInt("id"));
                building.setAddress(resultSet.getString("address"));
                building.setFloors(resultSet.getInt("floors"));
                buildings.add(building);
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {

            }
        }
        return buildings;
    }

    @Override
    public boolean update(BuildingEntity building) {
        Connection connection = null;
        PreparedStatement statement = null;
        boolean updated = false;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_UPDATE);
            statement.setString(1, building.getAddress());
            statement.setInt(2, building.getFloors());
            statement.setInt(3, building.getId());
            int rows = statement.executeUpdate();
            updated = rows > 0;
        } catch (SQLException e) {
            System.out.println("Ошибка обновления: " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {

            }
        }
        return updated;
    }

    @Override
    public void deleteById(int id) {
        // на всякий случай проверяем id
        if (id <= 0) {
            System.out.println("id не может быть меньше 0 ");
        }

        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(SQL_DELETE_BY_ID);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка удаления id=" + id + ": " + e.getMessage());
        } finally {
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Ошибка поиска: " + e.getMessage());
            }
        }
    }

}
