module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires junit;
    requires javafx.graphics;


    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}