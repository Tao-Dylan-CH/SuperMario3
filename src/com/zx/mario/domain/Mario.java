package com.zx.mario.domain;

import com.zx.mario.domain.*;
import com.zx.mario.manager.Application;
import com.zx.mario.utils.MathUtil;

import javax.swing.*;
import java.util.List;

import static com.zx.mario.domain.Status.*;
import static com.zx.mario.manager.Application.*;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 马里奥
 * @since 2022 - 07 - 18 - 23:01
 */
public class Mario extends GameObject {
    //状态
    private Status status;
    //跳跃时间
    private int upTime = Application.uptime;
    private int jumpSpeed = Application.jumpSpeed;
    //判断是否可左右移动
    private boolean canLeft = true;
    private boolean canRight = true;
    private boolean onObstacle = false;
    //无敌时间
    private int invincibleTime = Application.invincibleTime;
    private boolean isInvincible = false;
    public Mario(){}
    public Mario(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex){
        super(x, y, imgSize, speed, filePrefix, currentImgIndex);
        status = Status.s_mario_stand_R;
    }
    public boolean isBigMario = false;
    /**
     * 根据状态更新 显示图片
     * @param status 马里奥状态
     */
    public void update(Status status){
        this.status = status;
        imgSize = Application.getImgSizeByStatus(status);
        filePrefix = Application.getFilePrefixByStatus(status);
    }
    public void moveRight(){
        if(canRight && x < Application.marioMaxX + 100)
            x += speed;
    }
    public void moveLeft(){
        if(canLeft && x - speed > 0)
            x -= speed;
    }

    /**
     * 调用该方法更新玛丽奥 坐标 和 图片
     */
    public void response(){
//        System.out.println(x + ", " + y);
//        if(speed > 5){
//            System.out.println(speed);
//        }
        if(isInvincible){
            invincibleTime--;
            if(invincibleTime <= 0)
                isInvincible = false;
        }
        //从障碍物上下落
        if(!onGrass() && !onObstacle){
            if(isToRight()){
                if(!isBigMario)
                    update(Status.s_mario_jump_R);
                else
                    update(Status.mario_R_jump);
            }else if(isToLeft()){
                if(!isBigMario)
                    update(Status.s_mario_jump_L);
                else
                    update(Status.mario_L_jump);
            }else if(isStand()){
                if(!isBigMario){
                    if(status == Status.s_mario_stand_R)
                        update(Status.s_mario_jump_R);
                    else if(status == Status.s_mario_stand_L)
                        update(Status.s_mario_jump_L);
                }else{
                    if(status == Status.s_mario_stand_R){
                        update(Status.mario_R_jump);
                    }else if(status == Status.s_mario_stand_L){
                        update(Status.mario_L_jump);
                    }
                }
            }
        }
        //更新索引 图片
        super.update();
        //更新坐标 位置
        switch (status){
            case s_mario_run_R:
            case mario_R_run:
            case s_mario_stop_L:
            case mario_stop_L:
                moveRight();
                break;
            case s_mario_run_L:
            case mario_L_run:
            case s_mario_stop_R:
            case mario_stop_R:
                moveLeft();
                break;

            case s_mario_jump_R:
            case mario_R_jump:
                if(upTime > 0){
                    if(canRight && isPressD)
                        x += speed;
                    up();
                }else{
                    fall();
                }

                break;
            case s_mario_jump_L:
            case mario_L_jump:
                if(upTime > 0){
                    if(canLeft && isPressA && x - speed > 0)
                        x -= speed;
                    up();
                }else{
                    fall();
                }
                break;
            case s_mario_die:
                break;
            case s_mario_stand_R:
                break;
            case s_mario_stand_L:
                break;
        }
    }

    public boolean isToLeft() {
        return status == s_mario_run_L || status == Status.s_mario_stop_R || status == Status.s_mario_jump_L
                || status == Status.mario_L_run || status == Status.mario_stop_R || status == Status.mario_L_jump;
    }

    /**
     * 判断当前是否为站立
     * @return true if isStand
     */
    public boolean isStand(){
        return status == Status.s_mario_stand_R || status == Status.s_mario_stand_L
                || status == Status.mario_R_stand || status == Status.mario_L_stand;
    }

