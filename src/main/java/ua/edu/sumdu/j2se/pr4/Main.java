package ua.edu.sumdu.j2se.pr4;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Створюємо масив з 5 об'єктів
        Book[] books = new Book[5];
        Scanner scanner = new Scanner(System.in);

        System.out.println("--- Введення даних для 2-х книг з клавіатури ---");

        // Зчитуємо 2 книги з клавіатури
        for (int i = 0; i < 2; i++) {
            System.out.println("Книга №" + (i + 1));
            System.out.print("Введіть назву: ");
            String title = scanner.nextLine();

            System.out.print("Введіть автора: ");
            String author = scanner.nextLine();

            System.out.print("Введіть рік видання: ");
            int year = scanner.nextInt();
            scanner.nextLine(); // Очищення буфера після введення числа

            books[i] = new Book(title, author, year);
        }

        // Автоматично (частково) заповнюємо ще 3 книги
        System.out.println("\n--- Автоматичне додання ще 3-х книг... ---");
        books[2] = new Book("Відьмак. Останнє бажання", "Анджей Сапковський", 1993);
        books[3] = new Book("1984", "Джордж Оруелл", 1949);
        books[4] = new Book("Дюна", "Френк Герберт", 1965);

        // Виводимо інформацію про всі створені об'єкти
        System.out.println("\n--- Список усіх створених книг ---");
        for (int i = 0; i < books.length; i++) {
            System.out.println((i + 1) + ". " + books[i].toString());
        }

        scanner.close();
    }
}