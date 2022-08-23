package com.zx.mario.domain;

import com.zx.mario.manager.Application;



/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 游戏中的障碍物
 * @since 2022 - 07 - 19 - 22:08
 */
public class Obstacle extends GameObject{
    //障碍物类型
    private ObstacleType type;
    //切图片的频率
    public int updateFrequent = 3;
    //判断问号方块类型
    private BoxType boxType;
    //藏有多颗金币的普通方块

    private int goldsCount = 9;
    public Obstacle(){

    }
    public static Obstacle newBoxInstance(int x, int y, BoxType boxType){
        Obstacle obstacle = new Obstacle(x, y, 4, 0, "box", 0, ObstacleType.box);
        obstacle.setBoxType(boxType);
        return obstacle;
    }
    //可摧毁的砖块
    public static Obstacle newBrick0Instance(int x, int y){
        return new Obstacle(x, y, 1, 0, "brick0", 0, ObstacleType.brick0);
    }
    //不可摧毁的砖块
    public static Obstacle newBrick1Instance(int x, int y){
        return new Obstacle(x, y, 1, 0, "brick1", 0, ObstacleType.brick1);
    }
    //藏有金币的普通方块
    public static Obstacle newGoldBrick0Instance(int x, int y){
        return new Obstacle(x, y, 1, 0, "brick0", 0, ObstacleType.brick0ContainsGold);
    }
    //藏有星星的普通方块
    public static Obstacle newStarBrick0Instance(int x, int y){
        return new Obstacle(x, y, 1, 0, "brick0", 0, ObstacleType.brick0ContainsStar);
    }
    //水管
    public static Obstacle newPieInstance(int x, int y){
        return new Obstacle(x, y, 1, 0, "pipe", 0, ObstacleType.pipe);
    }
    //坑
    public static Obstacle newPitInstance(int x, int y){
        return new Obstacle(x, y, 1, 0, "pit", 0, ObstacleType.pit);
    }
    //旗帜
    public static Obstacle newFlagInstance(int x, int y){
        return new Obstacle(x, y, 1, 0, "flag", 0, ObstacleType.flag);
    }
    //旗杆
    public static Obstacle newGanInstance(int x, int y){
        return new Obstacle(x, y, 1, 0, "gan", 0, ObstacleType.gan);
    }
    //城堡
    public static Obstacle newTowerInstance(int x, int y){
        return new Obstacle(x, y, 1, 0, "tower", 0, ObstacleType.gan);
    }
    //隐藏的方块
    public static Obstacle newHiddenBrickInstance(int x, int y){
        return new Obstacle(x, y, 1, 0, "hiddenBrick", 0, ObstacleType.hiddenBrick);
    }
    public Obstacle(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex, ObstacleType type) {
        super(x, y, imgSize, speed, filePrefix, currentImgIndex);
        this.type = type;
    }
    public void update(int imgSize, String filePrefix, ObstacleType type){
        this.imgSize = imgSize;
        this.filePrefix = filePrefix;
        this.type = type;
    }
    @Override
    public void update() {
        //更新图片
        if(type == ObstacleType.box){
            updateFrequent--;
            if(updateFrequent == 0){
                updateFrequent = Application.boxUpdateFrequent;
                super.update();
            }
        }else{
            super.update();
        }
        //更新位置
//        if(speed > 0){
//            switch (type){
//                case mushroom:
//                    x -= speed;
//                    break;
//                case flower:
//                    y -= speed;
//                    break;
//                case goldBox:
//
//                    break;
//                case brick0:
//                    break;
//                case brick1:
//                    break;
//                case flag:
//
//                    break;
//                case pipe:
//
//                    break;
//            }
//        }
    }

    public int getUpdateFrequent() {
        return updateFrequent;
    }

    public void setUpdateFrequent(int updateFrequent) {
        this.updateFrequent = updateFrequent;
    }

    public BoxType getBoxType() {
        return boxType;
    }

    public void setBoxType(BoxType boxType) {
        this.boxType = boxType;
    }

    public ObstacleType getType() {
        return type;
    }

    public void setType(ObstacleType type) {
        this.type = type;
    }

    public int getGoldsCount() {
        return goldsCount;
    }

    public void setGoldsCount(int goldsCount) {
        this.goldsCount = goldsCount;
    }
}
