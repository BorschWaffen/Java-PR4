package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

/**
 * Базовий клас, що представляє книгу.
 */
public class Book {
    private String title;
    private String author;
    private int year;
    private int pages;
    private Genre genre;

    /**
     * Конструктор з параметрами.
     *
     * @param title  назва книги
     * @param author автор книги
     * @param year   рік видання
     * @param pages  кількість сторінок
     * @param genre  жанр книги
     */
    public Book(String title, String author, int year, int pages, Genre genre) {
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPages(pages);
        setGenre(genre);
    }

    /**
     * Конструктор копіювання.
     *
     * @param other об'єкт Book для копіювання
     */
    public Book(Book other) {
        if (other == null) {
            throw new IllegalArgumentException("Об'єкт для копіювання не може бути null.");
        }
        this.title = other.title;
        this.author = other.author;
        this.year = other.year;
        this.pages = other.pages;
        this.genre = other.genre;
    }

    public String getTitle() {
        return title;
    }

    /**
     * Встановлює назву книги.
     *
     * @param title назва книги (не порожня)
     */
    public void setTitle(String title) {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва не може бути порожньою.");
        }
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    /**
     * Встановлює автора книги.
     *
     * @param author автор книги (не порожній)
     */
    public void setAuthor(String author) {
        if (author == null || author.trim().isEmpty()) {
            throw new IllegalArgumentException("Автор не може бути порожнім.");
        }
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    /**
     * Встановлює рік видання.
     *
     * @param year рік від 1 до 2026
     */
    public void setYear(int year) {
        if (year <= 0 || year > 2026) {
            throw new IllegalArgumentException("Некоректний рік видання.");
        }
        this.year = year;
    }

    public int getPages() {
        return pages;
    }

    /**
     * Встановлює кількість сторінок.
     *
     * @param pages кількість сторінок (> 0)
     */
    public void setPages(int pages) {
        if (pages <= 0) {
            throw new IllegalArgumentException("Кількість сторінок має бути більшою за нуль.");
        }
        this.pages = pages;
    }

    public Genre getGenre() {
        return genre;
    }

    /**
     * Встановлює жанр книги.
     *
     * @param genre жанр (не null)
     */
    public void setGenre(Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Жанр не може бути порожнім (null).");
        }
        this.genre = genre;
    }

    /**
     * Повертає короткий опис типу видання для поліморфного виводу.
     *
     * @return опис типу видання
     */
    public String getEditionInfo() {
        return "Звичайне видання";
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
        return year == book.year &&
                pages == book.pages &&
                genre == book.genre &&
                Objects.equals(title, book.title) &&
                Objects.equals(author, book.author);
    }
}