    /**
     * 判断当前是否向右
     * @return true if isToRight
     */
    public boolean isToRight(){
//        return (status == Status.s_mario_jump_R && isPressD) || status == Status.s_mario_run_R || status == Status.s_mario_stop_L;
        return status == s_mario_run_R || status == Status.s_mario_stop_L || (status == Status.s_mario_jump_R && isPressD)
                || status == Status.mario_R_run || status == Status.mario_stop_L || (status == Status.mario_R_jump && isPressD);
    }

    /**
     * 判断当前是否可以跳跃
     * @return true if you can jump
     */
    public boolean canJump(){
        return !isJump() && (isStand() || isToLeft() || isToRight());
    }
    public boolean isJump(){
        return status == Status.mario_L_jump || status == Status.mario_R_jump
                || status == Status.s_mario_jump_L || status == Status.s_mario_jump_R;
    }
    public boolean isStop(){
        return status == Status.s_mario_stop_L || status == Status.s_mario_stop_R
                ||status == Status.mario_stop_L || status == Status.mario_stop_R;
    }
    public void fall(){
        if(onObstacle || onGrass()){  //落在障碍物上
            //修改状态
            if(status == Status.s_mario_jump_L || status == Status.mario_L_jump){
                if(isPressA)
                    if(!isBigMario)
                        update(s_mario_run_L);
                    else
                        update(Status.mario_L_run);
                else
                    if(!isBigMario)
                        update(Status.s_mario_stand_L);
                    else
                        update(Status.mario_L_stand);
            }else if(status == Status.s_mario_jump_R || status == Status.mario_R_jump){
                if(isPressD)
                    if(!isBigMario)
                        update(s_mario_run_R);
                    else
                        update(Status.mario_R_run);
                else
                    if(!isBigMario)
                        update(Status.s_mario_stand_R);
                    else
                        update(Status.mario_R_stand);
            }
        }else{  //下落
            down();
        }
//        if(y < Application.gameGrassY - height){    //下降
//            y += jumpSpeed;
//            if(status == Status.s_mario_jump_L){
//                if(isPressA && x - speed > 0)
//                    x -= speed;
//            }else if(status == Status.s_mario_jump_R){
//                if(isPressD)
//                    x += speed;
//            }
//        }else{
//            //恢复
//            upTime = Application.uptime;
//            //修改状态
//            if(status == Status.s_mario_jump_L){
//                if(isPressA)
//                    update(Status.s_mario_run_L);
//                else
//                    update(Status.s_mario_stand_L);
//            }else if(status == Status.s_mario_jump_R){
//                if(isPressD)
//                    update(Status.s_mario_run_R);
//                else
//                    update(Status.s_mario_stand_R);
//            }
//        }

    }
    public void up(){
        y -= jumpSpeed;
        upTime--;
    }
    private void down() {
        y += jumpSpeed;
        if(y + height > gameGrassY)
            y = gameGrassY - height;
        if(status == Status.s_mario_jump_L || status == Status.mario_L_jump){
            if(isPressA && x - speed > 0 && canLeft)
                x -= speed;
        }else if((status == Status.s_mario_jump_R && canRight) || (status == Status.mario_R_jump && canRight)){
            if(isPressD)
                x += speed;
        }
    }
    public void shapeShifting(){
        currentImgIndex = 0;
        if(isBigMario){
            //变小
            y += 15;
            switch (status){
                case mario_L_run:
                    update(s_mario_run_L);
                    break;
                case mario_R_run:
                    update(s_mario_run_R);
                    break;
                case mario_R_stand:
                    update(s_mario_stand_R);
                    break;
                case mario_L_stand:
                    update(s_mario_stand_L);
                    break;
                case mario_stop_L:
                    update(s_mario_stop_L);
                    break;
                case mario_stop_R:
                    update(s_mario_stop_R);
                    break;
                case mario_R_jump:
                    update(s_mario_jump_R);
                    break;
                case mario_L_jump:
                    update(s_mario_jump_L);
                    break;
            }
        }else{
            //变大
            y -= 15;
            switch (status){
                case s_mario_run_L:
                    update(Status.mario_L_run);
                    break;
                case s_mario_run_R:
                    update(Status.mario_R_run);
                    break;
                case s_mario_stand_R:
                    update(Status.mario_R_stand);
                    break;
                case s_mario_stand_L:
                    update(Status.mario_L_stand);
                    break;
                case s_mario_stop_L:
                    update(Status.mario_stop_L);
                    break;
                case s_mario_stop_R:
                    update(Status.mario_stop_R);
                    break;
                case s_mario_jump_R:
                    update(Status.mario_R_jump);
                    break;
                case s_mario_jump_L:
                    update(Status.mario_L_jump);
                    break;
            }
        }
        isBigMario = !isBigMario;
        //更新图片大小
        updateSize();
    }
    //碰撞检测
    public void checkMove(List<Obstacle> obstacles){
        boolean canLeft1 = true, canRight1 = true, onObstacle1 = false;
        int offset = 5, offset2 = 10;
        //遍历障碍物
        for (int i = 0; i < obstacles.size(); i++) {
            Obstacle obstacle = obstacles.get(i);
            if(obstacle == null || obstacle.getX() < -obstacle.getWidth()
                    || obstacle.getX() > Application.WindowWidth){
                continue;
            }
            //判断是否可以向左
            if (obstacle.getX() + obstacle.getWidth() >= x
                    && obstacle.getX() < x
                    && obstacle.getY() + obstacle.getHeight()  > y + offset
                    && obstacle.getY() + offset < y + height ) {
                canLeft1 = false;
//                System.out.println("不能向左移动!");
            }
            //判断是否可以向右
            if (obstacle.getX() <= x + width
                    && obstacle.getX() > x
                    && obstacle.getY() + offset < y + height
                    && obstacle.getY() + obstacle.getHeight() > y + offset) {
                canRight1 = false;
//                System.out.println("不能右移！");
            }
//            if(x > obstacle.x && x < obstacle.width + obstacle.x && y < obstacle.y){
//                System.out.println(y + height - obstacle.getY() + ">=0");
//                System.out.println(y + height - obstacle.getY() + "<=" + offset);
//                System.out.println(obstacle.getX() + offset + "<="+ (x + width));
//                System.out.println(obstacle.getX() + obstacle.getWidth() + ">" + (x +offset));
//            }
            //判断是否位于障碍物上
            if(y + height - obstacle.getY() >= -offset
                    && y + height - obstacle.getY() < offset
                    && obstacle.getX() + offset <= x + width
                    && obstacle.getX() + obstacle.getWidth() > x + offset){
//                System.out.println("在障碍物上");
                onObstacle1 = true;
            }

            //判断向上顶到砖块
            if(obstacle.getX() + offset <= x + width
                    && obstacle.getX() + obstacle.getWidth() > x + offset
                    && obstacle.getY() + obstacle.getHeight() - y >= 0
                    && obstacle.getY() + obstacle.getHeight() - y < offset2){
//                System.out.println("撞到砖块了...");
                //下降
                setUpTime(0);
                if(obstacle.getType() == ObstacleType.brick0){  //可破坏的砖块
                    //创建特效对象
                    List<SpecialProp> specialProps = gameWindow.getSpecialProps();
                    for(int j = 1; j <= 4; j++){
                        int x, y;
                        if(j == 1){
                            x = obstacle.x - offset;
                            y = obstacle.y - offset;
                        }else if(j == 2){
                            x = obstacle.x + obstacle.width / 2 + offset;
                            y = obstacle.y - offset;
                        }else if(j == 3){
                            x = obstacle.x - offset;
                            y = obstacle.y + obstacle.height / 2 + offset;
                        }else{
                            x = obstacle.x + obstacle.width / 2 + offset;
                            y = obstacle.y + obstacle.height / 2 + offset;
                        }
                        SpecialProp specialProp = new SpecialProp(x, y, 1, 20, "brick0" + j, j);
                        specialProps.add(specialProp);
                    }
                    obstacles.remove(obstacle);
                }else if(obstacle.getType() == ObstacleType.box){
                    //更改状态
                    obstacle.update(1, "nothing", ObstacleType.noting);
                    if(obstacle.isMushroom){    //蘑菇
                        Obstacle mushroom = new MushRoom(obstacle.x, obstacle.y, 1, 3, "mushroom1", 0, ObstacleType.mushroom);
                        mushroom.setX(obstacle.x + (obstacle.width - mushroom.width) / 2);
                        gameWindow.getGainProps().add(mushroom);
                        obstacle.isMushroom = false;
                    }else{      //金币
//                        Obstacle mushroom = new Obstacle(obstacle.x, obstacle.y, 1, 3, "mushroom1", 0, ObstacleType.mushroom);
//                        gameWindow.getGainProps().add(mushroom);
                    }
                }
            }
        }
        setCanLeft(canLeft1);
        setCanRight(canRight1);
        setOnObstacle(onObstacle1);
//        //判断马里奥是否可向左右移动
//        setCanLeft(MathUtil.checkCanLeft(obstacles, this));
//        setCanRight(MathUtil.checkCanRight(obstacles, this));
////        if(!MathUtil.checkCanRight(obstacles, this)){
////            System.out.println("不能向左!");
////        }
//        //判断是否在障碍物上
//        setOnObstacle(MathUtil.checkOnObstacle(obstacles, this));
    }

