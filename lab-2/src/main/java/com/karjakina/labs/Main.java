package com.karjakina.labs;

import com.karjakina.labs.dao.BuildingRepository;
import com.karjakina.labs.dao.BuildingRepositoryJdbc;
import com.karjakina.labs.db.ConnectionConfig;
import com.karjakina.labs.entity.BuildingEntity;
import com.karjakina.labs.exception.BuildingNotFoundException;
import com.karjakina.labs.service.BuildingService;
import com.karjakina.labs.service.BuildingServiceImpl;

import javax.sql.DataSource;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== Запуск приложения Building App ===\n");

        DataSource dataSource = ConnectionConfig.createDataSource();
        BuildingRepository buildingRepository = new BuildingRepositoryJdbc(dataSource);
        BuildingService buildingService = new BuildingServiceImpl(buildingRepository);

        demonstrateCrudOperations(buildingService);

        System.out.println("\n=== Приложение завершило работу ===");
    }

    private static void demonstrateCrudOperations(BuildingService buildingService) {

        //  CREATE
        System.out.println("--- CREATE: сохранение зданий ---");

        int firstId = buildingService.save("ул. Ленина, д. 1", 5);
        System.out.println("Сохранено здание с id=" + firstId);

        int secondId = buildingService.save("пр. Мира, д. 42", 12);
        System.out.println("Сохранено здание с id=" + secondId);

        int thirdId = buildingService.save("ул. Садовая, д. 7", 3);
        System.out.println("Сохранено здание с id=" + thirdId);

        // READ: findById
        System.out.println("\n--- READ: поиск по id ---");

        BuildingEntity foundBuilding = buildingService.findById(firstId);
        System.out.println("Найдено: " + foundBuilding);

        // READ: findByAddress
        System.out.println("\n--- READ: поиск по адресу ---");

        BuildingEntity byAddress = buildingService.findByAddress("пр. Мира, д. 42");
        System.out.println("Найдено по адресу: " + byAddress);

        //  READ: findAll
        System.out.println("\n--- READ: все здания ---");

        List<BuildingEntity> allBuildings = buildingService.findAll();
        for (BuildingEntity building : allBuildings) {
            System.out.println("  " + building);
        }

        //  UPDATE
        System.out.println("\n--- UPDATE: обновление здания ---");

        BuildingEntity buildingToUpdate = new BuildingEntity(firstId, "ул. Ленина, д. 1 (обновлено)", 9);
        buildingService.update(buildingToUpdate);
        System.out.println("Обновление id=" + firstId + " выполнено");

        BuildingEntity updatedBuilding = buildingService.findById(firstId);
        System.out.println("После обновления: " + updatedBuilding);

        //  UPDATE несуществующего
        System.out.println("\n--- UPDATE несуществующего здания ---");
        try {
            buildingService.update(new BuildingEntity(999999, "Адрес", 1));
        } catch (BuildingNotFoundException e) {
            System.out.println("Ожидаемое исключение: " + e.getMessage());
        }

        // DELETE
        System.out.println("\n--- DELETE: удаление здания ---");

        buildingService.deleteById(thirdId);
        System.out.println("Удалено здание id=" + thirdId);

        System.out.println("\nЗдания после удаления:");
        buildingService.findAll().forEach(building -> System.out.println("  " + building));

        // findById несуществующего
        System.out.println("\n--- findById для удалённого id=" + thirdId + " ---");
        try {
            buildingService.findById(thirdId);
        } catch (BuildingNotFoundException e) {
            System.out.println("Ожидаемое исключение: " + e.getMessage());
        }
    }
}
