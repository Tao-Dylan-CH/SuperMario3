package com.zx.mario.view;


import com.zx.mario.domain.MenuObject;
import com.zx.mario.service.ImageFactory;
import com.zx.mario.service.MessageService;
import com.zx.mario.manager.Application;
import com.zx.mario.service.MusicService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec com.zx.mario.view
 * @since 2022 - 07 - 17 - 10:30
 */
public class Menu extends JFrame implements Runnable{
    //背景
    private JLabel back;
    //图标
    private JLabel title;
    //用于选择的五个手指
    private JLabel[] fingers;
    //五个按钮
    private JButton[] buttons;
    //马里奥
    private MenuObject mario1;
    private MenuObject mario2;
    private MenuObject mario3;
    //云
    private MenuObject cloud1;
    private MenuObject cloud2;
    private MenuObject cloud3;
    public Menu(){
        //标题
        this.setTitle(MessageService.getTextByLanguage("menuTitle"));
        this.setIconImage(ImageFactory.getImg("menuIcon"));
        //窗口大小
        this.setSize(800, 600);
//        this.setResizable(false);
        //默认关闭行为
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //布局
        this.setLayout(null);
        //窗口居中
        this.setLocationRelativeTo(null);

        //初始化 （窗口背景、按钮、标签等）
        init();
        //设置监听 （键盘监听、聚焦监听）
        setListener();
        //获取焦点 （窗口可聚焦）
        this.setFocusable(true);
//        this.requestFocus();
        showMenu();
        //保存窗口对象
        Application.menu = this;
        //播放背景音乐
        MusicService.playBackGroundMusic("menuSound.wav");


        //背景人物
        mario1 = new MenuObject(0, Application.menuGrassY - 40, 3, 2, "mario_R_run", 0);
        add(mario1);
        mario2 = new MenuObject(200, Application.menuGrassY - 25, 2, 3, "s_mario_run_R", 0);
        add(mario2);
        mario3 = new MenuObject(0, 40, 1, 4, "plane", 0);
        add(mario3);
        //云
        cloud1 = new MenuObject(200, 20, 1, -1, "cloud", 0);
        add(cloud1);
        cloud2 = new MenuObject(800, 100, 1, -1, "cloud", 0);
        add(cloud2);
        cloud3 = new MenuObject(500, 80, 1, -1, "cloud", 0);
        add(cloud3);
        //启动窗口的线程
        new Thread(this).start();
    }

