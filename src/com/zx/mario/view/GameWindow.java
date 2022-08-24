package com.zx.mario.view;


import com.zx.mario.domain.*;
import com.zx.mario.manager.Application;
import com.zx.mario.service.ImageFactory;
import com.zx.mario.service.MessageService;
import com.zx.mario.service.MusicService;
import com.zx.mario.utils.MathUtil;

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
    //我方子弹
    private List<Bullet> heroBullets;
    //终点
    private Obstacle gan;
    private Obstacle flag;
    //游戏结束
    private boolean isGameEnd =false;
    private boolean isWinner = false;
    private boolean isEndAnimationFinished = false;
    //计时器
    private final GameClock gameClock;
    //对话框
    private GameDialog dialogForMario = null;
    //分数
    private int score = 0;
    //标头金币
    private GameObject gold = SpecialProp.newStaticGoldInstance(250, 65);
    private int goldCount = 0;
    //判断是否是因为踩空死亡
    private boolean isRegretfulLose = false;
    //用于显示得分动画
    private List<ScoreProp> scoreProps;
    public GameWindow() {
        //标题
        this.setTitle(MessageService.getTextByLanguage("menuTitle"));
        this.setIconImage(ImageFactory.getImg("menuIcon.png"));
        //窗口大小
        this.setSize(Application.WindowWidth, Application.WindowHeight);
        this.setResizable(false);
        //默认关闭行为
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //布局
        this.setLayout(null);
        //窗口居中
        this.setLocationRelativeTo(null);
        init();
        setListener();

        this.requestFocus();
        //保存游戏窗体对象
        gameWindow = this;

        //播放背景音乐
        if(isSelectPlayBackgroundMusic) {
            MusicService.stopBackGroundMusic();
            MusicService.playBackGroundMusic("main_theme.wav");
        }

        //计时
        gameClock = new GameClock();
        startTheGameClock();

        //启动线程
        new Thread(this).start();
    }
    public void startTheGameClock(){
        new Thread(gameClock).start();
    }
    class GameClock implements Runnable {
        private int time = 0;

        @Override
        public void run() {
            while(startGame){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time ++;
//                System.out.println("游戏时间： " + getGameTime() + " s");
                if(mario.getHeroType() == HeroType.FireMario && isSelectPlayGameSoundEffect){
                    if(time % 30 == 0){
                        MusicService.playSound("voice.wav");
                    }
                }
            }
        }

        public int getTime() {
            return time;
        }

        public void setTime(int time) {
            this.time = time;
        }
    }

    private void setListener() {
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(isGameEnd)   //游戏结束不响应键盘事件
                    return;
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_D) {   //右
                    isPressD = true;
                    if (mario.isStand()) {
                        mario.updateRunR();
                    } else if (mario.getStatus() == Status.s_mario_run_L || mario.getStatus() == Status.mario_L_run || mario.getStatus() == Status.hero_L_run) {
                        mario.updateStopR();
                    } else if (mario.isStop()) {
                        mario.updateRunR();
                    }
                }
                if (keyCode == KeyEvent.VK_A) { //左
                    isPressA = true;
                    if (mario.isStand()) {
                        mario.updateRunL();
                    } else if (mario.getStatus() == Status.s_mario_run_R || mario.getStatus() == Status.mario_R_run || mario.getStatus() == Status.hero_R_run) {
                        mario.updateStopL();
                    } else if (mario.isStop()) {
                        mario.updateRunL();
                    }
                }
                if (keyCode == KeyEvent.VK_K) {   //跳跃
                    isPressK = true;
                    if (mario.canJump()) {
                        //播放音效
                        MusicService.playGameSound("big_jump.wav");
                        mario.setUpTime(uptime);
                        if (mario.getStatus() == Status.s_mario_run_R || mario.getStatus() == Status.s_mario_stand_R || mario.getStatus() == Status.s_mario_stop_R) {
                            mario.update(Status.s_mario_jump_R);
                        }
                        if (mario.getStatus() == Status.s_mario_run_L || mario.getStatus() == Status.s_mario_stand_L || mario.getStatus() == Status.s_mario_stop_L) {
                            mario.update(Status.s_mario_jump_L);
                        }
                        if (mario.getStatus() == Status.mario_R_run || mario.getStatus() == Status.mario_R_stand || mario.getStatus() == Status.mario_stop_R) {
                            mario.update(Status.mario_R_jump);
                        }
                        if (mario.getStatus() == Status.mario_L_run || mario.getStatus() == Status.mario_L_stand || mario.getStatus() == Status.mario_stop_L) {
                            mario.update(Status.mario_L_jump);
                        }
                        if (mario.getStatus() == Status.hero_R_run || mario.getStatus() == Status.hero_R_stand || mario.getStatus() == Status.hero_stop_R) {
                            mario.update(Status.hero_R_jump);
                        }
                        if (mario.getStatus() == Status.hero_L_run || mario.getStatus() == Status.hero_L_stand || mario.getStatus() == Status.hero_stop_L) {
                            mario.update(Status.hero_L_jump);
                        }
                    }
                }
                if(keyCode == KeyEvent.VK_S){   //下水管
                    if(mario.isOnObstacle()){
                        if(Math.abs(mario.getDisplacement() - 1840) <= 10){
                            //播放音效
                            MusicService.playGameSound("pipe.wav");
                            mario.setY(-mario.getHeight());
                        }
//                        System.out.println("位移：" + mario.getDisplacement());
                        //1810-1840
                    }
                }
                //加速
                if(keyCode == KeyEvent.VK_B){
                    if(mario.getHeroType() == HeroType.FireMario){
                        if(mario.getSpeed() != 0){
                            mario.setSpeed(moveSpeed * 2);
                        }
                        else{
                            mountain.setSpeed(moveSpeed * 2);
                        }
                    }
//                    mario.setJumpSpeed(jumpSpeed * 2);
                }
                //攻击
                if(keyCode == KeyEvent.VK_J){
                    if(mario.getHeroType() == HeroType.FireMario && mario.getFireInterval() < 0){
                        //播放音效
                        MusicService.playGameSound("shootVoice.wav");
                        if (mario.getStatus() == Status.hero_R_run || mario.getStatus() == Status.hero_R_stand || mario.getStatus() == Status.hero_R_jump) {
                            mario.fireRight();
                        }
                        if (mario.getStatus() == Status.hero_L_run || mario.getStatus() == Status.hero_L_stand || mario.getStatus() == Status.hero_L_jump) {
                            mario.fireLeft();
                        }
                        //对话框
                        //对话框
                        gameWindow.setDialogForMario(GameDialog.newAttackDialogInstanceForMario());
                    }
                }
                if(keyCode == KeyEvent.VK_T){
//                    System.out.println(mario.getStatusBeforeAttack());
                    System.out.println(mario.getStatus());
//                    System.out.println(mario.getHeroType());
                    System.out.println("mario x:" + mario.getX());
                    System.out.println("mario y:" + mario.getY());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if(isGameEnd)   //游戏结束不响应键盘事件
                    return;
                int keyCode = e.getKeyCode();
                if (keyCode == KeyEvent.VK_D) {   //右
                    isPressD = false;
                    if (mario.getStatus() == Status.s_mario_run_R || mario.getStatus() == Status.mario_R_run || mario.getStatus() == Status.hero_R_run)
                        mario.updateStandR();

                    //添加攻击后的补丁
                    if(mario.isFiring()){
                        if(mario.getStatusBeforeAttack() == Status.hero_R_run){
                            mario.setStatusBeforeAttack(Status.hero_R_stand);
                        }
                    }
                }
                if (keyCode == KeyEvent.VK_A) { //左
                    isPressA = false;
                    if(mario.getStatus() == Status.s_mario_run_L || mario.getStatus() == Status.mario_L_run || mario.getStatus() == Status.hero_L_run)
                        mario.updateStandL();
//                    mario.update(Status.s_mario_stop_L);

                    //添加攻击后的补丁
                    if(mario.isFiring()){
                        if(mario.getStatusBeforeAttack() == Status.hero_L_run){
                            mario.setStatusBeforeAttack(Status.hero_L_stand);
                        }
                    }
                }
                if  (keyCode == KeyEvent.VK_K){
                    isPressK= false;
                    if(mario.getUpTime() > 6){
                        mario.setUpTime(6);
                    }
                }
                if(keyCode == KeyEvent.VK_B){
                    if(mario.getSpeed() != 0){
                        mario.setSpeed(moveSpeed);
                    }
                    else{
                        mountain.setSpeed(moveSpeed);
                    }

//                    mario.setJumpSpeed(jumpSpeed);
                }
            }
        });
    }

    public void init() {
        obstacles = new Vector<>();
        enemies = new Vector<>();
        specialProps = new Vector<>();
        gainProps = new Vector<>();
        heroBullets = new Vector<>();
        scoreProps = new Vector<>();
        //云
        cloud = new BackGround(0, 26, 1, "gameCloud");
        //山
        mountain = new BackGround(0, 26, 2, "gameBg");
        //马里奥
        mario = new Mario(200, 300, 1, Application.moveSpeed, "s_mario_stand_R", 0);
//        mario.switchToBigMario();
//        mario.switchToHero();

//        mario.setY(Application.gameGrassY - mario.getHeight());
        //创建障碍物和敌人
        createEnemyAndObstacle();
    }

    /**
     * 生成敌人和障碍物
     */
    private void createEnemyAndObstacle() {
//        //不可摧毁的砖块
//        for(int i = 0; i < 100; i++){
//            Obstacle brick1 = new Obstacle((i + 1) * 50, 200, 1, 0, "brick1", 0, ObstacleType.brick1);
//            obstacles.add(brick1);
//        }
//        //可摧毁的砖块
//        for(int i = 50; i < 100; i++){
//            Obstacle brick0 = new Obstacle((i + 1) * 50, 150, 1, 0, "brick0", 0, ObstacleType.brick0);
//            obstacles.add(brick0);
//        }
//        //楼梯
//        int x = 200, y = gameGrassY - 30;
//        for(int i = 0; i < 4; i++){
//            Obstacle obstacle = new Obstacle(x + i * 30, y - i * 30, 1, 0, "brick0", 0, ObstacleType.brick0);
//            obstacles.add(obstacle);
//        }
//        Obstacle obstacle = new Obstacle(20 , y , 1, 0, "brick0", 0, ObstacleType.brick0);
//        obstacles.add(obstacle);
//        Obstacle boxM = new Obstacle(100, 350, 4, 0, "box", 0, ObstacleType.box);
//
//        obstacles.add(boxM);
////        Obstacle obstacle = new Obstacle(200, 0, 1, 0, "brick0", 0, ObstacleType.brick0);
////        obstacle.setY(gameGrassY - obstacle.getHeight());
////        obstacles.add(obstacle);
//        //金币方块
//        for(int i = 0; i < 50; i++){
//            Obstacle box = new Obstacle((i + 1) * 50, 100, 4, 0, "box", 0, ObstacleType.box);
//            obstacles.add(box);
//        }
//        Obstacle brick0 = new Obstacle(30 * 70, gameGrassY - 30, 1, 0, "brick0", 0, ObstacleType.brick0);
//        obstacles.add(brick0);
//        brick0 = new Obstacle(33 * 70, gameGrassY - 30, 1, 0, "brick0", 0, ObstacleType.brick0);
//        obstacles.add(brick0);
//        //蘑菇
//        for(int i = 30; i < 50; i++){
//            Enemy enemy = new Enemy((i + 1) * 70, 400, 2, 3, "fungus", 0, EnemyType.fungus);
//            enemy.setY(gameGrassY - enemy.getHeight() - 100);
//            enemies.add(enemy);
//        }
//
//        //乌龟
//        for(int i = 60; i < 100; i++){
//            Enemy enemy = new Enemy((i + 1) * 70, 400, 2, 2, "tortoiseL", 0, EnemyType.tortoise);
//            enemy.setY(gameGrassY - enemy.getHeight());
//            enemies.add(enemy);
//        }
//
//        //食人花
//        for(int i = 0; i < 50; i++){
//            Enemy enemy = new Enemy((i + 1) * 50, 150, 6, 2, "flower", 0, EnemyType.flower);
////            enemy.setY(gameGrassY - enemy.getHeight());
//            enemies.add(enemy);
//        }
//
//        Enemy enemy = new Enemy(600, 200, 6, 2, "flower", 0, EnemyType.flower);
//        enemy.setY(gameGrassY - enemy.getHeight() - 20);
//        enemies.add(enemy);
//
//        Obstacle obstacle1 = new Obstacle(enemy.getX() - 10, enemy.getY() + 30, 1, 0, "pipe", 0, ObstacleType.pipe);
//        obstacles.add(obstacle1);
        int x = WindowWidth, y = 350, y0 = y - 110;
        Obstacle obstacle;
        Enemy enemy;
        obstacle = Obstacle.newBoxInstance(x, y, BoxType.gold);
        obstacles.add(obstacle);
        x += 100;

        enemy = Enemy.newFungusInstance(x + 70, 426);
        enemies.add(enemy);
        for(int i = 0; i < 5; i++){
            x += 30;
            if(i % 2 == 0){
                obstacle = Obstacle.newBrick0Instance(x, y);
            }else{
                if(i == 3){
                    obstacle = Obstacle.newBoxInstance(x, y, BoxType.mushroom);
                }else{
                    obstacle = Obstacle.newBoxInstance(x, y, BoxType.gold);
                }
            }
            obstacles.add(obstacle);
            if(i == 2){
                obstacle = Obstacle.newBoxInstance(x, y0, BoxType.gold);
                obstacles.add(obstacle);
            }
        }
        x += 80;
        //食人花
        enemy = Enemy.newFlowerInstance(x, 428, obstacles);

        enemies.add(enemy);
        x += 200;
        //水管和蘑菇
        obstacle = Obstacle.newPieInstance(x, 410);
        obstacles.add(obstacle);

        x += 100;
        enemy = Enemy.newFungusInstance(x, 426);
        enemies.add(enemy);

        x += 100;
        obstacle = Obstacle.newPieInstance(x, 370);
        obstacles.add(obstacle);

        x += 100;
        for(int i = 0; i < 3; i++){
            x += i * 50;
            enemy = Enemy.newTortoiseInstance(x, 426);
            enemies.add(enemy);
        }

        x += 50;
        obstacle = Obstacle.newPieInstance(x, 350);
        obstacles.add(obstacle);
        //坑
        x += 300;
        obstacle = Obstacle.newPitInstance(x, gameGrassY);
        obstacles.add(obstacle);

        //隐形方块（加命）
        obstacles.add(Obstacle.newHiddenBrickInstance(x - 100, y));
        //三个砖块，中间是问号方块
        x += 200;
        for(int i = 0; i < 3; i++){
            x += 30;
            if(i == 1){
                obstacle = Obstacle.newBoxInstance(x, y, BoxType.mushroom);
            }else{
                obstacle = Obstacle.newBrick0Instance(x, y);
            }
            obstacles.add(obstacle);
        }

        //横桥
        for(int i = 0; i < 10; i++){
            x += 30;
            obstacle = Obstacle.newBrick0Instance(x, y0);
            obstacles.add(obstacle);
        }
        //两个在横桥上的蘑菇
        enemies.add(Enemy.newFungusInstance(x - 200, y0 - 40));
        enemies.add(Enemy.newFungusInstance(x - 250, y0 - 40));

        //坑
        obstacles.add(Obstacle.newPitInstance(x - 150, gameGrassY));

        x += 30 * 3;
        //两个蘑菇
        enemies.add(Enemy.newFungusInstance(x + 100, 426));
        enemies.add(Enemy.newFungusInstance(x + 150, 426));

        //横桥
        for(int i = 0; i < 4; i++){
            x += 30;
            if(i == 3){
                obstacle = Obstacle.newBoxInstance(x, y0, BoxType.gold);
            }else{
                obstacle = Obstacle.newBrick0Instance(x, y0);
            }
            obstacles.add(obstacle);
        }


        //多个金币的普通砖块
        obstacles.add(Obstacle.newGoldBrick0Instance(x, y));

        //普通砖块
        x += 30 * 5;
        obstacles.add(Obstacle.newBrick0Instance(x, y));
        //产出无敌星星的普通方块
        x += 30;
        obstacles.add(Obstacle.newStarBrick0Instance(x, y));
        //乌龟
        enemies.add(Enemy.newTortoiseInstance(x + 100, gameGrassY - 40));
        //三角形布局问号方块
        for(int i = 0; i < 3; i++){
            x += 30 * 3;
            obstacles.add(Obstacle.newBoxInstance(x, y, BoxType.gold));
        }
        obstacles.add(Obstacle.newBoxInstance(x - 90, y - 110, BoxType.mushroom));

        //两个蘑菇
        enemies.add(Enemy.newFungusInstance(x + 100, 426));
        enemies.add(Enemy.newFungusInstance(x + 120, 426));

        x += 100;
        //普通方块
        obstacles.add(Obstacle.newBrick0Instance(x, y));
        x += 60;
        //三个普通方块
        for(int i = 0; i < 3; i++){
            x += 30;
            obstacles.add(Obstacle.newBrick0Instance(x, y0));
        }

        x += 30 * 4;
        //四个方块，中间两个问号
        for(int i = 0; i < 4; i++){
            x += 30;
            if(i == 0 || i == 3){
                obstacles.add(Obstacle.newBrick0Instance(x, y0));
            }else{
                obstacles.add(Obstacle.newBoxInstance(x, y0, BoxType.gold));
            }
        }
        //两个普通方块
        obstacles.add(Obstacle.newBrick0Instance(x - 30 * 2, y));
        obstacles.add(Obstacle.newBrick0Instance(x - 30, y));

        //两个蘑菇
        enemies.add(Enemy.newFungusInstance(x, 426));
        enemies.add(Enemy.newFungusInstance(x + 30, 426));

        //对称楼梯
        x += 30 * 3;
        int k = gameGrassY - 30;
        for(int i = 0; i < 4; i++){
            x += 30;
            for(int j = 0; j < 4 - i; j++){
               obstacles.add(Obstacle.newBrick1Instance(x + j * 30, k - i * 30));
            }
        }

        x += 30 * 3;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4 - i; j++){
                obstacles.add(Obstacle.newBrick1Instance(x + j * 30, k - i * 30));
            }
        }

        x += 30 * 8;
        //两面楼梯中间是坑
        for(int i = 0; i < 4; i++){
            x += 30;
            for(int j = 0; j < 5 - i; j++){
                obstacles.add(Obstacle.newBrick1Instance(x + j * 30, k - i * 30));
            }
        }
        x += 30 * 2;
        obstacles.add(Obstacle.newPitInstance(x, gameGrassY));
        x += 30 * 2;
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 4 - i; j++){
                obstacles.add(Obstacle.newBrick1Instance(x + j * 30, k - i * 30));
            }
        }
        x += 30 * 6;
        //水管
        obstacles.add(Obstacle.newPieInstance(x, 370));

        x += 30 * 4;
        //四个方块第三个是问号方块
        for(int i = 0; i < 4; i++){
            x += 30;
            if(i != 2){
                obstacles.add(Obstacle.newBrick0Instance(x, y));
            }else{
                obstacles.add(Obstacle.newBoxInstance(x, y, BoxType.gold));
            }
        }

        //两个蘑菇
        enemies.add(Enemy.newFungusInstance(x, 426));
        enemies.add(Enemy.newFungusInstance(x + 30, 426));

        x += 30 * 5;
        //水管
        obstacles.add(Obstacle.newPieInstance(x, 370));
        x += 30;
        for(int i = 0; i < 8; i++){
            x += 30;
            for(int j = 0; j < 9 - i; j++){
                obstacles.add(Obstacle.newBrick1Instance(x + j * 30, k - i * 30));
            }
        }

        //旗杆和旗帜
        x += 30 * 10;
        gan = Obstacle.newGanInstance(x, gameGrassY - 200);
