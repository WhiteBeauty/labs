package com.karjakina.labs.service;

import com.karjakina.labs.model.RickMortyCharacter;

import java.io.*;
import java.time.LocalDateTime;
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
      filePath  путь к CSV-файлу
      character новый персонаж
      IOException если возникла ошибка при работе с файлом
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
     filePath путь к CSV-файлу
     id       идентификатор персонажа
     Optional с персонажем, если найден
     IOException если возникла ошибка при чтении файла
     */
    public Optional<RickMortyCharacter> readCharacter(String filePath, int id) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);
        return characters.stream()
                .filter(c -> c.getId() == id)
                .findFirst();
    }

    /**
     * READ ALL - Получить всех персонажей
     filePath путь к CSV-файлу
     список всех персонажей
     IOException если возникла ошибка при чтении файла
     */
    public List<RickMortyCharacter> readAllCharacters(String filePath) throws IOException {
        return csvReader.readCharacters(filePath);
    }

    /**
     * UPDATE - Обновить данные персонажа
     filePath         путь к CSV-файлу
     updatedCharacter обновлённый персонаж
     true, если персонаж был найден и обновлён
     IOException если возникла ошибка при работе с файлом
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
     filePath путь к CSV-файлу
     id       идентификатор персонажа
     true, если персонаж был найден и удалён
     IOException если возникла ошибка при работе с файлом
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
     * Сохранение всех персонажей в CSV-файл
     filePath   путь к CSV-файлу
     characters список персонажей для сохранения
     IOException если возникла ошибка при записи
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
