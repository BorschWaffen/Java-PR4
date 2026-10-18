package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;

/**
 * Клас, що демонструє агрегацію.
 * Бібліотека агрегує (містить) колекцію об'єктів Book.
 */
public class Library {
    private String name;
    private ArrayList<Book> books;

    public Library(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва бібліотеки не може бути порожньою.");
        }
        this.name = name;
        this.books = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Метод для додавання книги в бібліотеку.
     */
    public void addBook(Book book) {
        if (book != null) {
            books.add(book);
        }
    }

    /**
     * Повертає список усіх книг у бібліотеці.
     */
    public ArrayList<Book> getBooks() {
        return books;
    }

    /**
     * Перевіряє, чи порожня бібліотека.
     */
    public boolean isEmpty() {
        return books.isEmpty();
    }
}