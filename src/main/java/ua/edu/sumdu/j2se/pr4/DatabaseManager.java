package ua.edu.sumdu.j2se.pr4;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * Клас для роботи з реляційною базою даних через JDBC.
 * Зчитує конфігурацію з файлу та зберігає об'єкти ієрархії через PreparedStatement.
 */
public class DatabaseManager {
    private static final String INSERT_SQL =
            "INSERT INTO books (" +
                    "type, title, author, publication_year, pages, genre, " +
                    "file_size_mb, file_format, weight_grams, cover_type, " +
                    "duration_minutes, narrator, subject_area, has_index" +
                    ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final String url;
    private final String user;
    private final String password;

    /**
     * Завантажує параметри підключення до БД із конфігураційного файлу.
     *
     * @param configPath шлях до .properties файлу
     * @throws IOException якщо файл не вдалося прочитати
     */
    public DatabaseManager(String configPath) throws IOException {
        if (configPath == null || configPath.trim().isEmpty()) {
            throw new IllegalArgumentException("Не вказано шлях до файлу конфігурації БД.");
        }

        Properties props = new Properties();
        FileInputStream input = new FileInputStream(configPath.trim());
        try {
            props.load(input);
        } finally {
            input.close();
        }

        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");

        if (url == null || url.trim().isEmpty() || user == null || user.trim().isEmpty()) {
            throw new IllegalArgumentException("У конфігураційному файлі відсутні обов'язкові параметри db.url або db.user.");
        }
    }

    /**
     * Зберігає об'єкт ієрархії у таблицю books через PreparedStatement.
     *
     * @param book об'єкт для збереження
     * @return true, якщо вставка виконана успішно
     */
    public boolean insertBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Об'єкт книги не може бути null.");
        }

        String type = resolveObjectType(book);

        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DriverManager.getConnection(url.trim(), user.trim(), password == null ? "" : password);
            statement = connection.prepareStatement(INSERT_SQL);

            statement.setString(1, type);
            statement.setString(2, book.getTitle());
            statement.setString(3, book.getAuthor());
            statement.setInt(4, book.getYear());
            statement.setInt(5, book.getPages());
            statement.setString(6, book.getGenre().name());

            setSubtypeColumns(statement, book);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.out.println("Помилка роботи з базою даних (SQLException): " + e.getMessage());
            return false;
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException ignored) {
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    /**
     * Визначає значення поля type відповідно до конкретного класу в ієрархії.
     */
    private String resolveObjectType(Book book) {
        if (book instanceof ReferenceBook) {
            return "REFERENCEBOOK";
        } else if (book instanceof PaperBook) {
            return "PAPERBOOK";
        } else if (book instanceof EBook) {
            return "EBOOK";
        } else if (book instanceof AudioBook) {
            return "AUDIOBOOK";
        } else {
            return "BOOK";
        }
    }

    /**
     * Заповнює параметри 7..14 у PreparedStatement залежно від підкласу.
     */
    private void setSubtypeColumns(PreparedStatement statement, Book book) throws SQLException {
        // 7: file_size_mb, 8: file_format
        if (book instanceof EBook) {
            EBook ebook = (EBook) book;
            statement.setDouble(7, ebook.getFileSizeMb());
            statement.setString(8, ebook.getFormat());
        } else {
            statement.setNull(7, Types.DOUBLE);
            statement.setNull(8, Types.VARCHAR);
        }

        // 9: weight_grams, 10: cover_type, 13: subject_area, 14: has_index
        if (book instanceof ReferenceBook) {
            ReferenceBook ref = (ReferenceBook) book;
            statement.setInt(9, ref.getWeightGrams());
            statement.setString(10, ref.getCoverType());
            statement.setNull(11, Types.INTEGER);
            statement.setNull(12, Types.VARCHAR);
            statement.setString(13, ref.getSubjectArea());
            statement.setBoolean(14, ref.isHasIndex());
            return;
        } else if (book instanceof PaperBook) {
            PaperBook paper = (PaperBook) book;
            statement.setInt(9, paper.getWeightGrams());
            statement.setString(10, paper.getCoverType());
            statement.setNull(11, Types.INTEGER);
            statement.setNull(12, Types.VARCHAR);
            statement.setNull(13, Types.VARCHAR);
            statement.setNull(14, Types.BOOLEAN);
            return;
        }

        // Для не-паперових книг поля паперу/довідника — NULL
        statement.setNull(9, Types.INTEGER);
        statement.setNull(10, Types.VARCHAR);

        // 11: duration_minutes, 12: narrator
        if (book instanceof AudioBook) {
            AudioBook audio = (AudioBook) book;
            statement.setInt(11, audio.getDurationMinutes());
            statement.setString(12, audio.getNarrator());
        } else {
            statement.setNull(11, Types.INTEGER);
            statement.setNull(12, Types.VARCHAR);
        }

        // 13: subject_area, 14: has_index
        statement.setNull(13, Types.VARCHAR);
        statement.setNull(14, Types.BOOLEAN);
    }
}