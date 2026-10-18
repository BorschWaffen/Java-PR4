package ua.edu.sumdu.j2se.pr4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        // Додали Genre.FICTION у виклик
        Book book = new Book("Valid Title", "Valid Author", 2020, 300, Genre.FICTION);

        assertThrows(IllegalArgumentException.class, () -> {
            book.setPages(-50);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            book.setTitle("");
        });
    }

    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Book("Title", "Author", 2050, 100, Genre.FICTION);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new Book("Title", "Author", 2020, 100, null); // Тест на null жанр
        });
    }
}