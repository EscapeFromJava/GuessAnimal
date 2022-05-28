module com.sigmait.guessanimal {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.sigmait.guessanimal to javafx.fxml;
    exports com.sigmait.guessanimal;
    exports com.sigmait.guessanimal.controller;
    opens com.sigmait.guessanimal.controller to javafx.fxml;
}