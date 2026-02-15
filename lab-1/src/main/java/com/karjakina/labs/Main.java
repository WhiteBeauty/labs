package com.karjakina.labs;

import com.karjakina.labs.model.RickMortyCharacter;
import com.karjakina.labs.service.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Главный класс приложения для работы с персонажами Rick and Morty
 */
public class Main {
    private static final String INPUT_FILE = "lab-1/characters.csv";
    private static final String OUTPUT_TEXT = "species_count.txt";

    private static final CsvReader csvReader = new CsvReader();
    private static final SpeciesProcessor speciesProcessor = new SpeciesProcessor();
    private static final ResultWriter resultWriter = new ResultWriter();
    private static final CharacterCrudManager crudManager = new CharacterCrudManager();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            showMenu();
            int choice = getIntInput("Выберите действие: ");
            System.out.println();

            try {
                switch (choice) {
                    case 1 -> processMainTask();
                    case 2 -> crudMenu();
                    case 3 -> viewAllCharacters();
                    case 0 -> {
                        System.out.println("Завершение работы программы. До свидания!");
                        return;
                    }
                    default -> System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println();
        }
    }

    /**
       подсчёт видов в порядке появления
     */
    private static void processMainTask() {
        try {

            // 1. Чтение данных из файла
            System.out.println("Чтение данных из файла " + INPUT_FILE);
            List<RickMortyCharacter> characters = csvReader.readCharacters(INPUT_FILE);
            System.out.println("Загружено персонажей: " + characters.size());
            System.out.println();

            // 2. Обработка данных - подсчёт видов с сохранением порядка
            System.out.println("Обработка данных");
            System.out.println("Используется LinkedHashMap для сохранения порядка первого появления");
            LinkedHashMap<String, Integer> speciesCount = speciesProcessor.countSpeciesInOrder(characters);
            System.out.println("Обработка завершена");

            // Вывод статистики
            speciesProcessor.printStatistics(speciesCount);
            System.out.println();

            // 3. Запись результатов в файлы
            System.out.println("Запись результатов в файлы");

            resultWriter.writeToText(speciesCount, OUTPUT_TEXT);

            System.out.println("Все результаты успешно сохранены");
            System.out.println();

            System.out.println("=== ЗАДАНИЕ ВЫПОЛНЕНО УСПЕШНО ===");

        } catch (Exception e) {
            System.err.println("Ошибка при выполнении задания: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Меню CRUD операций
     */
    private static void crudMenu() {
        while (true) {
            System.out.println("\n--- CRUD ОПЕРАЦИИ ---");
            System.out.println("1. Создать нового персонажа (CREATE)");
            System.out.println("2. Просмотреть персонажа по ID (READ)");
            System.out.println("3. Обновить персонажа (UPDATE)");
            System.out.println("4. Удалить персонажа (DELETE)");
            System.out.println("0. Вернуться в главное меню");

            int choice = getIntInput("Выберите действие: ");
            System.out.println();

            try {
                switch (choice) {
                    case 1 -> createCharacterInteractive();
                    case 2 -> readCharacterInteractive();
                    case 3 -> updateCharacterInteractive();
                    case 4 -> deleteCharacterInteractive();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Неверный выбор.");
                }
            } catch (Exception e) {
                System.err.println("Ошибка: " + e.getMessage());
            }
        }
    }

    /**
     * Создание нового персонажа
     */
    private static void createCharacterInteractive() throws Exception {
        System.out.println("=== СОЗДАНИЕ НОВОГО ПЕРСОНАЖА ===");

        RickMortyCharacter character = new RickMortyCharacter();

        System.out.print("Имя: ");
        character.setName(scanner.nextLine());

        System.out.print("Статус (Alive/Dead/unknown): ");
        character.setStatus(scanner.nextLine());

        System.out.print("Вид (Human/Alien/и т.д.): ");
        character.setSpecies(scanner.nextLine());

        System.out.print("Тип (можно оставить пустым): ");
        character.setType(scanner.nextLine());

        System.out.print("Пол (Male/Female/unknown): ");
        character.setGender(scanner.nextLine());

        System.out.print("Происхождение: ");
        character.setOriginName(scanner.nextLine());

        System.out.print("Местоположение: ");
        character.setLocationName(scanner.nextLine());

        character.setCreated(LocalDateTime.now());

        crudManager.createCharacter(INPUT_FILE, character);
    }

    /**
     * Чтение персонажа по ID
     */
    private static void readCharacterInteractive() throws Exception {
        int id = getIntInput("Введите ID персонажа: ");

        Optional<RickMortyCharacter> character = crudManager.readCharacter(INPUT_FILE, id);
        if (character.isPresent()) {
            System.out.println("\n=== ИНФОРМАЦИЯ О ПЕРСОНАЖЕ ===");
            printCharacter(character.get());
        } else {
            System.out.println("Персонаж с ID " + id + " не найден.");
        }
    }

    /**
     * Обновление персонажа
     */
    private static void updateCharacterInteractive() throws Exception {
        int id = getIntInput("Введите ID персонажа для обновления: ");

        Optional<RickMortyCharacter> existingCharacter = crudManager.readCharacter(INPUT_FILE, id);
        if (existingCharacter.isEmpty()) {
            System.out.println("Персонаж с ID " + id + " не найден.");
            return;
        }

        RickMortyCharacter character = existingCharacter.get();
        System.out.println("\nТекущие данные:");
        printCharacter(character);

        System.out.println("\nВведите новые данные (оставьте пустым для сохранения текущего значения):");

        System.out.print("Имя [" + character.getName() + "]: ");
        String input = scanner.nextLine();
        if (!input.isEmpty()) character.setName(input);

        System.out.print("Статус [" + character.getStatus() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) character.setStatus(input);

        System.out.print("Вид [" + character.getSpecies() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) character.setSpecies(input);

        System.out.print("Тип [" + character.getType() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) character.setType(input);

        System.out.print("Пол [" + character.getGender() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) character.setGender(input);

        System.out.print("Происхождение [" + character.getOriginName() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) character.setOriginName(input);

        System.out.print("Местоположение [" + character.getLocationName() + "]: ");
        input = scanner.nextLine();
        if (!input.isEmpty()) character.setLocationName(input);

        crudManager.updateCharacter(INPUT_FILE, character);
    }

    /**
     * Удаление персонажа
     */
    private static void deleteCharacterInteractive() throws Exception {
        int id = getIntInput("Введите ID персонажа для удаления: ");

        Optional<RickMortyCharacter> character = crudManager.readCharacter(INPUT_FILE, id);
        if (character.isPresent()) {
            System.out.println("\nПерсонаж для удаления:");
            printCharacter(character.get());

            System.out.print("\nВы уверены? (yes/no): ");
            String confirmation = scanner.nextLine();

            if ("yes".equalsIgnoreCase(confirmation)) {
                crudManager.deleteCharacter(INPUT_FILE, id);
            } else {
                System.out.println("Удаление отменено.");
            }
        } else {
            System.out.println("Персонаж с ID " + id + " не найден.");
        }
    }

    /**
     * Просмотр всех персонажей
     */
    private static void viewAllCharacters() throws Exception {
        System.out.println("=== ВСЕ ПЕРСОНАЖИ ===");
        List<RickMortyCharacter> characters = crudManager.readAllCharacters(INPUT_FILE);
        System.out.println("Всего персонажей: " + characters.size());
        System.out.println();

        for (RickMortyCharacter character : characters) {
            printCharacter(character);
        }
    }

    /**
     * Отображение информации о персонаже
     */
    private static void printCharacter(RickMortyCharacter c) {
        System.out.println("─".repeat(60));
        System.out.println("ID: " + c.getId());
        System.out.println("Имя: " + c.getName());
        System.out.println("Статус: " + c.getStatus());
        System.out.println("Вид: " + c.getSpecies());
        System.out.println("Тип: " + (c.getType() != null && !c.getType().isEmpty() ? c.getType() : "—"));
        System.out.println("Пол: " + c.getGender());
        System.out.println("Происхождение: " + c.getOriginName());
        System.out.println("Местоположение: " + c.getLocationName());
        System.out.println("Создан: " + c.getCreated());
    }

    /**
     * Отображение главного меню
     */
    private static void showMenu() {
        System.out.println("\n┌───────────────────────────────────────────────┐");
        System.out.println("│              ГЛАВНОЕ МЕНЮ                       │");
        System.out.println("├─────────────────────────────────────────────────┤");
        System.out.println("│ 1. Подсчёт видов в порядке появления            │");
        System.out.println("│ 2. CRUD операции                                │");
        System.out.println("│ 3. Просмотреть всех персонажей                  │");
        System.out.println("│ 0. Выход                                        │");
        System.out.println("└─────────────────────────────────────────────────┘");
    }

    /**
     * Получение целочисленного ввода от пользователя
     */
    private static int getIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите целое число.");
            }
        }
    }
}
