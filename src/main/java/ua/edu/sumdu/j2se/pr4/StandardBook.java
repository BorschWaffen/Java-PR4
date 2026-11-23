package ua.edu.sumdu.j2se.pr4;

/**
 * Конкретний нащадок абстрактного класу Book для базового книжкового видання.
 */
public class StandardBook extends Book {
    /**
     * Конструктор базового видання книги.
     *
     * @param title  назва книги
     * @param author автор книги
     * @param year   рік видання
     * @param pages  кількість сторінок
     * @param genre  жанр книги
     */
    public StandardBook(String title, String author, int year, int pages, Genre genre) {
        super(title, author, year, pages, genre);
    }

    @Override
    public String getEditionInfo() {
        return "Базове видання";
    }
}