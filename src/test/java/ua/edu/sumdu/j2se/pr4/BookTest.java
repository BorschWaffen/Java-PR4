package ua.edu.sumdu.j2se.pr4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Book book = new StandardBook("Valid Title", "Valid Author", 2020, 300, Genre.FICTION);

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
            new StandardBook("Title", "Author", 2050, 100, Genre.FICTION);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            new StandardBook("Title", "Author", 2020, 100, null);
        });
    }

    @Test
    void shouldCompareBooksByTitleConsistently() {
        Book first = new StandardBook("Alpha Book", "Author A", 2020, 150, Genre.FICTION);
        Book second = new StandardBook("Beta Book", "Author B", 2021, 200, Genre.SCIENCE);

        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
        assertEquals(0, first.compareTo(new StandardBook("Alpha Book", "Author A", 2020, 150, Genre.FICTION)));
    }
}