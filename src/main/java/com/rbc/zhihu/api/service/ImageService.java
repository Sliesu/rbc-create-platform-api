package com.rbc.zhihu.api.service;


import com.rbc.zhihu.api.entity.Image;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DingYihang
 */
public interface ImageService {
    String uploadImage(MultipartFile file);

}