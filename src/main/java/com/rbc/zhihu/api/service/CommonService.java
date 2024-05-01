package com.rbc.zhihu.api.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @program: spring-boot-learning
 * @description:
 * @author: ytq
 * @create: 2024-04-29 21:18
 **/
public interface CommonService {
    /**
     * 文件上传
     *
     * @param file 文件
     * @return 上传后的 url
     */
    String upload(MultipartFile file);
}
