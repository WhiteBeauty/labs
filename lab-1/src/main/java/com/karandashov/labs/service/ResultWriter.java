package com.karandashov.labs.service;

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
     * Записывает результаты в JSON-файл
     */
    public void writeToJson(LinkedHashMap<String, Integer> speciesCount, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("{");

            boolean first = true;
            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                if (!first) {
                    writer.write(",\n");
                }
                writer.write(String.format("  \"%s\": %d",
                        escapeJson(entry.getKey()),
                        entry.getValue()));
                first = false;
            }

            writer.write("\n}");
            System.out.println("Результат записан в JSON: " + outputPath);
        }
    }

    /**
     * Записывает результаты в текстовый файл в формате "ключ → значение"
     */
    public void writeToText(LinkedHashMap<String, Integer> speciesCount, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("=== Количество персонажей по видам (в порядке первого появления) ===\n\n");

            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                writer.write(String.format("%s → %d%n", entry.getKey(), entry.getValue()));
            }

            int total = speciesCount.values().stream().mapToInt(Integer::intValue).sum();
            writer.write(String.format("%nВсего персонажей: %d%n", total));
            writer.write(String.format("Всего видов: %d%n", speciesCount.size()));

            System.out.println("Результат записан в текстовый файл: " + outputPath);
        }
    }

    /**
     * Записывает результаты в CSV-файл
     */
    public void writeToCsv(LinkedHashMap<String, Integer> speciesCount, String outputPath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            writer.write("species,count\n");

            for (Map.Entry<String, Integer> entry : speciesCount.entrySet()) {
                writer.write(String.format("%s,%d%n", escapeCsv(entry.getKey()), entry.getValue()));
            }

            System.out.println("Результат записан в CSV: " + outputPath);
        }
    }

    /**
     * Экранирует специальные символы для JSON
     */
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * Экранирует специальные символы для CSV
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