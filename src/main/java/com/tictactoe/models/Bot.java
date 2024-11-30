package com.tictactoe.models;

import com.tictactoe.factories.BotPlayingStrategyFactory;
import com.tictactoe.strategies.bot_playing_strategy.BotPlayingStrategy;

public class Bot extends Player{

    private BotPlayingStrategy botPlayingStrategy;

    public Bot(char symbol, String name, BotLevel botLevel) {
        super(symbol, name);
        this.botLevel = botLevel;
        this.botPlayingStrategy = BotPlayingStrategyFactory.getBotPlayingStrategy(botLevel);
    }

    private BotLevel botLevel;

    @Override
    Pair<Integer, Integer> makeMove(Board board) {
        System.out.println("Bot is making it's move, beware!");
        return botPlayingStrategy.makeMove(board);
    }
}
