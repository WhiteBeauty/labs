package com.karjakina.labs.service;

import com.karjakina.labs.model.RickMortyCharacter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для чтения CSV файлов с данными персонажей
 */
public class CsvReader {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Чтение всех персонажей из CSV файла
     */
    public List<RickMortyCharacter> readCharacters(String filename) throws IOException {
        List<RickMortyCharacter> characters = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // Пропускаем заголовок
                }

                RickMortyCharacter character = parseCsvLine(line);
                if (character != null) {
                    characters.add(character);
                }
            }
        }

        return characters;
    }

    /**
     * Парсинг строки CSV в объект RickMortyCharacter
     */
    private RickMortyCharacter parseCsvLine(String line) {
        String[] fields = splitCsvLine(line);

        RickMortyCharacter character = new RickMortyCharacter();

        if (fields.length > 0 && !fields[0].isEmpty()) {
            character.setId(Integer.parseInt(fields[0]));
        }
        if (fields.length > 1) {
            character.setName(fields[1]);
        }
        if (fields.length > 2) {
            character.setStatus(fields[2]);
        }
        if (fields.length > 3) {
            character.setSpecies(fields[3]);
        }
        if (fields.length > 4) {
            character.setType(fields[4]);
        }
        if (fields.length > 5) {
            character.setGender(fields[5]);
        }
        if (fields.length > 6) {
            character.setOriginName(fields[6]);
        }
        if (fields.length > 7) {
            character.setLocationName(fields[7]);
        }
        if (fields.length > 8 && !fields[8].isEmpty()) {
            try {
                character.setCreated(LocalDateTime.parse(fields[8], DATE_FORMATTER));
            } catch (Exception e) {
                // Если не удалось распарсить дату, оставляем null
            }
        }

        return character;
    }

    /**
     * Разделение строки CSV с учётом кавычек
     */

    private String[] splitCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentField = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);

            if (c == '"') {
                // Проверяем, не экранированная ли это кавычка
                if (i < line.length() - 1 && line.charAt(i + 1) == '"') {
                    currentField.append('"');
                    i++; // Пропускаем следующую кавычку
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                fields.add(currentField.toString());
                currentField.setLength(0);
            } else {
                currentField.append(c);
            }
        }

        fields.add(currentField.toString());
        return fields.toArray(new String[0]);
    }
}