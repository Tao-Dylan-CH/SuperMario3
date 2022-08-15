package com.zx.mario.utils;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 音乐相关
 * @since 2022 - 07 - 16 - 23:09
 */
public class SoundUtil {
    //背景音乐:向外界提供该对象用于暂停或播放
    public static AudioDao backgroundMusic;
    //存放音频的目录
    private static final String path = "/com/zx/mario/sounds/";

    /**
     * 播放一次音声
     * @param fileName 文件名
     */
    public static void playSound(String fileName){
        //'/'表示绝对资源位置，考虑到要包装为jar包，获取资源资源使用这种方式
        URL url = AudioDao.class.getResource(path + fileName);
        AudioDao audioDao = new AudioDao();
        audioDao.open(url);
        audioDao.load();
        audioDao.start();
    }
    /**
     * 背景音乐
     */
    public static void playBackgroundMusic(String fileName){
        URL url = AudioDao.class.getResource(path + fileName);
        backgroundMusic = new AudioDao();//通过向外界提供该实例，用于播放或暂停
        backgroundMusic.open(url);
        backgroundMusic.load();
        backgroundMusic.loop();
        backgroundMusic.start();
    }
    public static void shutDownBackgroundMusic(){
        if(backgroundMusic != null){
            backgroundMusic.stop();
        }
    }
    public static void openBackgroundMusic(){
        if(backgroundMusic != null){
            backgroundMusic.start();
        }
    }


//    public static void main(String[] args) {
////        将MP3文件转为wav文件
//        String fileName = "点击4";
//        String filePath = "D:\\" + fileName + ".mp3";
//        String targetPath = "D:\\" + fileName +".wav";
//        byteToWav(getBytes(filePath), targetPath);
//    }

    public static boolean byteToWav(byte[] sourceBytes, String targetPath){
        if (sourceBytes == null || sourceBytes.length == 0) {
            System.out.println("Illegal Argument passed to this method");
            return false;
        }
        try (final ByteArrayInputStream bais = new ByteArrayInputStream(sourceBytes);
             final AudioInputStream sourceAIS = AudioSystem.getAudioInputStream(bais)) {
            AudioFormat sourceFormat = sourceAIS.getFormat();
            // 设置MP3的语音格式,并设置16bit
            AudioFormat mp3tFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, sourceFormat.getSampleRate(), 16, sourceFormat.getChannels(), sourceFormat.getChannels() * 2, sourceFormat.getSampleRate(), false);
            // 设置阿里语音识别的音频格式
            AudioFormat pcmFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 16000, 16, 1, 2, 16000, false);
            try (
                    // 先通过MP3转一次，使音频流能的格式完整
                    final AudioInputStream mp3AIS = AudioSystem.getAudioInputStream(mp3tFormat, sourceAIS);
                    // 转成阿里需要的流
                    final AudioInputStream pcmAIS = AudioSystem.getAudioInputStream(pcmFormat, mp3AIS)) {
                // 根据路径生成wav文件
                AudioSystem.write(pcmAIS, AudioFileFormat.Type.WAVE, new File(targetPath));
            }
            return true;
        } catch (IOException | UnsupportedAudioFileException e) {
            e.printStackTrace();
//            System.out.println("文件转换异常：" + e.getMessage());
            return false;
        }

    }

    public static byte[] getBytes(String filepath){
        byte[] buffer = null;
        try{
            File file =  new File(filepath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1000];
            int n;
            while((n=fis.read(b))!=-1){
                bos.write(b,0,n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();

        }catch(Exception e){
            e.printStackTrace();
        }
        return buffer;
    }
}
//音频访问对象 支持wav mid
class AudioDao {
    private AudioInputStream stream;//音频输入流
    private AudioFormat format;//音频格式
    private Clip clip;//音频夹

    /**
     * 打开声音文件方法
     * @param url 文件路径
     */
    public void open(URL url) {
        try {
            stream = AudioSystem.getAudioInputStream(url);//音频输入流
            format = stream.getFormat();//音频格式对象
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 建立播放音频的音频行
     */
    public void load() {
        //音频行信息
        DataLine.Info info = new DataLine.Info(Clip.class, format);//音频行信息
        try {
            clip = (Clip) AudioSystem.getLine(info);
            clip.open(stream);//将音频数据读入音频行
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        clip.stop();//暂停音频播放
    }

    public void start() {
        clip.start();//播放音频
    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);//循环播放
    }
}
