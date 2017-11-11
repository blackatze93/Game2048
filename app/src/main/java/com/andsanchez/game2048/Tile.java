package com.andsanchez.game2048;

public class Tile {
    int value;

    public Tile() {
        this(0);
    }

    public Tile(int value) {
        this.value = value;
    }

    public boolean isEmpty() {
        return this.value == 0;
    }
}
