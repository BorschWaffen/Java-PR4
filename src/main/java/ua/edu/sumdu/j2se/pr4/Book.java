package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

/**
 * Клас, що представляє книгу.
 * Демонструє статичні поля, конструктор копіювання та використання enum.
 */
public class Book {
    // Статичне поле для підрахунку створених об'єктів
    private static int bookCount = 0;

    private String title;
    private String author;
    private int year;
    private int pages;
    private Genre genre; // Використання enum

    /**
     * Основний конструктор з параметрами.
     */
    public Book(String title, String author, int year, int pages, Genre genre) {
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPages(pages);
        setGenre(genre);
        bookCount++; // Збільшуємо лічильник при створенні
    }

    /**
     * Конструктор копіювання.
     * @param other об'єкт для копіювання
     */
    public Book(Book other) {
        if (other == null) {
            throw new IllegalArgumentException("Об'єкт для копіювання не може бути null");
        }
        this.title = other.title;
        this.author = other.author;
        this.year = other.year;
        this.pages = other.pages;
        this.genre = other.genre;
        bookCount++; // Це теж новий об'єкт, тому збільшуємо лічильник
    }

    /**
     * Статичний метод для отримання загальної кількості створених книг.
     */
    public static int getBookCount() {
        return bookCount;
    }

    // --- Гетери та сетери ---

    public String getTitle() { return title; }
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) throw new IllegalArgumentException("Назва не може бути порожньою.");
        this.title = title;
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) throw new IllegalArgumentException("Автор не може бути порожнім.");
        this.author = author;
    }

    public int getYear() { return year; }
    public void setYear(int year) {
        if (year <= 0 || year > 2026) throw new IllegalArgumentException("Некоректний рік видання.");
        this.year = year;
    }

    public int getPages() { return pages; }
    public void setPages(int pages) {
        if (pages <= 0) throw new IllegalArgumentException("Кількість сторінок має бути більшою за нуль.");
        this.pages = pages;
    }

    public Genre getGenre() { return genre; }
    public void setGenre(Genre genre) {
        if (genre == null) throw new IllegalArgumentException("Жанр не може бути порожнім (null).");
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", pages=" + pages +
                ", genre=" + genre.getDescription() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year && pages == book.pages &&
                genre == book.genre &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }
}