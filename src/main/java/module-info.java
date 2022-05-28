module com.sigmait.guessanimal {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.sigmait.guessanimal to javafx.fxml;
    exports com.sigmait.guessanimal;
}