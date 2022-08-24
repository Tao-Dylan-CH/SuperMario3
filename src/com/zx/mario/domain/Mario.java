package com.zx.mario.domain;

import com.zx.mario.manager.Application;
import com.zx.mario.service.MusicService;
import com.zx.mario.utils.MathUtil;

import java.util.List;

import static com.zx.mario.domain.Status.*;
import static com.zx.mario.domain.Status.hero_L_stand;
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
    //不受伤害  (状态切换时)
    private boolean isInvincible = false;
    //无敌并且伤害敌人 （获得无敌星星）
    private boolean isHarmEnemyStatus = false;
    private int durationOfStar = Application.durationOfStar;
    //位移
    private int displacement;
    //记录攻击前的状态
    private Status statusBeforeAttack;
    private int fireTimeCount = -1;
    private int fireInterval = Application.fireInterval;

    public Mario(){}
    public Mario(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex){
        super(x, y, imgSize, speed, filePrefix, currentImgIndex);
        status = Status.s_mario_stand_R;
        displacement = x;
    }
    //当前英雄类型
    private HeroType heroType = HeroType.smallMario;
    //后面加了第三种形态， isBigMario 不再使用
    @Deprecated
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
        if(canRight && x < Application.marioMaxX + 100){
            displacement += speed;
            x += speed;
        }
    }
    public void moveLeft(){
        if(canLeft && x - speed > 0){
            displacement -= speed;
            x -= speed;
        }
    }

    //后面加了第三种形态， 使用updateXXX()根据形态 更新状态
    public void updateJumpR(){
        if(heroType == HeroType.smallMario){
            update(s_mario_jump_R);
        }else if(heroType == HeroType.bigMario){
            update(mario_R_jump);
        }else{
            update(hero_R_jump);
        }
    }

    public void updateJumpL(){
        if(heroType == HeroType.smallMario){
            update(s_mario_jump_L);
        }else if(heroType == HeroType.bigMario){
            update(mario_L_jump);
        }else{
            update(hero_L_jump);
        }
    }
    public void updateRunL(){
        if(heroType == HeroType.smallMario){
            update(s_mario_run_L);
        }else if(heroType == HeroType.bigMario){
            update(mario_L_run);
        }else{
            update(hero_L_run);
        }
    }

    public void updateRunR(){
        if(heroType == HeroType.smallMario){
            update(s_mario_run_R);
        }else if(heroType == HeroType.bigMario){
            update(mario_R_run);
        }else{
            update(hero_R_run);
        }
    }
    public void updateStandL(){
        if(heroType == HeroType.smallMario){
            update(s_mario_stand_L);
        }else if(heroType == HeroType.bigMario){
            update(mario_L_stand);
        }else{
            update(hero_L_stand);
        }
    }
    public void updateStandR(){
        if(heroType == HeroType.smallMario){
            update(s_mario_stand_R);
        }else if(heroType == HeroType.bigMario){
            update(mario_R_stand);
        }else{
            update(hero_R_stand);
        }
    }
    public void updateStopL(){
        if(heroType == HeroType.smallMario){
            update(s_mario_stop_L);
        }else if(heroType == HeroType.bigMario){
            update(mario_stop_L);
        }else{
            update(hero_stop_L);
        }
    }
    public void updateStopR(){
        if(heroType == HeroType.smallMario){
            update(s_mario_stop_R);
        }else if(heroType == HeroType.bigMario){
            update(mario_stop_R);
        }else{
            update(hero_stop_R);
        }
    }
    //开火
    public void fireRight(){
        //更改英雄状态
        this.fireInterval = Application.fireInterval;
        statusBeforeAttack = status;
        fireTimeCount = fireTime;
        update(hero_attack_R);
        //发射黑粒子
        shoot();
    }
    public void fireLeft(){
        //更改英雄状态
        this.fireInterval = Application.fireInterval;
        statusBeforeAttack = status;
        fireTimeCount = fireTime;
        update(hero_attack_L);
        //发射黑粒子
        shoot();
    }
    public boolean isFiring(){
        return status == hero_attack_L || status == hero_attack_R;
    }
    public void changeToInvincibleStatus(){
        isInvincible = true;
        invincibleTime = Application.invincibleTime;
    }
    public void changeToHarmEnemyStatus(){
        changeToInvincibleStatus();
        isHarmEnemyStatus = true;
    }

    @Override
    public void updateSize() {
        //大小超级玛丽跑的图片数量不同， 根据图片索引找图片可能找不到
        currentImgIndex = 0;
        super.updateSize();
    }

    /**
     * 调用该方法更新玛丽奥 坐标 和 图片
     */
    public void response(){
//        System.out.println(x + ", " + y);
//        if(speed > 5){
//            System.out.println(speed);
//        }
        //无敌状态
        if(isInvincible){
            invincibleTime--;
            if(invincibleTime <= 0){
                isInvincible = false;
                invincibleTime = Application.invincibleTime;
            }
        }
        if(isHarmEnemyStatus){
            durationOfStar--;
            if(durationOfStar <= 0){
                isHarmEnemyStatus = false;
                durationOfStar = Application.durationOfStar;
            }
        }
        //两次发射黑粒子要求时间间隔
        if(this.fireInterval >= 0)
            this.fireInterval--;

        //更新索引 图片
        super.update();
        //攻击状态
        if(heroType == HeroType.FireMario && (status == hero_attack_R || status == hero_attack_L)){
            if(fireTimeCount < 0){
                update(statusBeforeAttack);
            }else{
                fireTimeCount--;
            }
        }
        //从障碍物上下落
        if(!onGrass() && !onObstacle){
            if(isToRight()){
                updateJumpR();
            }else if(isToLeft()){
                updateJumpL();
            }else if(isStand()){
                if(heroType == HeroType.smallMario){
                    if(status == Status.s_mario_stand_R)
                        update(Status.s_mario_jump_R);
                    else if(status == Status.s_mario_stand_L)
                        update(Status.s_mario_jump_L);
                }else if(heroType == HeroType.bigMario){
                    if(status == mario_R_stand){
                        update(Status.mario_R_jump);
                    }else if(status == mario_L_stand){
                        update(Status.mario_L_jump);
                    }
                }else if(heroType == HeroType.FireMario){
                    if(status == hero_R_stand){
                        update(Status.hero_R_jump);
                    }else if(status == hero_L_stand){
                        update(Status.hero_L_jump);
                    }
                }
            }
        }

        //更新坐标 位置
        switch (status){
            case s_mario_run_R:
            case mario_R_run:
            case hero_R_run:
            case s_mario_stop_L:
            case mario_stop_L:
            case hero_stop_L:
                moveRight();
                break;

            case s_mario_run_L:
            case mario_L_run:
            case hero_L_run:
            case s_mario_stop_R:
            case mario_stop_R:
            case hero_stop_R:
                moveLeft();
                break;

            case s_mario_jump_R:
            case mario_R_jump:
            case hero_R_jump:
                if(upTime > 0){
                    if(canRight && isPressD)
//                        x += speed;
                        moveRight();
                    up();
                }else{
                    fall();
                }

                break;
            case s_mario_jump_L:
            case mario_L_jump:
            case hero_L_jump:
                if(upTime > 0){
                    if(canLeft && isPressA && x - speed > 0)
                        moveLeft();
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
                || status == Status.mario_L_run || status == Status.mario_stop_R || status == Status.mario_L_jump
                || status == Status.hero_L_run || status == Status.hero_stop_R || status == Status.hero_L_jump;
    }

    /**
     * 判断当前是否为站立
     * @return true if isStand
     */
    public boolean isStand(){
        return status == Status.s_mario_stand_R || status == Status.s_mario_stand_L
                || status == Status.mario_R_stand || status == Status.mario_L_stand
                || status == Status.hero_R_stand || status == Status.hero_L_stand;
    }

    public boolean isStandL(){
        return status == Status.s_mario_stand_L
                || status == Status.mario_L_stand
                || status == Status.hero_L_stand;
    }
    public boolean isStandR(){
        return status == Status.s_mario_stand_R
                || status == Status.mario_R_stand
                || status == Status.hero_R_stand;
    }

    /**
     * 判定是否是向右跑
     * @return
     */
    public boolean isRunR(){
        return status == s_mario_run_R || status == mario_R_run || status == hero_R_run;
    }
    /**
     * 判断当前是否向右
     * @return true if isToRight
     */
    public boolean isToRight(){
//        return (status == Status.s_mario_jump_R && isPressD) || status == Status.s_mario_run_R || status == Status.s_mario_stop_L;
        return status == s_mario_run_R || status == Status.s_mario_stop_L || (status == Status.s_mario_jump_R && isPressD)
                || status == Status.mario_R_run || status == Status.mario_stop_L || (status == Status.mario_R_jump && isPressD)
                || status == Status.hero_R_run || status == Status.hero_stop_L || (status == Status.hero_R_jump && isPressD);
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
                || status == Status.s_mario_jump_L || status == Status.s_mario_jump_R
                || status == hero_L_jump || status == hero_R_jump;
    }
    public boolean isStop(){
        return status == Status.s_mario_stop_L || status == Status.s_mario_stop_R
                ||status == Status.mario_stop_L || status == Status.mario_stop_R
                ||status == hero_stop_L || status == hero_stop_R;
    }
    public void fall(){
        if(onObstacle || onGrass()){  //落在障碍物上
            //修改状态
            if(status == Status.s_mario_jump_L || status == Status.mario_L_jump || status == hero_L_jump){
                if(isPressA)
                    updateRunL();
                else
                    updateStandL();
            }else if(status == Status.s_mario_jump_R || status == Status.mario_R_jump || status == hero_R_jump){
                if(isPressD)
                    updateRunR();
                else
                    updateStandR();
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
        if(status == Status.s_mario_jump_L || status == Status.mario_L_jump || status == hero_L_jump){
            if(isPressA && x - speed > 0 && canLeft)
//                x -= speed;
                moveLeft();
        }else if((status == Status.s_mario_jump_R && canRight) || (status == Status.mario_R_jump && canRight) || (status == hero_R_jump && canRight)){
            if(isPressD)
//                x += speed;
                moveRight();
        }
//        if(isPressA && x - speed > 0 && canLeft)
//                moveLeft();
//        if(isPressD)
//                moveRight();
    }

    /**
     * 从第二形态切换到第一形态
     */
    public void switchToSmallMario(){
        this.heroType = HeroType.smallMario;
        //变小
        y += 15;
        switch (status) {
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
        updateSize();
    }

    /**
     * 从第一或第三 形态切换到第二形态
     */
    public void switchToBigMario(){
        if(heroType == HeroType.smallMario){
            //变大
            y -= 15;
        }else if(heroType == HeroType.FireMario){
            //变小
            y += 30;
        }
        //切换英雄类型
        this.heroType = HeroType.bigMario;

        switch (status){
            case s_mario_run_L:
            case hero_L_run:
                update(Status.mario_L_run);
                break;
            case s_mario_run_R:
            case hero_R_run:
                update(Status.mario_R_run);
                break;
            case s_mario_stand_R:
            case hero_R_stand:
                update(Status.mario_R_stand);
                break;
            case s_mario_stand_L:
            case hero_L_stand:
                update(Status.mario_L_stand);
                break;
            case s_mario_stop_L:
            case hero_stop_L:
                update(Status.mario_stop_L);
                break;
            case s_mario_stop_R:
            case hero_stop_R:
                update(Status.mario_stop_R);
                break;
            case s_mario_jump_R:
            case hero_R_jump:
                update(Status.mario_R_jump);
                break;
            case s_mario_jump_L:
            case hero_L_jump:
                update(Status.mario_L_jump);
                break;
        }

        //释放黑粒子时碰到敌人会有bug, 状态为hero_attack_L， 而类型切换成了bigMario
        //补丁
        if(status == hero_attack_L){
            update(mario_L_stand);
        }else if(status == hero_attack_R){
            update(mario_R_stand);
        }
        //更改图片大小属性
        updateSize();
    }

    /**
     * 从第二形态切换到第三 形态
     */
    public void switchToHero(){
        this.heroType = HeroType.FireMario;
        //变大
        y -= 90;
        switch (status){
            case mario_L_run:
                update(hero_L_run);
                break;
            case mario_R_run:
                update(hero_R_run);
                break;
            case mario_R_stand:
                update(hero_R_stand);
                break;
            case mario_L_stand:
                update(hero_L_stand);
                break;
            case mario_stop_L:
                update(hero_stop_L);
                break;
            case mario_stop_R:
                update(hero_stop_R);
                break;
            case mario_R_jump:
                update(hero_R_jump);
                break;
            case mario_L_jump:
                update(hero_L_jump);
                break;
        }
        updateSize();
    }

    public void checkWin(){
        int ganX = gameWindow.getGan().getX();
        if(x + width >= ganX + 15){
            win();
            //音效
            MusicService.playGameSound("flagpole.wav");
        }

    }
    public void win(){
        gameWindow.winGame();
//        System.out.println("You Win");

    }
    public void lose(){
        gameWindow.loseGame();
    }
    public void regretfulLose(){
        lose();
        gameWindow.setRegretfulLose(true);
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
                //解决视觉上的透模问题
                if(!isJump())
                    x = obstacle.getX() + obstacle.width;

            }
            //判断是否可以向右
            if (obstacle.getX() <= x + width
                    && obstacle.getX() > x
                    && obstacle.getY() + offset < y + height
                    && obstacle.getY() + obstacle.getHeight() > y + offset) {
                canRight1 = false;
//                System.out.println("不能右移！");
                //解决视觉上的透模问题
                if(!isJump())
                    x = obstacle.getX() - width;
            }
//            if(x > obstacle.x && x < obstacle.width + oabstacle.x && y < obstacle.y){
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
                //解决视觉上凌空的问题
                y = obstacle.getY() - height;

                //是否踩到坑上
                if(obstacle.getType() == ObstacleType.pit){
                    if(x > obstacle.x - offset2 && x + width < obstacle.x + obstacle.width + offset2){
                        System.out.println("坠落");
                        regretfulLose();
                    }
                }
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
                    //播放音效
                    MusicService.playGameSound("brick_smash.wav");
                    //创建特效对象
                    List<SpecialProp> specialProps = gameWindow.getSpecialProps();
                    specialProps.addAll(SpecialProp.newBrickInstance(obstacle));
                    obstacles.remove(obstacle);
                }else if(obstacle.getType() == ObstacleType.brick0ContainsGold){ //包含金币的普通方块
                    if(obstacle.getGoldsCount() > 0){
                        //播放音效
                        MusicService.playGameSound("coin.wav");
                        obstacle.setGoldsCount(obstacle.getGoldsCount() - 1);
                        gameWindow.getSpecialProps().add(SpecialProp.newGoldInstance(obstacle.x, obstacle.y));
                        //得分
                        gameWindow.getScore(getGoldScore);
                        gameWindow.getGold();
                        gameWindow.addScoreProp(ScoreProp.newScorePropInstance(obstacle.getX(), obstacle.getY(), getGoldScore));
                    }else{
                        obstacle.setType(ObstacleType.brick0);
                    }
                }else if(obstacle.getType() == ObstacleType.brick0ContainsStar){
                    //更改状态
                    obstacle.update(1, "nothing", ObstacleType.noting);
                    //播放音效
                    Obstacle star = MobileGainProp.newStarInstance(obstacle.x, obstacle.y);
                    gameWindow.getGainProps().add(star);
                }else if(obstacle.getType() == ObstacleType.box){
                    //更改状态
                    obstacle.update(1, "nothing", ObstacleType.noting);
                    if(obstacle.getBoxType() == BoxType.mushroom){    //蘑菇
                        //播放音效
                        MusicService.playGameSound("power_up.wav");
                        if(heroType == HeroType.smallMario){
                            Obstacle mushroom = MobileGainProp.newMushroomInstance(obstacle.x, obstacle.y, 1);
                            mushroom.setX(obstacle.x + (obstacle.width - mushroom.width) / 2);
                            gameWindow.getGainProps().add(mushroom);
                        }else{  //如果当前为第二形态，则产出向日葵
                            Obstacle sunFlower = new Obstacle(0, 0, 4, 0, "sunFlower", 0, ObstacleType.flower);
                            sunFlower.setX(obstacle.x + (obstacle.width - sunFlower.width) / 2);
                            sunFlower.setY(obstacle.y - sunFlower.height);
                            gameWindow.getGainProps().add(sunFlower);
                        }
//                        obstacle.isMushroom = false;
                    }else if(obstacle.getBoxType() == BoxType.gold){      //金币
                        //播放音效
                        MusicService.playGameSound("coin.wav");
                        gameWindow.getSpecialProps().add(SpecialProp.newGoldInstance(obstacle.x, obstacle.y));
                        //得分
                        gameWindow.getScore(getGoldScore);
                        gameWindow.getGold();
                        gameWindow.addScoreProp(ScoreProp.newScorePropInstance(obstacle.getX(), obstacle.getY(), getGoldScore));
                    }
                }else if(obstacle.getType() == ObstacleType.hiddenBrick){
                    //更改状态
                    obstacle.update(1, "nothing", ObstacleType.noting);
                    //播放音效
                    MusicService.playGameSound("power_up.wav");
                    Obstacle mushroom = MobileGainProp.newMushroomInstance(obstacle.x, obstacle.y, 2);
                    mushroom.setX(obstacle.x + (obstacle.width - mushroom.width) / 2);
                    gameWindow.getGainProps().add(mushroom);
                }else{
                    //播放音效
                    MusicService.playGameSound("bump.wav");
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
            boolean intersect = MathUtil.checkTheCollision(obstacle, this);
            if(obstacle instanceof MobileGainProp){
                if(intersect){
                    if(obstacle.getType() == ObstacleType.mushroom){//蘑菇
                        //播放音效
                        MusicService.playGameSound("power_up_appears.wav");
                        //第一形态吃蘑菇到第二形态
                        if(heroType == HeroType.smallMario){
                            switchToBigMario();
                        }
                        ((MobileGainProp)obstacle).die();
//                        //移除
//                        gainProps.remove(obstacle);
                        //对话框
                        if(gameWindow.getDialogForMario() == null)
                            gameWindow.setDialogForMario(GameDialog.newBeBiggerDialogInstanceForMario());
                    }else if(obstacle.getType() == ObstacleType.mushroom1){
                        ((MobileGainProp)obstacle).die();
                        System.out.println("Life up...");
                    }else if(obstacle.getType() == ObstacleType.star){  //星星
                        //播放音效
                        MusicService.playGameSound("power_up_appears.wav");
                        //无敌状态
                        changeToHarmEnemyStatus();
                        ((MobileGainProp)obstacle).die();
//                        //移除
//                        gainProps.remove(obstacle);
                        //对话框
                        gameWindow.setDialogForMario(GameDialog.newInvincibleDialogInstanceForMario());
                    }
                    //得分
                    gameWindow.getScore(getGainScore);
                    gameWindow.addScoreProp(ScoreProp.newScorePropInstance(obstacle.getX(), obstacle.getY(), getGainScore));

                }

            }else{  //向日葵
                if(intersect){
                    //播放音效
                    MusicService.playGameSound("power_up_appears.wav");
                    //移除
                    gainProps.remove(obstacle);
                    //第一形态吃向日葵到第二形态
                    if(heroType == HeroType.smallMario){
                        switchToBigMario();
                        //对话框
                        if(gameWindow.getDialogForMario() == null)
                            gameWindow.setDialogForMario(GameDialog.newBeBiggerDialogInstanceForMario());
                    }else if(heroType == HeroType.bigMario){  //转换为第三 形态
                        switchToHero();
                        //对话框
                        if(gameWindow.getDialogForMario() == null)
                            gameWindow.setDialogForMario(GameDialog.newBeBiggestDialogInstanceForMario());
                    }
                    //得分
                    gameWindow.getScore(getGainScore);
                    gameWindow.addScoreProp(ScoreProp.newScorePropInstance(obstacle.getX(), obstacle.getY(), getGainScore));

                }
            }

        }
    }
    public void beAttacked(){
        if(heroType != HeroType.smallMario && gameWindow.getDialogForMario() == null){
            //对话框
            gameWindow.setDialogForMario(GameDialog.newBeAttackDialogInstanceForMario());
        }

        if(heroType == HeroType.bigMario){
//                        shapeShifting();
//                        isInvincible = true;
            if(!isInvincible){
                switchToSmallMario();
                changeToInvincibleStatus();
            }
        }else if(heroType == HeroType.smallMario){
            if(!isInvincible){
//                        JOptionPane.showMessageDialog(gameWindow, "死亡");
                System.out.println("死亡");
                lose();
            }
        }else if(heroType == HeroType.FireMario){
            if(!isInvincible){
                switchToBigMario();
                changeToInvincibleStatus();
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
                    //得分
                    if(enemy.getType() != EnemyType.flower){
                        gameWindow.getScore(treadScore);
                        gameWindow.addScoreProp(ScoreProp.newScorePropInstance(enemy.getX(), enemy.getY(), treadScore));
                    }
                    //播放音效
                    MusicService.playGameSound("stomp.wav");
                    setUpTime(3);
                    enemy.die();
                    //对话框
                    if(gameWindow.getDialogForMario() == null)
                        gameWindow.setDialogForMario(GameDialog.newTreadDialogInstanceForMario());
                }else if(enemy.getType() == EnemyType.tortoise ){
                    //播放音效
                    MusicService.playGameSound("stomp.wav");
                    if(!enemy.isShell){
                        setUpTime(3);
                        //变龟壳
                        enemy.update(1, "shell0", true);
                        //对话框
                        if(gameWindow.getDialogForMario() == null)
                            gameWindow.setDialogForMario(GameDialog.newTreadDialogInstanceForMario());
                    }else{
                        enemy.setSpeed(0);
                        enemy.setDuration(shellDuration);
                    }
                }else{  //食人花
                    if(isHarmEnemyStatus){
//                        System.out.println("无敌状态!");
                        enemy.die();
                    }else if(isInvincible){
                        System.out.println("不受伤害状态！");
                    }else{
                        beAttacked();
                    }
                }
            } else if (MathUtil.checkTheCollision(this, enemy)) {   //撞到敌人
                if(isHarmEnemyStatus){
//                    System.out.println("无敌状态!");
                    enemy.die();
                }else{
                    if(isInvincible){   //不受伤害的状态
                        if(enemy.getType() == EnemyType.tortoise){
                            if(enemy.isShell){
                                enemy.setSpeed(10);
                                //不再变乌龟
                                enemy.setDuration(Integer.MAX_VALUE);
                                enemy.setToRight(x <= enemy.getX());
                            }
                        }
                    }else{
                        if(enemy.getType() == EnemyType.tortoise){
                            if(enemy.isShell){
                                enemy.setSpeed(10);
                                //不再变乌龟
                                enemy.setDuration(Integer.MAX_VALUE);
                                enemy.setToRight(x <= enemy.getX());
                                //得分
                                if(!enemy.isCalculated){
                                    gameWindow.getScore(tortoise);
                                    gameWindow.addScoreProp(ScoreProp.newScorePropInstance(enemy.getX(), enemy.getY(), tortoise));
                                    enemy.isCalculated = true;
                                }
                                }else{
                                beAttacked();
                            }
                        }else{
                            beAttacked();
                        }
                    }
                }


            }
        }
    }

    /**
     * 发射黑粒子
     */
    public void shoot(){
        Bullet bullet = null;
        if (status == hero_attack_R) {
            bullet = new Bullet(x, y, 3, 10, "bullet", 0, true);
        }
        if (status == hero_attack_L) {
            bullet = new Bullet(x, y, 3, 10, "bullet", 0, false);
        }
        if(bullet != null)
            gameWindow.getHeroBullets().add(bullet);
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

    public boolean isInvincible() {
        return isInvincible;
    }

    public void setInvincible(boolean invincible) {
        isInvincible = invincible;
    }

    public int getDisplacement() {
        return displacement;
    }

    public void setDisplacement(int displacement) {
        this.displacement = displacement;
    }

    public boolean isBigMario() {
        return isBigMario;
    }

    public void setBigMario(boolean bigMario) {
        isBigMario = bigMario;
    }

    public HeroType getHeroType() {
        return heroType;
    }

    public void setHeroType(HeroType heroType) {
        this.heroType = heroType;
    }

    public Status getStatusBeforeAttack() {
        return statusBeforeAttack;
    }

    public void setStatusBeforeAttack(Status statusBeforeAttack) {
        this.statusBeforeAttack = statusBeforeAttack;
    }

    public int getFireTimeCount() {
        return fireTimeCount;
    }

    public void setFireTimeCount(int fireTimeCount) {
        this.fireTimeCount = fireTimeCount;
    }

    public int getFireInterval() {
        return fireInterval;
    }

    public void setFireInterval(int fireInterval) {
        this.fireInterval = fireInterval;
    }

    public boolean isHarmEnemyStatus() {
        return isHarmEnemyStatus;
    }

    public void setHarmEnemyStatus(boolean harmEnemyStatus) {
        isHarmEnemyStatus = harmEnemyStatus;
    }
}
