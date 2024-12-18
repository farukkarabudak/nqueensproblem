package org.example.seng465_proje;

import javafx.scene.control.Label;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

public class nQueens extends Application {
    private static final int SIZE = 8; // Tahta boyutu
    private int[] board = new int[SIZE]; // Vezirlerin pozisyonlarını tutan dizi
    private nQueensApp appController = new nQueensApp(); // nQueensApp kontrolcüsü
    private Pane boardPane;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(nQueens.class.getResource("hello-view.fxml"));
        boardPane = new Pane();
        Scene scene = new Scene(boardPane, SIZE * 80, SIZE * 80);

        appController.drawChessBoard(boardPane);
        generateRandomBoard();
        appController.drawQueens(board);

        // Hill Climbing algoritmasını adım adım çalıştır
        new Thread(() -> hillClimbingWithSteps(board)).start();

        stage.setTitle("N-Queens Visualizer");
        stage.setScene(scene);
        stage.show();
    }

    private void generateRandomBoard() {
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            board[i] = random.nextInt(SIZE);
        }
    }

    private void hillClimbingWithSteps(int[] board) {
        Random random = new Random();
        int attempts = 0;
        int maxAttempts = 1000;

        // Ekranda mesaj göstermek için Label oluştur
        Label messageLabel = new Label();
        messageLabel.setFont(new Font("Arial", 24));
        messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        messageLabel.setLayoutX(10);
        messageLabel.setLayoutY(10);

        Platform.runLater(() -> {
            if (!boardPane.getChildren().contains(messageLabel)) {
                boardPane.getChildren().add(messageLabel);
            }
        });

        while (attempts < maxAttempts) {
            int[] currentBoard = Arrays.copyOf(board, SIZE);
            int currentHeuristic = calculateHeuristic(currentBoard);

            while (currentHeuristic > 0) {
                int[] bestNeighbor = Arrays.copyOf(currentBoard, SIZE);
                int bestHeuristic = currentHeuristic;

                for (int col = 0; col < SIZE; col++) {
                    for (int row = 0; row < SIZE; row++) {
                        if (currentBoard[col] != row) {
                            int[] neighbor = Arrays.copyOf(currentBoard, SIZE);
                            neighbor[col] = row;
                            int neighborHeuristic = calculateHeuristic(neighbor);

                            if (neighborHeuristic < bestHeuristic) {
                                bestNeighbor = neighbor;
                                bestHeuristic = neighborHeuristic;

                                // Hareketi yazdır
                                System.out.println("Vezir taşındı: Sütun " + col + " -> Satır " + row);
                            }
                        }
                    }
                }

                if (bestHeuristic >= currentHeuristic) {
                    break;
                }

                currentBoard = bestNeighbor;
                currentHeuristic = bestHeuristic;

                int[] finalBoard = Arrays.copyOf(currentBoard, SIZE);
                Platform.runLater(() -> {
                    appController.drawChessBoard(boardPane);
                    appController.drawQueens(finalBoard);
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (currentHeuristic == 0) {
                System.out.println("Çözüm bulundu!");
                int[] finalCurrentBoard = Arrays.copyOf(currentBoard, SIZE);
                Platform.runLater(() -> {
                    messageLabel.setText("Solution Found!");
                    messageLabel.setStyle("-fx-text-fill: green; -fx-font-weight: bold;");
                    appController.drawChessBoard(boardPane);
                    appController.drawQueens(finalCurrentBoard);
                });
                return;
            }

            System.out.println("Yeniden başlatılıyor...");
            generateRandomBoard();
            attempts++;
        }

        // Çözüm bulunamazsa "No Solution Found" mesajı
        Platform.runLater(() -> {
            messageLabel.setText("No Solution Found!");
            messageLabel.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
        });
        System.out.println("Çözüm bulunamadı. Maksimum deneme sayısına ulaşıldı.");
    }

    private int calculateHeuristic(int[] board) {
        int conflicts = 0;
        for (int i = 0; i < SIZE; i++) {
            for (int j = i + 1; j < SIZE; j++) {
                if (board[i] == board[j] || Math.abs(board[i] - board[j]) == Math.abs(i - j)) {
                    conflicts++;
                }
            }
        }
        return conflicts;
    }

    public static void main(String[] args) {
        launch();
    }
}