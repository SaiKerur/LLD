package com.tictactoe.models;

import com.tictactoe.exceptions.InvalidBotCountException;
import com.tictactoe.strategies.winning_strategy.OrderOneWinningStrategy;
import com.tictactoe.strategies.winning_strategy.PlayerWonStrategy;

import java.util.ArrayList;
import java.util.List;

public class Game {
    private Board board;
    private List<Player> players;
    private int currentPlayerIdx;
    private GameStatus gameStatus;
    private List<Move> moves;
    private PlayerWonStrategy playerWonStrategy;
    private Move lastMove; // Stores the last move for undo

    private Game(GameBuilder gameBuilder) {
        this.players = gameBuilder.players;
        int n = players.size();
        this.board = new Board(n + 1);
        this.currentPlayerIdx = 0;
        this.moves = new ArrayList<>();
        this.gameStatus = GameStatus.IN_PROGRESS;
        this.playerWonStrategy = new OrderOneWinningStrategy(n + 1);
    }

    public static GameBuilder getBuilder() {
        return new GameBuilder();
    }

    public void printBoard() {
        this.board.printBoard();
    }

    public void makeMove() {
        Player player = players.get(currentPlayerIdx);
        Pair<Integer, Integer> pair = player.makeMove(this.board);

        // Validate if the cell is empty
        if (!board.getCell(pair.getKey(), pair.getValue()).isEmpty()) {
            System.out.println("Cell is already occupied. Try again.");
            return;
        }

        // Make the move
        this.board.setPlayer(pair.getKey(), pair.getValue(), player);
        Move move = new Move(player, this.board.getCell(pair.getKey(), pair.getValue()));
        this.moves.add(move);
        this.lastMove = move;

        // Check if the player has won
        if (playerWonStrategy.checkIfWon(this.board.getCell(pair.getKey(), pair.getValue()))) {
            this.gameStatus = GameStatus.WON;
            return;
        }

        // Check if the game is a draw (after confirming no winning condition is met)
        if (moves.size() == (board.getGrid().size() * board.getGrid().size())) {
            this.gameStatus = GameStatus.DRAW;
            return;
        }

        // Switch to the next player
        this.currentPlayerIdx = (this.currentPlayerIdx + 1) % this.players.size();
    }


    public boolean undoLastMove() {
        if (lastMove == null || !(lastMove.getPlayer() instanceof HumanPlayer)) {
            System.out.println("Undo not allowed for the current player or no moves to undo.");
            return false;
        }

        HumanPlayer humanPlayer = (HumanPlayer) lastMove.getPlayer();
        if (!humanPlayer.canUndo()) {
            System.out.println("Undo already used. Cannot undo again.");
            return false;
        }

        // Remove the player's mark from the board
        Cell lastCell = lastMove.getCell();
        lastCell.setPlayer(null);

        // Remove the move from the move list
        moves.remove(moves.size() - 1);
        lastMove = null;

        // Update winning strategy's state
        playerWonStrategy = new OrderOneWinningStrategy(board.getGrid().size());

        // Reset game state to IN_PROGRESS
        this.gameStatus = GameStatus.IN_PROGRESS;

        // Allow the human player to undo only once
        humanPlayer.useUndo();

        // Update the current player index to the previous player
        this.currentPlayerIdx = (this.currentPlayerIdx - 1 + players.size()) % players.size();
        return true;
    }

    public void replayGame() {
        System.out.println("Replaying the game:");
        Board replayBoard = new Board(this.board.getGrid().size());

        for (Move move : moves) {
            replayBoard.setPlayer(move.getCell().getX(), move.getCell().getY(), move.getPlayer());
            replayBoard.printBoard();
            System.out.println();
        }
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public Player getWinner() {
        return this.players.get(currentPlayerIdx);
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public int getCurrentPlayerIdx() {
        return this.currentPlayerIdx;
    }

    public static class GameBuilder {
        private List<Player> players;

        public GameBuilder setPlayers(List<Player> players) {
            this.players = players;
            return this;
        }

        public Game build() throws InvalidBotCountException {
            int botCount = 0;
            for (Player player : players) {
                if (player instanceof Bot) {
                    botCount++;
                }
                if (botCount > 1) {
                    throw new InvalidBotCountException("At max 1 bot allowed");
                }
            }
            return new Game(this);
        }
    }
}
