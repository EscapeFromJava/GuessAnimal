package com.sigmait.guessanimal.controller;

import com.sigmait.guessanimal.requests.Insert;
import com.sigmait.guessanimal.requests.Select;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MainController {
    @FXML
    private Button btnRestart, btnAddAnimal;
    @FXML
    private Label lblAnswer, lblDifference, lblQuestion;
    @FXML
    private TextField textName, textDescription;
    @FXML
    private VBox vBoxAddAnimal, vBoxStart, vBoxQuestions, vBoxResult;
    private ArrayList<String> positiveAnswers = new ArrayList<>();
    private ArrayList<String> negativeAnswers = new ArrayList<>();
    private Connection conn;
    private String currentQuestion = "";
    private String possibleAnimal = "";

    public void initialize() {
        connectingSQL();
        btnAddAnimal.disableProperty().bind(
                Bindings.isEmpty(textName.textProperty()).
                        or(Bindings.isEmpty(textDescription.textProperty()))
        );
        textName.textProperty().addListener((observableValue, s, t1) -> {
            lblDifference.setText("Чем " + t1 + " отличается от " + possibleAnimal + "?");
        });
    }

    private void connectingSQL() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/GuessAnimal", "postgres", "123");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void onButtonStartClick() {
        vBoxStart.setVisible(false);
        vBoxQuestions.setVisible(true);
        currentQuestion = getFirstQuestion();
        lblQuestion.setText("Ваше животное " + currentQuestion + "?");

    }

    private String getFirstQuestion() {
        return Select.getFirstQuestion(conn);
    }

    private void getListAnimals() {
        ArrayList<String> animals = Select.getAnimals(conn, positiveAnswers, negativeAnswers);
        if (animals.size() == 1) {
            guessAnimal(animals);
        } else {
            getNextQuestion(animals);
        }
    }

    private void getNextQuestion(ArrayList<String> animals){
        currentQuestion = Select.nextQuestion(conn, animals, positiveAnswers);
        lblQuestion.setText("Ваше животное " + currentQuestion + "?");
    }
    private void guessAnimal(ArrayList<String> animals){
        vBoxQuestions.setVisible(false);
        vBoxResult.setVisible(true);
        possibleAnimal = animals.get(0);
        lblAnswer.setText("Вы загадали " + possibleAnimal + "?");
    }

    public void onButtonYesClick() {
        positiveAnswers.add(currentQuestion);
        getListAnimals();
    }

    public void onButtonNoClick() {
        negativeAnswers.add(currentQuestion);
        getListAnimals();
    }

    public void onButtonResultYesClick() {
        btnRestart.setVisible(true);
        vBoxResult.setVisible(false);
    }

    public void onButtonResultNoClick() {
        vBoxAddAnimal.setVisible(true);
        vBoxResult.setVisible(false);
    }

    public void onButtonAddAnimal() {
        int newIdAnimal = Insert.insertNewAnimal(conn, textName.getText());
        int newIdDescription = Insert.insertNewDescription(conn, textDescription.getText());
        Insert.insertAnimalInDescription(conn, newIdAnimal, newIdDescription, positiveAnswers);
        btnRestart.setVisible(true);
        textName.clear();
        textDescription.clear();
        vBoxAddAnimal.setVisible(false);
    }

    public void onButtonRestartClick() {
        restartGame();
    }

    private void restartGame() {
        positiveAnswers.clear();
        negativeAnswers.clear();
        btnRestart.setVisible(false);
        vBoxResult.setVisible(false);
        vBoxStart.setVisible(true);
    }
}