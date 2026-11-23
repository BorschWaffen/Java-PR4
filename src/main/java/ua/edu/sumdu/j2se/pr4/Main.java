package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Клас-драйвер для Лабораторної роботи №16.
 * Підтримує абстрактний базовий клас з UUID та Comparable, ієрархію нащадків,
 * пошук у колекції (включно з пошуком за UUID), сортування через Comparator та роботу з файлом.
 */
public class Main {
    private static final String DATA_FILE = "input.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library("Бібліотека СумДУ");
        boolean running = true;

        System.out.println("===================================================");
        System.out.println("Лабораторна робота №16: UUID та JavaFX GUI");
        System.out.println("===================================================");

        BookFileStorage.loadFromFile(DATA_FILE, library);

        while (running) {
            System.out.println("\n--- ГОЛОВНЕ МЕНЮ (" + library.getName() + ") ---");
            System.out.println("1. Пошук об’єкта");
            System.out.println("2. Створити новий об’єкт");
            System.out.println("3. Вивести інформацію про всі об’єкти");
            System.out.println("4. Вивести відсортовану інформацію про всі книги");
            System.out.println("5. Завершити роботу програми");
            System.out.print("Оберіть пункт меню: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    searchMenu(scanner, library);
                    break;

                case "2":
                    createObjectMenu(scanner, library);
                    break;

                case "3":
                    printAllEntries(library);
                    break;

                case "4":
                    sortMenu(scanner, library);
                    break;

                case "5":
                    BookFileStorage.saveToFile(DATA_FILE, library);
                    System.out.println("Роботу програми завершено.");
                    running = false;
                    break;

                default:
                    System.out.println("Некоректний вибір. Введіть число від 1 до 5.");
            }
        }