    //获取增益检测
    public void checkMove2(List<Obstacle> gainProps){
        for (int i = 0; i < gainProps.size(); i++) {
            Obstacle obstacle = gainProps.get(i);
            if(obstacle instanceof MushRoom){
                MushRoom mushRoom = (MushRoom) obstacle;
                boolean intersect = MathUtil.checkTheCollision(mushRoom, this);
                if(intersect){
                    //移除蘑菇
                    gainProps.remove(mushRoom);
                    //转换形态
                    shapeShifting();
                }
            }
        }
    }
    //检测与敌人发生碰撞
    public void checkMove3(List<Enemy> enemies){
        int offset = 5;
        for (Enemy enemy : enemies) {
            //踩到敌人
            if (y + height - enemy.getY() >= -offset
                    && y + height - enemy.getY() < offset
                    && enemy.getX() + offset <= x + width
                    && enemy.getX() + enemy.getWidth() > x + offset) {
                if (enemy.getType() == EnemyType.fungus) {
                    setUpTime(3);
                    enemy.die();
                }else if(enemy.getType() == EnemyType.tortoise ){
                    if(!enemy.isShell){
                        setUpTime(3);
                        enemy.update(1, "shell0", true);
                    }else{
                        setUpTime(4);
                        enemy.die();
                    }

                }
            } else if (MathUtil.checkTheCollision(this, enemy)) {
                if(enemy.getType() == EnemyType.tortoise){
                    if(enemy.isShell){
                        System.out.println("龟壳！");
                    }else{
                        System.out.println("乌龟！");
                    }
                }else{
                    if(isBigMario){
                        shapeShifting();
                        isInvincible = true;
                    }else if(!isInvincible){
//                        JOptionPane.showMessageDialog(gameWindow, "死亡");
                        System.out.println("死亡");
                    }
                }
            }
        }
    }
    public void stopRight(){
        currentImgIndex = 0;
    }
    public void stopLeft(){
        currentImgIndex = 0;
    }

    public Status getStatus() {
        return status;
    }
    public void setStatus(Status status) {
        this.status = status;
    }

    public int getUpTime() {
        return upTime;
    }

    public void setUpTime(int upTime) {
        this.upTime = upTime;
    }

    public int getJumpSpeed() {
        return jumpSpeed;
    }

    public void setJumpSpeed(int jumpSpeed) {
        this.jumpSpeed = jumpSpeed;
    }

    public boolean isCanLeft() {
        return canLeft;
    }

    public void setCanLeft(boolean canLeft) {
        this.canLeft = canLeft;
    }

    public boolean isCanRight() {
        return canRight;
    }

    public void setCanRight(boolean canRight) {
        this.canRight = canRight;
    }

    public boolean isOnObstacle() {
        return onObstacle;
    }

    public void setOnObstacle(boolean onObstacle) {
        this.onObstacle = onObstacle;
    }

    public int getInvincibleTime() {
        return invincibleTime;
    }

    public void setInvincibleTime(int invincibleTime) {
        this.invincibleTime = invincibleTime;
    }
}
