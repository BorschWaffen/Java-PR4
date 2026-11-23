package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;

/**
 * Клас-драйвер для Практичної роботи №14.
 * Використовує анонімні внутрішні класи Comparator для сортування колекції за трьома критеріями.
 */
public class Main {
    private static final String DATA_FILE = "input.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library("Бібліотека СумДУ");
        boolean running = true;

        System.out.println("===================================================");
        System.out.println("Практична робота №14: Внутрішні класи та Comparator");
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
     * Підменю вибору критерію сортування з використанням анонімних внутрішніх класів Comparator.
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
                comparator = new Comparator<BookEntry>() {
                    @Override
                    public int compare(BookEntry o1, BookEntry o2) {
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
                    }
                };
            } else if ("2".equals(sortChoice)) {
                criterionName = "за роком видання (від старіших до новіших)";
                comparator = new Comparator<BookEntry>() {
                    @Override
                    public int compare(BookEntry o1, BookEntry o2) {
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
                    }
                };
            } else if ("3".equals(sortChoice)) {
                criterionName = "за кількістю сторінок (від менших до більших)";
                comparator = new Comparator<BookEntry>() {
                    @Override
                    public int compare(BookEntry o1, BookEntry o2) {
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
                    }
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
                        System.out.println("Операцію додавання Book виконано.");
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
                        System.out.println("Операцію додавання EBook виконано.");
                        return;
                    }
                    case "3": {
                        BookData data = readBookData(scanner);
                        System.out.print("Введіть вагу (грами): ");
                        int weight = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть тип палітурки: ");
                        String cover = scanner.nextLine();
                        int qty = readQuantity(scanner);

                        PaperBook paperBook = new PaperBook(
                                data.title, data.author, data.year,
                                data.pages, data.genre, weight, cover
                        );
                        library.addNewBook(paperBook, qty);
                        System.out.println("Операцію додавання PaperBook виконано.");
                        return;
                    }
                    case "4": {
                        BookData data = readBookData(scanner);
                        System.out.print("Введіть тривалість у хвилинах: ");
                        int duration = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть ім'я диктора: ");
                        String narrator = scanner.nextLine();
                        int qty = readQuantity(scanner);

                        AudioBook audioBook = new AudioBook(
                                data.title, data.author, data.year,
                                data.pages, data.genre, duration, narrator
                        );
                        library.addNewBook(audioBook, qty);
                        System.out.println("Операцію додавання AudioBook виконано.");
                        return;
                    }
                    case "5": {
                        BookData data = readBookData(scanner);
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
                                data.title, data.author, data.year,
                                data.pages, data.genre, weight, cover, subject, hasIndex
                        );
                        library.addNewBook(refBook, qty);
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

    private static BookData readBookData(Scanner scanner) {
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

        return new BookData(title, author, year, pages, genre);
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

    /**
     * Допоміжна структура для тимчасового зберігання базових полів книги під час вводу.
     */
    private static final class BookData {
        private final String title;
        private final String author;
        private final int year;
        private final int pages;
        private final Genre genre;

        private BookData(String title, String author, int year, int pages, Genre genre) {
            this.title = title;
            this.author = author;
            this.year = year;
            this.pages = pages;
            this.genre = genre;
        }
    }
}