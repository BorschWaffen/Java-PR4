package ua.edu.sumdu.j2se.pr4;

import java.util.Scanner;

/**
 * Клас-драйвер із консольним меню.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Агрегація: створюємо об'єкт Library замість ArrayList у Main
        Library library = new Library("Центральна Бібліотека СумДУ");
        boolean running = true;
        Book lastAddedBook = null; // Зберігаємо останню додану книгу для демонстрації копіювання

        while (running) {
            System.out.println("\n--- " + library.getName() + " ---");
            // Демонстрація роботи статичного методу
            System.out.println("Загалом створено об'єктів Book у системі: " + Book.getBookCount());
            System.out.println("1. Додати нову книгу");
            System.out.println("2. Вивести всі книги бібліотеки");
            System.out.println("3. Створити копію останньої доданої книги (Конструктор копіювання)");
            System.out.println("4. Вийти");
            System.out.print("Оберіть дію: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    try {
                        System.out.print("Введіть назву: ");
                        String title = scanner.nextLine();

                        System.out.print("Введіть автора: ");
                        String author = scanner.nextLine();

                        System.out.print("Введіть рік: ");
                        int year = Integer.parseInt(scanner.nextLine().trim());

                        System.out.print("Введіть кількість сторінок: ");
                        int pages = Integer.parseInt(scanner.nextLine().trim());

                        System.out.println("Оберіть жанр: 1-Художня, 2-Фентезі, 3-Наукова, 4-Історія, 5-Інше");
                        int genreChoice = Integer.parseInt(scanner.nextLine().trim());
                        Genre genre;
                        switch (genreChoice) {
                            case 1: genre = Genre.FICTION; break;
                            case 2: genre = Genre.FANTASY; break;
                            case 3: genre = Genre.SCIENCE; break;
                            case 4: genre = Genre.HISTORY; break;
                            default: genre = Genre.OTHER; break;
                        }

                        Book newBook = new Book(title, author, year, pages, genre);
                        library.addBook(newBook); // Додаємо до об'єкта Library
                        lastAddedBook = newBook;
                        System.out.println("Книгу успішно додано!");

                    } catch (NumberFormatException e) {
                        System.out.println("Помилка: Числові значення (рік, сторінки, жанр) введено неправильно!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Помилка валідації: " + e.getMessage());
                    }
                    break;

                case "2":
                    if (library.isEmpty()) {
                        System.out.println("Бібліотека порожня.");
                    } else {
                        System.out.println("\n--- Список книг ---");
                        int i = 1;
                        for (Book b : library.getBooks()) {
                            System.out.println((i++) + ". " + b.toString());
                        }
                    }
                    break;

                case "3":
                    if (lastAddedBook == null) {
                        System.out.println("Ви ще не додали жодної книги для копіювання!");
                    } else {
                        // Виклик конструктора копіювання
                        Book copy = new Book(lastAddedBook);
                        library.addBook(copy);
                        System.out.println("Копію успішно створено та додано до бібліотеки!");
                    }
                    break;

                case "4":
                    System.out.println("Завершення роботи.");
                    running = false;
                    break;

                default:
                    System.out.println("Невідома команда. Спробуйте ще раз.");
            }
        }
        scanner.close();
    }
}