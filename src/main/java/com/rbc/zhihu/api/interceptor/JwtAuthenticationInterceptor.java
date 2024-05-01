package com.rbc.zhihu.api.interceptor;

import cn.hutool.json.JSONObject;
import com.rbc.zhihu.api.utils.QRCodeUtil;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import com.rbc.zhihu.api.utils.JwtUtil;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author DingYihang
 */
@Slf4j
@Component
public class JwtAuthenticationInterceptor implements HandlerInterceptor {

    /**
     * 在请求到达处理器之前验证请求头中的token是否有效
     *
     * @param request  请求
     * @param response 响应
     * @param handler  处理器
     * @return 如果token有效，返回true；否则返回false
     * @throws Exception 异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        if (requestURI.contains("swagger") || requestURI.contains("knife4j")) {
            return true;
        }

        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token)) {
            if (JwtUtil.validate(token)) {
                log.info("Token验证通过");
                return true;
            } else {
                log.warn("Token验证失败");
                setUnauthorizedResponse(response, "无效的Token或Token过期");
                return false;
            }
        } else {
            log.warn("请求未包含Token");
            setUnauthorizedResponse(response, "请求未包含Token，访问被拒绝");
            return false;
        }
    }

    private void setUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(createErrorResponse(message).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }

    private String createErrorResponse(String message) {
        return String.format("{\"code\": 401,\"success\": false, \"message\": \"%s\"}", message);
    }
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws IOException {

    }
}

