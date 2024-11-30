package com.tictactoe.strategies.winning_strategy;

import com.tictactoe.models.Cell;

public interface PlayerWonStrategy {

    boolean checkIfWon(Cell cell);
}