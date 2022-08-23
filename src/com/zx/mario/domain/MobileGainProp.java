package com.zx.mario.domain;

import com.zx.mario.manager.Application;
import com.zx.mario.utils.MathUtil;


/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 蘑菇 星星
 * @since 2022 - 07 - 24 - 17:48
 */
public class MobileGainProp extends Obstacle implements Runnable{
    private boolean isLive = true;
    public MobileGainProp() {
    }

    public static MobileGainProp newMushroomInstance(int x, int y, int type){
        ObstacleType obstacleType;
        if(type == 1){
            obstacleType = ObstacleType.mushroom;
        }else{
            obstacleType = ObstacleType.mushroom1;
        }
        return new MobileGainProp(x, y, 1, 3, "mushroom" + type, 0, obstacleType);
    }
    public static MobileGainProp newStarInstance(int x, int y){
        return new MobileGainProp(x, y, 1, 3, "star", 0, ObstacleType.star);
    }
    public MobileGainProp(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex, ObstacleType type) {
        super(x, y, imgSize, speed, filePrefix, currentImgIndex, type);
        //在方块上
        this.y = y - getHeight();
        //随机方向
        toRight = MathUtil.getRandomNum(0, 4) > 0;
        //启动线程
        new Thread(this).start();
    }
    public void die(){
        isLive = false;
    }
    public void fallOff(){
        die();
        SpecialProp specialProp = null;
        if(getType() == ObstacleType.mushroom){
            specialProp = SpecialProp.newMushroom1Instance(x, y);
        }else if(getType() == ObstacleType.star){
            specialProp = SpecialProp.newStarInstance(x, y);
        }
        if(specialProp != null)
            Application.gameWindow.getSpecialProps().add(specialProp);
    }
    @Override
    public void run() {
        while(isLive){
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(x < -width || x >Application.WindowWidth)
                isLive = false;
            MathUtil.moveWithCollisionChecking(this, Application.gameWindow.getObstacles());
        }
    }

    public boolean isLive() {
        return isLive;
    }

    public void setLive(boolean live) {
        isLive = live;
    }
}
