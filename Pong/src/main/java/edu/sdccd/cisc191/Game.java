package edu.sdccd.cisc191;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.SQLException;

public class Game extends Application {
    private Paddle playerPaddle;
    private Paddle aiPaddle;
    private Ball ball;
    private Timeline gameLoop;
    private final double paddleSpeed = 5.0;
    private final double ballSpeed = 3.0;
    private boolean moveUp = false;
    private boolean moveDown = false;
    private int playerScore = 0;
    private int aiScore = 0;
    private Label scoreLabel;

    private DatabaseHelper dbHelper;

    @Override
    public void start(Stage primaryStage) {
        Pane root = new Pane();
        VBox vbox = new VBox();
        scoreLabel = new Label("Player: 0  AI: 0");
        vbox.getChildren().add(scoreLabel);

        Scene scene = new Scene(root, 800, 600);
        root.getChildren().add(vbox);

        primaryStage.setTitle("Pong Game");
        primaryStage.setScene(scene);

        playerPaddle = new Paddle(20, 250, 10, 100);
        aiPaddle = new Paddle(770, 250, 10, 100);
        ball = new Ball(400, 300, 10);

        root.getChildren().addAll(playerPaddle.getShape(), aiPaddle.getShape(), ball.getShape());

        gameLoop = new Timeline(new KeyFrame(Duration.millis(16), e -> {
            try {
                runGameLoop();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }));
        gameLoop.setCycleCount(Timeline.INDEFINITE);
        gameLoop.play();

        scene.setOnKeyPressed(e -> handleKeyPress(e.getCode()));
        scene.setOnKeyReleased(e -> handleKeyRelease(e.getCode()));

        // Initialize database helper
        dbHelper = new DatabaseHelper();
        try {
            dbHelper.connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        primaryStage.show();
    }

    private void runGameLoop() throws SQLException {
        if (moveUp) {
            playerPaddle.move(-paddleSpeed);
        }
        if (moveDown) {
            playerPaddle.move(paddleSpeed);
        }

        ball.move();
        checkCollision();

        if (ball.isOutOfBounds()) {
            if (ball.getShape().getCenterX() < 0) {
                aiScore++;
            } else if (ball.getShape().getCenterX() > 800) {
                playerScore++;
            }
            ball.reset();
            updateScore();

            // Save last game score
            dbHelper.saveLastGameScore(playerScore, aiScore);
        }

        updateAiPaddle();
    }

    private void checkCollision() {
        if (ball.getShape().getBoundsInParent().intersects(playerPaddle.getShape().getBoundsInParent())) {
            ball.bounceHorizontal();
        }
        if (ball.getShape().getBoundsInParent().intersects(aiPaddle.getShape().getBoundsInParent())) {
            ball.bounceHorizontal();
        }

        if (ball.getShape().getCenterY() <= 0 || ball.getShape().getCenterY() >= 600) {
            ball.bounceVertical();
        }
    }

    private void updateAiPaddle() {
        double aiPaddleSpeed = 2.0;
        double aiY = ball.getShape().getCenterY() - (aiPaddle.getShape().getHeight() / 2);
        double currentY = aiPaddle.getShape().getY();

        double error = Math.random() * 20 - 10;

        aiY += error;

        if (aiY > currentY + aiPaddleSpeed) {
            aiPaddle.getShape().setY(Math.min(600 - aiPaddle.getShape().getHeight(), currentY + aiPaddleSpeed));
        } else if (aiY < currentY - aiPaddleSpeed) {
            aiPaddle.getShape().setY(Math.max(0, currentY - aiPaddle.getShape().getHeight()));
        }
    }

    private void handleKeyPress(KeyCode keyCode) {
        if (keyCode == KeyCode.UP) {
            moveUp = true;
        }
        if (keyCode == KeyCode.DOWN) {
            moveDown = true;
        }
    }

    private void handleKeyRelease(KeyCode keyCode) {
        if (keyCode == KeyCode.UP) {
            moveUp = false;
        }
        if (keyCode == KeyCode.DOWN) {
            moveDown = false;
        }
    }

    private void updateScore() {
        scoreLabel.setText("Player: " + playerScore + "  AI: " + aiScore);
    }

    @Override
    public void stop() {
        try {
            dbHelper.disconnect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
