package org.example.seng465_proje;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class nQueensApp {
    private static final int SIZE = 8; // Tahta boyutu
    private static final int TILE_SIZE = 80; // Kare boyutu
    private Pane boardPane;

    @FXML
    private Label welcomeText;

    protected void drawChessBoard(Pane boardPane) {
        this.boardPane = boardPane;
        boardPane.getChildren().clear(); // Tahtayı temizle
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                Rectangle square = new Rectangle(TILE_SIZE, TILE_SIZE);
                square.setX(col * TILE_SIZE);
                square.setY(row * TILE_SIZE);
                square.setFill((row + col) % 2 == 0 ? Color.BEIGE : Color.GRAY);
                boardPane.getChildren().add(square);
            }
        }
    }

    protected void drawQueens(int[] board) {
        for (int col = 0; col < SIZE; col++) {
            Circle queen = new Circle(TILE_SIZE / 3);
            queen.setFill(Color.DARKRED);
            queen.setCenterX(col * TILE_SIZE + TILE_SIZE / 2.0);
            queen.setCenterY(board[col] * TILE_SIZE + TILE_SIZE / 2.0);
            boardPane.getChildren().add(queen);
        }
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
