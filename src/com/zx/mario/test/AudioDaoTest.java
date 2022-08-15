package com.zx.mario.test;




import com.zx.mario.utils.SoundUtil;

import javax.swing.*;


/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec com.zx.mario.test
 * @since 2022 - 07 - 16 - 22:44
 */
public class AudioDaoTest {

    public static void main(String[] args) {
        JFrame jFrame = new JFrame();
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.setSize(400, 500);
        jFrame.setVisible(true);
        jFrame.setLocationRelativeTo(null);
        SoundUtil.playBackgroundMusic("1.wav");
    }
}