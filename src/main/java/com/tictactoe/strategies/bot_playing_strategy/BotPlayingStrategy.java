package com.tictactoe.strategies.bot_playing_strategy;

import com.tictactoe.models.Board;
import com.tictactoe.models.Pair;

public interface BotPlayingStrategy {
    public Pair<Integer, Integer> makeMove(Board board);
}