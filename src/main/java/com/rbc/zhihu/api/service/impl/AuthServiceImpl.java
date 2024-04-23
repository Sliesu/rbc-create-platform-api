package com.rbc.zhihu.api.service.impl;


import com.rbc.zhihu.api.Keys.RedisKeys;
import com.rbc.zhihu.api.config.RedisCache;
import com.rbc.zhihu.api.exception.ErrorCode;
import com.rbc.zhihu.api.exception.ServerException;
import com.rbc.zhihu.api.mapper.UserMapper;
import com.rbc.zhihu.api.service.AuthService;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.UUID;


/**
 * @author DingYihang
 */
@Service
public class AuthServiceImpl implements AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    @Resource
    private RedisCache redisCache;

    @Resource
    private UserMapper userMapper;

    @Override
    public Boolean loginByPhone(String phone, String code) {
        // 获取验证码的 key
        String smsCacheKey = RedisKeys.getSmsKey(phone);

        // 从 Redis 中获取验证码
        Integer redisCode = (Integer) redisCache.get(smsCacheKey);

        if (ObjectUtils.isEmpty(redisCode)) {
            // 验证码已失效
            throw new ServerException(ErrorCode.SMS_CODE_EXPIRE);
        } else if (!redisCode.toString().equals(code)) {
            // 验证码错误
            throw new ServerException(ErrorCode.SMS_CODE_ERROR);
        }

        // 删除用过的验证码
        redisCache.delete(smsCacheKey);

        // 返回 token，这里用 UUID 模拟，实际上是要根据用户 id，构造一个 JWT Token
        // String token = UUID.randomUUID().toString();
        // logger.info("User with phone {} logged in successfully.", phone);
        return true;
    }
}
