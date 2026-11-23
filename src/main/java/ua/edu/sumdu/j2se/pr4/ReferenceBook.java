package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

/**
 * Похідний клас, що представляє довідкове видання (нащадок PaperBook).
 */
public class ReferenceBook extends PaperBook {
    private String subjectArea;
    private boolean hasIndex;

    /**
     * Конструктор довідкового видання.
     *
     * @param title       назва
     * @param author      автор
     * @param year        рік видання
     * @param pages       кількість сторінок
     * @param genre       жанр
     * @param weightGrams вага в грамах
     * @param coverType   тип палітурки
     * @param subjectArea предметна галузь (не порожня)
     * @param hasIndex    наявність предметного покажчика
     */
    public ReferenceBook(String title, String author, int year, int pages, Genre genre,
                         int weightGrams, String coverType, String subjectArea, boolean hasIndex) {
        super(title, author, year, pages, genre, weightGrams, coverType);
        setSubjectArea(subjectArea);
        this.hasIndex = hasIndex;
    }

    public String getSubjectArea() {
        return subjectArea;
    }

    /**
     * Встановлює предметну галузь довідника.
     *
     * @param subjectArea галузь (не порожня)
     */
    public void setSubjectArea(String subjectArea) {
        if (subjectArea == null || subjectArea.trim().isEmpty()) {
            throw new IllegalArgumentException("Предметна галузь не може бути порожньою.");
        }
        this.subjectArea = subjectArea.trim();
    }

    public boolean isHasIndex() {
        return hasIndex;
    }

    public void setHasIndex(boolean hasIndex) {
        this.hasIndex = hasIndex;
    }

    @Override
    public String getEditionInfo() {
        String indexText = hasIndex ? "з покажчиком" : "без покажчика";
        return "Довідник [" + subjectArea + ", " + indexText + ", " + getCoverType() + ", " + getWeightGrams() + " г]";
    }

    @Override
    public String toString() {
        return "ReferenceBook{" +
                "title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", year=" + getYear() +
                ", pages=" + getPages() +
                ", genre=" + getGenre().getDescription() +
                ", coverType='" + getCoverType() + '\'' +
                ", weightGrams=" + getWeightGrams() +
                ", subjectArea='" + subjectArea + '\'' +
                ", hasIndex=" + hasIndex +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ReferenceBook that = (ReferenceBook) o;
        return hasIndex == that.hasIndex &&
                Objects.equals(subjectArea, that.subjectArea);
    }
}