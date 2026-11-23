package ua.edu.sumdu.j2se.pr4;

import java.util.Objects;

/**
 * Похідний клас, що представляє аудіокнигу.
 */
public class AudioBook extends Book {
    private int durationMinutes;
    private String narrator;

    /**
     * Конструктор аудіокниги.
     *
     * @param title           назва
     * @param author          автор
     * @param year            рік видання
     * @param pages           кількість сторінок (еквівалент/опис)
     * @param genre           жанр
     * @param durationMinutes тривалість у хвилинах (> 0)
     * @param narrator        ім'я диктора (не порожнє)
     */
    public AudioBook(String title, String author, int year, int pages, Genre genre,
                     int durationMinutes, String narrator) {
        super(title, author, year, pages, genre);
        setDurationMinutes(durationMinutes);
        setNarrator(narrator);
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    /**
     * Встановлює тривалість аудіокниги в хвилинах.
     *
     * @param durationMinutes тривалість (> 0)
     */
    public void setDurationMinutes(int durationMinutes) {
        if (durationMinutes <= 0) {
            throw new IllegalArgumentException("Тривалість аудіокниги має бути більшою за нуль.");
        }
        this.durationMinutes = durationMinutes;
    }

    public String getNarrator() {
        return narrator;
    }

    /**
     * Встановлює ім'я диктора.
     *
     * @param narrator диктор (не порожній)
     */
    public void setNarrator(String narrator) {
        if (narrator == null || narrator.trim().isEmpty()) {
            throw new IllegalArgumentException("Ім'я диктора не може бути порожнім.");
        }
        this.narrator = narrator.trim();
    }

    /**
     * Повертає тривалість у зручному форматі годин і хвилин.
     *
     * @return рядок тривалості
     */
    public String getFormattedDuration() {
        int hours = durationMinutes / 60;
        int mins = durationMinutes % 60;
        return hours + " год " + mins + " хв";
    }

    @Override
    public String getEditionInfo() {
        return "Аудіокнига (" + getFormattedDuration() + ", диктор: " + narrator + ")";
    }

    @Override
    public String toString() {
        return "AudioBook{" +
                "title='" + getTitle() + '\'' +
                ", author='" + getAuthor() + '\'' +
                ", year=" + getYear() +
                ", pages=" + getPages() +
                ", genre=" + getGenre().getDescription() +
                ", durationMinutes=" + durationMinutes +
                ", narrator='" + narrator + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AudioBook audioBook = (AudioBook) o;
        return durationMinutes == audioBook.durationMinutes &&
                Objects.equals(narrator, audioBook.narrator);
    }
}