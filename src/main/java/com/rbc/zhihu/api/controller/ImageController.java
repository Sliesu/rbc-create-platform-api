package com.rbc.zhihu.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rbc.zhihu.api.common.ResponseResult;
import com.rbc.zhihu.api.entity.Image;
import com.rbc.zhihu.api.service.ImageService;
import com.rbc.zhihu.api.utils.MyFileUtil;
import com.rbc.zhihu.api.utils.QRCodeUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * @author DingYihang
 */
@RestController
@Tag(name = "图片接口")
@RequestMapping(value = "/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 图片上传OSS
     * @param file 上传文件
     * @param request 请求
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @Operation(summary = "图片上传")
    public ResponseResult uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        // 获取临时文件的路径
        String tempFilePath = (String) request.getAttribute("watermarkedImageTempPath");

        if(tempFilePath != null && !tempFilePath.isEmpty()) {
            // 用临时文件替代原始文件
            File tempFile = new File(tempFilePath);
            file = MyFileUtil.fileToMultipartFile(tempFile,tempFilePath);

            // 删除临时文件
            tempFile.delete();
        }

        String imageUrl = imageService.uploadImage(file);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("image", imageUrl);
        jsonObject.put("base64Code", QRCodeUtil.crateQRCode(imageUrl, 140, 140));
        return ResponseResult.ok(jsonObject);
    }
}