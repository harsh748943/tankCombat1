module com.example.tankmark1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.tankmark1 to javafx.fxml;
    exports com.example.tankmark1;
}