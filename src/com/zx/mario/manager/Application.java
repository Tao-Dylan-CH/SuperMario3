package com.zx.mario.manager;

import com.zx.mario.domain.Status;
import com.zx.mario.service.MessageService;
import com.zx.mario.view.GameWindow;
import com.zx.mario.view.Menu;

import javax.rmi.CORBA.Stub;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 游戏管理
 * @since 2022 - 07 - 17 - 14:57
 */
public class Application {
    //语言
    public static int language = MessageService.ENGLISH;
    //是否开始游戏
    public static boolean startGame = false;
    //菜单
    public static Menu menu;
    //游戏窗口
    public static GameWindow gameWindow;
    public static int menuGrassY = 515;
    //窗口大小
    public static final int WindowWidth = 800;
    public static final int WindowHeight = 600;
    //设置模块
    public static boolean isSelectPlayBackgroundMusic = true;//播放背景音乐
    public static boolean isSelectPlayGameSoundEffect = true;//播放游戏音效
    public static boolean isSelectPlayClickSoundEffect = true;//播放按键音效
    /*游戏模块*/
    public static String getFilePrefixByStatus(Status status){
        return status.toString();
    }
    public static final int gameGrassY = 461;
    //玛丽奥每种状态图片的数量
    private static final Map<Status, Integer> statusToImgSize;
    static {
        statusToImgSize = new HashMap<>();
        statusToImgSize.put(Status.s_mario_die, 1);
        statusToImgSize.put(Status.s_mario_jump_L, 1);
        statusToImgSize.put(Status.s_mario_jump_R, 1);
        statusToImgSize.put(Status.s_mario_run_L, 2);
        statusToImgSize.put(Status.s_mario_run_R, 2);
        statusToImgSize.put(Status.s_mario_stand_L, 1);
        statusToImgSize.put(Status.s_mario_stand_R, 1);
        statusToImgSize.put(Status.s_mario_stop_L, 1);
        statusToImgSize.put(Status.s_mario_stop_R, 1);

        statusToImgSize.put(Status.mario_R_stand, 1);
        statusToImgSize.put(Status.mario_L_stand, 1);
        statusToImgSize.put(Status.mario_R_run, 3);
        statusToImgSize.put(Status.mario_L_run, 3);
        statusToImgSize.put(Status.mario_R_jump, 1);
        statusToImgSize.put(Status.mario_L_jump, 1);
        statusToImgSize.put(Status.mario_stop_R, 1);
        statusToImgSize.put(Status.mario_stop_L, 1);

//        statusToImgSize.put(Status.hero_R_stand, 1);
//        statusToImgSize.put(Status.hero_L_stand, 1);
//        statusToImgSize.put(Status.hero_R_run, 3);
//        statusToImgSize.put(Status.hero_L_run, 3);
//        statusToImgSize.put(Status.hero_R_jump, 1);
//        statusToImgSize.put(Status.hero_L_jump, 1);
//        statusToImgSize.put(Status.hero_stop_R, 1);
//        statusToImgSize.put(Status.hero_stop_L, 1);
        statusToImgSize.put(Status.hero_R_stand, 1);
        statusToImgSize.put(Status.hero_L_stand, 1);
        statusToImgSize.put(Status.hero_R_run, 1);
        statusToImgSize.put(Status.hero_L_run, 1);
        statusToImgSize.put(Status.hero_R_jump, 1);
        statusToImgSize.put(Status.hero_L_jump, 1);
        statusToImgSize.put(Status.hero_stop_R, 1);
        statusToImgSize.put(Status.hero_stop_L, 1);
        statusToImgSize.put(Status.hero_attack_R, 1);
        statusToImgSize.put(Status.hero_attack_L, 1);
    }
    public static int getImgSizeByStatus(Status status){
        return statusToImgSize.get(status);
    }

    //玛丽奥移动
    public static int marioSpeed;   //用于窗口一半山体移动，马里奥速度置零 之后的恢复
    //游戏界面 马里奥最远到达的X
    public static final int marioMaxX = WindowWidth / 2 - 50;
    //跳跃上升时间
    public static final int uptime = 12;
    public static final int jumpSpeed = 10;
    public static final int moveSpeed = 5;
    //跳跃直立 和 斜着图片一样 由按键区分
    public static boolean isPressA = false;
    public static boolean isPressD = false;
    public static boolean isPressK = false;
    //食人花更新频率控制
    public static final int flowerUpdateFrequent = 7;
    //问号方块更新频率控制
    public static final int boxUpdateFrequent = 3;
    //蘑菇死亡显示图片控制
    public static final int fungusDuration = 3;
    //乌龟被踩后恢复控制
    public static  final int shellDuration = 70;
    //乌龟被踩前后的y坐标变化
    public static final int tortoiseGap = 12;
    //马里奥无敌时间
    public static final int invincibleTime = 50;
    //无敌星星持续时间
    public static final int durationOfStar = 200;
    //开火
    public static final int fireTime = 7;
    public static final int fireInterval = 20;
    //分数
    public static final int treadScore = 100;
    public static final int beatEnemy = 100;
    public static final int tortoise = 400;
    public static final int getGainScore = 1000;
    public static final int getGoldScore = 200;
}
