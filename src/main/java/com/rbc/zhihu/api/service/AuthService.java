package com.rbc.zhihu.api.service;

/**
 * @author DingYihang
 */
public interface AuthService {

    /**
     * 通过手机登录
     * @param phone 手机号
     * @param code 验证码
     * @return
     */
    public Boolean loginByPhone(String phone, String code);

}
