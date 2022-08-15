package com.zx.mario.test;

import com.zx.mario.service.MessageService;

import java.util.Arrays;

/**
 * @author 挚爱之夕
 * @version 1.0
 * @implSpec com.zx.mario.test
 * @since 2022 - 07 - 17 - 14:37
 */
public class MessageDaoTest {
    public static void main(String[] args) {
        String[] result = MessageService.getMenuText(MessageService.ENGLISH);
        System.out.println(Arrays.toString(result));
    }
}
