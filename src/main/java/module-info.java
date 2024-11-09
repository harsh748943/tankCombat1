module com.example.tankmark1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.media;


    opens com.example.tankmark1 to javafx.fxml;
    exports com.example.tankmark1;
    exports com.example.tankmark1.map;
    opens com.example.tankmark1.map to javafx.fxml;
    exports com.example.tankmark1.weapons;
    opens com.example.tankmark1.weapons to javafx.fxml;
    exports com.example.tankmark1.tanks;
    opens com.example.tankmark1.tanks to javafx.fxml;
}