package com.zx.mario.domain;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 障碍物类型
 * @since 2022 - 07 - 19 - 22:19
 */
public enum ObstacleType {
    mushroom,   //蘑菇
    mushroom1,  //加命蘑菇
    star,       //星星
    flower,     //花
    box,    //未知方块
    noting, //什么都没有的方块
    brick0,     //可以摧毁的砖块
    brick0ContainsGold, //含有多个金币
    brick0ContainsStar, //含有无敌星星
    brick1,     //不可摧毁的砖块
    flag,       //旗帜
    gan,        //旗杆
    pipe,       //水管
    shell,       //龟壳
    pit,          //坑
    tower,        //城堡
    hiddenBrick     //隐形方块
}
