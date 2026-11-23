package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;
import java.util.UUID;

/**
 * Абстрактний базовий клас, що представляє книгу.
 * Реалізує Identifiable (унікальний UUID) та Comparable для сортування за базовим атрибутом.
 */
public abstract class Book implements Comparable<Book>, Identifiable {
    private UUID uuid;
    private String title;
    private String author;
    private int year;
    private int pages;
    private Genre genre;

    /**
     * Конструктор з параметрами. Автоматично генерує унікальний UUID.
     *
     * @param title  назва книги
     * @param author автор книги
     * @param year   рік видання
     * @param pages  кількість сторінок
     * @param genre  жанр книги
     */
    public Book(String title, String author, int year, int pages, Genre genre) {
        this.uuid = UUID.randomUUID();
        setTitle(title);
        setAuthor(author);
        setYear(year);
        setPages(pages);
        setGenre(genre);
    }

    /**
     * Конструктор копіювання. Створює новий об'єкт із власним UUID.
     *
     * @param other об'єкт Book для копіювання
     */
    public Book(Book other) {
        if (other == null) {
            throw new InvalidFieldValueException("Об'єкт для копіювання не може бути null.");
        }
        this.uuid = UUID.randomUUID();
        this.title = other.title;
        this.author = other.author;
        this.year = other.year;
        this.pages = other.pages;
        this.genre = other.genre;
    }

    @Override
    public UUID getUuid() {
        return uuid;
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
            throw new InvalidFieldValueException("Назва не може бути порожньою.");
        }
        this.title = title.trim();
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
            throw new InvalidFieldValueException("Автор не може бути порожнім.");
        }
        this.author = author.trim();
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
            throw new InvalidFieldValueException("Некоректний рік видання: " + year + ".");
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
            throw new InvalidFieldValueException("Кількість сторінок має бути більшою за нуль: " + pages + ".");
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
            throw new InvalidFieldValueException("Жанр не може бути порожнім (null).");
        }
        this.genre = genre;
    }

    /**
     * Повертає короткий опис типу видання для поліморфного виводу.
     *
     * @return опис типу видання
     */
    public abstract String getEditionInfo();

    /**
     * Коротке представлення об'єкта для списків (назва/автор + UUID).
     *
     * @return короткий рядок
     */
    public String toShortString() {
        return "Book: " + title + " (" + author + ") | UUID: " + uuid.toString();
    }

    @Override
    public int compareTo(Book other) {
        if (other == null) {
            return 1;
        }
        int byTitle = this.title.compareToIgnoreCase(other.title);
        if (byTitle != 0) {
            return byTitle;
        }
        int byTitleExact = this.title.compareTo(other.title);
        if (byTitleExact != 0) {
            return byTitleExact;
        }
        int byAuthor = this.author.compareToIgnoreCase(other.author);
        if (byAuthor != 0) {
            return byAuthor;
        }
        int byYear = Integer.compare(this.year, other.year);
        if (byYear != 0) {
            return byYear;
        }
        int byPages = Integer.compare(this.pages, other.pages);
        if (byPages != 0) {
            return byPages;
        }
        return this.genre.name().compareTo(other.genre.name());
    }

    @Override
    public String toString() {
        return "Book{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
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