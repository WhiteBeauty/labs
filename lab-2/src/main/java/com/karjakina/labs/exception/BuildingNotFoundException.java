package com.karjakina.labs.exception;

public class BuildingNotFoundException extends RuntimeException {

    public BuildingNotFoundException(int id) {
        super("Здание с id=" + id + " не найдено");
    }

    public BuildingNotFoundException(String address) {
        super("Здание с адресом '" + address + "' не найдено");
    }
}
