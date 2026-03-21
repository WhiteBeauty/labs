package com.karjakina.labs.service;

import com.karjakina.labs.entity.BuildingEntity;

import java.util.List;

public interface BuildingService {

    // Create возвращает id созданной записи
    int save(String address, int floors);

    // Read  (бросает исключение, если запись не найдена)
    BuildingEntity findById(int id);

    // Read (бросает исключение, если запись не найдена)
    BuildingEntity findByAddress(String address);

    List<BuildingEntity> findAll();

    // Update (используйте id для поиска сущности и бросайте исключение, если запись не найдена)
    void update(BuildingEntity building);

    // Delete
    void deleteById(int id);
}
