package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

public class Book {
    private String title;
    private String author;
    private int year;

    // Конструктор з параметрами
    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    // Гетери та сетери
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    // Перевизначений метод toString() для красивого виводу
    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }

    // Перевизначений метод equals() для порівняння об'єктів
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }
}