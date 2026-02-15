package com.karjakina.labs.service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Сервис для записи результатов в различные форматы
 */
public class ResultWriter {

    /**
     * Записывает результаты в текстовый файл в формате "ключ -> значение"
     */
    public void writeToText(LinkedHashMap<String, Integer> speciesCount, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("=== Количество персонажей по видам (в порядке первого появления) ===\n\n");

            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                writer.write(String.format("%s -> %d%n", entry.getKey(), entry.getValue()));
            }

            int total = speciesCount.values().stream().mapToInt(Integer::intValue).sum();
            writer.write(String.format("%nВсего персонажей: %d%n", total));
            writer.write(String.format("Всего видов: %d%n", speciesCount.size()));

            System.out.println("Результат записан в текстовый файл: " + outputPath);
        }
    }

}