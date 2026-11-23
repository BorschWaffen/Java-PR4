package ua.edu.sumdu.j2se.pr4;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;

/**
 * Клас-стартер JavaFX GUI для перегляду колекції, додавання об'єктів та пошуку за UUID.
 */
public class MainApp extends Application {
    private Library library;
    private ListView<String> shortListView;
    private TextArea detailsArea;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        library = new Library("Бібліотека СумДУ (GUI)");

        shortListView = new ListView<String>();
        detailsArea = new TextArea();
        detailsArea.setEditable(false);
        detailsArea.setPrefRowCount(6);
        detailsArea.setWrapText(true);

        statusLabel = new Label("Готово до роботи. Додайте об'єкт або виконайте пошук за UUID.");

        VBox addBox = buildAddObjectPanel();
        VBox listBox = buildShortListPanel();
        VBox searchBox = buildUuidSearchPanel();

        VBox centerBox = new VBox(12, addBox, listBox, searchBox);
        centerBox.setPadding(new Insets(12));

        BorderPane root = new BorderPane();
        root.setCenter(centerBox);
        root.setBottom(statusLabel);
        BorderPane.setMargin(statusLabel, new Insets(6, 12, 10, 12));

        Scene scene = new Scene(root, 780, 680);
        primaryStage.setTitle("Практична робота №16 — UUID та JavaFX GUI");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox buildAddObjectPanel() {
        Label header = new Label("1. Додавання нового об'єкта (UUID генерується автоматично)");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        ComboBox<String> typeCombo = new ComboBox<String>();
        typeCombo.getItems().addAll("Book", "EBook", "PaperBook", "AudioBook", "ReferenceBook");
        typeCombo.setValue("Book");

        TextField titleField = new TextField();
        titleField.setPromptText("Назва книги");

        TextField authorField = new TextField();
        authorField.setPromptText("Автор");

        TextField yearField = new TextField();
        yearField.setPromptText("Рік (напр., 2024)");

        TextField pagesField = new TextField();
        pagesField.setPromptText("Сторінки (напр., 250)");

        ComboBox<Genre> genreCombo = new ComboBox<Genre>();
        genreCombo.getItems().addAll(Genre.values());
        genreCombo.setValue(Genre.FICTION);

        TextField extraField = new TextField();
        extraField.setPromptText("Дод. параметр (формат/палітурка/диктор/галузь)");

        TextField qtyField = new TextField();
        qtyField.setPromptText("Кількість (напр., 1)");
        qtyField.setText("1");

        Button addButton = new Button("Додати");
        addButton.setOnAction(event -> {
            try {
                String type = typeCombo.getValue();
                String title = titleField.getText();
                String author = authorField.getText();
                int year = Integer.parseInt(yearField.getText().trim());
                int pages = Integer.parseInt(pagesField.getText().trim());
                Genre genre = genreCombo.getValue();
                int qty = Integer.parseInt(qtyField.getText().trim());
                String extra = extraField.getText().trim();

                Book created = createBookByType(type, title, author, year, pages, genre, extra);
                library.addNewBook(created, qty);
                refreshShortList();

                titleField.clear();
                authorField.clear();
                yearField.clear();
                pagesField.clear();
                extraField.clear();

                statusLabel.setText("Додано об'єкт (" + type + ") з UUID: " + created.getUuid().toString());
            } catch (NumberFormatException ex) {
                statusLabel.setText("Помилка вводу: рік, сторінки та кількість мають бути цілими числами.");
            } catch (IllegalArgumentException ex) {
                statusLabel.setText("Помилка валідації: " + ex.getMessage());
            }
        });

        GridPane grid = new GridPane();
        grid.setHgap(8);
        grid.setVgap(8);
        grid.add(new Label("Тип:"), 0, 0);
        grid.add(typeCombo, 1, 0);
        grid.add(new Label("Назва:"), 2, 0);
        grid.add(titleField, 3, 0);

        grid.add(new Label("Автор:"), 0, 1);
        grid.add(authorField, 1, 1);
        grid.add(new Label("Жанр:"), 2, 1);
        grid.add(genreCombo, 3, 1);

        grid.add(new Label("Рік:"), 0, 2);
        grid.add(yearField, 1, 2);
        grid.add(new Label("Сторінки:"), 2, 2);
        grid.add(pagesField, 3, 2);

        grid.add(new Label("Додатково:"), 0, 3);
        grid.add(extraField, 1, 3);
        grid.add(new Label("Кількість:"), 2, 3);
        grid.add(qtyField, 3, 3);

        HBox buttonRow = new HBox(10, addButton);
        return new VBox(8, header, grid, buttonRow);
    }

    private VBox buildShortListPanel() {
        Label header = new Label("2. Колекція об'єктів (короткий вигляд: назва + UUID)");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");
        shortListView.setPrefHeight(140);
        return new VBox(6, header, shortListView);
    }

    private VBox buildUuidSearchPanel() {
        Label header = new Label("3. Пошук за UUID та повна інформація про об'єкт");
        header.setStyle("-fx-font-weight: bold; -fx-font-size: 13px;");

        TextField uuidSearchField = new TextField();
        uuidSearchField.setPromptText("Введіть UUID (наприклад, 550e8400-e29b-41d4-a716-446655440000)");
        uuidSearchField.setPrefWidth(420);

        Button findButton = new Button("Знайти");
        findButton.setOnAction(event -> {
            String rawUuid = uuidSearchField.getText().trim();
            try {
                BookEntry entry = library.findBookByUuid(rawUuid);
                if (entry == null) {
                    detailsArea.setText("Об'єкт із UUID \"" + rawUuid + "\" не знайдено.");
                    statusLabel.setText("Пошук завершено: об'єкт не знайдено.");
                } else {
                    detailsArea.setText(entry.toString());
                    statusLabel.setText("Знайдено об'єкт з UUID: " + entry.getBook().getUuid().toString());
                }
            } catch (IllegalArgumentException ex) {
                detailsArea.setText("Некоректний формат UUID: " + ex.getMessage());
                statusLabel.setText("Помилка формату UUID.");
            }
        });

        HBox searchRow = new HBox(8, uuidSearchField, findButton);
        return new VBox(6, header, searchRow, new Label("Повна інформація:"), detailsArea);
    }

    private Book createBookByType(String type, String title, String author, int year, int pages, Genre genre, String extra) {
        String safeExtra = (extra == null || extra.trim().isEmpty()) ? "Базове" : extra.trim();
        if ("EBook".equals(type)) {
            return new EBook(title, author, year, pages, genre, 5.0, safeExtra);
        } else if ("PaperBook".equals(type)) {
            return new PaperBook(title, author, year, pages, genre, 350, safeExtra);
        } else if ("AudioBook".equals(type)) {
            return new AudioBook(title, author, year, pages, genre, 180, safeExtra);
        } else if ("ReferenceBook".equals(type)) {
            return new ReferenceBook(title, author, year, pages, genre, 600, "Тверда", safeExtra, true);
        } else {
            return new StandardBook(title, author, year, pages, genre);
        }
    }

    private void refreshShortList() {
        ArrayList<String> items = new ArrayList<String>();
        for (int i = 0; i < library.size(); i++) {
            Book book = library.getEntry(i).getBook();
            items.add(book.getTitle() + " | UUID: " + book.getUuid().toString());
        }
        shortListView.getItems().setAll(items);
    }

    public static void main(String[] args) {
        launch(args);
    }
}