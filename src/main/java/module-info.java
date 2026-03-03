module aparicio.firstscreen {
    requires javafx.controls;
    requires javafx.fxml;


    opens aparicio.controller to javafx.fxml;
    exports aparicio.controller;
    exports model;
    opens model to javafx.fxml;
}