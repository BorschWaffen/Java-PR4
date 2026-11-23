package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Клас-драйвер для демонстрації наслідування, поліморфізму та роботи з ArrayList.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Book> books = new ArrayList<>();
        boolean running = true;

        System.out.println("=========================================");
        System.out.println("Практична робота №7: Книги (Book / EBook / PaperBook)");
        System.out.println("=========================================");

        while (running) {
            System.out.println("\n--- МЕНЮ ---");
            System.out.println("1. Додати звичайну книгу (Book)");
            System.out.println("2. Додати електронну книгу (EBook)");
            System.out.println("3. Додати паперову книгу (PaperBook)");
            System.out.println("4. Вивести всі книги (поліморфний перегляд)");
            System.out.println("5. Вийти");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        Book book = readBaseBook(scanner);
                        books.add(book);
                        System.out.println("Звичайну книгу додано.");
                        break;

                    case "2":
                        Book baseForEBook = readBaseBook(scanner);
                        System.out.print("Введіть розмір файлу (МБ): ");
                        double sizeMb = Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
                        System.out.print("Введіть формат (наприклад, EPUB, PDF): ");
                        String format = scanner.nextLine();

                        EBook eBook = new EBook(
                                baseForEBook.getTitle(),
                                baseForEBook.getAuthor(),
                                baseForEBook.getYear(),
                                baseForEBook.getPages(),
                                baseForEBook.getGenre(),
                                sizeMb,
                                format
                        );
                        books.add(eBook);
                        System.out.println("Електронну книгу додано.");
                        break;

                    case "3":
                        Book baseForPaper = readBaseBook(scanner);
                        System.out.print("Введіть вагу (грами): ");
                        int weight = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Введіть тип палітурки (наприклад, Тверда, М'яка): ");
                        String cover = scanner.nextLine();

                        PaperBook paperBook = new PaperBook(
                                baseForPaper.getTitle(),
                                baseForPaper.getAuthor(),
                                baseForPaper.getYear(),
                                baseForPaper.getPages(),
                                baseForPaper.getGenre(),
                                weight,
                                cover
                        );
                        books.add(paperBook);
                        System.out.println("Паперову книгу додано.");
                        break;

                    case "4":
                        if (books.isEmpty()) {
                            System.out.println("Список книг порожній.");
                        } else {
                            System.out.println("\n--- Усі книги в колекції ---");
                            for (int i = 0; i < books.size(); i++) {
                                Book current = books.get(i);
                                System.out.println((i + 1) + ". [" + current.getEditionInfo() + "] " + current.toString());
                            }
                        }
                        break;

                    case "5":
                        System.out.println("Завершення роботи.");
                        running = false;
                        break;

                    default:
                        System.out.println("Невідома команда. Спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Помилка: очікувалося коректне числове значення.");
            } catch (IllegalArgumentException e) {
                System.out.println("Помилка валідації: " + e.getMessage());
            }
        }

        scanner.close();
    }

    /**
     * Зчитує спільні поля базового класу Book з консолі.
     *
     * @param scanner сканер вхідного потоку
     * @return створений об'єкт Book
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
}