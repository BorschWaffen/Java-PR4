package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

/**
 * Клас, що представляє книгу.
 * Містить перевірку вхідних даних.
 */
public class Book {
    private String title;
    private String author;
    private int year;
    private int pages; // Новий (4-й) атрибут

    /**
     * Конструктор з повною валідацією параметрів.
     */
    public Book(String title, String author, int year, int pages) {
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPages(pages);
    }

    public String getTitle() { return title; }

    /**
     * Встановлює назву. Не може бути порожньою.
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва книги не може бути порожньою.");
        }
        this.title = title;
    }

    public String getAuthor() { return author; }

    /**
     * Встановлює автора. Не може бути порожнім.
     */
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Ім'я автора не може бути порожнім.");
        }
        this.author = author;
    }

    public int getYear() { return year; }

    /**
     * Встановлює рік. Не може бути меншим за 1 і більшим за поточний (2026).
     */
    public void setYear(int year) {
        if (year <= 0 || year > 2026) {
            throw new IllegalArgumentException("Некоректний рік видання. Має бути від 1 до 2026.");
        }
        this.year = year;
    }

    public int getPages() { return pages; }

    /**
     * Встановлює кількість сторінок. Повинна бути більше 0.
     */
    public void setPages(int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Кількість сторінок має бути більшою за нуль.");
        }
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", pages=" + pages +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year && pages == book.pages &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }
}