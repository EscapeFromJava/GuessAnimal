package com.sigmait.guessanimal.controller;

import com.sigmait.guessanimal.requests.Insert;
import com.sigmait.guessanimal.requests.Procedure;
import com.sigmait.guessanimal.requests.Select;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainController {
    @FXML
    Button btnRestart, btnResultYes, btnResultNo;
    @FXML
    TextField textName, textDescription;
    @FXML
    AnchorPane paneAddAnimal, paneResult;
    @FXML
    Label lblAnswer, lblQuestion;
    ArrayList<String> positiveAnswers = new ArrayList<>();
    ArrayList<String> negativeAnswers = new ArrayList<>();

    String currentQuestion = "";
    Connection conn;

    public void initialize() {
        connectingSQL();
        currentQuestion = getQuestion();
        lblQuestion.setText("Ваше животное " + currentQuestion + "?");
    }

    private String getQuestion() {
        return Select.getFirstQuestion(conn);
    }

    private void connectingSQL() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/GuessAnimal", "postgres", "123");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onButtonYesClick(ActionEvent actionEvent) {
        positiveAnswers.add(currentQuestion);
        getAnswer();
    }

    public void onButtonNoClick(ActionEvent actionEvent) {
        negativeAnswers.add(currentQuestion);
        getAnswer();
    }

    private void getAnswer() {
        ArrayList<String> animals = Select.getAnimals(conn, positiveAnswers, negativeAnswers);
        if (animals.size() == 1) {
            paneResult.setVisible(true);
            btnResultYes.setVisible(true);
            btnResultNo.setVisible(true);
            lblAnswer.setText("Вы загадали " + animals.get(0) + "?");
        } else {
            currentQuestion = Select.nextQuestion(conn, animals, positiveAnswers);
            lblQuestion.setText("Ваше животное " + currentQuestion + "?");
        }
    }

    public void onButtonRestartClick(ActionEvent actionEvent) {
        restartGame();
    }

    private void restartGame() {
        currentQuestion = getQuestion();
        lblQuestion.setText("Ваше животное " + currentQuestion + "?");
        paneResult.setVisible(false);
        positiveAnswers.clear();
        negativeAnswers.clear();
        btnRestart.setVisible(false);
        paneAddAnimal.setVisible(false);
        btnResultYes.setVisible(false);
        btnResultNo.setVisible(false);
    }

    public void onButtonResultYesClick(ActionEvent actionEvent) {
        btnRestart.setVisible(true);
    }

    public void onButtonResultNoClick(ActionEvent actionEvent) {
        paneAddAnimal.setVisible(true);
    }

    public void onButtonAddAnimal(ActionEvent actionEvent) {
        int newIdAnimal = Insert.insertNewAnimal(conn, textName.getText());
        int newIdDescription = Insert.insertNewDescription(conn, textDescription.getText());
        Insert.insertAnimalInDescription(conn, newIdAnimal, newIdDescription, positiveAnswers);
        btnRestart.setVisible(true);
        textName.clear();
        textDescription.clear();
    }

    public void onButtonClearDBClick(ActionEvent actionEvent) {
        Procedure.clearDB(conn);
        restartGame();
    }
}