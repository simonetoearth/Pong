package edu.sdccd.cisc191;

import java.sql.*;

public class DatabaseHelper {
    public void initializeDatabase() throws SQLException {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS LastGameScore (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "playerScore INTEGER NOT NULL, " +
                "aiScore INTEGER NOT NULL)";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTableSQL);
        }
    }

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:src/main/resources/database/highscores.db");
        System.out.println("Database connected");
        initializeDatabase();
        System.out.println("Database initialized");
    }

    public void printLastGameScore() {
        try {
            LastGameScore lastScore = getLastGameScore();
            if (lastScore != null) {
                System.out.println("Last Game Score - Player: " + lastScore.getPlayerScore() + ", AI: " + lastScore.getAiScore());
            } else {
                System.out.println("No scores available.");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving last game score: " + e.getMessage());
            e.printStackTrace();
        }
    }


    // Add a print statement in your saveLastGameScore method
    public void saveLastGameScore(int playerScore, int aiScore) throws SQLException {
        String sql = "INSERT INTO LastGameScore (playerScore, aiScore) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, playerScore);
            pstmt.setInt(2, aiScore);
            pstmt.executeUpdate();
            System.out.println("Score saved: Player = " + playerScore + ", AI = " + aiScore); // Debug print
        } catch (SQLException e) {
            System.err.println("Error saving last game score: " + e.getMessage());
            e.printStackTrace();
        }
    }



    public LastGameScore getLastGameScore() throws SQLException {
        String sql = "SELECT * FROM LastGameScore ORDER BY id DESC LIMIT 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new LastGameScore(rs.getInt("playerScore"), rs.getInt("aiScore"));
            }
            return null; // No scores available
        } catch (SQLException e) {
            System.err.println("Error retrieving last game score: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public void disconnect() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
