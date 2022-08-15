package com.zx.mario.test;

import com.zx.mario.domain.Status;
import com.zx.mario.manager.Application;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec com.zx.mario.test
 * @since 2022 - 07 - 19 - 0:21
 */
public class ApplicationTest {
    public static void main(String[] args) {
        String prefix = Application.getFilePrefixByStatus(Status.s_mario_die);
        System.out.println(prefix);
        System.out.println(Application.getImgSizeByStatus(Status.s_mario_run_R));
    }
}
