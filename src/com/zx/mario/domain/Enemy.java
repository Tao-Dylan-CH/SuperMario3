package com.zx.mario.domain;

import com.zx.mario.manager.Application;
import com.zx.mario.utils.MathUtil;

import java.util.List;

import static com.zx.mario.manager.Application.gameWindow;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 敌人
 * @since 2022 - 07 - 19 - 22:09
 */
public class Enemy extends GameObject {
    //敌人类型
    private EnemyType type;
    //用于乌龟的判断
    public boolean isShell = false;
    //持续时间
    private int duration;
    public int updateFrequent = 5;
    private boolean isDown = true;  //用于判断食人花向下
    private boolean isLive = true;
    public Enemy() {
    }
    //乌龟变龟壳
    public void update(int imgSize, String filePrefix, boolean toShell){
        this.imgSize = imgSize;
        this.filePrefix = filePrefix;
        if(toShell){
            isShell = true;
            y += Application.tortoiseGap;
            speed = 0;
        }else{
            isShell = false;
            y -= Application.tortoiseGap;
            speed = 2;
        }
    }
    @Override
    public void update() {
        if(type == EnemyType.tortoise && isShell){
            if(duration > 0){
                duration--;
            }else{
                duration = Application.shellDuration;
                update(2, "tortoiseL", false);
            }
        }
        if (type == EnemyType.flower) {
            updateFrequent--;
            if (updateFrequent == 0) {
                updateFrequent = Application.flowerUpdateFrequent;
                if (isDown) {
                    currentImgIndex++;
                    if (currentImgIndex == 5)
                        isDown = false;
                } else {
                    currentImgIndex--;
                    if (currentImgIndex == 0)
                        isDown = true;
                }
            }

        } else {
            //切图片
            super.update();
        }
        //移动
        if (type == EnemyType.flower) {   //垂直移动

//            if(currentImgIndex == 2){
//                y += 15;
//            }else if(currentImgIndex == 4){
//                y += 15;
//            }else if(currentImgIndex == 0){
//                y -= 30;
//            }
            if(updateFrequent == Application.flowerUpdateFrequent){
                if (isDown) {
                    if (currentImgIndex == 2) {
                        y += 15;
                    } else if (currentImgIndex == 4) {
                        y += 15;
                    }
                } else {
                    if (currentImgIndex == 3) {
                        y -= 15;
                    } else if (currentImgIndex == 1) {
                        y -= 15;
                    }
                }
            }

        } else {  //水平移动
            //带有碰撞检测的移动
            MathUtil.moveWithCollisionChecking(this, Application.gameWindow.getObstacles());
        }
        if(x < -width){
            isLive = false;
            System.out.println("敌人失效！");
        }
    }
    public void die(){
        this.setLive(false);
        SpecialProp specialProp = null;
        if(type == EnemyType.fungus){
            specialProp = new SpecialProp(x, y, 1, 20, "fungus2", 0);
        }else if(type == EnemyType.tortoise){
            specialProp = new SpecialProp(x, y, 1, 20, "shell0", 0);
        }
        gameWindow.getSpecialProps().add(specialProp);
    }
    public Enemy(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex, EnemyType type) {
        super(x, y, imgSize, speed, filePrefix, currentImgIndex);
        this.type = type;
        if(type == EnemyType.fungus){
            duration = Application.fungusDuration;
        }else if(type == EnemyType.tortoise){
            duration = Application.shellDuration;
        }
    }
    public void moveLeft(){
        x -= speed;
    }
    public void moveRight(){
        x += speed;
    }

//    public Obstacle toObstacle(){
//        return new Obstacle()
//    }
    public EnemyType getType() {
        return type;
    }

    public void setType(EnemyType type) {
        this.type = type;
    }

    public boolean isLive() {
        return isLive;
    }

    public boolean isShell() {
        return isShell;
    }

    public void setShell(boolean shell) {
        isShell = shell;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setLive(boolean live) {
        isLive = live;
    }

    public boolean isToRight() {
        return toRight;
    }

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }
}
