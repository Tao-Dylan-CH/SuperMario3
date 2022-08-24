package com.zx.mario.domain;

import com.zx.mario.manager.Application;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec 游戏中显示对话框
 * @since 2022 - 08 - 22 - 18:16
 */
public class GameDialog extends GameObject{
    private int duration;
    private boolean isValid = true;
    private static int normalDuration = 30;
    public GameDialog(String filePrefix) {
        super(0, 0, 1, 0, filePrefix, 0);
    }
    public GameDialog(String filePrefix, int duration){
        this(filePrefix);
        this.duration = duration;
    }
    public static GameDialog newInvincibleDialogInstanceForMario(){
        return new GameDialog("dialog_invincible", Application.durationOfStar);
    }
    public static GameDialog newDieDialogInstanceForMario(){
        return new GameDialog("dialog_die", 50);
    }
    public static GameDialog newAttackDialogInstanceForMario(){
        return new GameDialog("dialog_attack", normalDuration);
    }

    public static GameDialog newBeAttackDialogInstanceForMario(){
        return new GameDialog("dialog_beAttacked", Application.invincibleTime);
    }

    public static GameDialog newBeBiggerDialogInstanceForMario(){
        return new GameDialog("dialog_bigger", normalDuration);
    }

    public static GameDialog newBeBiggestDialogInstanceForMario(){
        return new GameDialog("dialog_biggest", normalDuration * 2);
    }

    public static GameDialog newWinDialogInstanceForMario(){
        return new GameDialog("dialog_win", 50);
    }
    public static GameDialog newTreadDialogInstanceForMario(){
        return new GameDialog("dialog_tread", normalDuration);
    }
    public boolean checkValid(){
        duration--;
        if(duration < 0)
            isValid = false;
        return isValid;
    }
}
