package ua.edu.sumdu.j2se.pr4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Клас для збереження та завантаження колекції книг у текстовий файл.
 */
public class BookFileStorage {
    private static final String DELIMITER = "\\|";
    private static final String SEP = "|";

    /**
     * Зчитує об'єкти ієрархії з файлу та додає їх у переданий список.
     * Якщо файл ще не існує — створює порожній файл.
     *
     * @param filePath шлях до файлу (наприклад, input.txt)
     * @param books    колекція, куди додаються зчитані об'єкти
     */
    public static void loadFromFile(String filePath, ArrayList<Book> books) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Шлях до файлу не може бути порожнім.");
        }
        if (books == null) {
            throw new IllegalArgumentException("Колекція книг не може бути null.");
        }

        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null && !parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
                System.out.println("Файл " + filePath + " не знайдено — створено новий порожній файл.");
            } catch (IOException e) {
                System.out.println("Не вдалося створити файл " + filePath + ": " + e.getMessage());
            }
            return;
        }

        int lineNumber = 0;
        int loadedCount = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                try {
                    Book parsed = parseBookLine(trimmed);
                    books.add(parsed);
                    loadedCount++;
                } catch (IllegalArgumentException e) {
                    System.out.println("Помилка у рядку " + lineNumber + " файлу " + filePath
                            + " (" + e.getMessage() + "). Рядок пропущено.");
                }
            }
            System.out.println("Завантажено об'єктів з " + filePath + ": " + loadedCount);
        } catch (IOException e) {
            System.out.println("Помилка читання файлу " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Записує актуальний стан колекції об'єктів у файл.
     *
     * @param filePath шлях до файлу (наприклад, input.txt)
     * @param books    колекція об'єктів для збереження
     */
    public static void saveToFile(String filePath, ArrayList<Book> books) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Шлях до файлу не може бути порожнім.");
        }
        if (books == null) {
            throw new IllegalArgumentException("Колекція книг не може бути null.");
        }

        File file = new File(filePath);
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                for (int i = 0; i < books.size(); i++) {
                    Book book = books.get(i);
                    writer.write(serializeBook(book));
                    writer.newLine();
                }
            }
            System.out.println("Збережено об'єктів у " + filePath + ": " + books.size());
        } catch (IOException e) {
            System.out.println("Помилка запису у файл " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Перетворює один рядок файлу на відповідний об'єкт ієрархії.
     */
    private static Book parseBookLine(String line) {
        String[] parts = line.split(DELIMITER, -1);
        if (parts.length < 6) {
            throw new IllegalArgumentException("недостатньо полів");
        }

        String type = parts[0].trim().toUpperCase();
        String title = sanitizeField(parts[1]);
        String author = sanitizeField(parts[2]);
        int year = parseIntStrict(parts[3], "рік");
        int pages = parseIntStrict(parts[4], "кількість сторінок");
        Genre genre = parseGenreStrict(parts[5]);

        if ("BOOK".equals(type)) {
            if (parts.length != 6) {
                throw new IllegalArgumentException("невірна кількість полів для BOOK");
            }
            return new Book(title, author, year, pages, genre);
        } else if ("EBOOK".equals(type)) {
            if (parts.length != 8) {
                throw new IllegalArgumentException("невірна кількість полів для EBOOK");
            }
            double sizeMb = parseDoubleStrict(parts[6], "розмір файлу");
            String format = sanitizeField(parts[7]);
            return new EBook(title, author, year, pages, genre, sizeMb, format);
        } else if ("PAPERBOOK".equals(type)) {
            if (parts.length != 8) {
                throw new IllegalArgumentException("невірна кількість полів для PAPERBOOK");
            }
            int weight = parseIntStrict(parts[6], "вага");
            String cover = sanitizeField(parts[7]);
            return new PaperBook(title, author, year, pages, genre, weight, cover);
        } else if ("AUDIOBOOK".equals(type)) {
            if (parts.length != 8) {
                throw new IllegalArgumentException("невірна кількість полів для AUDIOBOOK");
            }
            int duration = parseIntStrict(parts[6], "тривалість");
            String narrator = sanitizeField(parts[7]);
            return new AudioBook(title, author, year, pages, genre, duration, narrator);
        } else if ("REFERENCEBOOK".equals(type)) {
            if (parts.length != 10) {
                throw new IllegalArgumentException("невірна кількість полів для REFERENCEBOOK");
            }
            int weight = parseIntStrict(parts[6], "вага");
            String cover = sanitizeField(parts[7]);
            String subject = sanitizeField(parts[8]);
            boolean hasIndex = parseBooleanStrict(parts[9]);
            return new ReferenceBook(title, author, year, pages, genre, weight, cover, subject, hasIndex);
        } else {
            throw new IllegalArgumentException("невідомий тип об'єкта: " + type);
        }
    }

    /**
     * Перетворює об'єкт ієрархії у текстовий рядок для запису у файл.
     */
    private static String serializeBook(Book book) {
        String base = cleanPipe(book.getTitle()) + SEP
                + cleanPipe(book.getAuthor()) + SEP
                + book.getYear() + SEP
                + book.getPages() + SEP
                + book.getGenre().name();

        if (book instanceof ReferenceBook) {
            ReferenceBook ref = (ReferenceBook) book;
            return "REFERENCEBOOK" + SEP + base + SEP
                    + ref.getWeightGrams() + SEP
                    + cleanPipe(ref.getCoverType()) + SEP
                    + cleanPipe(ref.getSubjectArea()) + SEP
                    + ref.isHasIndex();
        } else if (book instanceof PaperBook) {
            PaperBook paper = (PaperBook) book;
            return "PAPERBOOK" + SEP + base + SEP
                    + paper.getWeightGrams() + SEP
                    + cleanPipe(paper.getCoverType());
        } else if (book instanceof EBook) {
            EBook ebook = (EBook) book;
            return "EBOOK" + SEP + base + SEP
                    + ebook.getFileSizeMb() + SEP
                    + cleanPipe(ebook.getFormat());
        } else if (book instanceof AudioBook) {
            AudioBook audio = (AudioBook) book;
            return "AUDIOBOOK" + SEP + base + SEP
                    + audio.getDurationMinutes() + SEP
                    + cleanPipe(audio.getNarrator());
        } else {
            return "BOOK" + SEP + base;
        }
    }

    private static String sanitizeField(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private static String cleanPipe(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("|", "/");
    }

    private static int parseIntStrict(String raw, String fieldName) {
        try {
            return Integer.parseInt(raw.trim());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("некоректне ціле число у полі '" + fieldName + "'");
        }
    }

    private static double parseDoubleStrict(String raw, String fieldName) {
        try {
            return Double.parseDouble(raw.trim().replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("некоректне дійсне число у полі '" + fieldName + "'");
        }
    }

    private static Genre parseGenreStrict(String raw) {
        String normalized = raw.trim().toUpperCase();
        try {
            return Genre.valueOf(normalized);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("невідомий жанр '" + raw.trim() + "'");
        }
    }

    private static boolean parseBooleanStrict(String raw) {
        String normalized = raw.trim().toLowerCase();
        if ("true".equals(normalized) || "1".equals(normalized)) {
            return true;
        }
        if ("false".equals(normalized) || "0".equals(normalized)) {
            return false;
        }
        throw new IllegalArgumentException("некоректне логічне значення '" + raw.trim() + "'");
    }
}