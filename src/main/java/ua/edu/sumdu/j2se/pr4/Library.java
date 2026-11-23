package ua.edu.sumdu.j2se.pr4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;

/**
 * Клас-контейнер (агрегація), що зберігає колекцію книг і кількість кожного примірника.
 * Підтримує додавання, модифікацію (update), видалення (delete), пошук (включно з UUID)
 * та сортування через Comparable і Comparator.
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
            throw new InvalidFieldValueException("Назва бібліотеки не може бути порожньою.");
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
            throw new InvalidFieldValueException("Книга не може бути null.");
        }
        if (quantity <= 0) {
            throw new InvalidFieldValueException("Кількість книг має бути більшою за нуль.");
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
     * Модифікує існуючий об'єкт книги у колекції, замінюючи його на newObject
     * зі збереженням поточної кількості примірників у позиції.
     *
     * @param existingObject об'єкт, який потрібно знайти та оновити
     * @param newObject      новий стан об'єкта книги
     * @return true, якщо оновлення виконано
     */
    public boolean update(Book existingObject, Book newObject) {
        if (existingObject == null) {
            throw new ObjectNotFoundException("Об'єкт для оновлення не вказано (null).");
        }
        if (newObject == null) {
            throw new InvalidFieldValueException("Новий об'єкт книги не може бути null.");
        }
        if (entries.isEmpty()) {
            throw new ObjectNotFoundException("Колекція порожня — об'єкт для оновлення не знайдено.");
        }

        for (int i = 0; i < entries.size(); i++) {
            BookEntry currentEntry = entries.get(i);
            if (currentEntry.getBook().equals(existingObject)) {
                int currentQty = currentEntry.getQuantity().intValue();
                entries.set(i, new BookEntry(newObject, currentQty));
                return true;
            }
        }
        throw new ObjectNotFoundException("Об'єкт для оновлення не знайдено у колекції.");
    }

    /**
     * Видаляє позицію з вказаним об'єктом книги з колекції бібліотеки.
     *
     * @param existingObject об'єкт книги, який потрібно видалити
     * @return true, якщо видалення виконано
     */
    public boolean delete(Book existingObject) {
        if (existingObject == null) {
            throw new ObjectNotFoundException("Об'єкт для видалення не вказано (null).");
        }
        if (entries.isEmpty()) {
            throw new ObjectNotFoundException("Колекція порожня — об'єкт для видалення не знайдено.");
        }

        for (int i = 0; i < entries.size(); i++) {
            BookEntry currentEntry = entries.get(i);
            if (currentEntry.getBook().equals(existingObject)) {
                entries.remove(i);
                return true;
            }
        }
        throw new ObjectNotFoundException("Об'єкт для видалення не знайдено у колекції.");
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
            throw new InvalidFieldValueException("Компаратор не може бути null.");
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
     * Кидає InvalidFieldValueException при некоректному форматі UUID.
     *
     * @param uuidString рядок з UUID
     * @return знайдена позиція BookEntry або null, якщо не знайдено
     */
    public BookEntry findBookByUuid(String uuidString) {
        if (uuidString == null || uuidString.trim().isEmpty()) {
            throw new InvalidFieldValueException("UUID не може бути порожнім.");
        }
        try {
            UUID targetUuid = UUID.fromString(uuidString.trim());
            for (int i = 0; i < entries.size(); i++) {
                BookEntry entry = entries.get(i);
                if (targetUuid.equals(entry.getBook().getUuid())) {
                    return entry;
                }
            }
            return null;
        } catch (IllegalArgumentException e) {
            throw new InvalidFieldValueException("Некоректний формат UUID: " + uuidString.trim());
        }
    }
}