package com.rbc.zhihu.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.rbc.zhihu.api.entity.Image;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DingYihang
 */
public interface ImageService extends IService<Image> {
    String uploadImage(MultipartFile file);
    boolean uploadImageAndSaveInfo(String userId, MultipartFile file, String title, String info);
}