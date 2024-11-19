package com.example.demo2;

import java.util.ArrayList;
import java.util.List;

public class AlphaBetaAlgorithm {
    public int heapIndexforAi;
    EvaluationFunction E = new EvaluationFunction();
    public int alphabeta (NimGame g, int depth, int alpha, int beta, boolean maximizingPlayer) {
        if (depth == 0 || g.isGameOver()) {
            return E.evaluate(g);
        }
        if (maximizingPlayer) {
            int bestValue = Integer.MIN_VALUE;
            for (Move m : possibleMoves(g)) {
                NimGame newGameState = newStateFunc(g, m);
                heapIndexforAi=m.getHeapIndex();
                bestValue = Math.max(bestValue, alphabeta(newGameState, depth - 1, alpha, beta, false));
                alpha = Math.max(alpha, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestValue;
        } else {
            int bestValue = Integer.MAX_VALUE;
            for (Move m : possibleMoves(g)) {
                NimGame newGameState = newStateFunc(g, m);
                bestValue = Math.min(bestValue, alphabeta(newGameState, depth - 1, alpha, beta, true));
                beta = Math.min(beta, bestValue);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestValue;
        }
    }

    private List<Move> possibleMoves(NimGame g) {
        List<Integer> heapsizes = g.getHeapsizes();
        List<Move> possibleMoveList = new ArrayList<>();
        for (int heapIndex = 0; heapIndex < heapsizes.size(); heapIndex++) {
            int heapSize = heapsizes.get(heapIndex);
            for (int numOfStone = 1; numOfStone <= heapSize; numOfStone++) {
                possibleMoveList.add(new Move(heapIndex, numOfStone));
            }
        }
        return possibleMoveList;
    }

    private NimGame newStateFunc(NimGame g, Move m) {
        NimGame newState = new NimGame(g.getHeapsizes().stream().mapToInt(Integer::intValue).toArray());

        int heapIndex = m.getHeapIndex();
        int stonesToRemove = m.getStonesToRemove();

        newState.move(heapIndex, stonesToRemove);

        return newState;
    }
}
