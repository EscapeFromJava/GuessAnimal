package com.sigmait.guessanimal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Select {
    public static String getFirstQuestion(Connection conn) {
        String discription = "";
        try {
            String request = "SELECT text FROM description OFFSET FLOOR (RANDOM() * (SELECT COUNT(*) FROM description)) LIMIT 1;";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                discription = rs.getString("text");
            }
        } catch (SQLException e) {
            System.out.println("getFirstQuestion ERROR: " + e.getMessage());
        }
        return discription;
    }

    public static ArrayList<String> getAnimals(Connection conn, ArrayList<String> positiveAnswers, ArrayList<String> negativeAnswers) {
        StringBuilder sbRequest = new StringBuilder("SELECT DISTINCT name FROM animal WHERE ");
        if (positiveAnswers.size() > 0) {
            for (int i = 0; i < positiveAnswers.size(); i++) {
                sbRequest.append("name IN (SELECT name FROM relations WHERE text = '" + positiveAnswers.get(i) + "')");
                if (i < positiveAnswers.size() - 1) {
                    sbRequest.append(" AND ");
                }
            }
            if (negativeAnswers.size() > 0) {
                sbRequest.append(" AND ");
            }
        }
        for (int i = 0; i < negativeAnswers.size(); i++) {
            sbRequest.append("name NOT IN (SELECT name FROM relations WHERE text = '" + negativeAnswers.get(i) + "')");
            if (i < negativeAnswers.size() - 1) {
                sbRequest.append(" AND ");
            }
        }
        sbRequest.append(";");

        ArrayList<String> result = new ArrayList<>();
        try {
            PreparedStatement statement = conn.prepareStatement(String.valueOf(sbRequest));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                result.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println("getAnimals ERROR: " + e.getMessage());
        }

        return result;
    }

    public static String nextQuestion(Connection conn, ArrayList<String> animals, ArrayList<String> positiveAnswers) {
        StringBuilder sbRequest = new StringBuilder("SELECT text FROM relations WHERE name IN (");
        for (int i = 0; i < animals.size(); i++) {
            sbRequest.append("'" + animals.get(i) + "'");
            if (i < animals.size() - 1) {
                sbRequest.append(", ");
            }
        }
        sbRequest.append(") ");
        if (positiveAnswers.size() > 0) {
            sbRequest.append("AND text NOT IN (");
            for (int i = 0; i < positiveAnswers.size(); i++) {
                sbRequest.append("'" + positiveAnswers.get(i) + "'");
                if (i < positiveAnswers.size() - 1) {
                    sbRequest.append(", ");
                }
            }
            sbRequest.append(") ");
        }
        sbRequest.append("LIMIT 1;");

        String discription = "";
        try {
            PreparedStatement statement = conn.prepareStatement(String.valueOf(sbRequest));
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                discription = rs.getString("text");
            }
        } catch (SQLException e) {
            System.out.println("nextQuestion ERROR: " + e.getMessage());
        }
        return discription;
    }
}
