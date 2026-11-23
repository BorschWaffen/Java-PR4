package ua.edu.sumdu.j2se.pr4;

import java.util.UUID;

/**
 * Контракт для об'єктів, що мають унікальний ідентифікатор UUID.
 */
public interface Identifiable {
    /**
     * Повертає унікальний ідентифікатор об'єкта.
     *
     * @return об'єкт UUID
     */
    UUID getUuid();
}