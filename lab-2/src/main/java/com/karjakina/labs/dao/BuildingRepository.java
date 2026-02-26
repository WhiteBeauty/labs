package com.karjakina.labs.dao;

import com.karjakina.labs.entity.BuildingEntity;

import java.util.List;

public interface BuildingRepository {

    // Create — возвращает id созданной записи
    int save(BuildingEntity building);

    // Read — возвращает null, если запись не найдена
    BuildingEntity findById(int id);

    List<BuildingEntity> findAll();

    // Update — возвращает false, если запись не найдена
    boolean update(BuildingEntity building);

    // Delete
    void deleteById(int id);
}
