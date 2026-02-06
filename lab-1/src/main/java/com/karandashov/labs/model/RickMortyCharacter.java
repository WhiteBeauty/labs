package com.karandashov.labs.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Модель персонажа из сериала Rick and Morty
 */
public class RickMortyCharacter {
    private int id;
    private String name;
    private String status;
    private String species;
    private String type;
    private String gender;
    private String originName;
    private String locationName;
    private LocalDateTime created;

    public RickMortyCharacter() {
    }

    public RickMortyCharacter(int id, String name, String status, String species, String type,
                              String gender, String originName, String locationName, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.species = species;
        this.type = type;
        this.gender = gender;
        this.originName = originName;
        this.locationName = locationName;
        this.created = created;
    }

    // Геттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public String getSpecies() {
        return species;
    }

    public String getType() {
        return type;
    }

    public String getGender() {
        return gender;
    }

    public String getOriginName() {
        return originName;
    }

    public String getLocationName() {
        return locationName;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    // Сеттеры
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RickMortyCharacter character = (RickMortyCharacter) o;
        return id == character.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Character{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", species='" + species + '\'' +
                ", type='" + type + '\'' +
                ", gender='" + gender + '\'' +
                ", originName='" + originName + '\'' +
                ", locationName='" + locationName + '\'' +
                ", created=" + created +
                '}';
    }

    /**
     * Конвертация в CSV-строку
     */
    public String toCsvLine() {
        return String.join(",",
                String.valueOf(id),
                escapeCsv(name),
                escapeCsv(status),
                escapeCsv(species),
                escapeCsv(type),
                escapeCsv(gender),
                escapeCsv(originName),
                escapeCsv(locationName),
                created != null ? created.toString() : ""
        );
    }

    /**
     * Экранирование специальных символов для CSV
     */
    private String escapeCsv(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}
