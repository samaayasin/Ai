package com.example.demo2;


public class Move {
    private int heapIndex;
    private int stonesToRemove;

    public Move(int heapIndex, int stonesToRemove) {
        this.heapIndex = heapIndex;
        this.stonesToRemove = stonesToRemove;
    }

    public int getHeapIndex() {

        return heapIndex;
    }

    public int getStonesToRemove() {

        return stonesToRemove;
    }
}