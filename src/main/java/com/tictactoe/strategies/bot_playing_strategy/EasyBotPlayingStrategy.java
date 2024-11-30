package com.tictactoe.strategies.bot_playing_strategy;

import com.tictactoe.models.Board;
import com.tictactoe.models.Cell;
import com.tictactoe.models.Pair;

import java.util.List;

public class EasyBotPlayingStrategy implements BotPlayingStrategy{

    @Override
    public Pair<Integer, Integer> makeMove(Board board) {
        for(List<Cell> row: board.getGrid()){
            for(Cell cell: row){
                if(cell.isEmpty()){
                    return new Pair<>(cell.getX(), cell.getY());
                }
            }
        }
        return null;
    }
}
