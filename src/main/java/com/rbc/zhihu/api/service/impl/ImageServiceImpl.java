package com.rbc.zhihu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rbc.zhihu.api.common.ResponseResult;
import com.rbc.zhihu.api.config.OssConfig;
import com.rbc.zhihu.api.entity.Image;
import com.rbc.zhihu.api.mapper.ImageMapper;
import com.rbc.zhihu.api.service.CommonService;
import com.rbc.zhihu.api.service.ImageService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * @author DingYihang
 */
@Slf4j
@Service
public class ImageServiceImpl  extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Resource
    private CommonService commonService;

    @Resource
    private ImageMapper imageMapper;

    /**
     * 上传图片
     * @param file   MultipartFile 对象，包含要上传的文件
     * @return String 上传成功后的图片 URL
     * @throws RuntimeException 上传图片失败时抛出异常
     */
    @Override
    public String uploadImage(MultipartFile file) {
        try {
            // 生成唯一文件名
            String oldFileName = file.getOriginalFilename();
            assert oldFileName != null;
            int index = oldFileName.lastIndexOf(".");
            String suffixName = oldFileName.substring(index);
            String newFileName = UUID.randomUUID().toString() + suffixName;
            // 将文件上传到存储位置
            String url = commonService.upload(file);

            return url;
        } catch (Exception e) {
            throw new RuntimeException("上传图片失败");
        }
    }

    /**
     * 用户上传作品
     * @param userId 用户id
     * @param file 作品文件
     * @param title 作品标题
     * @param info 作品信息
     * @return
     */
    @Override
    public boolean uploadImageAndSaveInfo(String userId, MultipartFile file, String title, String info) {
        // 上传作品文件
        String imageUrl = this.uploadImage(file);

        // 构建作品信息对象
        Image image = new Image()
                .setOriginalName(file.getOriginalFilename())
                .setUserId(Integer.parseInt(userId))
                .setSize((int) file.getSize())
                .setUrl(imageUrl)
                .setTitle(title)
                .setType(file.getContentType())
                .setInfo(info);

        return this.save(image);
    }
}