package ua.edu.sumdu.j2se.pr4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LibraryExceptionTest {

    @Test
    void shouldThrowObjectNotFoundExceptionWhenDeletingNonExistingObject() {
        Library library = new Library("Тестова бібліотека");
        Book book = new StandardBook("Clean Code", "Robert Martin", 2008, 464, Genre.SCIENCE);

        assertThrows(ObjectNotFoundException.class, () -> {
            library.delete(book);
        });
    }

    @Test
    void shouldThrowObjectNotFoundExceptionWhenUpdatingNonExistingObject() {
        Library library = new Library("Тестова бібліотека");
        Book existing = new StandardBook("First Book", "Author One", 2020, 200, Genre.FICTION);
        Book updated = new StandardBook("Updated Book", "Author One", 2020, 200, Genre.FICTION);

        assertThrows(ObjectNotFoundException.class, () -> {
            library.update(existing, updated);
        });
    }

    @Test
    void shouldThrowInvalidFieldValueExceptionWhenBookFieldIsInvalid() {
        assertThrows(InvalidFieldValueException.class, () -> {
            new StandardBook("", "Author", 2020, 150, Genre.FICTION);
        });

        Book validBook = new StandardBook("Valid Title", "Author", 2020, 150, Genre.FICTION);
        assertThrows(InvalidFieldValueException.class, () -> {
            validBook.setPages(-10);
        });
    }
}