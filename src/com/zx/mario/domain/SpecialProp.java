package com.zx.mario.domain;

import com.zx.mario.manager.Application;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 特效
 * @since 2022 - 07 - 24 - 16:56
 */
public class SpecialProp extends Obstacle{
    private double rate = 1;
    public SpecialProp() {
    }
    public SpecialProp(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex) {
        super(x, y, imgSize, speed, filePrefix, currentImgIndex, null);
    }
    public void down(){
        y += speed;
    }

    @Override
    public void update() {
        rate /= 1.3;
//        System.out.println(rate);
//        super.update();
        down();
        switch (currentImgIndex){
            case 1:
            case 3:
                x -= (speed * rate);
                break;
            case 2:
            case 4:
                x += (speed * rate);
                break;
        }
    }
    public boolean isInvalid(){
        return x < 0 || x > Application.WindowWidth || y > Application.WindowHeight;
    }
}
