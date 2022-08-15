package com.zx.mario.service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import static com.zx.mario.manager.Application.*;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 负责处理文件和消息显示
 * @since 2022 - 07 - 17 - 14:28
 */
public class MessageService {
    //配置文件
    private static final Properties properties;
    //统一资源定位符
    private static URL url;
    //配置文件在工程中的路径
    private static String path = "/com/zx/mario/language/";
    //语言
    public static final int CHINESE = 0;
    public static final int ENGLISH = 1;

    static {
        properties = new Properties();
    }

    /**
     * 根据语言选项，加载对应的配置文件
     * @param language 语言 0-中文 1-英文
     */
    private static void load(int language) {
        if (language == 0) {
            try {
                url = MessageService.class.getResource(path + "menu_CH.properties");
                assert url != null;
                properties.load(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                url = MessageService.class.getResource(path + "menu_US.properties");
                assert url != null;
                properties.load(url.openStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取菜单界面的按钮文字
     *
     * @param language 指定的语言
     * @return 文字数组
     */
    public static String[] getMenuText(int language) {
        String[] result = new String[6];
        //加载文件
        load(language);
        for (int i = 0; i < 6; i++) {
            result[i] = properties.getProperty("" + i);
        }
        return result;
    }

    /**
     * 获取组件文字
     * @param key 关键字
     * @return 组件文字
     */
    public static String getTextByLanguage(String key) {
        load(language);
        return properties.getProperty(key);
    }

    /**
     * 在主菜单显示游戏帮助
     *
     * @param frame    父窗口
     * @param language 语言
     * @see MessageService
     */
    public static void showGameHelpMessage(JFrame frame, int language) {
        String message = MessageService.getTextByLanguage("help");
        ImageIcon helpIcon = ImageFactory.getIcon("help.png");
        JOptionPane.showMessageDialog(frame, message, getTextByLanguage("helpTitle"), JOptionPane.INFORMATION_MESSAGE, helpIcon);
    }

    /*游戏设置*/
    public static void showGameSetting(JFrame parent) {
        //显示对话框
        JDialog dialog = new JDialog(parent, getTextByLanguage("settingTitle"));
        //对话框添加事件监听，关闭窗口显示主界面
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
//                System.out.println("关闭");
                menu.setVisible(true);
            }
        });
        dialog.setLocationRelativeTo(parent);
        dialog.setBounds(parent.getBounds().x, parent.getBounds().y + 100, 800, 400);
        dialog.setIconImage(ImageFactory.getImg("setting.png"));
        dialog.setResizable(false);
        dialog.setVisible(true);
        dialog.setLayout(null);
        Font titleFont = new Font("楷体", Font.BOLD, 20);
        Font textFont = new Font("楷体", Font.BOLD, 15);
        /*音效设置*/
        //音乐标签
        JLabel musicLabel = new JLabel(getTextByLanguage("settingMusicLabel"));
        musicLabel.setBounds(10, -20, 600, 100);
        musicLabel.setFont(titleFont);
        dialog.add(musicLabel);
        //复选按钮：背景音乐、爆炸音效、按键音效
        JCheckBox checkBox1 = new JCheckBox(getTextByLanguage("settingCheckBox1"));
        JCheckBox checkBox2 = new JCheckBox(getTextByLanguage("settingCheckBox2"));
        JCheckBox checkBox3 = new JCheckBox(getTextByLanguage("settingCheckBox3"));
        checkBox1.setBounds(10, 70, 100, 30);
        checkBox2.setBounds(150, 70, 100, 30);
        checkBox3.setBounds(290, 70, 100, 30);
        checkBox1.setFont(textFont);
        checkBox2.setFont(textFont);
        checkBox3.setFont(textFont);
        //添加到对话框
        dialog.add(checkBox1);
        dialog.add(checkBox2);
        dialog.add(checkBox3);
        //设置选中状态
        if (isSelectPlayBackgroundMusic)
            checkBox1.setSelected(true);
        if (isSelectPlayGameSoundEffect)
            checkBox2.setSelected(true);
        if (isSelectPlayClickSoundEffect)
            checkBox3.setSelected(true);
        //背景音乐
        checkBox1.addItemListener(e -> {
            isSelectPlayBackgroundMusic = !isSelectPlayBackgroundMusic;
            if (isSelectPlayBackgroundMusic) {
                MusicService.startBackGroundMusic();
            } else {
                MusicService.stopBackGroundMusic();
            }
        });
        //游戏音效
        checkBox2.addItemListener(e -> {
            isSelectPlayGameSoundEffect = !isSelectPlayGameSoundEffect;
        });
        //点击音效
        checkBox3.addItemListener(e -> isSelectPlayClickSoundEffect = !isSelectPlayClickSoundEffect);

        /*语言选项*/
        JLabel languageLabel = new JLabel(getTextByLanguage("settingLanguageLabel"));
        languageLabel.setBounds(10, 120, 600, 100);
        languageLabel.setFont(titleFont);
        dialog.add(languageLabel);

        //单选按钮
        ButtonGroup group = new ButtonGroup();
        JRadioButton chineseBtn = new JRadioButton();
        chineseBtn.setBounds(10, 200, 20, 20);
        group.add(chineseBtn);
        dialog.add(chineseBtn);
        JRadioButton englishBtn = new JRadioButton();
        englishBtn.setBounds(10, 250, 20, 20);
        group.add(englishBtn);
        dialog.add(englishBtn);

        //标签
        JLabel chinesLabel = new JLabel(getTextByLanguage("settingRadioBox1"));
        chinesLabel.setBounds(50, 200, 100, 30);
        chinesLabel.setVerticalAlignment(SwingConstants.TOP);
        chinesLabel.setHorizontalAlignment(SwingConstants.LEFT);
        chinesLabel.setFont(textFont);
        dialog.add(chinesLabel);
        JLabel englishLabel = new JLabel(getTextByLanguage("settingRadioBox2"));
        englishLabel.setBounds(50, 250, 100, 30);
        englishLabel.setVerticalAlignment(SwingConstants.TOP);
        englishLabel.setHorizontalAlignment(SwingConstants.LEFT);
        englishLabel.setFont(textFont);
        dialog.add(englishLabel);
        //语言选择
        if(language == ENGLISH){
            englishBtn.setSelected(true);
        }else{
            chineseBtn.setSelected(true);
        }
//        englishBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("英文");
//            }
//        });

        //两个按钮是同一个组，状态同时变化，添加一个监听即可
        englishBtn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
//                System.out.println("语言切换");
//                System.out.println(englishLabel.getBounds());
                if(englishBtn.isSelected()){
                    language = ENGLISH;
                }else{
                    language = CHINESE;
                }
                dialog.setTitle(getTextByLanguage("settingTitle"));
                musicLabel.setText(getTextByLanguage("settingMusicLabel"));
                checkBox1.setText(getTextByLanguage("settingCheckBox1"));
                checkBox2.setText(getTextByLanguage("settingCheckBox2"));
                checkBox3.setText(getTextByLanguage("settingCheckBox3"));
                languageLabel.setText(getTextByLanguage("settingLanguageLabel"));
                chinesLabel.setText(getTextByLanguage("settingRadioBox1"));
                englishLabel.setText(getTextByLanguage("settingRadioBox2"));
                menu.flush();
            }
        });
        //点击标签，也触发事件
        englishLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("点击英文标签");
                englishBtn.setSelected(true);
            }
        });
        chinesLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
//                System.out.println("点击中文标签");
                chineseBtn.setSelected(true);
            }
        });
//        chineseBtn.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                System.out.println("中文");
//            }
//        });
//        chineseBtn.addItemListener(new ItemListener() {
//            @Override
//            public void itemStateChanged(ItemEvent e) {
//                System.out.println("中文切换");
//                language = CHINESE;
//                menu.flush();
//            }
//        });

    }
}
