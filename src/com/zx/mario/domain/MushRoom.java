package com.zx.mario.domain;

import com.zx.mario.manager.Application;
import com.zx.mario.utils.MathUtil;

import java.util.List;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 蘑菇
 * @since 2022 - 07 - 24 - 17:48
 */
public class MushRoom extends Obstacle implements Runnable{
    private boolean isLive = true;
    public MushRoom() {
    }

    public MushRoom(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex, ObstacleType type) {
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
