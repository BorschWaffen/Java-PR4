package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

/**
 * Клас-контейнер (агрегація), що зберігає колекцію книг і кількість кожного примірника.
 * Підтримує додавання з обліком кількості, пошук (включно з UUID) та сортування.
 */
public class Library {
    private String name;
    private ArrayList<BookEntry> entries;

    /**
     * Конструктор бібліотеки.
     *
     * @param name назва бібліотеки
     */
    public Library(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Назва бібліотеки не може бути порожньою.");
        }
        this.name = name.trim();
        this.entries = new ArrayList<BookEntry>();
    }

    public String getName() {
        return name;
    }

    /**
     * Додає книгу до бібліотеки з вказаною кількістю.
     * Якщо така книга вже є в колекції (за equals), збільшує її кількість.
     *
     * @param bk       об'єкт книги
     * @param quantity кількість примірників (> 0)
     */
    public void addNewBook(Book bk, int quantity) {
        if (bk == null) {
            throw new IllegalArgumentException("Книга не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість книг має бути більшою за нуль.");
        }

        for (int i = 0; i < entries.size(); i++) {
            BookEntry existingEntry = entries.get(i);
            if (existingEntry.getBook().equals(bk)) {
                existingEntry.addQuantity(quantity);
                return;
            }
        }

        entries.add(new BookEntry(bk, quantity));
    }

    /**
     * Перевіряє, чи порожня бібліотека.
     *
     * @return true, якщо записів немає
     */
    public boolean isEmpty() {
        return entries.isEmpty();
    }

    /**
     * Повертає кількість унікальних позицій у бібліотеці.
     *
     * @return розмір списку позицій
     */
    public int size() {
        return entries.size();
    }

    /**
     * Повертає позицію за індексом.
     *
     * @param index індекс у списку
     * @return позиція BookEntry
     */
    public BookEntry getEntry(int index) {
        return entries.get(index);
    }

    /**
     * Повертає відсортований список позицій бібліотеки з використанням Comparable.
     * Початковий порядок у внутрішньому списку entries не змінюється.
     *
     * @return відсортований список BookEntry
     */
    public ArrayList<BookEntry> getSortedEntries() {
        ArrayList<BookEntry> sorted = new ArrayList<BookEntry>();
        for (int i = 0; i < entries.size(); i++) {
            sorted.add(entries.get(i));
        }
        Collections.sort(sorted);
        return sorted;
    }

    /**
     * Повертає відсортований список позицій бібліотеки з використанням переданого Comparator.
     * Початковий порядок у внутрішньому списку entries не змінюється.
     *
     * @param comparator компаратор для порівняння елементів BookEntry
     * @return відсортований список BookEntry
     */
    public ArrayList<BookEntry> getSortedEntries(Comparator<BookEntry> comparator) {
        if (comparator == null) {
            throw new IllegalArgumentException("Компаратор не може бути null.");
        }
        ArrayList<BookEntry> sorted = new ArrayList<BookEntry>();
        for (int i = 0; i < entries.size(); i++) {
            sorted.add(entries.get(i));
        }
        Collections.sort(sorted, comparator);
        return sorted;
    }

    /**
     * Пошук 1: за підрядком у назві (без урахування регістру). Не змінює колекцію.
     *
     * @param query фрагмент назви
     * @return список знайдених позицій
     */
    public ArrayList<BookEntry> findBooksByTitle(String query) {
        ArrayList<BookEntry> matches = new ArrayList<BookEntry>();
        String normalizedQuery = query == null ? "" : query.trim().toLowerCase();

        for (int i = 0; i < entries.size(); i++) {
            BookEntry entry = entries.get(i);
            if (entry.getBook().getTitle().toLowerCase().contains(normalizedQuery)) {
                matches.add(entry);
            }
        }
        return matches;
    }

    /**
     * Пошук 2: за жанром книги. Не змінює колекцію.
     *
     * @param genre жанр
     * @return список знайдених позицій
     */
    public ArrayList<BookEntry> findBooksByGenre(Genre genre) {
        ArrayList<BookEntry> matches = new ArrayList<BookEntry>();
        if (genre == null) {
            return matches;
        }

        for (int i = 0; i < entries.size(); i++) {
            BookEntry entry = entries.get(i);
            if (entry.getBook().getGenre() == genre) {
                matches.add(entry);
            }
        }
        return matches;
    }

    /**
     * Пошук 3: за мінімальною кількістю сторінок. Не змінює колекцію.
     *
     * @param minPages мінімальна кількість сторінок
     * @return список знайдених позицій
     */
    public ArrayList<BookEntry> findBooksByMinPages(int minPages) {
        ArrayList<BookEntry> matches = new ArrayList<BookEntry>();

        for (int i = 0; i < entries.size(); i++) {
            BookEntry entry = entries.get(i);
            if (entry.getBook().getPages() >= minPages) {
                matches.add(entry);
            }
        }
        return matches;
    }

    /**
     * Пошук 4: за рядковим представленням UUID.
     * Кидає IllegalArgumentException при некоректному форматі UUID.
     *
     * @param uuidString рядок з UUID
     * @return знайдена позиція BookEntry або null, якщо не знайдено
     */
    public BookEntry findBookByUuid(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            throw new IllegalArgumentException("UUID не може бути порожнім.");
        }
        UUID targetUuid = UUID.fromString(uuidString.trim());
        for (int i = 0; i < entries.size(); i++) {
            BookEntry entry = entries.get(i);
            if (targetUuid.equals(entry.getBook().getUuid())) {
                return entry;
            }
        }
        return null;
    }
}