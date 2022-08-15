package com.zx.mario.domain;

import com.zx.mario.service.ImageFactory;

import java.awt.*;

import static com.zx.mario.manager.Application.gameGrassY;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 游戏中物体的抽象
 * @since 2022 - 07 - 18 - 22:57
 */
public class GameObject {
    //坐标
    protected int x;
    protected int y;
    //图片数
    protected int imgSize;
    //速度
    protected int speed;
    //文件名前缀
    protected String filePrefix;
    //当前图片序列
    protected int currentImgIndex;
    //物体的宽和高
    protected int width;
    protected int height;
    //Enemy 和 MushRoom都有的属性
    protected boolean toRight = false;
    public GameObject(){}
    public GameObject(int x, int y, int imgSize, int speed, String filePrefix, int currentImgIndex) {
        this.x = x;
        this.y = y;
        this.imgSize = imgSize;
        this.speed = speed;
        this.filePrefix = filePrefix;
        this.currentImgIndex = currentImgIndex;
        updateSize();
    }
    public void updateSize(){
        if(imgSize == 1){
            this.width = ImageFactory.getImg(filePrefix + ".png").getWidth();
            this.height = ImageFactory.getImg(filePrefix + ".png").getHeight();
        }else{
            this.width = ImageFactory.getImg(filePrefix + currentImgIndex + ".png").getWidth();
            this.height = ImageFactory.getImg(filePrefix + currentImgIndex + ".png").getHeight();
        }
//        System.out.println(width + ", " + height);
    }

    public Image getImg(){
        if(imgSize == 1){
            return ImageFactory.getImg(filePrefix + ".png");
        }else{
            return ImageFactory.getImg(filePrefix + currentImgIndex + ".png");
        }
    }
    public void update(){
        if(imgSize > 1){
            currentImgIndex = (currentImgIndex + 1) % imgSize;
        }
    }
    public boolean onGrass(){
        return Math.abs(y + height - gameGrassY) <= 1;
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

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isToRight() {
        return toRight;
    }

    public void setToRight(boolean toRight) {
        this.toRight = toRight;
    }
}
