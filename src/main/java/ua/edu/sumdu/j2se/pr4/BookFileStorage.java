package ua.edu.sumdu.j2se.pr4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Клас для завантаження та збереження стану об'єкта Library у текстовий файл.
 */
public class BookFileStorage {
    private static final String DELIMITER = "\\|";
    private static final String SEP = "|";

    /**
     * Зчитує об'єкти з файлу та додає їх до бібліотеки через addNewBook.
     *
     * @param filePath шлях до файлу
     * @param library  об'єкт бібліотеки
     */
    public static void loadFromFile(String filePath, Library library) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Шлях до файлу не може бути порожнім.");
        }
        if (library == null) {
            throw new IllegalArgumentException("Бібліотека не може бути null.");
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
        int loadedEntries = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String trimmed = line.trim();
                if (trimmed.isEmpty() || trimmed.startsWith("#")) {
                    continue;
                }

                try {
                    ParsedRecord record = parseLine(trimmed);
                    library.addNewBook(record.book, record.quantity.intValue());
                    loadedEntries++;
                } catch (IllegalArgumentException e) {
                    System.out.println("Помилка у рядку " + lineNumber + " файлу " + filePath
                            + " (" + e.getMessage() + "). Рядок пропущено.");
                }
            }
            System.out.println("Оброблено записів з " + filePath + ": " + loadedEntries);
        } catch (IOException e) {
            System.out.println("Помилка читання файлу " + filePath + ": " + e.getMessage());
        }
    }

    /**
     * Записує актуальний вміст бібліотеки у файл.
     *
     * @param filePath шлях до файлу
     * @param library  об'єкт бібліотеки
     */
    public static void saveToFile(String filePath, Library library) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("Шлях до файлу не може бути порожнім.");
        }
        if (library == null) {
            throw new IllegalArgumentException("Бібліотека не може бути null.");
        }

        File file = new File(filePath);
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, false))) {
                for (int i = 0; i < library.size(); i++) {
                    BookEntry entry = library.getEntry(i);
                    writer.write(serializeEntry(entry));
                    writer.newLine();
                }
            }
            System.out.println("Збережено позицій у " + filePath + ": " + library.size());
        } catch (IOException e) {
            System.out.println("Помилка запису у файл " + filePath + ": " + e.getMessage());
        }
    }

    private static ParsedRecord parseLine(String line) {
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
            int qty = extractQuantity(parts, 6, 7);
            Book book = new Book(title, author, year, pages, genre);
            return new ParsedRecord(book, qty);
        } else if ("EBOOK".equals(type)) {
            if (parts.length < 8) {
                throw new IllegalArgumentException("недостатньо полів для EBOOK");
            }
            int qty = extractQuantity(parts, 8, 9);
            double sizeMb = parseDoubleStrict(parts[6], "розмір файлу");
            String format = sanitizeField(parts[7]);
            Book book = new EBook(title, author, year, pages, genre, sizeMb, format);
            return new ParsedRecord(book, qty);
        } else if ("PAPERBOOK".equals(type)) {
            if (parts.length < 8) {
                throw new IllegalArgumentException("недостатньо полів для PAPERBOOK");
            }
            int qty = extractQuantity(parts, 8, 9);
            int weight = parseIntStrict(parts[6], "вага");
            String cover = sanitizeField(parts[7]);
            Book book = new PaperBook(title, author, year, pages, genre, weight, cover);
            return new ParsedRecord(book, qty);
        } else if ("AUDIOBOOK".equals(type)) {
            if (parts.length < 8) {
                throw new IllegalArgumentException("недостатньо полів для AUDIOBOOK");
            }
            int qty = extractQuantity(parts, 8, 9);
            int duration = parseIntStrict(parts[6], "тривалість");
            String narrator = sanitizeField(parts[7]);
            Book book = new AudioBook(title, author, year, pages, genre, duration, narrator);
            return new ParsedRecord(book, qty);
        } else if ("REFERENCEBOOK".equals(type)) {
            if (parts.length < 10) {
                throw new IllegalArgumentException("недостатньо полів для REFERENCEBOOK");
            }
            int qty = extractQuantity(parts, 10, 11);
            int weight = parseIntStrict(parts[6], "вага");
            String cover = sanitizeField(parts[7]);
            String subject = sanitizeField(parts[8]);
            boolean hasIndex = parseBooleanStrict(parts[9]);
            Book book = new ReferenceBook(title, author, year, pages, genre, weight, cover, subject, hasIndex);
            return new ParsedRecord(book, qty);
        } else {
            throw new IllegalArgumentException("невідомий тип об'єкта: " + type);
        }
    }

    private static int extractQuantity(String[] parts, int withoutQtyLen, int withQtyLen) {
        if (parts.length == withoutQtyLen) {
            return 1;
        }
        if (parts.length == withQtyLen) {
            return parseIntStrict(parts[withQtyLen - 1], "кількість");
        }
        throw new IllegalArgumentException("невірна кількість полів у рядку");
    }

    private static String serializeEntry(BookEntry entry) {
        Book book = entry.getBook();
        String base = cleanPipe(book.getTitle()) + SEP
                + cleanPipe(book.getAuthor()) + SEP
                + book.getYear() + SEP
                + book.getPages() + SEP
                + book.getGenre().name();

        String serialized;
        if (book instanceof ReferenceBook) {
            ReferenceBook ref = (ReferenceBook) book;
            serialized = "REFERENCEBOOK" + SEP + base + SEP
                    + ref.getWeightGrams() + SEP
                    + cleanPipe(ref.getCoverType()) + SEP
                    + cleanPipe(ref.getSubjectArea()) + SEP
                    + ref.isHasIndex();
        } else if (book instanceof PaperBook) {
            PaperBook paper = (PaperBook) book;
            serialized = "PAPERBOOK" + SEP + base + SEP
                    + paper.getWeightGrams() + SEP
                    + cleanPipe(paper.getCoverType());
        } else if (book instanceof EBook) {
            EBook ebook = (EBook) book;
            serialized = "EBOOK" + SEP + base + SEP
                    + ebook.getFileSizeMb() + SEP
                    + cleanPipe(ebook.getFormat());
        } else if (book instanceof AudioBook) {
            AudioBook audio = (AudioBook) book;
            serialized = "AUDIOBOOK" + SEP + base + SEP
                    + audio.getDurationMinutes() + SEP
                    + cleanPipe(audio.getNarrator());
        } else {
            serialized = "BOOK" + SEP + base;
        }

        return serialized + SEP + entry.getQuantity().intValue();
    }

    private static String sanitizeField(String value) {
        return value == null ? "" : value.trim();
    }

    private static String cleanPipe(String value) {
        return value == null ? "" : value.replace("|", "/");
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

    private static final class ParsedRecord {
        private final Book book;
        private final Integer quantity;

        private ParsedRecord(Book book, int quantity) {
            this.book = book;
            this.quantity = Integer.valueOf(quantity);
        }
    }
}