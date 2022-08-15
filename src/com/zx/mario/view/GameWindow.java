package com.zx.mario.view;


import com.zx.mario.domain.*;
import com.zx.mario.manager.Application;
import com.zx.mario.service.ImageFactory;
import com.zx.mario.service.MessageService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Vector;

import static com.zx.mario.manager.Application.*;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec com.zx.mario.view
 * @since 2022 - 07 - 18 - 22:26
 */
public class GameWindow extends JFrame implements Runnable {
    //用于缓存
    private Image bufferedImage;
    private BackGround cloud;
    private BackGround mountain;
    private Mario mario;
    //障碍物
    private List<Obstacle> obstacles;
    //敌人
    private List<Enemy> enemies;
    //特效道具
    private List<SpecialProp> specialProps;
    //增益
    private List<Obstacle> gainProps;
    public GameWindow() {
        //标题
        this.setTitle(MessageService.getTextByLanguage("menuTitle"));
        this.setIconImage(ImageFactory.getImg("menuIcon"));
        //窗口大小
        this.setSize(Application.WindowWidth, Application.WindowHeight);
//        this.setResizable(false);
        //默认关闭行为
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //布局
        this.setLayout(null);
        //窗口居中
        this.setLocationRelativeTo(null);
        init();
        setListener();
        //启动线程
        new Thread(this).start();
        this.requestFocus();
        //保存游戏窗体对象
        gameWindow = this;
    }


