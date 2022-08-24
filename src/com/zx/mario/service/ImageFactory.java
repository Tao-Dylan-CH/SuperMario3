package com.zx.mario.service;

import com.zx.mario.utils.ImageUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
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
//        init();       //jar包下不能通过File 获取目录下的文件名
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
        //得到目录对象
        try {
            URI uri = url.toURI();
//            file = new File(uri);   //运行jar 报错
            String pathStr = url.getPath();
            System.out.println("path:  " + pathStr);
            file = new File(pathStr);
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
//    public static ImageIcon getIcon(String name){
//        return imageIconMap.get(name);
//    }
//    public static BufferedImage getImg(String name){
//        return imageMap.get(name);
//    }

    //动态加载
    public static ImageIcon getIcon(String name){
        if(imageIconMap.get(name) == null){
            imageIconMap.put(name, ImageUtil.getIcon(name));
        }
        return imageIconMap.get(name);
    }
    public static BufferedImage getImg(String name){
        if(imageMap.get(name) == null){
            imageMap.put(name, ImageUtil.getImage(name));
        }
        return imageMap.get(name);
    }
}
