package com.zx.mario.domain;

import com.zx.mario.manager.Application;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 黑粒子
 * @since 2022 - 08 - 19 - 9:36
 */
public class Bullet extends GameObject{
    public boolean isInvalid = false;
    public Bullet() {
    }

    public Bullet(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex, boolean isToRight) {
        super(x, y, imgSize, speed, filePrefix, currentImgIndex);
        this.toRight = isToRight;
    }
    public boolean isInvalid(){
        return isInvalid || x < -width || x > Application.WindowWidth;
    }

    @Override
    public void update() {
        super.update();
        move();
    }

    public void move(){
        if(toRight){
            x += speed;
        }else{
            x -= speed;
        }
    }
}
