package com.karjakina.labs.service;

import com.karjakina.labs.model.RickMortyCharacter;

import java.io.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class CharacterCrudManager {

    private final CsvReader csvReader;

    public CharacterCrudManager() {
        this.csvReader = new CsvReader();
    }

    /**
     * ДОБАВИТЬ нового персонажа в файл.
     * Автоматически проставляет ID и дату создания, если их нет.
     */
    public void createCharacter(String filePath, RickMortyCharacter character) throws IOException {
        // Считываем текущий список
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);


        int maxId = 0;
        for (RickMortyCharacter c : characters) {
            if (c.getId() > maxId) {
                maxId = c.getId();
            }
        }
        character.setId(maxId + 1);

        // Если дата создания не задана — ставим текущую
        if (character.getCreated() == null) {
            character.setCreated(LocalDateTime.now());
        }

        // Добавляем и сохраняем всё обратно
        characters.add(character);
        saveAllCharacters(filePath, characters);

        System.out.println("Персонаж добавлен. Новый ID: " + character.getId());
    }

    /**
     * НАЙТИ одного персонажа по ID.
     * Возвращает Optional.empty(), если не найден.
     */
    public Optional<RickMortyCharacter> readCharacter(String filePath, int id) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);

        for (RickMortyCharacter character : characters) {
            if (character.getId() == id) {
                return Optional.of(character);
            }
        }
        return Optional.empty();
    }

    /**
     * ВЕРНУТЬ всех персонажей из файла.
     */
    public List<RickMortyCharacter> readAllCharacters(String filePath) throws IOException {
        return csvReader.readCharacters(filePath);
    }

    /**
     * ОБНОВИТЬ данные персонажа по ID.
     * Возвращает true, если обновление прошло успешно.
     */
    public boolean updateCharacter(String filePath, RickMortyCharacter updatedCharacter) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);
        int targetId = updatedCharacter.getId();
        boolean found = false;

        // Проходим по списку и ищем совпадение по ID
        for (int i = 0; i < characters.size(); i++) {
            RickMortyCharacter current = characters.get(i);
            if (current.getId() == targetId) {
                characters.set(i, updatedCharacter);
                found = true;
                break;
            }
        }

        if (found) {
            saveAllCharacters(filePath, characters);
            System.out.println(" Персонаж #" + targetId + " обновлён");
        } else {
            System.out.println(" Персонаж #" + targetId + " не найден, обновление отменено");
        }

        return found;
    }

    /**
     * УДАЛИТЬ персонажа по ID.
     * Возвращает true, если удаление прошло успешно.
     */
    public boolean deleteCharacter(String filePath, int id) throws IOException {
        List<RickMortyCharacter> characters = csvReader.readCharacters(filePath);

        // Простой и понятный способ удаления
        boolean removed = false;
        for (int i = 0; i < characters.size(); i++) {
            if (characters.get(i).getId() == id) {
                characters.remove(i);
                removed = true;
                break;
            }
        }

        if (removed) {
            saveAllCharacters(filePath, characters);
            System.out.println("Персонаж #" + id + " удалён");
        } else {
            System.out.println("Персонаж #" + id + " не найден");
        }

        return removed;
    }

    /**
     * Вспомогательный метод: записать весь список персонажей в CSV.
     * Перезаписывает файл полностью.
     */
    private void saveAllCharacters(String filePath, List<RickMortyCharacter> characters) throws IOException {
        //  try-with-resources, чтобы файл точно закрылся
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            // Пишем шапку таблицы
            writer.write("id,name,status,species,type,gender,origin/name,location/name,created");
            writer.newLine();

            // Пишем каждую строку
            for (RickMortyCharacter character : characters) {
                writer.write(character.toCsvLine());
                writer.newLine();
            }
        }
    }
}