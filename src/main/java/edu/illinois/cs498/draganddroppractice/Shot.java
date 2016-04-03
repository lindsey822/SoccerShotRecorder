package edu.illinois.cs498.draganddroppractice;

/**
 * Created by Lindsey Liu on 16-03-30.
 */

public class Shot {

    private int half;
    private int position;
    private int type;

    public Shot(int half, int position, int type) {
        this.half = half;
        this.position = position;
        this.type = type;
    }

    public int getHalf() {
        return half;
    }

    public void setHalf(int half) {
        this.half = half;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
