package ua.edu.sumdu.j2se.pr4;

/**
 * Власний виняток, що сигналізує про некоректне значення поля або параметра об'єкта.
 */
public class InvalidFieldValueException extends RuntimeException {
    public InvalidFieldValueException(String message) {
        super(message);
    }
}