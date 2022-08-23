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
    //持续时间（乌龟壳）
    private int duration;
    private boolean isLive = true;

    //换图频率控制
    public int updateFrequent = Application.flowerUpdateFrequent;
    //用于判断食人花向下
    private boolean isDown = false;
    //食人花上下判断
    public int  displacement = 0;
    public Enemy() {
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

    /**
     * 得到一个食人花对象
     * @param x x坐标
     * @param y y坐标
     * @param obstacles 游戏窗体的障碍物集合，用于添加水管
     * @return 一个指定坐标的食人花对象
     */
    public static Enemy newFlowerInstance(int x, int y, List<Obstacle> obstacles){
        obstacles.add(Obstacle.newPieInstance(x - 7, y - 10));
        return new Enemy(x, y, 2, 3, "flower", 0, EnemyType.flower);
    }
//    public static Enemy newFlowerInstance(int x, int y, List<Obstacle> obstacles){
//        obstacles.add(Obstacle.newPieInstance(x - 7, y - 10));
//        return new Enemy(x, y, 6, 3, "flower", 5, EnemyType.flower);
//    }
    public static Enemy newFungusInstance(int x, int y){
        return new Enemy(x, y, 2, 4, "fungus", 0, EnemyType.fungus);
    }
    public static Enemy newTortoiseInstance(int x, int y){
        return new Enemy(x, y, 2, 3, "tortoiseL", 0, EnemyType.tortoise);
    }
    //乌龟变龟壳
    public void update(int imgSize, String filePrefix, boolean toShell){
        this.imgSize = imgSize;
        this.filePrefix = filePrefix;
        if(toShell){
            isShell = true;
            System.out.println(y);
            y += Application.tortoiseGap;
            System.out.println(y);
            speed = 0;
        }else{
            isShell = false;
            y -= Application.tortoiseGap;
            speed = 3;
        }
        updateSize();
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
            //6张图效果不好
//            updateFrequent--;
//            if (updateFrequent == 0) {
//                updateFrequent = Application.flowerUpdateFrequent;
//                if (isDown) {
//                    currentImgIndex++;
//                    if (currentImgIndex == 5)
//                        isDown = false;
//                } else {
//                    currentImgIndex--;
//                    if (currentImgIndex == 0)
//                        isDown = true;
//                }
//            }

            updateFrequent--;
            if (updateFrequent == 0) {
                updateFrequent = Application.flowerUpdateFrequent;
                //换图
                super.update();
            }
            if(displacement > 70){
                isDown = true;
            }else if(displacement <= 0){
                isDown = false;
            }
        } else {
            //切图片
            super.update();
        }
        //移动
        if (type == EnemyType.flower) {   //垂直移动
//瞬间移动，效果不好
//            if(currentImgIndex == 2){
//                y += 15;
//            }else if(currentImgIndex == 4){
//                y += 15;
//            }else if(currentImgIndex == 0){
//                y -= 30;
//            }

//这个是一套完整动画，但探出水管的部分太少
//            if(updateFrequent == Application.flowerUpdateFrequent){
//                if (isDown) {
//                    if (currentImgIndex == 2) {
//                        y += 15;
//                    } else if (currentImgIndex == 4) {
//                        y += 15;
//                    }
//                } else {
//                    if (currentImgIndex == 3) {
//                        y -= 15;
//                    } else if (currentImgIndex == 1) {
//                        y -= 15;
//                    }
//                }
//            }

            //2张图片
            if(!isDown){
                displacement += speed;
                y -= speed;
            }else{
                displacement -= speed;
                y += speed;
            }
        } else {  //水平移动
            //带有碰撞检测的移动
            MathUtil.moveWithCollisionChecking(this, Application.gameWindow.getObstacles());
        }
        //乌龟的两个方向图片不一样
        if(type == EnemyType.tortoise && !isShell){
            if(toRight){
                filePrefix = "tortoiseR";
            }else{
                filePrefix = "tortoiseL";
            }
        }
//        出左边窗口移除
        if(x < -width){
            isLive = false;
//            System.out.println("敌人失效！");
        }
    }

    /**
     * 敌人死亡时调用
     * 设置死亡，添加动画
     */
    public void die(){
        this.setLive(false);
        SpecialProp specialProp = null;
        if(type == EnemyType.fungus){
            specialProp = SpecialProp.newSquashedFungusInstance(x, y);
        }else if(type == EnemyType.tortoise){
            specialProp = SpecialProp.newShellInstance(x, y);
        }
        if(specialProp != null)
            gameWindow.getSpecialProps().add(specialProp);
    }

    /**
     * 踩坑掉落出窗口时调用该方法
     *
     */
    public void fallOff(){
        this.setLive(false);
        SpecialProp specialProp = null;
        if(type == EnemyType.fungus){
            specialProp = SpecialProp.newFungusInstance(x, y);
        }else if(type == EnemyType.tortoise){
            specialProp = SpecialProp.newTortoiseInstance(x, y, toRight);
        }
        if(specialProp != null)
            gameWindow.getSpecialProps().add(specialProp);
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
