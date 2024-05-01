package com.rbc.zhihu.api.utils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.json.JSONObject;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.RegisteredPayload;
import com.rbc.zhihu.api.constant.Constant;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

/**
 * @author mqxu
 * @date 2024/4/28
 * @description JwtUtil
 **/
@Slf4j
@Component
public class JwtUtil {


    /**
     * 盐值很重要，不能泄漏，且每个项目都应该不一样，可以放到配置文件中
     */
    private static final String KEY = "rbc-create-platform";

    /**
     * 创建 JWT Token，并将用户ID和昵称等信息加入到 Token 的自定义载荷中。
     * @param userId   用户ID
     * @param nickname 用户昵称
     * @return String 返回生成的 JWT Token
     */
    public static String createToken(Integer userId, String nickname) {
        DateTime now = DateTime.now();
        // 超时时间
        DateTime expTime = now.offsetNew(DateField.HOUR, 48);
        Map<String, Object> payload = new HashMap<>();
        // 签发时间
        payload.put(RegisteredPayload.ISSUED_AT, now);
        // 过期时间
        payload.put(RegisteredPayload.EXPIRES_AT, expTime);
        // 生效时间
        payload.put(RegisteredPayload.NOT_BEFORE, now);
        // 自定义载荷
        payload.put(Constant.USER_ID, userId);
        payload.put(Constant.NICKNAME, nickname);
        String token = JWTUtil.createToken(payload, KEY.getBytes());
        log.info("生成 JWT token：{}", token);
        return token;
    }

    /**
     * 校验 JWT Token 的有效性。
     *
     * @param token JWT Token
     * @return boolean 返回 JWT Token 是否有效，true 表示有效，false 表示无效
     */
    public static boolean validate(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token).setKey(KEY.getBytes());
            boolean validate = jwt.validate(0);
            log.info("JWT token 校验结果：{}", validate);
            return validate;
        } catch (Exception e) {
            log.error("JWT token 校验异常：{}", e.getMessage());
            return false;
        }
    }
    /**
     * 获取 JWT Token 的自定义载荷信息，去除了默认的签发时间、过期时间和生效时间。
     *
     * @param token JWT Token
     * @return JSONObject 返回 JWT Token 的自定义载荷信息
     */
    public static JSONObject getPayload(String token) {
        JWT jwt = JWTUtil.parseToken(token).setKey(KEY.getBytes());
        JSONObject payloads = jwt.getPayloads();
        payloads.remove(RegisteredPayload.ISSUED_AT);
        payloads.remove(RegisteredPayload.EXPIRES_AT);
        payloads.remove(RegisteredPayload.NOT_BEFORE);
        log.info("payloads: {}", payloads);
        return payloads;
    }
    /**
     * 从 HTTP 请求中获取 JWT Token。
     * 支持从请求头或请求参数中获取，优先从请求头获取。
     *
     * @param request HTTP 请求对象
     * @return String 返回获取到的 JWT Token，如果未获取到则返回 null
     */
    public static String getAccessToken(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (StringUtils.isBlank(accessToken)) {
            accessToken = request.getParameter("accessToken");
        }
        return accessToken;
    }

    public static void main(String[] args) {
        String token = createToken(10001,"zhangsan");
        //boolean validate = validate("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYmYiOjE3MTQzNzY5MzAsIm5pY2tuYW1lIjoiemhhbmdzYW4iLCJleHAiOjE3MTQ1NDk3MzAsImlhdCI6MTcxNDM3NjkzMCwidXNlcklkIjoxMDAwMX0.h0WKQrEsRavLYM6g0T-94Wg-epYHb9kkJUZWFijkipg");
        //System.out.println(validate);
        //System.out.println(token);
        //System.out.println(token);
        //boolean validate = validate(token);
        //System.out.println(validate);
        //JSONObject payload = getPayload(token);
        //System.out.println(payload);
    }
}