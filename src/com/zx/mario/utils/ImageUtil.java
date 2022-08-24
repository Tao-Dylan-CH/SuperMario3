package com.zx.mario.utils;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 用于的加载
 * @since 2022 - 07 - 16 - 23:15
 */
public class ImageUtil {
    public static String path = "/com/zx/mario/img/";
    //获取图片
    public static BufferedImage getImage(String fileName){
        URL url = ImageUtil.class.getResource(path + fileName);
        if(url == null){
            throw new RuntimeException("文件加载失败！");
        }

        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
    //获取图标
    public static ImageIcon getIcon(String fileName){
        URL url = ImageUtil.class.getResource(path + fileName);
        if(url == null){
            throw new RuntimeException("文件加载失败！");
        }
        return new ImageIcon(url);
    }
}
