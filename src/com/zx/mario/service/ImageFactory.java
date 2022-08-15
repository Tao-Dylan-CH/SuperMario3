package com.zx.mario.service;

import com.zx.mario.utils.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 负责加载图片资源，向外提供接口
 * @since 2022 - 07 - 18 - 9:55
 */
public class ImageFactory {
    private static final String path = "/com/zx/mario/img";
    //存储所有ImageIcon
    private static Map<String, ImageIcon> imageIconMap;
    //存储所有BufferedImage
    private static Map<String, BufferedImage> imageMap;
    static {
        imageIconMap = new HashMap<>();
        imageMap = new HashMap<>();
        init();
    }
    public static void init(){
//        //BufferedImag
//        BufferedImage menuIcon = ImageUtil.getImage("menuIcon.png");
//        imageMap.put("menuIcon", menuIcon);
//
//        //ImageIcon
//        ImageIcon menuBg = ImageUtil.getIcon("menuBg.png");

        //通过反射 加载所有图片资源
        URL url = Object.class.getResource(path);
        assert url != null;
        File file = null;
        try {
            //得到目录对象
            file = new File(url.toURI());
            if(file.isDirectory()){
                //遍历目录下的文件
                File[] files = file.listFiles();
                if(files != null){
                    for (File f:files
                    ) {
                        String fileName = f.getName();
                        //得到图片对象
                        BufferedImage img = ImageUtil.getImage(fileName);
                        ImageIcon imageIcon = ImageUtil.getIcon(fileName);
                        //将文件名作为键，保存的map中
                        imageMap.put(f.getName(), img);
                        imageIconMap.put(f.getName(), imageIcon);
//                        System.out.println(f.getName());
                    }
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public static ImageIcon getIcon(String name){
        return imageIconMap.get(name);
    }
    public static BufferedImage getImg(String name){
        return imageMap.get(name);
    }
}
