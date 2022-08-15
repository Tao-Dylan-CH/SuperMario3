package com.zx.mario.domain;

import com.zx.mario.manager.Application;

import static com.zx.mario.manager.Application.gameGrassY;

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
    //判断问号方块是否有蘑菇
    public boolean isMushroom = false;
    public Obstacle(){

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
    public ObstacleType getType() {
        return type;
    }

    public void setType(ObstacleType type) {
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

}
