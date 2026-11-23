package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Клас-драйвер для Практичної роботи №8.
 * Демонструє розширену ієрархію з 5 класів, поліморфізм та роботу з ArrayList.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Book> books = new ArrayList<>();
        boolean running = true;

        System.out.println("===================================================");
        System.out.println("Практична робота №8: Ієрархія класів та поліморфізм");
        System.out.println("===================================================");

        while (running) {
            System.out.println("\n--- ГОЛОВНЕ МЕНЮ ---");
            System.out.println("1. Створити новий об’єкт");
            System.out.println("2. Вивести інформацію про всі об’єкти");
            System.out.println("3. Завершити роботу програми");
            System.out.print("Оберіть пункт меню: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    createObjectMenu(scanner, books);
                    break;

                case "2":
                    printAllBooks(books);
                    break;

                case "3":
                    System.out.println("Роботу програми завершено.");
                    running = false;
                    break;

                default:
                    System.out.println("Некоректний вибір. Введіть 1, 2 або 3.");
            }
        }

        scanner.close();
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

        Genre genre;
        switch (genreChoice) {
            case 1:
                genre = Genre.FICTION;
                break;
            case 2:
                genre = Genre.FANTASY;
                break;
            case 3:
                genre = Genre.SCIENCE;
                break;
            case 4:
                genre = Genre.HISTORY;
                break;
            default:
                genre = Genre.OTHER;
                break;
        }

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