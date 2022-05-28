package com.sigmait.guessanimal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Insert {
    public static int insertNewAnimal(Connection conn, String animal) {
        int newIdAnimal = 0;
        try {
            String requestAnimal = "SELECT id FROM animal WHERE name = '" + animal + "';";
            PreparedStatement statement = conn.prepareStatement(requestAnimal);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                newIdAnimal = rs.getInt("id");
            }
            if (newIdAnimal == 0) {
                String insertAnimal = "INSERT INTO animal(name) VALUES ('" + animal + "') RETURNING id;";
                statement = conn.prepareStatement(insertAnimal);
                rs = statement.executeQuery();
                rs.next();
                newIdAnimal = rs.getInt("id");
            }
            return newIdAnimal;
        } catch (SQLException e) {
            System.out.println("insertNewAnimal ERROR: " + e.getMessage());
        }
        return 0;
    }

    public static int insertNewDescription(Connection conn, String description) {
        int newIdDescription = 0;
        try {
            String requestDescription = "SELECT id FROM description WHERE text = '" + description + "';";
            PreparedStatement statement = conn.prepareStatement(requestDescription);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                newIdDescription = rs.getInt("id");
            }
            if (newIdDescription == 0) {
                String insertDescription = "INSERT INTO description(text) VALUES ('" + description + "') RETURNING id;";
                statement = conn.prepareStatement(insertDescription);
                rs = statement.executeQuery();
                rs.next();
                newIdDescription = rs.getInt("id");
            }
            return newIdDescription;
        } catch (SQLException e) {
            System.out.println("insertNewAnimal ERROR: " + e.getMessage());
        }
        return 0;
    }

    public static void insertAnimalInDescription(Connection conn, int idAnimal, int idDescription, ArrayList<String> positiveAnswers) {
        StringBuilder sbRequest = new StringBuilder("INSERT INTO animal_in_description(animal, description) VALUES (" + idAnimal + ", " + idDescription + ");");
        for (String currentAnswer: positiveAnswers) {
            sbRequest.append("INSERT INTO animal_in_description(animal, description) VALUES (" + idAnimal + ", (SELECT id FROM description WHERE text = '" + currentAnswer + "'));");
        }
        try {
            PreparedStatement statement = conn.prepareStatement(String.valueOf(sbRequest));
            statement.execute();

        } catch (
                SQLException e) {
            System.out.println("insertAnimalInDescription ERROR: " + e.getMessage());
        }
    }
}