    /**
     * 该方法用于语言切换
     */
    public void flush(){
        //标题
        this.setTitle(MessageService.getTextByLanguage("menuTitle"));
        //初始界面 英文
        String[] menuText = MessageService.getMenuText(Application.language);
        for(int i = 0; i < 6; i++){
            buttons[i].setText(menuText[i]);
        }
    }
    public void showMenu(){
        this.setVisible(true);
    }
    private void init(){
        //菜单背景
        back = new JLabel(ImageFactory.getIcon("menuBg.png"));
        setBackground();
        //游戏图标
        ImageIcon titleIcon = ImageFactory.getIcon("title.png");
        title = new JLabel(titleIcon);
        title.setBounds((800 - titleIcon.getIconWidth()) / 2, 0, titleIcon.getIconWidth(), 200);
        add(title);
        //五个按钮
        buttons = new JButton[6];
        ImageIcon btnIcon = ImageFactory.getIcon("btn.png");
        //初始界面 英文
        String[] menuText = MessageService.getMenuText(Application.language);
//        Application.language = MessageService.ENGLISH;
        int x = (800 - btnIcon.getIconWidth()) / 2, y = 200;
        for(int i = 0; i < 6; i++){
            //带图片和文字的按钮
            buttons[i] = new JButton(menuText[i], btnIcon);
            //设置字体
            buttons[i].setFont(new Font("楷体", Font.BOLD, 20));
            //设置字体颜色
            buttons[i].setForeground(Color.YELLOW);
            //设置文本对齐方式
            buttons[i].setHorizontalTextPosition(SwingConstants.CENTER);
            buttons[i].setVerticalTextPosition(SwingConstants.CENTER);
            //设置按钮位置（空布局使用setBounds方法）
            buttons[i].setBounds(x, y + i * 50, btnIcon.getIconWidth(), btnIcon.getIconHeight());
            this.add(buttons[i]);
        }
        //6个手指
        fingers = new JLabel[6];
        ImageIcon fingerIcon = ImageFactory.getIcon("finger.png");
        x = (800 + btnIcon.getIconWidth()) / 2;
        y = 215;
        for(int i = 0; i < 6; i++){
            fingers[i] = new JLabel(fingerIcon);
            fingers[i].setBounds(x, y + i * 50, fingerIcon.getIconWidth(), fingerIcon.getIconHeight());
            //显示第一个
            fingers[i].setVisible(i == 0);
            this.add(fingers[i]);
        }
    }
    private void setListener(){
        //窗口添加键盘监听事件， 上下箭头选择
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();
                int index = getVisibleIndex();
                //确认
                if(keyCode == KeyEvent.VK_ENTER){
                    if(Application.isSelectPlayClickSoundEffect)
                        MusicService.playSound("click2.wav");
                    choice(index);
                }

                //选择菜单
                if(keyCode == KeyEvent.VK_UP){
                    if(Application.isSelectPlayClickSoundEffect)
                        MusicService.playSound("click1.wav");
                    fingers[index].setVisible(false);
                    if(index == 0){
                        fingers[5].setVisible(true);
                    }else{
                        fingers[index - 1].setVisible(true);
                    }
                }else if(keyCode == KeyEvent.VK_DOWN){
                    if(Application.isSelectPlayClickSoundEffect)
                        MusicService.playSound("click1.wav");
                    fingers[index].setVisible(false);
                    if(index == 5){
                        fingers[0].setVisible(true);
                    }else{
                        fingers[index + 1].setVisible(true);
                    }
                }


            }
        });
        //为按钮添加聚焦事件，单击触发
        for(int i = 0; i < 6; i++){
            addFocusListenerToButton(i);
        }

    }

    /**
     * 设置图片为窗口背景
     */
    private void setBackground() {
        //设置背景图片位置大小
        back.setBounds(0, -45, 800, 600);
        //面板透明
        JPanel j = (JPanel)getContentPane();
        j.setOpaque(false);
        //设置背景
        getLayeredPane().add(back, new Integer(Integer.MIN_VALUE));//背景添加到分层面板
    }

    //获取唯一显示的手指对应的数组中的下标
    private int getVisibleIndex(){
        for (int i = 0; i < 6; i++) {
            if(fingers[i].isVisible()){
                return i;
            }
        }
        return -1;
    }

    //开始游戏
    private void startGame(){
//        System.out.println("开始游戏");
//        JOptionPane.showMessageDialog(this, "功能开发中，敬请期待");
        Application.startGame = true;
        Application.isPressA = false;
        Application.isPressD = false;
        Application.isPressK = false;
        GameWindow gameWindow = new GameWindow();
        this.setVisible(false);
        gameWindow.setVisible(true);
        gameWindow.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                Application.menu.setVisible(true);
                Application.startGame = false;
                new Thread(Application.menu).start();
                if(Application.isSelectPlayBackgroundMusic){
                    MusicService.closeBackGroundMusic();
                    MusicService.playBackGroundMusic("menuSound.wav");
                }
            }
        });
    }
    //继续游戏
    private void continueGame(){
//        System.out.println("继续游戏");
        GameWindow gameWindow = Application.gameWindow;
        if(gameWindow != null && !gameWindow.isGameEnd()){
            this.setVisible(false);
            Application.startGame = true;
            Application.isPressA = false;
            Application.isPressD = false;
            Application.isPressK = false;
            gameWindow.setVisible(true);
            new Thread(Application.gameWindow).start();
            //启动计时
            gameWindow.startTheGameClock();
            if(Application.isSelectPlayBackgroundMusic){
                MusicService.closeBackGroundMusic();
                MusicService.playBackGroundMusic("main_theme.wav");
            }
        } else{
            JOptionPane.showMessageDialog(this, "没有存档，请开始游戏");
        }
    }
    //游戏设置
    private void setting(){
//        System.out.println("游戏设置");
        this.setVisible(false);
        MessageService.showGameSetting(this);
    }
    //游戏帮助
    private void help(){
//        System.out.println("游戏帮助");
        MessageService.showGameHelpMessage(this);
    }
    //手册
    private void handBook(){
//        System.out.println("图鉴");
        JOptionPane.showMessageDialog(this, "功能开发中，敬请期待");
    }
    //退出游戏
    private void quitGame(){
        System.out.println("退出游戏");
        System.exit(0);
    }

    /**
     * 为按钮添加聚焦事件，单击触发
     * @param index 按钮在按钮数组中对应的下标
     */
    private void addFocusListenerToButton(int index){
        buttons[index].addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if(e.getSource().equals(buttons[index])){
                    if(Application.isSelectPlayClickSoundEffect)
                        MusicService.playSound("btnSound.wav");
                    int visibleIndex = getVisibleIndex();
                    if(visibleIndex != index){
                        fingers[visibleIndex].setVisible(false);
                        fingers[index].setVisible(true);
                    }
                    //聚焦窗口
                    Application.menu.requestFocus();
                    //调用对应模块
                    choice(index);
                }
            }
        });
    }

    /**
     * 选择菜单模块
     * @param index 模块对应的下标
     */
    private void choice(int index){
        switch (index){
            case 0:
                startGame();
                break;
            case 1:
                continueGame();
                break;
            case 2:
                setting();
                break;
            case 3:
                help();
                break;
            case 4:
                handBook();
                break;
            case 5:
                quitGame();
                break;
        }
    }
    @Override
    public void run() {
        while(!Application.startGame){
            try {
                mario1.move();
                mario2.move();
                mario3.move();
                cloud1.move();
                cloud2.move();
                cloud3.move();
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

}
