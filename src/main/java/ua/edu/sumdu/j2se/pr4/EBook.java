package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

/**
 * Похідний клас, що представляє електронну книгу.
 */
public class EBook extends Book {
    private double fileSizeMb;
    private String format;

    /**
     * Конструктор електронної книги.
     *
     * @param title      назва
     * @param author     автор
     * @param year       рік видання
     * @param pages      кількість сторінок
     * @param genre      жанр
     * @param fileSizeMb розмір файлу в МБ (> 0)
     * @param format     формат файлу (наприклад, EPUB, PDF)
     */
    public EBook(String title, String author, int year, int pages, Genre genre,
                 double fileSizeMb, String format) {
        super(title, author, year, pages, genre);
        setFileSizeMb(fileSizeMb);
        setFormat(format);
    }

    public double getFileSizeMb() {
        return fileSizeMb;
    }

    /**
     * Встановлює розмір файлу в мегабайтах.
     *
     * @param fileSizeMb розмір (> 0)
     */
    public void setFileSizeMb(double fileSizeMb) {
        if (fileSizeMb <= 0) {
            throw new IllegalArgumentException("Розмір файлу має бути більшим за нуль.");
        }
        this.fileSizeMb = fileSizeMb;
    }

    public String getFormat() {
        return format;
    }

    /**
     * Встановлює формат електронного файлу.
     *
     * @param format формат (не порожній)
     */
    public void setFormat(String format) {
        if (format == null || format.trim().isEmpty()) {
            throw new IllegalArgumentException("Формат файлу не може бути порожнім.");
        }
        this.format = format.trim().toUpperCase();
    }

    @Override
    public String getEditionInfo() {
        return "Електронна книга (" + format + ", " + fileSizeMb + " МБ)";
    }

    @Override
    public String toString() {
        return "EBook{" +
                "title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", year=" + getYear() +
                ", pages=" + getPages() +
                ", genre=" + getGenre().getDescription() +
                ", format='" + format + '\'' +
                ", fileSizeMb=" + fileSizeMb +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        EBook eBook = (EBook) o;
        return Double.compare(eBook.fileSizeMb, fileSizeMb) == 0 &&
                Objects.equals(format, eBook.format);
    }
}