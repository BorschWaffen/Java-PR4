package ua.edu.sumdu.j2se.pr4;

/**
 * Перерахування можливих жанрів книги.
 */
public enum Genre {
    FICTION("Художня література"),
    FANTASY("Фентезі"),
    SCIENCE("Наукова література"),
    HISTORY("Історія"),
    OTHER("Інше");

    private String description;

    Genre(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}