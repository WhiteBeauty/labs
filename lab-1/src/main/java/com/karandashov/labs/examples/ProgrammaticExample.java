package com.karandashov.labs.examples;

import com.karandashov.labs.model.RickMortyCharacter;
import com.karandashov.labs.service.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

/**
 * Пример программного использования API без интерактивного меню
 */
public class ProgrammaticExample {
    
    public static void main(String[] args) {
        String csvFile = "characters.csv";
        
        try {
            // ========== ОСНОВНОЕ ЗАДАНИЕ ==========
            System.out.println("=== ОСНОВНОЕ ЗАДАНИЕ: LinkedHashMap для подсчёта видов ===\n");
            
            // 1. Чтение данных
            CsvReader reader = new CsvReader();
            List<RickMortyCharacter> characters = reader.readCharacters(csvFile);
            System.out.println("Загружено персонажей: " + characters.size());
            
            // 2. Обработка с использованием LinkedHashMap
            SpeciesProcessor processor = new SpeciesProcessor();
            LinkedHashMap<String, Integer> speciesCount = processor.countSpeciesInOrder(characters);
            
            // 3. Вывод результатов
            processor.printStatistics(speciesCount);
            
            // 4. Сохранение в файлы
            ResultWriter writer = new ResultWriter();
            writer.writeToJson(speciesCount, "species_count.json");
            writer.writeToText(speciesCount, "species_count.txt");
            writer.writeToCsv(speciesCount, "species_count.csv");
            
            System.out.println("\n" + "=".repeat(60));
            
            // ========== ДОПОЛНИТЕЛЬНОЕ ЗАДАНИЕ: CRUD ==========
            System.out.println("\n=== ДОПОЛНИТЕЛЬНОЕ ЗАДАНИЕ: CRUD операции ===\n");
            
            CharacterCrudManager crudManager = new CharacterCrudManager();
            
            // CREATE - Создание нового персонажа
            System.out.println("--- CREATE: Создание нового персонажа ---");
            RickMortyCharacter newCharacter = new RickMortyCharacter();
            newCharacter.setName("Test Character");
            newCharacter.setStatus("Alive");
            newCharacter.setSpecies("Robot");
            newCharacter.setType("Test Type");
            newCharacter.setGender("Male");
            newCharacter.setOriginName("Test Dimension");
            newCharacter.setLocationName("Test Location");
            newCharacter.setCreated(LocalDateTime.now());
            
            crudManager.createCharacter(csvFile, newCharacter);
            System.out.println();
            
            // READ - Чтение персонажа по ID
            System.out.println("--- READ: Чтение персонажа с ID=1 ---");
            Optional<RickMortyCharacter> character = crudManager.readCharacter(csvFile, 1);
            if (character.isPresent()) {
                RickMortyCharacter c = character.get();
                System.out.println("Найден: " + c.getName() + " (" + c.getSpecies() + ")");
            }
            System.out.println();
            
            // Поиск по виду
            System.out.println("--- SEARCH: Поиск всех людей (Human) ---");
            List<RickMortyCharacter> humans = crudManager.findBySpecies(csvFile, "Human");
            System.out.println("Найдено людей: " + humans.size());
            humans.stream()
                    .limit(5)
                    .forEach(c -> System.out.println("  - " + c.getName()));
            if (humans.size() > 5) {
                System.out.println("  ... и ещё " + (humans.size() - 5) + " персонажей");
            }
            System.out.println();
            
            // Поиск по статусу
            System.out.println("--- SEARCH: Поиск живых персонажей (Alive) ---");
            List<RickMortyCharacter> alive = crudManager.findByStatus(csvFile, "Alive");
            System.out.println("Найдено живых: " + alive.size());
            System.out.println();
            
            // UPDATE - Обновление персонажа
            System.out.println("--- UPDATE: Обновление созданного персонажа ---");
            List<RickMortyCharacter> allChars = crudManager.readAllCharacters(csvFile);
            RickMortyCharacter lastChar = allChars.get(allChars.size() - 1);
            lastChar.setStatus("Unknown");
            lastChar.setLocationName("Updated Location");
            crudManager.updateCharacter(csvFile, lastChar);
            System.out.println();
            
            // DELETE - Удаление персонажа
            System.out.println("--- DELETE: Удаление тестового персонажа ---");
            boolean deleted = crudManager.deleteCharacter(csvFile, lastChar.getId());
            if (deleted) {
                System.out.println("Тестовый персонаж успешно удалён");
            }
            System.out.println();
            
            System.out.println("=".repeat(60));
            System.out.println("Все операции выполнены успешно!");
            
        } catch (Exception e) {
            System.err.println("Ошибка: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
