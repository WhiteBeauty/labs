package com.karjakina.labs;

import com.karjakina.labs.dao.BuildingRepository;
import com.karjakina.labs.dao.BuildingRepositoryJdbc;
import com.karjakina.labs.db.ConnectionConfig;
import com.karjakina.labs.entity.BuildingEntity;

import javax.sql.DataSource;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Запуск приложения Building App ===\n");

        DataSource dataSource = ConnectionConfig.createDataSource();
        BuildingRepository buildingRepository = new BuildingRepositoryJdbc(dataSource);

        demonstrateCrudOperations(buildingRepository);

        System.out.println("\n=== Приложение завершило работу ===");
    }

    private static void demonstrateCrudOperations(BuildingRepository buildingRepository) {

        // --- CREATE ---
        System.out.println("--- CREATE: сохранение зданий ---");

        BuildingEntity firstBuilding = new BuildingEntity("ул. Ленина, д. 1", 5);
        int firstId = buildingRepository.save(firstBuilding);
        System.out.println("Сохранено здание с id=" + firstId);

        BuildingEntity secondBuilding = new BuildingEntity("пр. Мира, д. 42", 12);
        int secondId = buildingRepository.save(secondBuilding);
        System.out.println("Сохранено здание с id=" + secondId);

        BuildingEntity thirdBuilding = new BuildingEntity("ул. Садовая, д. 7", 3);
        int thirdId = buildingRepository.save(thirdBuilding);
        System.out.println("Сохранено здание с id=" + thirdId);

        // --- READ: findById ---
        System.out.println("\n--- READ: поиск по id ---");

        BuildingEntity foundBuilding = buildingRepository.findById(firstId);
        System.out.println("Найдено: " + foundBuilding);

        BuildingEntity notFoundBuilding = buildingRepository.findById(999999);
        System.out.println("Поиск по несуществующему id=999999: " + notFoundBuilding);

        // --- READ: findAll ---
        System.out.println("\n--- READ: все здания ---");

        // получаем все здания из базы
        List<BuildingEntity> allBuildings = buildingRepository.findAll();
// выводим каждое здание на консоль
        for (BuildingEntity building : allBuildings) {
            System.out.println("  " + building);
        }

        // --- UPDATE ---
        System.out.println("\n--- UPDATE: обновление здания ---");

        BuildingEntity buildingToUpdate = new BuildingEntity(firstId, "ул. Ленина, д. 1 (обновлено)", 9);
        boolean isUpdated = buildingRepository.update(buildingToUpdate);
        System.out.println("Обновление id=" + firstId + " выполнено: " + isUpdated);

        BuildingEntity nonExistentBuilding = new BuildingEntity(999999, "Несуществующий адрес", 1);
        boolean isUpdatedNonExistent = buildingRepository.update(nonExistentBuilding);
        System.out.println("Обновление несуществующего id=999999 выполнено: " + isUpdatedNonExistent);

        BuildingEntity updatedBuilding = buildingRepository.findById(firstId);
        System.out.println("После обновления: " + updatedBuilding);

        // --- DELETE ---
        System.out.println("\n--- DELETE: удаление здания ---");

        buildingRepository.deleteById(thirdId);
        System.out.println("Удалено здание id=" + thirdId);

        buildingRepository.deleteById(999999);
        System.out.println("Удаление несуществующего id=999999 — ошибки нет");

        System.out.println("\nЗдания после удаления:");
        buildingRepository.findAll().forEach(building -> System.out.println("  " + building));

        // Повторный вызов findById для удалённой записи
        System.out.println("\n---  findById для удалённого id=" + thirdId + " ---");
        BuildingEntity deletedBuilding = buildingRepository.findById(thirdId);
        System.out.println("Результат: " + deletedBuilding);
    }
}
