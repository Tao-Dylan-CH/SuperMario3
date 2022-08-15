package com.zx.mario.domain;

import com.zx.mario.manager.Application;
import com.zx.mario.service.ImageFactory;
import com.zx.mario.utils.MathUtil;

import javax.swing.*;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 菜单背景的移动物体
 * @since 2022 - 07 - 17 - 22:01
 */
public class MenuObject extends JLabel {
    //坐标
    private int x;
    private int y;
    //图片数
    private int imgSize;
    //速度
    private int speed;
    //文件名前缀
    private String filePrefix;
    //当前图片序列
    private int currentImgIndex;
    private int width;
    private int height;
    public void move() {
        x += speed;
        if(speed > 0){  //向右
            if(x > Application.WindowWidth){
                x = -MathUtil.getRandomNum(100, 400);
            }

        }else{          //向左
            if(x < -width){
                x = Application.WindowWidth + MathUtil.getRandomNum(100, 400);
            }
        }
        currentImgIndex = (currentImgIndex + 1) % imgSize;
        super.setIcon(getImg());
    }

    public ImageIcon getImg(){
        return ImageFactory.getIcon(filePrefix + currentImgIndex+ ".png");
    }

    public MenuObject(){}

    public MenuObject(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex) {
        this.x = x;
        this.y = y;
        this.imgSize = imgSize;
        this.speed = speed;
        this.filePrefix = filePrefix;
        this.currentImgIndex = currentImgIndex;
        this.width = ImageFactory.getImg(filePrefix + currentImgIndex + ".png").getWidth();
        this.height = ImageFactory.getImg(filePrefix + currentImgIndex + ".png").getHeight();
        super.setIcon(getImg());
        super.setBounds(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getImgSize() {
        return imgSize;
    }

    public void setImgSize(int imgSize) {
        this.imgSize = imgSize;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getFilePrefix() {
        return filePrefix;
    }

    public void setFilePrefix(String filePrefix) {
        this.filePrefix = filePrefix;
    }

    public int getCurrentImgIndex() {
        return currentImgIndex;
    }

    public void setCurrentImgIndex(int currentImgIndex) {
        this.currentImgIndex = currentImgIndex;
    }
}
