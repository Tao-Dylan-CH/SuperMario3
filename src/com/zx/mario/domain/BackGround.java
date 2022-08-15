package com.zx.mario.domain;

import com.zx.mario.manager.Application;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 游戏背景
 * @since 2022 - 07 - 18 - 23:05
 */
public class BackGround extends GameObject{
    public BackGround(){}
    public BackGround(int x, int y, int speed, String filePrefix){
        super(x, y, 1, speed, filePrefix, 0);
    }
    public void move(){
        x -= speed;
        if(x <= -Application.WindowWidth){
            x = 0;
        }
    }
}
