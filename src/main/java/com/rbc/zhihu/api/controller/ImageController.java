package com.rbc.zhihu.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rbc.zhihu.api.common.ResponseResult;
import com.rbc.zhihu.api.entity.Image;
import com.rbc.zhihu.api.result.Result;
import com.rbc.zhihu.api.service.ImageService;
import com.rbc.zhihu.api.utils.JwtUtil;
import com.rbc.zhihu.api.utils.MyFileUtil;
import com.rbc.zhihu.api.utils.QRCodeUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

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
    public Result uploadImage(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
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
        return Result.ok(jsonObject);
    }

    /**
     * 用户上传作品
     * @param file 作品文件
     * @param title 作品标题
     * @param info 作品信息
     * @param request 请求对象
     * @return 包含上传成功后的图片 URL 和 HTTP 状态码的响应结果
     * @throws IOException 当文件处理出现问题时抛出异常
     */
    @PostMapping("/upload-image")
    @Operation(summary = "用户上传作品")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file,
                                      @RequestParam("title") String title,
                                      @RequestParam("info") String info,
                                      HttpServletRequest request) throws IOException {
        // 获取用户ID
        String token = request.getHeader("Authorization");
        cn.hutool.json.JSONObject tokenPayload = JwtUtil.getPayload(token);
        String userId = tokenPayload.getStr("userId");
        boolean success = imageService.uploadImageAndSaveInfo(userId,file,title,info);
        if(success){
            return Result.ok("作品上传成功");
        } else {
            return Result.error("作品上传失败");
        }
    }

    /**
     * 分页查询所有图片信息
     *
     * @param pageNo   当前页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @return Result 包含图片信息和 HTTP 状态码的响应结果
     */
    @GetMapping("/all-images")
    @Operation(summary = "分页查询所有图片信息")
    public Result<IPage<Image>> getImageInfo(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                             @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {
        Page<Image> page = new Page<Image>(pageNo, pageSize);
        IPage<Image> images = imageService.page(page);
        return Result.ok(images);
    }


    /**
     * 分页查询当前用户的所有图片信息
     *
     * @param pageNo   当前页码，默认为1
     * @param pageSize 每页大小，默认为10
     * @param request  请求对象
     * @return Result 包含图片信息和 HTTP 状态码的响应结果
     */
    @GetMapping("/my-images")
    @Operation(summary = "分页查询当前用户的所有图片信息")
    public Result<IPage<Image>> getMyImageInfo(@RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                               @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                               HttpServletRequest request) {
        // 获取用户ID
        String token = request.getHeader("Authorization");
        cn.hutool.json.JSONObject tokenPayload = JwtUtil.getPayload(token);
        String userId = tokenPayload.getStr("userId");

        // 构建分页对象
        Page<Image> page = new Page<Image>(pageNo, pageSize);

        // 分页查询当前用户的所有图片信息
        IPage<Image> images = imageService.page(page, new QueryWrapper<Image>().eq("user_id", userId));

        return Result.ok(images);
    }

}