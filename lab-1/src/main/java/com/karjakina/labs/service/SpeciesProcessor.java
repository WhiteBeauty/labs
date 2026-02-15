package com.karjakina.labs.service;

import com.karjakina.labs.model.RickMortyCharacter;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Процессор для обработки данных персонажей
 * Использует LinkedHashMap для сохранения порядка первого появления
 */
public class SpeciesProcessor {
    
    /**
     * Подсчитывает количество персонажей по видам в порядке их первого появления
     */
    public LinkedHashMap<String, Integer> countSpeciesInOrder(List<RickMortyCharacter> characters) {
        LinkedHashMap<String, Integer> speciesCount = new LinkedHashMap<>();
        
        for (RickMortyCharacter character : characters) {
            String species = character.getSpecies();
            
            // Пропускаем персонажей без указанного вида
            if (species == null || species.isEmpty()) {
                continue;
            }
            
            // getOrDefault для увеличения счётчика
            // LinkedHashMap сохраняет порядок вставки ключей
            speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
        }
        
        return speciesCount;
    }
    
    /**
     * Выводит статистику по видам в консоль
      speciesCount карта с подсчётом видов
     */
    public void printStatistics(LinkedHashMap<String, Integer> speciesCount) {
        System.out.println("\n=== Статистика по видам (в порядке первого появления) ===");
        System.out.println("Всего различных видов: " + speciesCount.size());
        System.out.println();
        
        int totalCharacters = 0;
        for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
            System.out.printf("%-20s : %d персонаж(ей)%n", entry.getKey(), entry.getValue());
            totalCharacters += entry.getValue();
        }
        
        System.out.println("\nВсего персонажей обработано: " + totalCharacters);
        System.out.println("=".repeat(60));
    }
}
