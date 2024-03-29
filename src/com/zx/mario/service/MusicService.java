package com.zx.mario.service;

import com.zx.mario.manager.Application;
import com.zx.mario.utils.SoundUtil;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 音乐相关的操作
 * @since 2022 - 07 - 17 - 14:57
 */
public class MusicService {
    //播放背景音乐
    public static void playBackGroundMusic(String filename){
        SoundUtil.playBackgroundMusic(filename);
    }
    //暂停播放背景音乐
    public static void stopBackGroundMusic(){
        SoundUtil.stopBackgroundMusic();
    }
    //继续播放背景音乐
    public static void startBackGroundMusic(){
        SoundUtil.openBackgroundMusic();
    }
    //关闭音乐播放
    public static void closeBackGroundMusic(){
        SoundUtil.closeBackgroundMusic();
    }
    //播放音效
    public static void playSound(String filename){
        SoundUtil.playSound(filename);
    }
    //播放游戏音效
    public static void playGameSound(String fileName){
        if(Application.isSelectPlayGameSoundEffect)
            playSound(fileName);
    }
}
