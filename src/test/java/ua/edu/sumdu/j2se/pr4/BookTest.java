package ua.edu.sumdu.j2se.pr4;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    void shouldThrowExceptionWhenInvalidValueInSetter() {
        Book book = new Book("Valid Title", "Valid Author", 2020, 300);

        // Перевіряємо, що встановлення від'ємної кількості сторінок викине виняток
        assertThrows(IllegalArgumentException.class, () -> {
            book.setPages(-50);
        });

        // Перевіряємо порожню назву
        assertThrows(IllegalArgumentException.class, () -> {
            book.setTitle("");
        });
    }

    @Test
    void shouldThrowExceptionWhenInvalidConstructorData() {
        // Перевіряємо створення об'єкта з некоректним роком (з майбутнього або від'ємним)
        assertThrows(IllegalArgumentException.class, () -> {
            new Book("Title", "Author", 2050, 100);
        });
    }
}