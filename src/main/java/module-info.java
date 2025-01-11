module com.html_editor {
    requires javafx.fxml;
    requires javafx.web;


    opens com.html_editor to javafx.fxml;
    exports com.html_editor;
}