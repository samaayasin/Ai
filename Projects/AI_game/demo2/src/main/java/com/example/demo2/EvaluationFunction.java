package com.example.demo2;


import java.util.List;

public class EvaluationFunction {
    public int evaluate(NimGame game) {
        if (game.isGameOver()) {
            if (game.getCurrentPlayer() == 1) {
                return -1; // ai wins
            }
            else {
                return 1;     //player 1 wins
            }
        }

        int nimSum = calculateNimSum(game.getHeapsizes());
        return (nimSum == 0) ? 1 : 0;
    }

    private int calculateNimSum(List<Integer> heapSizes) {
        int nimSum = 0;
        for (int heapSize : heapSizes) {
            nimSum ^= heapSize;
        }
        return nimSum;
    }
}
