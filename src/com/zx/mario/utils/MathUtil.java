package com.zx.mario.utils;

import com.zx.mario.domain.Enemy;
import com.zx.mario.domain.GameObject;
import com.zx.mario.domain.MushRoom;
import com.zx.mario.domain.Obstacle;
import com.zx.mario.manager.Application;

import java.util.List;
import java.util.Random;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 数学计算相关
 * @since 2022 - 07 - 18 - 13:31
 */
public class MathUtil {
    private static Random random = new Random();
    private static int offset = 5;
    private static int offset2 = 10;
    /**
     * 获取一个指定范围的数
     *
     * @param min 最小值
     * @param max 最大值
     * @return 一个指定范围的数
     */
    public static int getRandomNum(int min, int max) {
        return random.nextInt(max - min) + min;
    }

    /**
     * 检查当前对象是否可以向左移动
     *
     * @param obstacles 障碍物
     * @param o         当前对象
     * @return if object can move left return true otherwise return false
     */

    public static boolean checkCanLeft(List<Obstacle> obstacles, GameObject o) {
        //遍历障碍物
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if(obstacle == null || obstacle.getX() < -obstacle.getWidth() || obstacle.getX() > Application.WindowWidth) {
                continue;
            }
            //判断是否可以向左
            if (obstacle.getX() + obstacle.getWidth() >= o.getX()
                    && obstacle.getX() < o.getX()
                    && obstacle.getY() + obstacle.getHeight() > o.getY() + offset
                    && obstacle.getY() + offset < o.getY() + o.getHeight()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检查当前对象是否可以向右移动
     *
     * @param obstacles 障碍物
     * @param o         当前对象
     * @return if object can move right return true otherwise return false
     */

    public static boolean checkCanRight(List<Obstacle> obstacles, GameObject o) {
        //遍历障碍物
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if(obstacle == null || obstacle.getX() < -obstacle.getWidth() || obstacle.getX() > Application.WindowWidth){
                continue;
            }
            //判断是否可以向右
            if (obstacle.getX() <= o.getX() + o.getWidth()
                    && obstacle.getX() > o.getX()
                    && obstacle.getY() + obstacle.getHeight()  > o.getY() + offset
                    && obstacle.getY() + offset < o.getY() + o.getHeight()) {
                return false;
            }
        }

        return true;
    }

    public static boolean checkOnObstacle(List<Obstacle> obstacles, GameObject o){
        for(int i = 0; i < obstacles.size(); i++){
            Obstacle obstacle = obstacles.get(i);
            if(obstacle == null || obstacle.getX() < -obstacle.getWidth() || obstacle.getX() > Application.WindowWidth){
                continue;
            }
            //判断是否位于障碍物上
            if(o.getY() + o.getHeight() - obstacle.getY() >= -offset
                    && o.getY() + o.getHeight() - obstacle.getY() < offset
                    && obstacle.getX() + offset <= o.getX() + o.getWidth()
                    && obstacle.getX() + obstacle.getWidth() > o.getX() + offset){
//                System.out.println("在障碍物上");
                return true;
            }
        }
        return false;
    }


    /**
     * 判断两个矩形是否相交
     * (x01, y01) 左上顶点 (x02, y02)右下顶点
     * @return 相交return true
     */
    public static boolean rectIntersect(int x01, int y01, int x02, int y02,
                          int x11, int y11, int x12, int y12) {
        int zx = Math.abs(x01 + x02 - x11 - x12);
        int x = Math.abs(x01 - x02) + Math.abs(x11 - x12);
        int zy = Math.abs(y01 + y02 - y11 - y12);
        int y = Math.abs(y01 - y02) + Math.abs(y11 - y12);
        return (zx <= x && zy <= y);
    }

    /**
     * 判断两个物体是否相撞
     * @param o1 物体1
     * @param o2 物体2
     * @return 如果相撞返回true,否则返回false
     */
    public static boolean checkTheCollision(GameObject o1, GameObject o2){
        int x01 = o1.getX() + offset2, y01 = o1.getY()+ offset2;
        int x02 = x01 + o1.getWidth() - offset2, y02 = y01 + o1.getHeight() - offset2;
        int x11 = o2.getX() + offset2, y11 = o2.getY() + offset2;
        int x12 = x11 + o2.getWidth() - offset2, y12 = y11 + o2.getHeight() - offset2;
        return rectIntersect(x01, y01, x02, y02, x11 , y11, x12, y12);
    }

    /**
     * 带有碰撞检测的移动
     * @param o 当前物体
     * @param obstacles 障碍物
     */
    public static void moveWithCollisionChecking(GameObject o, List<Obstacle> obstacles){
        boolean onObstacle = checkOnObstacle(obstacles, o);
        boolean canLeft = checkCanLeft(obstacles, o);
        boolean canRight = checkCanRight(obstacles, o);

        if (o.getX() >= -o.getWidth() && o.getX() <= Application.WindowWidth) {  //在窗口中出现
            if(!onObstacle && !o.onGrass()){
                o.setY(o.getY() + o.getSpeed());
            }else{
                if(o.isToRight()){
                    if(canRight){
                        o.setX(o.getX() + o.getSpeed());
                    }else{
                        o.setToRight(false);
                    }
                }else{
                    if(canLeft){
                        o.setX(o.getX() - o.getSpeed());
                    }else{
                        o.setToRight(true);
                    }
                }
            }
        }
    }
}
