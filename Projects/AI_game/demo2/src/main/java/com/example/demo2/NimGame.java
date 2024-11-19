package com.example.demo2;



import java.util.ArrayList;
import java.util.List;

public class NimGame {

    private List<Integer> heapsizes;
    private int currentPlayer;

    public NimGame(int[] initialSizes) {
        heapsizes = new ArrayList<>();
        for (int size : initialSizes) {
            heapsizes.add(size);
        }
        currentPlayer = 1;
    }

    public boolean move(int heapIndex, int numOfStonesToRemove) {
        if (isValidMove(heapIndex, numOfStonesToRemove)) {
            int newHeapSize = heapsizes.get(heapIndex) - numOfStonesToRemove;
            heapsizes.set(heapIndex, newHeapSize);
            togglePlayer();

            return true;
        }
        return false;
    }
    public void setHeapsizes(List<Integer> heapsizes) {

        this.heapsizes = heapsizes;
    }

    public boolean isGameOver() {

        return heapsizes.stream().allMatch(size -> size == 0);
    }

    public int getCurrentPlayer() {

        return currentPlayer;
    }

    public int getTotalHeaps() {

        return heapsizes.size();
    }

    private boolean isValidMove(int heapIndex, int numOfStonesToRemove) {
        return heapIndex >= 0 && heapIndex < heapsizes.size() &&
                numOfStonesToRemove >= 1 && numOfStonesToRemove <= heapsizes.get(heapIndex);
    }

    private void togglePlayer() {

        currentPlayer = (currentPlayer == 1) ? 2 : 1;
    }
    public List<Integer> getHeapsizes() {
        return heapsizes;
    }
}
