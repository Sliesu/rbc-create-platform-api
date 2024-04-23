package com.rbc.zhihu.api.Keys;

public class RedisKeys {
    public static String getSmsKey(String phone) {
        return "sms:captcha" + phone;
    }
}
