package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Клас-драйвер для Практичної роботи №12.
 * Підтримує ієрархію з 5 класів, поліморфізм, пошук у колекції Library,
 * роботу з файлом input.txt та збереження створених об'єктів у БД через JDBC.
 */
public class Main {
    private static final String DATA_FILE = "input.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library("Бібліотека СумДУ");
        boolean running = true;

        System.out.println("===================================================");
        System.out.println("Практична робота №12: Інтеграція з базою даних (JDBC)");
        System.out.println("===================================================");

        DatabaseManager dbManager = initializeDatabaseManager(args);
        BookFileStorage.loadFromFile(DATA_FILE, library);

        while (running) {
            System.out.println("\n--- ГОЛОВНЕ МЕНЮ (" + library.getName() + ") ---");
            System.out.println("1. Пошук об’єкта");
            System.out.println("2. Створити новий об’єкт");
            System.out.println("3. Вивести інформацію про всі об’єкти");
            System.out.println("4. Завершити роботу програми");
            System.out.print("Оберіть пункт меню: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    searchMenu(scanner, library);
                    break;

                case "2":
                    createObjectMenu(scanner, library, dbManager);
                    break;

                case "3":
                    printAllEntries(library);
                    break;

                case "4":
                    BookFileStorage.saveToFile(DATA_FILE, library);
                    System.out.println("Роботу програми завершено.");
                    running = false;
                    break;

                default:
                    System.out.println("Некоректний вибір. Введіть число від 1 до 4.");
            }
        }

