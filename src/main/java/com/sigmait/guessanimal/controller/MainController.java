package com.sigmait.guessanimal.controller;

import com.sigmait.guessanimal.requests.Select;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainController {

    @FXML
    Label lblQuestion;

    Connection conn;
    public void initialize(){
        connectingSQL();
        ArrayList<String> listDiscriptions = Select.selectFull(conn);
        lblQuestion.setText(String.valueOf(listDiscriptions));
    }

    private void connectingSQL() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/GuessAnimal", "postgres", "123");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onButtonYesClick(ActionEvent actionEvent) {

    }

    public void onButtonNoClick(ActionEvent actionEvent) {

    }
}