//        obstacles.add(gan);
        flag = Obstacle.newFlagInstance(x + 15, gameGrassY - 200);
//        obstacles.add();

        //城堡
        x += 30 * 6;
        obstacles.add(Obstacle.newTowerInstance(x, gameGrassY - 150));
    }
    private void drawEnemiesAndObstacles(Graphics graphics){
        //画旗杆 和 旗帜
        if(gan.inWindow()){
            graphics.drawImage(gan.getImg(), gan.getX(), gan.getY(), this);
        }
        if(flag.inWindow()){
            graphics.drawImage(flag.getImg(), flag.getX(), flag.getY(), this);
        }
        //画敌人
        for (Enemy enemy:enemies
             ) {
            if(enemy.inWindow())
                graphics.drawImage(enemy.getImg(), enemy.getX(), enemy.getY(), this);
        }
        //画障碍物
        for (Obstacle o:obstacles
        ) {
            if(o.inWindow()){
//                if(o.getType() == ObstacleType.pipe){
//                    continue;
//                }
                //坑是覆盖在地图上的，向上一些视觉上不会显得突兀
                if(o.getType() == ObstacleType.pit){
                    graphics.drawImage(o.getImg(), o.getX(), o.getY() - 2, this);
                }else{
                    graphics.drawImage(o.getImg(), o.getX(), o.getY(), this);
                }
            }
        }
        //画特效
        for (SpecialProp prop:specialProps
        ) {
            graphics.drawImage(prop.getImg(), prop.getX(), prop.getY(), this);
        }
        //画增益
        for (Obstacle gainProp:gainProps
             ) {
            if(gainProp.inWindow())
                graphics.drawImage(gainProp.getImg(), gainProp.getX(), gainProp.getY(), this);
        }
        //画黑粒子
        for(Bullet bullet : heroBullets){
            if(bullet.inWindow())
                graphics.drawImage(bullet.getImg() ,bullet.getX(), bullet.getY(), this);
        }
        graphics.setFont(new Font("楷书", Font.BOLD, 20));
        graphics.setColor(Color.white);
        //得分动画
        for(ScoreProp scoreProp : scoreProps){
            if(!scoreProp.isInvalid()){
                graphics.drawString(scoreProp.getScore() + "", scoreProp.getX(), scoreProp.getY());
            }
        }
    }

    /**
     * 更新游戏窗体中物体的位置和外观
     * 切换图片和移动
     */
    private void updateGameObject(){
        /*敌人*/
        for(int i = 0; i < enemies.size(); i++){
            Enemy enemy = enemies.get(i);
            if(enemy.inWindow() && enemy.isLive()){
                //当马里奥靠近食人花，食人花不再探头
//                if(enemy.getType() == EnemyType.flower){
//                    if(enemy.getX() > mario.getX() + mario.getWidth()){
//                        if(enemy.getX() - mario.getX() - mario.getWidth() > 10){
//                            enemy.update();
//                        }else{
//                            if(enemy.getCurrentImgIndex() != 5){
//                                enemy.update();
//                            }
//                        }
//                    }else if(enemy.getX() + enemy.getWidth() < mario.getX()){
//                        if(mario.getX() - enemy.getX() - enemy.getWidth() > 10){
//                            enemy.update();
//                        }else{
//                            if(enemy.getCurrentImgIndex() != 5){
//                                enemy.update();
//                            }
//                        }
//                    }
//                }else{
//                    enemy.update();
//                }
                if (enemy.getType() == EnemyType.flower) {
                    if (enemy.getX() > mario.getX() + mario.getWidth()) {
                        if (enemy.getX() - mario.getX() - mario.getWidth() > 10) {
                            enemy.update();
                        } else {
                            if (enemy.displacement > 0) {
                                enemy.update();
                            }
                        }
                    } else if (enemy.getX() + enemy.getWidth() < mario.getX()) {
                        if (mario.getX() - enemy.getX() - enemy.getWidth() > 10) {
                            enemy.update();
                        } else {
                            if (enemy.displacement > 0) {
                                enemy.update();
                            }
                        }
                    }else{  //像素差的问题， 两图片重叠
//                        System.out.println(enemy.getX() + " en");
//                        System.out.println(mario.getX() + mario.getWidth());
                        if (enemy.displacement > 0) {
                            enemy.update();
                        }
                    }
                } else {
                    enemy.update();
                }
            }
            if(!enemy.isLive() || enemy.getX() < - enemy.getWidth() - 50){
                enemies.remove(enemy);
            }
        }
        /*障碍物*/
        for(int i = 0; i < obstacles.size(); i++){
            Obstacle obstacle = obstacles.get(i);
            obstacle.update();
            //出窗口移除
            if(obstacle.getX() < - obstacle.getWidth() - 5){
                obstacles.remove(obstacle);
            }
        }

        /*特效*/

        for(int i = 0; i < specialProps.size(); i++){
            SpecialProp prop = specialProps.get(i);
            if(prop.isInvalid()){   //失效移除
                specialProps.remove(prop);
            }else{
                prop.update();
            }
        }
        /*增益*/
        //更新和移除
        for(int i = 0; i < gainProps.size(); i++){
            GameObject object = gainProps.get(i);
            if(object instanceof MobileGainProp){ //蘑菇 星星
                MobileGainProp mobileGainProp = (MobileGainProp) object;
                if(!mobileGainProp.isLive()){ //蘑菇做成了线程，出窗口isLive返回false
                    gainProps.remove(object);
                }
            }else{  //向日葵
                object.update();
                if(object.getX() < -object.getWidth()){
                    gainProps.remove(object);
                }
            }
        }
        /*黑粒子*/
        for(int i = 0; i < heroBullets.size(); i++){
            Bullet bullet = heroBullets.get(i);
            bullet.update();
            if(bullet.isInvalid()){
                heroBullets.remove(bullet);
//                System.out.println("移除失效黑粒子...");
            }
        }
        /*得分动画*/
        for(int i = 0; i < scoreProps.size(); i++){
            ScoreProp scoreProp = scoreProps.get(i);
            scoreProp.update();
            if(scoreProp.isInvalid()){
                scoreProps.remove(scoreProp);
            }
        }
    }
    public void sceneMove(){
        //马里奥不能向右，则地图不能移动
        if(!mario.isCanRight() || isGameEnd){
            return;
        }
        //旗杆 和 旗帜
        gan.setX(gan.getX() - mountain.getSpeed());
        flag.setX(flag.getX() - mountain.getSpeed());

        //记录玛丽奥的位移
        mario.setDisplacement(mario.getDisplacement() + mountain.getSpeed());
        mountain.move();
        //障碍物
        for (Obstacle o : obstacles) {
            //移动（相对于山保持相对静止）
            o.setX(o.getX() - mountain.getSpeed());
        }
        //敌人
        for (Enemy o : enemies) {
            //移动（如果这个敌人没有移动则相对于山保持相对静止）
            o.setX(o.getX() - mountain.getSpeed());
        }
        //增益
        for (Obstacle gainProp:gainProps
             ) {
            //相对于山体保持原来相对移动
            gainProp.setX(gainProp.getX() - mountain.getSpeed());
        }
        //动画
        for (SpecialProp prop:specialProps
             ) {
            //相对于山体保持原来相对移动
            prop.setX(prop.getX() - mountain.getSpeed());
        }
        //黑粒子
        for (Bullet bullet : heroBullets) {
            bullet.setX(bullet.getX() - mountain.getSpeed());
        }
    }
    private void checkBulletHitObject() {
        for (Bullet bullet:heroBullets
             ) {
            for (Enemy enemy:enemies
                 ) {
                if(MathUtil.checkTheCollision(bullet, enemy)){
                    MusicService.playGameSound("bulletHit.wav");
                    enemy.die();
                    bullet.isInvalid = true;
                    //分数
                    if(enemy.getType() == EnemyType.tortoise){
                        score += beatEnemy * 2;
                        scoreProps.add(ScoreProp.newScorePropInstance(enemy.getX(), enemy.getY(), beatEnemy * 2));
                    }else{
                        score += beatEnemy;
                        scoreProps.add(ScoreProp.newScorePropInstance(enemy.getX(), enemy.getY(), beatEnemy));
                    }
                }
            }
        }
    }
    private void checkShellHitObject(){
        for(Enemy enemy : enemies){
            if(enemy.getType() == EnemyType.tortoise && enemy.isShell){

                //玛丽奥和龟壳
                if(enemy.getSpeed() != 0 && !mario.isInvincible() && !mario.isHarmEnemyStatus() && MathUtil.checkTheCollision(mario, enemy)){
                    if(!mario.isJump()){        //解决踩龟壳受伤的问题
                        mario.beAttacked();
                        //被乌龟撞起的效果
                        mario.setUpTime(10);
                        if(mario.isStandL()){
                            mario.updateJumpL();
                        }else if(mario.isStandR()){
                            mario.updateJumpR();
                        }
                    }
                }else if(mario.isHarmEnemyStatus()){
                    enemy.die();
                }
                //龟壳和其他生物
                for(Enemy enemy1: enemies){
                    //去除自身
                    if(enemy1 != enemy){
                        if(enemy.getSpeed() != 0 && MathUtil.checkTheCollision(enemy1, enemy)){
                            enemy1.die();
                            if(enemy1.getType() == EnemyType.tortoise){
                                score += beatEnemy * 2;
                                addScoreProp(ScoreProp.newScorePropInstance(enemy1.getX(), enemy1.getY(), beatEnemy * 2));
                            }else{
                                score += beatEnemy;
                                addScoreProp(ScoreProp.newScorePropInstance(enemy1.getX(), enemy1.getY(), beatEnemy));
                            }
                            //音效
                            MusicService.playGameSound("stomp.wav");
                        }
                    }
                }
            }
        }
    }

    public void winGame(){
        isGameEnd = true;
        isWinner = true;
        if(mario.getY() <= 261){
            score += 1000;
        }else if(mario.getY() <= 350){
            score += 400;
        }else{
            score += 100;
        }
    }
    public void loseGame(){
        isGameEnd = true;
        isWinner = false;
    }
    public void backToMenu(){
        this.setVisible(false);
        startGame = false;
        menu.setVisible(true);
        if(isSelectPlayBackgroundMusic){
            MusicService.closeBackGroundMusic();
            MusicService.playBackGroundMusic("menuSound.wav");
        }
        new Thread(menu).start();
    }
    @Override
    public void run() {
        while (startGame) {
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
//                    System.out.println("重置速度");
                    mario.setSpeed(moveSpeed);
                }
            }else if(mario.getSpeed() == 0){
                mario.setSpeed(moveSpeed);
            }
            //更新敌人障碍物等
            updateGameObject();


            //子弹击中物体的检测
            checkBulletHitObject();
            //龟壳移动检测
            checkShellHitObject();
            //检测是否到达终点
            if(!isGameEnd){
                mario.checkWin();
                //根据当前状态做出响应
                mario.response();
                //碰撞检测
                mario.checkMove(obstacles);
                mario.checkMove2(gainProps);
                mario.checkMove3(enemies);
            }else{
                if(!isEndAnimationFinished){
                    if(isWinner){
                        //旗帜
                        if(flag.getY() + flag.getHeight() < gameGrassY){
                            flag.setY(flag.getY() + 3);
                            if(flag.getY() + flag.getHeight() > gameGrassY){
                                flag.setY(gameGrassY - flag.getHeight());
                            }
                        }
//                        System.out.println(mario.getStatus());
                        //玛丽奥
                        if(mario.getY() + mario.getHeight() < gameGrassY){  //还未落地
//                            System.out.println("还未落地");
                            mario.setY(mario.getY() + 3);
                            if(mario.getY() + mario.getHeight() > gameGrassY){
                                mario.setY(gameGrassY - mario.getHeight());
                            }
                        }else if(flag.getY() + flag.getHeight() >= gameGrassY){ //旗帜落地，向右移动
                            if(!mario.isRunR()){  //落地切换右跑状态
//                                System.out.println("落地切换右跑状态");
                                if(mario.getHeroType() == HeroType.smallMario){
                                    mario.update(Status.s_mario_run_R);
                                }else if(mario.getHeroType() == HeroType.bigMario){
                                    mario.update(Status.mario_R_run);
                                }else{
                                    mario.update(Status.hero_R_run);
                                }
                                //对话框
                                if(dialogForMario == null)
                                    dialogForMario = GameDialog.newWinDialogInstanceForMario();
                            }
//                            System.out.println("旗帜落地，向右移动");

                            mario.setX(mario.getX() + 5);
                            mario.update();
//                            System.out.println(mario.getX());
                            if(mario.getX() > gan.getX() + 235){
                                //播放音效
                                MusicService.playGameSound("stage_clear.wav");
                                MessageService.showWinMessageDialog(this);
                                isEndAnimationFinished = true;
                            }
                        }
                    }else{
                        if (!isRegretfulLose) {
                            if (!mario.getFilePrefix().equals("s_mario_die")) {
                                mario.setImgSize(1);
                                mario.setFilePrefix("s_mario_die");
                                mario.updateSize();
                                mario.setY(mario.getY() - 20);
                            }
                        }
                        mario.setY(mario.getY() + 7);
                        if(mario.getY() > WindowHeight){
                            //音效
                            MusicService.playGameSound("death.wav");
                            MessageService.showLoseMessageDialog(this);
                            isEndAnimationFinished = true;
                        }
                    }
                }else{
                    backToMenu();
                }
            }
            //检测对话框的有效性
            if(dialogForMario == null){
                if(mario.getFilePrefix().equals("s_mario_die")){
                    dialogForMario = GameDialog.newDieDialogInstanceForMario();
                }
            }else{
                if(!dialogForMario.checkValid()){
                    dialogForMario = null;
                }
            }
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
        //障碍物和敌人
        drawEnemiesAndObstacles(graphics);

        //马里奥
        graphics.drawImage(mario.getImg(), mario.getX(), mario.getY(), this);

        //标头
        String timeStr, scoreStr, worldStr;
        graphics.setFont(new Font("楷书", Font.BOLD, 20));
        graphics.setColor(Color.white);
        if(language == MessageService.ENGLISH){
            timeStr = "TIME";
            scoreStr = "MARIO";
            worldStr = "WORLD";
        }else{
            timeStr = "时间";
            scoreStr = "马里奥";
            worldStr = "世界";
        }
        //分数
        graphics.drawString(scoreStr, 50, 70);
        if(score < 10){
            graphics.drawString("00000" + score, 50, 100);
        }else if(score < 100){
            graphics.drawString("0000" + score, 50, 100);
        }else if(score < 1000){
            graphics.drawString("000" + score, 50, 100);
        }else if(score < 10000){
            graphics.drawString("00" + score, 50, 100);
        }else if(score < 100000){
            graphics.drawString("0" + score, 50, 100);
        }else{
            graphics.drawString("" + score, 50, 100);
        }
        //金币
        graphics.drawImage(gold.getImg(), gold.getX(), gold.getY(), this);
        gold.update();
        if(goldCount < 10){
            graphics.drawString(" × 0" + goldCount, 280, 90);
        }else{
            graphics.drawString(" × " + goldCount, 280, 90);
        }

        //世界
        graphics.drawString(worldStr, 500, 70);
        graphics.drawString("1 - 1", 500, 100);

        //时间
        graphics.drawString(timeStr, 700, 70);
        graphics.drawString(getGameTime() + "", 700, 100);
        //对话框
        if(dialogForMario != null){
            graphics.drawImage(dialogForMario.getImg(), mario.getX() - 22, mario.getY() - 40, this);
        }



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

    public List<Bullet> getHeroBullets() {
        return heroBullets;
    }

    public void setHeroBullets(List<Bullet> heroBullets) {
        this.heroBullets = heroBullets;
    }

    public Obstacle getGan() {
        return gan;
    }

    public void setGan(Obstacle gan) {
        this.gan = gan;
    }

    public boolean isGameEnd() {
        return isGameEnd;
    }

    public void setGameEnd(boolean gameEnd) {
        isGameEnd = gameEnd;
    }

    public int getGameTime(){
        return gameClock.getTime();
    }

    public GameDialog getDialogForMario() {
        return dialogForMario;
    }

    public void setDialogForMario(GameDialog dialogForMario) {
        this.dialogForMario = dialogForMario;

    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
    public void getScore(int score){
        this.score += score;
    }
    public void getGold(){
        goldCount++;
    }

    public boolean isRegretfulLose() {
        return isRegretfulLose;
    }

    public void setRegretfulLose(boolean regretfulLose) {
        isRegretfulLose = regretfulLose;
    }

    public void addScoreProp(ScoreProp scoreProp){
        scoreProps.add(scoreProp);
    }
}
