package com.karandashov.labs.examples;

import java.util.*;

/**
 * Демонстрация работы LinkedHashMap и его отличий от HashMap
 */
public class LinkedHashMapDemo {
    
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║  Демонстрация LinkedHashMap vs HashMap                   ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();
        
        demonstrateHashMap();
        System.out.println();
        demonstrateLinkedHashMap();
        System.out.println();
        demonstratePerformance();
        System.out.println();
        demonstrateOrderPreservation();
    }
    
    /**
     * Демонстрация обычного HashMap (порядок не сохраняется)
     */
    private static void demonstrateHashMap() {
        System.out.println("=== HashMap (порядок НЕ гарантируется) ===");
        
        Map<String, Integer> hashMap = new HashMap<>();
        
        // Добавляем элементы в определённом порядке
        hashMap.put("Human", 14);
        hashMap.put("Alien", 6);
        hashMap.put("Robot", 3);
        hashMap.put("Humanoid", 2);
        hashMap.put("Animal", 1);
        
        System.out.println("Порядок добавления: Human → Alien → Robot → Humanoid → Animal");
        System.out.print("Порядок при итерации: ");
        
        List<String> keys = new ArrayList<>(hashMap.keySet());
        System.out.println(String.join(" → ", keys));
        System.out.println("❌ Порядок НЕ сохранён!");
    }
    
    /**
     * Демонстрация LinkedHashMap (порядок сохраняется)
     */
    private static void demonstrateLinkedHashMap() {
        System.out.println("=== LinkedHashMap (порядок ГАРАНТИРУЕТСЯ) ===");
        
        Map<String, Integer> linkedHashMap = new LinkedHashMap<>();
        
        // Добавляем элементы в определённом порядке
        linkedHashMap.put("Human", 14);
        linkedHashMap.put("Alien", 6);
        linkedHashMap.put("Robot", 3);
        linkedHashMap.put("Humanoid", 2);
        linkedHashMap.put("Animal", 1);
        
        System.out.println("Порядок добавления: Human → Alien → Robot → Humanoid → Animal");
        System.out.print("Порядок при итерации: ");
        
        List<String> keys = new ArrayList<>(linkedHashMap.keySet());
        System.out.println(String.join(" → ", keys));
        System.out.println("✅ Порядок ПОЛНОСТЬЮ сохранён!");
    }
    
    /**
     * Демонстрация производительности
     */
    private static void demonstratePerformance() {
        System.out.println("=== Сравнение производительности ===");
        
        int iterations = 100000;
        
        // HashMap
        long startTime = System.nanoTime();
        Map<Integer, String> hashMap = new HashMap<>();
        for (int i = 0; i < iterations; i++) {
            hashMap.put(i, "Value" + i);
        }
        long hashMapTime = System.nanoTime() - startTime;
        
        // LinkedHashMap
        startTime = System.nanoTime();
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
        for (int i = 0; i < iterations; i++) {
            linkedHashMap.put(i, "Value" + i);
        }
        long linkedHashMapTime = System.nanoTime() - startTime;
        
        System.out.printf("HashMap:       %d мс%n", hashMapTime / 1_000_000);
        System.out.printf("LinkedHashMap: %d мс%n", linkedHashMapTime / 1_000_000);
        System.out.printf("Разница:       %.2fx%n", (double) linkedHashMapTime / hashMapTime);
        System.out.println("💡 LinkedHashMap немного медленнее из-за поддержки двусвязного списка");
    }
    
    /**
     * Демонстрация сохранения порядка при обновлениях
     */
    private static void demonstrateOrderPreservation() {
        System.out.println("=== Сохранение порядка при обновлениях ===");
        
        LinkedHashMap<String, Integer> map = new LinkedHashMap<>();
        
        System.out.println("Шаг 1: Добавляем виды по мере их появления");
        map.put("Human", 1);
        System.out.println("  Текущий порядок: " + map.keySet());
        
        map.put("Alien", 1);
        System.out.println("  Текущий порядок: " + map.keySet());
        
        map.put("Robot", 1);
        System.out.println("  Текущий порядок: " + map.keySet());
        
        System.out.println("\nШаг 2: Увеличиваем счётчики (обновляем значения)");
        map.put("Human", map.get("Human") + 1);  // Human: 1 → 2
        System.out.println("  Обновили Human до 2");
        System.out.println("  Порядок: " + map.keySet());
        System.out.println("  ✅ Human остался первым!");
        
        map.put("Alien", map.get("Alien") + 1);  // Alien: 1 → 2
        System.out.println("  Обновили Alien до 2");
        System.out.println("  Порядок: " + map.keySet());
        System.out.println("  ✅ Порядок не изменился!");
        
        map.put("Human", map.get("Human") + 1);  // Human: 2 → 3
        System.out.println("  Обновили Human до 3");
        System.out.println("  Порядок: " + map.keySet());
        System.out.println("  ✅ Human всё ещё первый, несмотря на наибольшее значение!");
        
        System.out.println("\nИтоговая карта:");
        map.forEach((species, count) -> 
            System.out.printf("  %s: %d%n", species, count)
        );
    }
    
    /**
     * Бонус: Демонстрация применения для задачи подсчёта видов
     */
    public static void demonstrateSpeciesCounting() {
        System.out.println("=== Применение для подсчёта видов персонажей ===");
        
        // Симуляция обработки персонажей по мере чтения из файла
        String[] charactersInOrder = {
            "Human",    // Rick Sanchez
            "Human",    // Morty Smith
            "Human",    // Summer Smith
            "Human",    // Beth Smith
            "Human",    // Jerry Smith
            "Alien",    // Abadango Cluster Princess
            "Human",    // Abradolf Lincler
            "Human",    // Adjudicator Rick
            "Human",    // Agency Director
            "Human",    // Alan Rails
            "Alien",    // Alien Googah
            "Alien",    // Alien Morty
            "Alien",    // Alien Rick
            "Human",    // Annie
            "Human",    // Antenna Morty
            "Human",    // Antenna Rick
            "Robot"     // Butter Robot (например)
        };
        
        LinkedHashMap<String, Integer> speciesCount = new LinkedHashMap<>();
        
        System.out.println("Обработка персонажей в порядке появления...");
        for (int i = 0; i < charactersInOrder.length; i++) {
            String species = charactersInOrder[i];
            
            // Используем getOrDefault для увеличения счётчика
            speciesCount.put(species, speciesCount.getOrDefault(species, 0) + 1);
            
            // Показываем прогресс для первых нескольких
            if (i < 7) {
                System.out.printf("  Персонаж %d: %s → %s%n", 
                    i + 1, species, speciesCount);
            }
        }
        
        System.out.println("  ...");
        System.out.println("\nИтоговая статистика (в порядке первого появления):");
        speciesCount.forEach((species, count) -> 
            System.out.printf("  %-10s: %d персонаж(ей)%n", species, count)
        );
        
        System.out.println("\n💡 Ключевое преимущество: виды появляются в том же порядке,");
        System.out.println("   в котором они впервые встретились в файле!");
    }
}
