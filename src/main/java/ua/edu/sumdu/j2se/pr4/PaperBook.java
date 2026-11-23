package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

/**
 * Похідний клас, що представляє паперову книгу.
 */
public class PaperBook extends Book {
    private int weightGrams;
    private String coverType;

    /**
     * Конструктор паперової книги.
     *
     * @param title       назва
     * @param author      автор
     * @param year        рік видання
     * @param pages       кількість сторінок
     * @param genre       жанр
     * @param weightGrams вага в грамах (> 0)
     * @param coverType   тип палітурки
     */
    public PaperBook(String title, String author, int year, int pages, Genre genre,
                     int weightGrams, String coverType) {
        super(title, author, year, pages, genre);
        setWeightGrams(weightGrams);
        setCoverType(coverType);
    }

    public int getWeightGrams() {
        return weightGrams;
    }

    /**
     * Встановлює вагу книги в грамах.
     *
     * @param weightGrams вага (> 0)
     */
    public void setWeightGrams(int weightGrams) {
        if (weightGrams <= 0) {
            throw new IllegalArgumentException("Вага книги має бути більшою за нуль.");
        }
        this.weightGrams = weightGrams;
    }

    public String getCoverType() {
        return coverType;
    }

    /**
     * Встановлює тип палітурки.
     *
     * @param coverType тип палітурки (не порожній)
     */
    public void setCoverType(String coverType) {
        if (coverType == null || coverType.trim().isEmpty()) {
            throw new IllegalArgumentException("Тип палітурки не може бути порожнім.");
        }
        this.coverType = coverType.trim();
    }

    @Override
    public String getEditionInfo() {
        return "Паперова книга (" + coverType + ", " + weightGrams + " г)";
    }

    @Override
    public String toString() {
        return "PaperBook{" +
                "title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", year=" + getYear() +
                ", pages=" + getPages() +
                ", genre=" + getGenre().getDescription() +
                ", coverType='" + coverType + '\'' +
                ", weightGrams=" + weightGrams +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PaperBook paperBook = (PaperBook) o;
        return weightGrams == paperBook.weightGrams &&
                Objects.equals(coverType, paperBook.coverType);
    }
}