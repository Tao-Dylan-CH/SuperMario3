package com.zx.mario.test;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec com.zx.mario.test
 * @since 2022 - 07 - 17 - 16:26
 */
public class ListenerTest extends JFrame {
    public ListenerTest(){
        this.setSize(1000, 500);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println("按下");
            }
        });
        this.setVisible(true);
    }

    public static void main(String[] args) {
        new ListenerTest();
    }
}
