package ua.edu.sumdu.j2se.pr4;

/**
 * Власний виняток, що сигналізує про відсутність об'єкта у колекції під час операції.
 */
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}