package com.tictactoe.models;

import java.util.Scanner;

public class HumanPlayer extends Player {

    private boolean undoUsed;

    public HumanPlayer(char symbol, String name) {
        super(symbol, name);
        this.undoUsed = false;
    }

    @Override
    Pair<Integer, Integer> makeMove(Board board) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("It's " + getName() + "'s turn");
        System.out.println("Enter x:");
        int x = scanner.nextInt();
        System.out.println("Enter y:");
        int y = scanner.nextInt();
        return new Pair<>(x, y);
    }

    public boolean canUndo() {
        return !undoUsed;
    }

    public void useUndo() {
        undoUsed = true;
    }
}

