package ua.edu.sumdu.j2se.pr4;

/**
 * Елемент колекції бібліотеки, що поєднує об'єкт Book та його кількість (клас-обгортка Integer).
 */
public class BookEntry implements Comparable<BookEntry> {
    private Book book;
    private Integer quantity;

    /**
     * Конструктор позиції в бібліотеці.
     *
     * @param book     об'єкт книги (не null)
     * @param quantity кількість примірників (> 0)
     */
    public BookEntry(Book book, int quantity) {
        if (book == null) {
            throw new IllegalArgumentException("Об'єкт книги не може бути null.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Кількість примірників має бути більшою за нуль.");
        }
        this.book = book;
        this.quantity = Integer.valueOf(quantity);
    }

    public Book getBook() {
        return book;
    }

    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Додає вказану кількість примірників до поточної кількості.
     *
     * @param additionalCount додаткова кількість (> 0)
     */
    public void addQuantity(int additionalCount) {
        if (additionalCount <= 0) {
            throw new IllegalArgumentException("Додаткова кількість має бути більшою за нуль.");
        }
        int current = this.quantity.intValue();
        this.quantity = Integer.valueOf(current + additionalCount);
    }

    @Override
    public int compareTo(BookEntry other) {
        if (other == null) {
            return 1;
        }
        return this.book.compareTo(other.book);
    }

    @Override
    public String toString() {
        return "[" + book.getEditionInfo() + "] " + book.toString() + " | Кількість: " + quantity;
    }
}