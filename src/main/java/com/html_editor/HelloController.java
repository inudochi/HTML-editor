package com.html_editor;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.event.ActionEvent;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class HelloController {
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private TextArea sourceCode;
    @FXML
    private WebView webView;

    // Инициализация
    @FXML
    public void initialize() {
        // Синхронизация изменений между HTMLEditor и TextArea
        htmlEditor.setOnKeyReleased(event -> syncEditorToSource());
        sourceCode.setOnKeyReleased(event -> syncSourceToEditor());
    }

    // Вставка картинки
    @FXML
    public void onInsertImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            // Получаем путь к файлу
            String imagePath = file.toURI().toString();
            // Вставляем HTML-код с изображением
            String imgTag = "<img src='" + imagePath + "' alt='Image' width='200'/>";
            htmlEditor.setHtmlText(htmlEditor.getHtmlText() + imgTag);
            // Синхронизируем с TextArea
            syncEditorToSource();
            // Обновляем предпросмотр
            updatePreview();
        }
    }

    // Загрузка файла
    @FXML
    public void onOpen(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open HTML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            loadFile(file);
        }
    }

    // Сохранение файла
    @FXML
    public void onSave(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save HTML File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML Files", "*.html"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            saveFile(file);
        }
    }

    // Перезагрузка предпросмотра
    @FXML
    public void onReload(ActionEvent event) {
        updatePreview();
    }

    // Загрузка файла в редактор и предпросмотр
    private void loadFile(File file) {
        try (Scanner scanner = new Scanner(file)) {
            StringBuilder content = new StringBuilder();
            while (scanner.hasNextLine()) {
                content.append(scanner.nextLine()).append("\n");
            }
            sourceCode.setText(content.toString());
            htmlEditor.setHtmlText(content.toString());
            updatePreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Сохранение файла
    private void saveFile(File file) {
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(htmlEditor.getHtmlText());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Обновление предпросмотра
    private void updatePreview() {
        webView.getEngine().loadContent(htmlEditor.getHtmlText());
    }

    // Синхронизация HTMLEditor -> TextArea
    private void syncEditorToSource() {
        sourceCode.setText(htmlEditor.getHtmlText());
    }

    // Синхронизация TextArea -> HTMLEditor
    private void syncSourceToEditor() {
        htmlEditor.setHtmlText(sourceCode.getText());
    }
}