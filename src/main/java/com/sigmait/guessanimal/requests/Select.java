package com.sigmait.guessanimal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Select {

    public static ArrayList<String> selectFull(Connection conn) {
        ArrayList<String> listDiscriptions = new ArrayList<>();
        try {
            String request = "SELECT * FROM relations";
            PreparedStatement statement = conn.prepareStatement(request);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                listDiscriptions.add(rs.getString("text"));
            }
        } catch (SQLException e) {
            System.out.println("select ERROR: " + e.getMessage());
        }
        return listDiscriptions;
    }
}