        scanner.close();
    }

    /**
     * Підменю вибору критерію сортування з використанням лямбда-виразів Comparator.
     *
     * @param scanner сканер вхідного потоку
     * @param library бібліотека
     */
    private static void sortMenu(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- СОРТУВАННЯ: ОБЕРІТЬ КРИТЕРІЙ ---");
            System.out.println("1. Сортувати за назвою (А-Я)");
            System.out.println("2. Сортувати за роком видання (від старіших до новіших)");
            System.out.println("3. Сортувати за кількістю сторінок (від менших до більших)");
            System.out.println("0. Повернутися в головне меню");
            System.out.print("Ваш вибір: ");

            String sortChoice = scanner.nextLine().trim();

            if ("0".equals(sortChoice)) {
                System.out.println("Повернення в головне меню.");
                return;
            }

            Comparator<BookEntry> comparator = null;
            String criterionName = "";

            if ("1".equals(sortChoice)) {
                criterionName = "за назвою (А-Я)";
                comparator = (o1, o2) -> {
                    if (o1 == o2) {
                        return 0;
                    }
                    if (o1 == null) {
                        return -1;
                    }
                    if (o2 == null) {
                        return 1;
                    }
                    int byTitle = o1.getBook().getTitle().compareToIgnoreCase(o2.getBook().getTitle());
                    if (byTitle != 0) {
                        return byTitle;
                    }
                    return Integer.compare(o1.getBook().getYear(), o2.getBook().getYear());
                };
            } else if ("2".equals(sortChoice)) {
                criterionName = "за роком видання (від старіших до новіших)";
                comparator = (o1, o2) -> {
                    if (o1 == o2) {
                        return 0;
                    }
                    if (o1 == null) {
                        return -1;
                    }
                    if (o2 == null) {
                        return 1;
                    }
                    int byYear = Integer.compare(o1.getBook().getYear(), o2.getBook().getYear());
                    if (byYear != 0) {
                        return byYear;
                    }
                    return o1.getBook().getTitle().compareToIgnoreCase(o2.getBook().getTitle());
                };
            } else if ("3".equals(sortChoice)) {
                criterionName = "за кількістю сторінок (від менших до більших)";
                comparator = (o1, o2) -> {
                    if (o1 == o2) {
                        return 0;
                    }
                    if (o1 == null) {
                        return -1;
                    }
                    if (o2 == null) {
                        return 1;
                    }
                    int byPages = Integer.compare(o1.getBook().getPages(), o2.getBook().getPages());
                    if (byPages != 0) {
                        return byPages;
                    }
                    return o1.getBook().getTitle().compareToIgnoreCase(o2.getBook().getTitle());
                };
            } else {
                System.out.println("Некоректний вибір критерію. Оберіть 1–3 або 0 для повернення.");
                continue;
            }

            if (library.isEmpty()) {
                System.out.println("Список книг порожній. Немає об'єктів для виведення (" + criterionName + ").");
                continue;
            }

            ArrayList<BookEntry> sorted = library.getSortedEntries(comparator);
            System.out.println("\n--- ВІДСОРТОВАНІ ОБ'ЄКТИ (" + criterionName + ", позицій: " + sorted.size() + ") ---");
            for (int i = 0; i < sorted.size(); i++) {
                System.out.println((i + 1) + ". " + sorted.get(i).toString());
            }
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
            System.out.println("4. За UUID");
            System.out.println("0. Повернутися до головного меню");
            System.out.print("Ваш вибір: ");

            String criterionChoice = scanner.nextLine().trim();

            if ("0".equals(criterionChoice)) {
                System.out.println("Повернення до головного меню.");
                return;
            }

            if (library.isEmpty() && !"4".equals(criterionChoice)) {
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

                case "4":
                    System.out.print("Введіть UUID об'єкта: ");
                    String uuidInput = scanner.nextLine().trim();
                    try {
                        BookEntry found = library.findBookByUuid(uuidInput);
                        if (found == null) {
                            System.out.println("Об'єкт із вказаним UUID не знайдено.");
                        } else {
                            System.out.println("\n--- ЗНАЙДЕНО ЗА UUID ---");
                            System.out.println(found.toString());
                        }
                    } catch (IllegalArgumentException e) {
                        System.out.println("Помилка: некоректний формат UUID (" + e.getMessage() + ").");
                    }
                    break;

                default:
                    System.out.println("Невідомий критерій пошуку. Оберіть 1–4 або 0 для виходу.");
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
     * Підменю створення об'єкта та додавання до бібліотеки з урахуванням кількості.
     *
     * @param scanner сканер вхідного потоку
     * @param library бібліотека
     */
    private static void createObjectMenu(Scanner scanner, Library library) {
        while (true) {
            System.out.println("\n--- СТВОРЕННЯ ОБ'ЄКТА: ОБЕРІТЬ ТИП ---");
            System.out.println("1. Book (звичайне видання)");
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
                        BookData data = readBookData(scanner);
                        int qty = readQuantity(scanner);
                        Book standardBook = new StandardBook(
                                data.title, data.author, data.year, data.pages, data.genre
                        );
                        library.addNewBook(standardBook, qty);
                        System.out.println("Операцію додавання Book виконано. UUID: " + standardBook.getUuid());
                        return;
                    }
                    case "2": {
                        BookData data = readBookData(scanner);
                        System.out.print("Введіть розмір файлу (МБ): ");
                        double sizeMb = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                        System.out.print("Введіть формат файлу (наприклад, EPUB, PDF): ");
                        String format = scanner.nextLine();
                        int qty = readQuantity(scanner);

                        EBook eBook = new EBook(
                                data.title, data.author, data.year,
                                data.pages, data.genre, sizeMb, format
                        );
                        library.addNewBook(eBook, qty);
                        System.out.println("Операцію додавання EBook виконано. UUID: " + eBook.getUuid());
                        return;
                    }
                    case "3": {
                        BookData data = readBookData(scanner);
                        System.out.print("Введіть вагу (грами): ");
                        int weight = Integer