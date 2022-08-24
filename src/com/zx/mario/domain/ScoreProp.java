package com.zx.mario.domain;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 用于游戏界面显示得分的效果
 * @since 2022 - 08 - 24 - 13:50
 */
public class ScoreProp{
    private int x;
    private int y;
    private int speed;
    private int score;
    private int duration;
    public ScoreProp(int x, int y, int speed, int score, int duration) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.score = score;
        this.duration = duration;
    }
    public static ScoreProp newScorePropInstance(int x, int y, int score){
        return new ScoreProp(x, y, 5, score, 10);
    }

    public void update() {
        //向右上角移动
        x += speed / 2;
        y -= speed;
        duration--;
    }

    public boolean isInvalid(){
        return duration <= 0;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