        scanner.close();
    }

    /**
     * Ініціалізує DatabaseManager на основі шляху до конфіг-файлу, переданого через args.
     *
     * @param args аргументи командного рядка
     * @return екземпляр DatabaseManager або null, якщо конфігурація недоступна
     */
    private static DatabaseManager initializeDatabaseManager(String[] args) {
        if (args == null || args.length == 0) {
            System.out.println("Попередження: шлях до файлу конфігурації БД не передано через args.");
            return null;
        }

        String configPath = args[0].trim();
        if (configPath.isEmpty()) {
            System.out.println("Попередження: передано порожній шлях до файлу конфігурації БД.");
            return null;
        }

        try {
            DatabaseManager manager = new DatabaseManager(configPath);
            System.out.println("Конфігурацію БД успішно завантажено з файлу: " + configPath);
            return manager;
        } catch (Exception e) {
            System.out.println("Помилка ініціалізації підключення до БД (" + configPath + "): " + e.getMessage());
            return null;
        }
    }

    /**
     * Зберігає створений об'єкт у БД, якщо DatabaseManager ініціалізований.
     *
     * @param dbManager менеджер бази даних
     * @param book      створений об'єкт
     */
    private static void saveToDatabaseIfAvailable(DatabaseManager dbManager, Book book) {
        if (dbManager == null) {
            System.out.println("Запис у БД пропущено: DatabaseManager не ініціалізовано.");
            return;
        }

        boolean inserted = dbManager.insertBook(book);
        if (inserted) {
            System.out.println("Запис успішно додано до таблиці books у базі даних.");
        } else {
            System.out.println("Не вдалося додати запис до бази даних.");
        }
    }

    /**
     * Підменю пошуку у бібліотеці без зміни її колекції.
     *
     * @param scanner сканер вхідного потоку
     * @param library бібліотека
     */
    private static void searchMenu(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- ПОШУК ОБ'ЄКТА: ОБЕРІТЬ КРИТЕРІЙ ---");
            System.out.println("1. За назвою (містить текст)");
            System.out.println("2. За жанром");
            System.out.println("3. За мінімальною кількістю сторінок");
            System.out.println("0. Повернутися до головного меню");
            System.out.print("Ваш вибір: ");

            String criterionChoice = scanner.nextLine().trim();

            if ("0".equals(criterionChoice)) {
                System.out.println("Повернення до головного меню.");
                return;
            }

            if (library.isEmpty()) {
                System.out.println("Бібліотека порожня — пошук не дав результатів.");
                continue;
            }

            switch (criterionChoice) {
                case "1":
                    System.out.print("Введіть фрагмент назви для пошуку: ");
                    String titleQuery = scanner.nextLine().trim();
                    ArrayList<BookEntry> byTitle = library.findBooksByTitle(titleQuery);
                    printSearchResults(byTitle, "за назвою \"" + titleQuery + "\"");
                    break;

                case "2":
                    Genre selectedGenre = readGenreForSearch(scanner);
                    if (selectedGenre != null) {
                        ArrayList<BookEntry> byGenre = library.findBooksByGenre(selectedGenre);
                        printSearchResults(byGenre, "за жанром \"" + selectedGenre.getDescription() + "\"");
                    }
                    break;

                case "3":
                    System.out.print("Введіть мінімальну кількість сторінок: ");
                    String pagesRaw = scanner.nextLine().trim();
                    try {
                        int minPages = Integer.parseInt(pagesRaw);
                        if (minPages <= 0) {
                            System.out.println("Помилка: мінімальна кількість сторінок має бути більшою за нуль.");
                        } else {
                            ArrayList<BookEntry> byPages = library.findBooksByMinPages(minPages);
                            printSearchResults(byPages, "з кількістю сторінок >= " + minPages);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Помилка: введіть коректне ціле число.");
                    }
                    break;

                default:
                    System.out.println("Невідомий критерій пошуку. Оберіть 1–3 або 0 для виходу.");
            }
        }
    }

    /**
     * Виводить результати пошуку або повідомлення про відсутність збігів.
     *
     * @param results        список знайдених позицій
     * @param criterionLabel опис критерію
     */
    private static void printSearchResults(ArrayList<BookEntry> results, String criterionLabel) {
        if (results.isEmpty()) {
            System.out.println("Жоден об'єкт не відповідає умовам пошуку (" + criterionLabel + ").");
            return;
        }

        System.out.println("\n--- РЕЗУЛЬТАТИ ПОШУКУ (" + criterionLabel + "), знайдено: " + results.size() + " ---");
        for (int i = 0; i < results.size(); i++) {
            System.out.println((i + 1) + ". " + results.get(i).toString());
        }
    }

    /**
     * Зчитує вибір жанру для пошуку.
     *
     * @param scanner сканер вхідного потоку
     * @return обраний жанр або null
     */
    private static Genre readGenreForSearch(Scanner scanner) {
        System.out.println("Оберіть жанр: 1-Художня, 2-Фентезі, 3-Наукова, 4-Історія, 5-Інше");
        System.out.print("Ваш вибір: ");
        String raw = scanner.nextLine().trim();
        try {
            int genreChoice = Integer.parseInt(raw);
            return mapGenreChoice(genreChoice);
        } catch (NumberFormatException e) {
            System.out.println("Помилка: введіть номер жанру від 1 до 5.");
            return null;
        }
    }

    private static Genre mapGenreChoice(int genreChoice) {
        switch (genreChoice) {
            case 1:
                return Genre.FICTION;
            case 2:
                return Genre.FANTASY;
            case 3:
                return Genre.SCIENCE;
            case 4:
                return Genre.HISTORY;
            default:
                return Genre.OTHER;
        }
    }

    /**
     * Підменю створення об'єкта, додавання до бібліотеки та збереження в БД.
     *
     * @param scanner   сканер вхідного потоку
     * @param library   бібліотека
     * @param dbManager менеджер бази даних
     */
    private static void createObjectMenu(Scanner scanner, Library library, DatabaseManager dbManager) {
        while (true) {
            System.out.println("\n--- СТВОРЕННЯ ОБ'ЄКТА: ОБЕРІТЬ ТИП ---");
            System.out.println("1. Book (звичайна книга)");
            System.out.println("2. EBook (електронна книга)");
            System.out.println("3. PaperBook (паперова книга)");
            System.out.println("4. AudioBook (аудіокнига)");
            System.out.println("5. ReferenceBook (довідкове видання)");
            System.out.println("0. Повернутися до головного меню");
            System.out.print("Ваш вибір: ");

            String typeChoice = scanner.nextLine().trim();

            if ("0".equals(typeChoice)) {
                System.out.println("Повернення до головного меню без створення об'єкта.");
                return;
            }

            try {
                switch (typeChoice) {
                    case "1": {
                        Book base = readBaseBook(scanner);
                        int qty = readQuantity(scanner);
                        library.addNewBook(base, qty);
                        saveToDatabaseIfAvailable(dbManager, base);
                        System.out.println("Операцію додавання Book виконано.");
                        return;
                    }
                    case "2": {
                        Book base = readBaseBook(scanner);
                        System.out.print("Введіть розмір файлу (МБ): ");
                        double sizeMb = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                        System.out.print("Введіть формат файлу (наприклад, EPUB, PDF): ");
                        String format = scanner.nextLine();
                        int qty = readQuantity(scanner);

                        EBook eBook = new EBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), sizeMb, format
                        );
                        library.addNewBook(eBook, qty);
                        saveToDatabaseIfAvailable(dbManager, eBook);
                        System.out.println("Операцію додавання EBook виконано.");
                        return;
                    }
                    case "3": {
                        Book base = readBaseBook(scanner);
                        System.out.print("Введіть вагу (грами): ");
                        int weight = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть тип палітурки: ");
                        String cover = scanner.nextLine();
                        int qty = readQuantity(scanner);

                        PaperBook paperBook = new PaperBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), weight, cover
                        );
                        library.addNewBook(paperBook, qty);
                        saveToDatabaseIfAvailable(dbManager, paperBook);
                        System.out.println("Операцію додавання PaperBook виконано.");
                        return;
                    }
                    case "4": {
                        Book base = readBaseBook(scanner);
                        System.out.print("Введіть тривалість у хвилинах: ");
                        int duration = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть ім'я диктора: ");
                        String narrator = scanner.nextLine();
                        int qty = readQuantity(scanner);

                        AudioBook audioBook = new AudioBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), duration, narrator
                        );
                        library.addNewBook(audioBook, qty);
                        saveToDatabaseIfAvailable(dbManager, audioBook);
                        System.out.println("Операцію додавання AudioBook виконано.");
                        return;
                    }
                    case "5": {
                        Book base = readBaseBook(scanner);
                        System.out.print("Введіть вагу (грами): ");
                        int weight = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть тип палітурки: ");
                        String cover = scanner.nextLine();
                        System.out.print("Введіть предметну галузь: ");
                        String subject = scanner.nextLine();
                        System.out.print("Чи є покажчик (true/false або так/ні): ");
                        boolean hasIndex = parseBooleanInput(scanner.nextLine().trim());
                        int qty = readQuantity(scanner);

                        ReferenceBook refBook = new ReferenceBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), weight, cover, subject, hasIndex
                        );
                        library.addNewBook(refBook, qty);
                        saveToDatabaseIfAvailable(dbManager, refBook);
                        System.out.println("Операцію додавання ReferenceBook виконано.");
                        return;
                    }
                    default:
                        System.out.println("Невідомий тип об'єкта. Спробуйте ще раз або натисніть 0 для виходу.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Помилка введення: очікувалося коректне числове значення.");
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка валідації: " + e.getMessage());
            }
        }
    }

    /**
     * Виводить усі позиції з бібліотеки та їхню кількість.
     *
     * @param library бібліотека
     */
    private static void printAllEntries(Library library) {
        if (library.isEmpty()) {
            System.out.println("Бібліотека порожня. Спочатку створіть хоча б один об'єкт.");
            return;
        }

        System.out.println("\n--- УСІ ОБ'ЄКТИ В БІБЛІОТЕЦІ (позицій: " + library.size() + ") ---");
        for (int i = 0; i < library.size(); i++) {
            System.out.println((i + 1) + ". " + library.getEntry(i).toString());
        }
    }

    private static int readQuantity(Scanner scanner) {
        System.out.print("Введіть кількість примірників: ");
        return Integer.parseInt(scanner.nextLine().trim());
    }

    private static Book readBaseBook(Scanner scanner) {
        System.out.print("Введіть назву: ");
        String title = scanner.nextLine();

        System.out.print("Введіть автора: ");
        String author = scanner.nextLine();

        System.out.print("Введіть рік видання: ");
        int year = Integer.parseInt(scanner.nextLine().trim());

        System.out.print("Введіть кількість сторінок: ");
        int pages = Integer.parseInt(scanner.nextLine().trim());

        System.out.println("Оберіть жанр: 1-Художня, 2-Фентезі, 3-Наукова, 4-Історія, 5-Інше");
        int genreChoice = Integer.parseInt(scanner.nextLine().trim());
        Genre genre = mapGenreChoice(genreChoice);

        return new Book(title, author, year, pages, genre);
    }

    private static boolean parseBooleanInput(String input) {
        String normalized = input.toLowerCase();
        if ("true".equals(normalized) || "так".equals(normalized) || "yes".equals(normalized) || "1".equals(normalized)) {
            return true;
        }
        if ("false".equals(normalized) || "ні".equals(normalized) || "no".equals(normalized) || "0".equals(normalized)) {
            return false;
        }
        throw new IllegalArgumentException("Очікувалося true/false або так/ні.");
    }
}