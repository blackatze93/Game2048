package com.andsanchez.game2048;

public class User {
    private String name;
    private String photoUrl;
    private long max;
    private long score;

    public User() {}

    public User(String name, String photoUrl) {
        this.name = name;
        this.photoUrl = photoUrl;
        this.max = 0;
        this.score = 0;
    }

    public String getName() {
        return name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public long getMax() {
        return max;
    }

    public void setMax(long max) {
        this.max = max;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }
}
