package com.sigmait.guessanimal.requests;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Procedure {

    public static void clearDB(Connection conn) {
        try {
            String request = "CALL clear_db();";
            PreparedStatement statement = conn.prepareStatement(request);
            statement.execute();
        } catch (SQLException e) {
            System.out.println("clearDB ERROR: " + e.getMessage());
        }
    }
}
