package com.andsanchez.game2048;

public class User {
    private String name;
    private String photoUrl;
    private int max;
    private int score;

    public User() {}

    public User(String name, String photoUrl, int max, int score) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.max = max;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
