package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Клас-драйвер із консольним меню.
 */
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Book> books = new ArrayList<>();
        boolean running = true;

        while (running) {
            System.out.println("\n--- МЕНЮ ---");
            System.out.println("1. Створити нову книгу");
            System.out.println("2. Вивести всі книги");
            System.out.println("3. Вийти");
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

                        // Спроба створення об'єкта. Якщо дані не пройдуть валідацію, викинеться IllegalArgumentException
                        Book newBook = new Book(title, author, year, pages);
                        books.add(newBook);
                        System.out.println("Книгу успішно додано!");

                    } catch (NumberFormatException e) {
                        System.out.println("Помилка: Рік та кількість сторінок повинні бути числами!");
                    } catch (IllegalArgumentException e) {
                        System.out.println("Помилка валідації: " + e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Невідома помилка: " + e.getMessage());
                    }
                    break;

                case "2":
                    if (books.isEmpty()) {
                        System.out.println("Список книг порожній.");
                    } else {
                        System.out.println("\n--- Список книг ---");
                        for (int i = 0; i < books.size(); i++) {
                            System.out.println((i + 1) + ". " + books.get(i).toString());
                        }
                    }
                    break;

                case "3":
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