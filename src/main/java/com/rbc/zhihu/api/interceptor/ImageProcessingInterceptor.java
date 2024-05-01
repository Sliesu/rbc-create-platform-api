package com.rbc.zhihu.api.interceptor;

import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONObject;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.rbc.zhihu.api.utils.JwtUtil;
import com.rbc.zhihu.api.utils.MyFileUtil;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * @author DingYihang
 */
@Slf4j
@Component
public class ImageProcessingInterceptor implements HandlerInterceptor {

    /**
     * 在处理请求之前执行预处理操作。
     *
     * @param request  HTTP请求对象
     * @param response HTTP响应对象
     * @param handler  处理程序对象
     * @return 如果预处理成功并且应该继续处理请求，则返回true；否则返回false
     * @throws Exception 如果预处理过程中发生异常
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查请求是否包含图像上传路径
        if (request.getRequestURI().contains("/images/upload")) {
            // 从请求头中获取授权Token
            String token = request.getHeader("Authorization");
            // 验证Token是否有效
            if (JwtUtil.validate(token)) {
                // 从Token中获取用户ID和昵称
                JSONObject tokenPayload = JwtUtil.getPayload(token);
                String userId = tokenPayload.getStr("userId");
                String nickname = tokenPayload.getStr("nickname");
                // 解析HTTP请求中的MultipartFile
                MultipartFile file = new StandardServletMultipartResolver().resolveMultipart(request).getFile("file");

                // 如果文件不为空
                if (file != null && !file.isEmpty()) {
                    // 将MultipartFile转换为File对象
                    File imageFile = MyFileUtil.multipartFileToFile(file);
                    // 设置水印文本
                    String watermarkText = userId + " " + nickname;
                    // 创建临时文件目录
                    File tempDir = new File(System.getProperty("java.io.tmpdir"));
                    // 创建临时文件
                    File tempFile = new File(tempDir, "watermarked_" + imageFile.getName());

                    // 添加水印
                    ImgUtil.pressText(
                            imageFile,
                            tempFile,
                            watermarkText,
                            Color.WHITE, // 文字颜色
                            new Font("黑体", Font.BOLD, 50), // 字体
                            0, // X坐标修正值
                            0, // Y坐标修正值
                            0.8f // 透明度
                    );

                    // 在请求中添加一个属性来引用这个临时文件的位置
                    request.setAttribute("watermarkedImageTempPath", tempFile.getAbsolutePath());
                }

                // 图片加水印后，放行
                return true;
            } else {
                log.warn("Token验证失败");
                setUnauthorizedResponse(response, "Token验证失败");
                return false;
            }
        }
        // 如果不是上传图片的请求，直接放行
        return true;
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
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("进入后置处理");
    }
}
