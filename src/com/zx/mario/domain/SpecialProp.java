package com.zx.mario.domain;

import com.zx.mario.manager.Application;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 特效
 * @since 2022 - 07 - 24 - 16:56
 */
public class SpecialProp extends GameObject{
    private SpecialPropType specialPropType;
    //砖块碎裂抛物线
    private double rate = 1;
    //金币上抛和下降
    private int uptime;
    //动画是否结束
    private boolean isAnimationEnd = false;
    public SpecialProp() {
    }
    public SpecialProp(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex) {
        super(x, y, imgSize, speed, filePrefix, currentImgIndex);
    }
    public SpecialProp(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex, SpecialPropType type) {
        super(x, y, imgSize, speed, filePrefix, currentImgIndex);
        this.specialPropType = type;
    }

    /**
     * 根据砖块的信息 得到破碎后的四个碎片
     * @param obstacle 砖块
     * @return
     */
    public static List<SpecialProp> newBrickInstance(Obstacle obstacle){
        int offset = 5;
        List<SpecialProp> specialProps = new ArrayList<>();
        for(int j = 1; j <= 4; j++){
            int x0, y0;
            if(j == 1){
                x0 = obstacle.x - offset;
                y0 = obstacle.y - offset;
            }else if(j == 2){
                x0 = obstacle.x + obstacle.width / 2 + offset;
                y0 = obstacle.y - offset;
            }else if(j == 3){
                x0 = obstacle.x - offset;
                y0 = obstacle.y + obstacle.height / 2 + offset;
            }else{
                x0 = obstacle.x + obstacle.width / 2 + offset;
                y0 = obstacle.y + obstacle.height / 2 + offset;
            }
            SpecialProp specialProp = new SpecialProp(x0, y0, 1, 20, "brick0" + j, j, SpecialPropType.brick);
            specialProps.add(specialProp);
        }
        return specialProps;
    }
    //踩扁的蘑菇
    public static SpecialProp newSquashedFungusInstance(int x, int y){
        return new SpecialProp(x, y, 1, 20, "fungus2", 0, SpecialPropType.fungus);
    }
    //正常蘑菇
    public static SpecialProp newFungusInstance(int x, int y){
        return new SpecialProp(x, y, 1, 20, "fungus0", 0, SpecialPropType.fungus);
    }
    //龟壳
    public static SpecialProp newShellInstance(int x, int y){
        return new SpecialProp(x, y, 1, 20, "shell0", 0, SpecialPropType.shell);
    }
    //乌龟
    public static SpecialProp newTortoiseInstance(int x, int y, boolean toRight){
        String filePrefix = null;
        if(toRight){
            filePrefix = "tortoiseR0";
        }else{
            filePrefix = "tortoiseL0";
        }
        return new SpecialProp(x, y, 1, 20, filePrefix, 0, SpecialPropType.shell);
    }
    //金币
    public static SpecialProp newGoldInstance(int x, int y){
        SpecialProp prop = new SpecialProp(x, y, 6, 20, "gold", 0, SpecialPropType.gold);
        prop.uptime = 5;
        prop.y = y - prop.getHeight();
//        prop.x = x + 8;
        return prop;
    }
    //金币
    public static SpecialProp newStaticGoldInstance(int x, int y){
        return new SpecialProp(x, y, 6, 20, "gold", 0, SpecialPropType.staticGold);
    }
    //蘑菇
    public static SpecialProp newMushroom1Instance(int x, int y){
        return new SpecialProp(x, y, 1, 20, "mushroom1", 0, SpecialPropType.mushroom1);
    }
    //星星
    public static SpecialProp newStarInstance(int x, int y){
        return new SpecialProp(x, y, 1, 20, "star", 0, SpecialPropType.star);
    }

    public void down(){
        y += speed;
    }

    @Override
    public void update() {
        //更新图片
        super.update();
        //移动
        if(specialPropType == SpecialPropType.brick){  //砖块破碎
            rate /= 1.3;
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
        }else if(isDownSpecialProp()){  //蘑菇、乌龟下落
            down();
        }else if(specialPropType == SpecialPropType.gold){ //金币
            uptime--;
            if(uptime > 0){
                y -= speed;
            }else{
                y += speed;
            }
            if(uptime < -3){
                isAnimationEnd = true;
            }

        }
    }
    private boolean isDownSpecialProp(){
        return specialPropType == SpecialPropType.fungus
                || specialPropType == SpecialPropType.shell
                || specialPropType == SpecialPropType.mushroom1
                || specialPropType == SpecialPropType.star;
    }
    /**
     * 判断动画失效
     * @return 是否失效
     */
    public boolean isInvalid(){
        if(specialPropType == SpecialPropType.gold){
            return isAnimationEnd;
        }else{
            return x < 0 || x > Application.WindowWidth || y > Application.WindowHeight;
        }
    }


    public boolean isAnimationEnd() {
        return isAnimationEnd;
    }

    public void setAnimationEnd(boolean animationEnd) {
        isAnimationEnd = animationEnd;
    }
}
