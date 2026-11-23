package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Клас-драйвер для Практичної роботи №10.
 * Реалізує меню з пошуком об'єктів у колекції за різними критеріями без її зміни.
 */
public class Main {
    private static final String DATA_FILE = "input.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Book> books = new ArrayList<>();
        boolean running = true;

        System.out.println("===================================================");
        System.out.println("Практична робота №10: Пошук у колекціях");
        System.out.println("===================================================");

        BookFileStorage.loadFromFile(DATA_FILE, books);

        while (running) {
            System.out.println("\n--- ГОЛОВНЕ МЕНЮ ---");
            System.out.println("1. Пошук об’єкта");
            System.out.println("2. Створити новий об’єкт");
            System.out.println("3. Вивести інформацію про всі об’єкти");
            System.out.println("4. Завершити роботу програми");
            System.out.print("Оберіть пункт меню: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    searchMenu(scanner, books);
                    break;

                case "2":
                    createObjectMenu(scanner, books);
                    break;

                case "3":
                    printAllBooks(books);
                    break;

                case "4":
                    BookFileStorage.saveToFile(DATA_FILE, books);
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
     * Підменю вибору критерію пошуку або повернення до головного меню.
     * Не змінює передану колекцію.
     *
     * @param scanner сканер вхідного потоку
     * @param books   колекція об'єктів Book
     */
    private static void searchMenu(Scanner scanner, ArrayList<Book> books) {
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

            if (books.isEmpty()) {
                System.out.println("Колекція порожня — пошук не дав результатів.");
                continue;
            }

            switch (criterionChoice) {
                case "1":
                    System.out.print("Введіть фрагмент назви для пошуку: ");
                    String titleQuery = scanner.nextLine().trim();
                    ArrayList<Book> byTitle = findBooksByTitle(books, titleQuery);
                    printSearchResults(byTitle, "за назвою \"" + titleQuery + "\"");
                    break;

                case "2":
                    Genre selectedGenre = readGenreForSearch(scanner);
                    if (selectedGenre != null) {
                        ArrayList<Book> byGenre = findBooksByGenre(books, selectedGenre);
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
                            ArrayList<Book> byPages = findBooksByMinPages(books, minPages);
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
     * Критерій 1: пошук усіх книг, у назві яких зустрічається заданий підрядок (без урахування регістру).
     *
     * @param books вихідна колекція (не модифікується)
     * @param query шуканий фрагмент назви
     * @return список знайдених збігів
     */
    private static ArrayList<Book> findBooksByTitle(ArrayList<Book> books, String query) {
        ArrayList<Book> matches = new ArrayList<>();
        String normalizedQuery = query.toLowerCase();

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getTitle().toLowerCase().contains(normalizedQuery)) {
                matches.add(book);
            }
        }
        return matches;
    }

    /**
     * Критерій 2: пошук усіх книг за вказаним жанром.
     *
     * @param books вихідна колекція (не модифікується)
     * @param genre жанр книги
     * @return список знайдених збігів
     */
    private static ArrayList<Book> findBooksByGenre(ArrayList<Book> books, Genre genre) {
        ArrayList<Book> matches = new ArrayList<>();

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getGenre() == genre) {
                matches.add(book);
            }
        }
        return matches;
    }

    /**
     * Критерій 3: пошук усіх книг із кількістю сторінок не менше заданого порогу.
     *
     * @param books    вихідна колекція (не модифікується)
     * @param minPages мінімальна кількість сторінок
     * @return список знайдених збігів
     */
    private static ArrayList<Book> findBooksByMinPages(ArrayList<Book> books, int minPages) {
        ArrayList<Book> matches = new ArrayList<>();

        for (int i = 0; i < books.size(); i++) {
            Book book = books.get(i);
            if (book.getPages() >= minPages) {
                matches.add(book);
            }
        }
        return matches;
    }

    /**
     * Виводить результати пошуку або повідомлення про відсутність збігів.
     *
     * @param results         список знайдених об'єктів
     * @param criterionLabel опис критерію для виводу
     */
    private static void printSearchResults(ArrayList<Book> results, String criterionLabel) {
        if (results.isEmpty()) {
            System.out.println("Жоден об'єкт не відповідає умовам пошуку (" + criterionLabel + ").");
            return;
        }

        System.out.println("\n--- РЕЗУЛЬТАТИ ПОШУКУ (" + criterionLabel + "), знайдено: " + results.size() + " ---");
        for (int i = 0; i < results.size(); i++) {
            Book item = results.get(i);
            System.out.println((i + 1) + ". [" + item.getEditionInfo() + "] " + item.toString());
        }
    }

    /**
     * Зчитує вибір жанру для пошуку.
     *
     * @param scanner сканер вхідного потоку
     * @return обраний жанр або null при некоректному вводі
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

    /**
     * Перетворює числовий вибір на значення enum Genre.
     *
     * @param genreChoice номер пункту (1-5)
     * @return відповідне значення Genre
     */
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
     * Підменю створення нового об'єкта обраного типу з можливістю повернення назад.
     *
     * @param scanner сканер вхідного потоку
     * @param books   колекція створених об'єктів
     */
    private static void createObjectMenu(Scanner scanner, ArrayList<Book> books) {
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
                        books.add(base);
                        System.out.println("Об'єкт Book додано до колекції.");
                        return;
                    }
                    case "2": {
                        Book base = readBaseBook(scanner);
                        System.out.print("Введіть розмір файлу (МБ): ");
                        double sizeMb = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                        System.out.print("Введіть формат файлу (наприклад, EPUB, PDF): ");
                        String format = scanner.nextLine();

                        EBook eBook = new EBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), sizeMb, format
                        );
                        books.add(eBook);
                        System.out.println("Об'єкт EBook додано до колекції.");
                        return;
                    }
                    case "3": {
                        Book base = readBaseBook(scanner);
                        System.out.print("Введіть вагу (грами): ");
                        int weight = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть тип палітурки: ");
                        String cover = scanner.nextLine();

                        PaperBook paperBook = new PaperBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), weight, cover
                        );
                        books.add(paperBook);
                        System.out.println("Об'єкт PaperBook додано до колекції.");
                        return;
                    }
                    case "4": {
                        Book base = readBaseBook(scanner);
                        System.out.print("Введіть тривалість у хвилинах: ");
                        int duration = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть ім'я диктора: ");
                        String narrator = scanner.nextLine();

                        AudioBook audioBook = new AudioBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), duration, narrator
                        );
                        books.add(audioBook);
                        System.out.println("Об'єкт AudioBook додано до колекції.");
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

                        ReferenceBook refBook = new ReferenceBook(
                                base.getTitle(), base.getAuthor(), base.getYear(),
                                base.getPages(), base.getGenre(), weight, cover, subject, hasIndex
                        );
                        books.add(refBook);
                        System.out.println("Об'єкт ReferenceBook додано до колекції.");
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
     * Виводить інформацію про всі об'єкти з колекції через поліморфізм.
     *
     * @param books колекція об'єктів Book
     */
    private static void printAllBooks(ArrayList<Book> books) {
        if (books.isEmpty()) {
            System.out.println("Колекція порожня. Спочатку створіть хоча б один об'єкт.");
            return;
        }

        System.out.println("\n--- УСІ ОБ'ЄКТИ В КОЛЕКЦІЇ (всього: " + books.size() + ") ---");
        for (int i = 0; i < books.size(); i++) {
            Book item = books.get(i);
            System.out.println((i + 1) + ". [" + item.getEditionInfo() + "] " + item.toString());
        }
    }

    /**
     * Зчитує базові атрибути книги з консолі.
     *
     * @param scanner сканер вхідного потоку
     * @return об'єкт Book з базовими полями
     */
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

    /**
     * Розпізнає логічне значення з тексту (true/false/так/ні/1/0).
     *
     * @param input рядок користувача
     * @return логічне значення
     */
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