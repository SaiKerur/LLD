package com.tictactoe;

import com.tictactoe.controllers.GameController;
import com.tictactoe.exceptions.InvalidBotCountException;
import com.tictactoe.models.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicTacToe {

    public static void main(String[] args) throws InvalidBotCountException {
        GameController gameController = new GameController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter number of human players:");
        int n = scanner.nextInt();
        List<Player> playerList = new ArrayList<>();

        // Create human players
        for (int i = 0; i < n; i++) {
            System.out.println("Enter name:");
            String name = scanner.next();
            System.out.println("Enter symbol:");
            char symbol = scanner.next().charAt(0);
            Player humanPlayer = new HumanPlayer(symbol, name);
            playerList.add(humanPlayer);
        }

        // Add bot if required
        System.out.println("Are bots going to play? (Y/N)");
        if (scanner.next().charAt(0) == 'Y') {
            System.out.println("Enter bot level: (E/M/H)");
            char level = scanner.next().charAt(0);
            BotLevel botLevel = BotLevel.EASY;
            if (level == 'M') {
                botLevel = BotLevel.MEDIUM;
            } else if (level == 'H') {
                botLevel = BotLevel.HARD;
            }
            playerList.add(new Bot('B', "Bot - 1", botLevel));
        }

        // Create game
        Game game = gameController.createGame(playerList);

        // Game loop
        while (gameController.getGameStatus(game) == GameStatus.IN_PROGRESS) {
            gameController.printBoard(game);

            Player currentPlayer = game.getPlayers().get(game.getCurrentPlayerIdx());
            if (currentPlayer instanceof Bot) {
                // Bot's turn
                System.out.println(currentPlayer.getName() + " is making its move...");
                gameController.makeMove(game); // Bot makes its move automatically
            } else {
                // Human player's turn
                boolean turnComplete = false;

                while (!turnComplete) {
                    System.out.println("Options: 1. Make Move 2. Undo Last Move 3. Replay Game 4. Quit");
                    int choice = scanner.nextInt();

                    switch (choice) {
                        case 1: // Make Move
                            gameController.makeMove(game);
                            System.out.println("Move made. Do you want to undo?");
                            System.out.println("Options: 1. Continue 2. Undo Last Move 3. Replay Game 4. Quit");
                            int postMoveChoice = scanner.nextInt();

                            if (postMoveChoice == 2) {
                                if (gameController.undoMove(game)) {
                                    System.out.println("Undo successful. Please make your move again.");
                                } else {
                                    System.out.println("Undo failed.");
                                }
                            } else if (postMoveChoice == 3) {
                                gameController.replayGame(game);
                            } else if (postMoveChoice == 4) {
                                System.out.println("Exiting game.");
                                return;
                            }
                            turnComplete = true; // Exit loop after processing the human player's move
                            break;

                        case 2: // Undo Last Move
                            if (!gameController.undoMove(game)) {
                                System.out.println("Undo failed. Try another option.");
                            }
                            break;

                        case 3: // Replay Game
                            gameController.replayGame(game);
                            break;

                        case 4: // Quit
                            System.out.println("Exiting game.");
                            return;

                        default:
                            System.out.println("Invalid choice. Try again.");
                    }
                }
            }
        }

        // Game result
        if (gameController.getGameStatus(game) == GameStatus.WON) {
            Player winner = gameController.getWinner(game);
            System.out.println(winner.getName() + " with symbol: " + winner.getSymbol() + " has won");
        } else {
            System.out.println("Game is a draw");
        }
        gameController.printBoard(game);
    }
}
