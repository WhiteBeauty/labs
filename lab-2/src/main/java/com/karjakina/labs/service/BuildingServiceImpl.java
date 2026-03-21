package com.karjakina.labs.service;

import com.karjakina.labs.dao.BuildingRepository;
import com.karjakina.labs.entity.BuildingEntity;
import com.karjakina.labs.exception.BuildingNotFoundException;

import java.util.List;

public class BuildingServiceImpl implements BuildingService {

    private final BuildingRepository buildingRepository;

    public BuildingServiceImpl(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Override
    public int save(String address, int floors) {
        BuildingEntity building = new BuildingEntity(address, floors);
        return buildingRepository.save(building);
    }

    @Override
    public BuildingEntity findById(int id) {
        BuildingEntity building = buildingRepository.findById(id);
        if (building == null) {
            throw new BuildingNotFoundException(id);
        }
        return building;
    }

    @Override
    public BuildingEntity findByAddress(String address) {

        List<BuildingEntity> allBuildings = buildingRepository.findAll();
        for (BuildingEntity building : allBuildings) {
            if (building.getAddress().equals(address)) {
                return building;
            }
        }
        throw new BuildingNotFoundException(address);
    }

    @Override
    public List<BuildingEntity> findAll() {
        return buildingRepository.findAll();
    }

    @Override
    public void update(BuildingEntity building) {
        boolean updated = buildingRepository.update(building);
        if (!updated) {
            throw new BuildingNotFoundException(building.getId());
        }
    }

    @Override
    public void deleteById(int id) {
        buildingRepository.deleteById(id);
    }
}
