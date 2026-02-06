package com.karandashov.labs.service;

import com.karandashov.labs.model.RickMortyCharacter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CRUD операции для управления персонажами
 */
public class CharacterCrudManager {
    private final CsvReader csvReader;

    public CharacterCrudManager() {
        this.csvReader = new CsvReader();
    }

    /**
     * CREATE - Добавить нового персонажа
     *
     * @param filePath  путь к CSV-файлу
     * @param character новый персонаж
     * @throws IOException если возникла ошибка при работе с файлом
     */
    public void createCharacter(String filePath, RickMortyCharacter character) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);

        // Генерируем новый ID
        int maxId = characters.stream()
                .mapToInt(RickMortyCharacter::getId)
                .max()
                .orElse(0);
        character.setId(maxId + 1);

        // Устанавливаем дату создания
        if (character.getCreated() == null) {
            character.setCreated(LocalDateTime.now());
        }

        characters.add(character);
        saveAllCharacters(filePath, characters);

        System.out.println("Персонаж успешно добавлен с ID: " + character.getId());
    }

    /**
     * READ - Получить персонажа по ID
     *
     * @param filePath путь к CSV-файлу
     * @param id       идентификатор персонажа
     * @return Optional с персонажем, если найден
     * @throws IOException если возникла ошибка при чтении файла
     */
    public Optional<RickMortyCharacter> readCharacter(String filePath, int id) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);
        return characters.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    /**
     * READ ALL - Получить всех персонажей
     *
     * @param filePath путь к CSV-файлу
     * @return список всех персонажей
     * @throws IOException если возникла ошибка при чтении файла
     */
    public List<RickMortyCharacter> readAllCharacters(String filePath) throws IOException {
        return csvReader.readCharacters(filePath);
    }

    /**
     * UPDATE - Обновить данные персонажа
     *
     * @param filePath         путь к CSV-файлу
     * @param updatedCharacter обновлённый персонаж
     * @return true, если персонаж был найден и обновлён
     * @throws IOException если возникла ошибка при работе с файлом
     */
    public boolean updateCharacter(String filePath, RickMortyCharacter updatedCharacter) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);
        boolean found = false;

        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getId() == updatedCharacter.getId()) {
                characters.set(i, updatedCharacter);
                found = true;
                break;
            }
        }

        if (found) {
            saveAllCharacters(filePath, characters);
            System.out.println("Персонаж с ID " + updatedCharacter.getId() + " успешно обновлён");
        } else {
            System.out.println("Персонаж с ID " + updatedCharacter.getId() + " не найден");
        }

        return found;
    }

    /**
     * DELETE - Удалить персонажа по ID
     *
     * @param filePath путь к CSV-файлу
     * @param id       идентификатор персонажа
     * @return true, если персонаж был найден и удалён
     * @throws IOException если возникла ошибка при работе с файлом
     */
    public boolean deleteCharacter(String filePath, int id) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);
        boolean removed = characters.removeIf(c -> c.getId() == id);

        if (removed) {
            saveAllCharacters(filePath, characters);
            System.out.println("Персонаж с ID " + id + " успешно удалён");
        } else {
            System.out.println("Персонаж с ID " + id + " не найден");
        }

        return removed;
    }

    /**
     * Поиск персонажей по виду
     *
     * @param filePath путь к CSV-файлу
     * @param species  вид для поиска
     * @return список персонажей указанного вида
     * @throws IOException если возникла ошибка при чтении файла
     */
    public List<RickMortyCharacter> findBySpecies(String filePath, String species) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);
        List<RickMortyCharacter> result = new ArrayList<>();

        for (RickMortyCharacter character : characters) {
            if (species.equalsIgnoreCase(character.getSpecies())) {
                result.add(character);
            }
        }

        return result;
    }

    /**
     * Поиск персонажей по статусу
     *
     * @param filePath путь к CSV-файлу
     * @param status   статус для поиска
     * @return список персонажей с указанным статусом
     * @throws IOException если возникла ошибка при чтении файла
     */
    public List<RickMortyCharacter> findByStatus(String filePath, String status) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);
        List<RickMortyCharacter> result = new ArrayList<>();

        for (RickMortyCharacter character : characters) {
            if (status.equalsIgnoreCase(character.getStatus())) {
                result.add(character);
            }
        }

        return result;
    }

    /**
     * Сохранение всех персонажей в CSV-файл
     *
     * @param filePath   путь к CSV-файлу
     * @param characters список персонажей для сохранения
     * @throws IOException если возникла ошибка при записи
     */
    private void saveAllCharacters(String filePath, List<RickMortyCharacter> characters) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Записываем заголовок
            writer.write("id,name,status,species,type,gender,origin/name,location/name,created\n");

            // Записываем данные персонажей
            for (RickMortyCharacter character : characters) {
                writer.write(character.toCsvLine());
                writer.write("\n");
            }
        }
    }
}