    private void setListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_D) {   //右
                    isPressD = true;
                    if (mario.isStand()) {
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_run_R);
                        else
                            mario.update(Status.mario_R_run);
                    } else if (mario.getStatus() == Status.s_mario_run_L || mario.getStatus() == Status.mario_L_run) {
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_stop_R);
                        else
                            mario.update(Status.mario_stop_R);
                    } else if (mario.isStop()) {
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_run_R);
                        else
                            mario.update(Status.mario_R_run);
                    }
                }
                if (keyCode == KeyEvent.VK_A) { //左
                    isPressA = true;
                    if (mario.isStand()) {
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_run_L);
                        else
                            mario.update(Status.mario_L_run);
                    } else if (mario.getStatus() == Status.s_mario_run_R || mario.getStatus() == Status.mario_R_run) {
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_stop_L);
                        else
                            mario.update(Status.mario_stop_L);
                    } else if (mario.isStop()) {
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_run_L);
                        else
                            mario.update(Status.mario_L_run);
                    }
                }
                if (keyCode == KeyEvent.VK_K) {   //跳跃
                    isPressK = true;
                    if (mario.canJump()) {
                        mario.setUpTime(uptime);
                        if (mario.getStatus() == Status.s_mario_run_R || mario.getStatus() == Status.s_mario_stand_R) {
                            mario.update(Status.s_mario_jump_R);
                        }
                        if (mario.getStatus() == Status.s_mario_run_L || mario.getStatus() == Status.s_mario_stand_L) {
                            mario.update(Status.s_mario_jump_L);
                        }
                        if (mario.getStatus() == Status.mario_R_run || mario.getStatus() == Status.mario_R_stand) {
                            mario.update(Status.mario_R_jump);
                        }
                        if (mario.getStatus() == Status.mario_L_run || mario.getStatus() == Status.mario_L_stand) {
                            mario.update(Status.mario_L_jump);
                        }
                    }
                }
                if(keyCode == KeyEvent.VK_B){
                    if(mario.getSpeed() != 0){
                        mario.setSpeed(moveSpeed * 2);
                    }
                    else{
                        mountain.setSpeed(moveSpeed * 2);
                    }
                    mario.setJumpSpeed(jumpSpeed * 2);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_D) {   //右
                    isPressD = false;
                    if (mario.getStatus() == Status.s_mario_run_R || mario.getStatus() == Status.mario_R_run)
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_stand_R);
                        else
                            mario.update(Status.mario_R_stand);
                }
                if (keyCode == KeyEvent.VK_A) { //左
                    isPressA = false;
                    if(mario.getStatus() == Status.s_mario_run_L || mario.getStatus() == Status.mario_L_run)
                        if(!mario.isBigMario)
                            mario.update(Status.s_mario_stand_L);
                        else
                            mario.update(Status.mario_L_stand);
//                    mario.update(Status.s_mario_stop_L);
                }
                if  (keyCode == KeyEvent.VK_K){
                    isPressK= false;
                }
                if(keyCode == KeyEvent.VK_B){
                    if(mario.getSpeed() != 0){
                        mario.setSpeed(moveSpeed);
                    }
                    else{
                        mountain.setSpeed(moveSpeed);
                    }

                    mario.setJumpSpeed(jumpSpeed);
                }
            }
        });
    }

    public void init() {
        obstacles = new Vector<>();
        enemies = new Vector<>();
        specialProps = new Vector<>();
        gainProps = new Vector<>();
        //云
        cloud = new BackGround(0, 26, 1, "gameCloud");
        //山
        mountain = new BackGround(0, 26, 2, "gameBg");
        //马里奥
        mario = new Mario(20, 100, 1, Application.moveSpeed, "s_mario_stand_R", 0);
//        mario.setY(Application.gameGrassY - mario.getHeight());
        //创建障碍物和敌人
        createEnemyAndObstacle();
    }
    private void createEnemyAndObstacle() {
        //不可摧毁的砖块
        for(int i = 0; i < 100; i++){
            Obstacle brick1 = new Obstacle((i + 1) * 50, 200, 1, 0, "brick1", 0, ObstacleType.brick1);
            obstacles.add(brick1);
        }
        //可摧毁的砖块
        for(int i = 50; i < 100; i++){
            Obstacle brick0 = new Obstacle((i + 1) * 50, 150, 1, 0, "brick0", 0, ObstacleType.brick0);
            obstacles.add(brick0);
        }
        //楼梯
        int x = 200, y = gameGrassY - 30;
        for(int i = 0; i < 4; i++){
            Obstacle obstacle = new Obstacle(x + i * 30, y - i * 30, 1, 0, "brick0", 0, ObstacleType.brick0);
            obstacles.add(obstacle);
        }
        Obstacle obstacle = new Obstacle(20 , y , 1, 0, "brick0", 0, ObstacleType.brick0);
        obstacles.add(obstacle);
        Obstacle boxM = new Obstacle(100, 350, 4, 0, "box", 0, ObstacleType.box);
        boxM.isMushroom = true;
        obstacles.add(boxM);
//        Obstacle obstacle = new Obstacle(200, 0, 1, 0, "brick0", 0, ObstacleType.brick0);
//        obstacle.setY(gameGrassY - obstacle.getHeight());
//        obstacles.add(obstacle);
        //金币方块
        for(int i = 0; i < 50; i++){
            Obstacle box = new Obstacle((i + 1) * 50, 100, 4, 0, "box", 0, ObstacleType.box);
            obstacles.add(box);
        }
        Obstacle brick0 = new Obstacle(30 * 70, gameGrassY - 30, 1, 0, "brick0", 0, ObstacleType.brick0);
        obstacles.add(brick0);
        brick0 = new Obstacle(33 * 70, gameGrassY - 30, 1, 0, "brick0", 0, ObstacleType.brick0);
        obstacles.add(brick0);
        //蘑菇
        for(int i = 30; i < 50; i++){
            Enemy enemy = new Enemy((i + 1) * 70, 400, 2, 3, "fungus", 0, EnemyType.fungus);
            enemy.setY(gameGrassY - enemy.getHeight() - 100);
            enemies.add(enemy);
        }

        //乌龟
        for(int i = 60; i < 100; i++){
            Enemy enemy = new Enemy((i + 1) * 70, 400, 2, 2, "tortoiseL", 0, EnemyType.tortoise);
            enemy.setY(gameGrassY - enemy.getHeight());
            enemies.add(enemy);
        }

        //食人花
        for(int i = 0; i < 50; i++){
            Enemy enemy = new Enemy((i + 1) * 50, 150, 6, 2, "flower", 0, EnemyType.flower);
//            enemy.setY(gameGrassY - enemy.getHeight());
            enemies.add(enemy);
        }

        Enemy enemy = new Enemy(600, 200, 6, 2, "flower", 0, EnemyType.flower);
        enemy.setY(gameGrassY - enemy.getHeight() - 20);
        enemies.add(enemy);

        Obstacle obstacle1 = new Obstacle(enemy.getX() - 10, enemy.getY() + 30, 1, 0, "pipe", 0, ObstacleType.pipe);
        obstacles.add(obstacle1);
    }
    private void drawEnemiesAndObstacles(Graphics graphics){

        //画敌人
        for (Enemy enemy:enemies
             ) {
            graphics.drawImage(enemy.getImg(), enemy.getX(), enemy.getY(), this);
        }
        //画障碍物
        for (Obstacle o:obstacles
        ) {
            graphics.drawImage(o.getImg(), o.getX(), o.getY(), this);
        }
        //画特效
        for (SpecialProp prop:specialProps
        ) {
            graphics.drawImage(prop.getImg(), prop.getX(), prop.getY(), this);
        }
        //画增益
        for (Obstacle gainProp:gainProps
             ) {
            graphics.drawImage(gainProp.getImg(), gainProp.getX(), gainProp.getY(), this);
        }
    }

    /**
     * 切换图片和移动
     */
    private void updateEnemiesAndObstacle(){
        for(int i = 0; i < enemies.size(); i++){
            Enemy enemy = enemies.get(i);
            if(enemy.isLive()){
                enemy.update();
            }else{
                enemies.remove(enemy);
            }
        }

        for(Obstacle obstacle:obstacles){
            obstacle.update();
        }
        //特效
        for (SpecialProp prop:specialProps
             ) {
            prop.update();
        }
        //失效移除
        for(int i = 0; i < specialProps.size(); i++){
            SpecialProp prop = specialProps.get(i);
            if(prop.isInvalid()){
                specialProps.remove(prop);
            }
        }
        //蘑菇移除
        for(int i = 0; i < gainProps.size(); i++){
            GameObject object = gainProps.get(i);
            if(object instanceof MushRoom){
                MushRoom mushRoom = (MushRoom) object;
                if(!mushRoom.isLive()){
                    System.out.println("移除失效蘑菇！");
                    gainProps.remove(mushRoom);
                }
            }
        }
    }
    public void sceneMove(){
        if(!mario.isCanRight()){
            return;
        }
        mountain.move();
        //障碍物
        for(int i = 0; i < obstacles.size(); i++){
            Obstacle o = obstacles.get(i);
            //移动（相对于山保持相对静止）
//            o.setSpeed(mountain.getSpeed());
            o.setX(o.getX() - mountain.getSpeed());
            //出窗口移除
            if(o.getX() < - o.getWidth() - 50){
                obstacles.remove(o);
            }
        }
        //敌人
        for(int i = 0; i < enemies.size(); i++){
            Enemy o = enemies.get(i);
            //移动（如果这个敌人没有移动则相对于山保持相对静止）
//            o.setSpeed(mountain.getSpeed());
            o.setX(o.getX() - mountain.getSpeed());
            //出窗口移除
            if(o.getX() < - o.getWidth() - 50){
                enemies.remove(o);
            }
        }
        //增益
        for (Obstacle gainProp:gainProps
             ) {
            if(gainProp instanceof MushRoom){
                MushRoom mushRoom = (MushRoom) gainProp;
                //相对于山体保持原来相对移动
                mushRoom.setX(mushRoom.getX() - mountain.getSpeed());
            }
        }
        //动画
        for (SpecialProp prop:specialProps
             ) {
            //相对于山体保持原来相对移动
            prop.setX(prop.getX() - mountain.getSpeed());
        }
    }
    @Override
    public void run() {
        while (true) {
            try {
                repaint();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            cloud.move();

            //走到这个位置，地图开始动，马里奥相对于窗口不再移动
            if (mario.getX() >= Application.marioMaxX) {
                int speed = mario.getSpeed();
                if (speed != 0) {
                    mountain.setSpeed(speed);
                    Application.marioSpeed = speed;
                }
                //此时马里奥向右相对于窗口不动，山体动
                if (mario.isToRight()) {
                    mario.setSpeed(0);
                    //背景移动
                    sceneMove();
                } else {
                    mario.setSpeed(moveSpeed);
                }
            }
            //更新敌人障碍物等
            updateEnemiesAndObstacle();
            //碰撞检测
            mario.checkMove(obstacles);
            mario.checkMove2(gainProps);
            mario.checkMove3(enemies);
            //根据当前状态做出响应
            mario.response();
        }
    }

    @Override
    public void paint(Graphics g) {
        if (bufferedImage == null) {
            bufferedImage = createImage(this.getWidth(), this.getHeight());
        }
        //缓冲
        Graphics graphics = bufferedImage.getGraphics();
        //云
        graphics.drawImage(cloud.getImg(), cloud.getX(), cloud.getY(), this);
        //山
        graphics.drawImage(mountain.getImg(), mountain.getX(), mountain.getY(), this);
        int l = 435 + 26;
        //花
        graphics.drawImage(ImageFactory.getImg("flower0.png"), mountain.getX() + 300, l - 63, this);
        //障碍物和敌人
        drawEnemiesAndObstacles(graphics);

        //马里奥
        graphics.drawImage(mario.getImg(), mario.getX(), mario.getY(), this);

        //一次展现在窗口
        g.drawImage(bufferedImage, 0, 0, this);

    }

    public List<SpecialProp> getSpecialProps() {
        return specialProps;
    }

    public void setSpecialProps(List<SpecialProp> specialProps) {
        this.specialProps = specialProps;
    }

    public List<Obstacle> getObstacles() {
        return obstacles;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Obstacle> getGainProps() {
        return gainProps;
    }

    public void setGainProps(List<Obstacle> gainProps) {
        this.gainProps = gainProps;
    }
}
