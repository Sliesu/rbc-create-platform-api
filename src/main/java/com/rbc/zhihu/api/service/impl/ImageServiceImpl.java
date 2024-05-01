package com.rbc.zhihu.api.service.impl;

import com.rbc.zhihu.api.config.OssConfig;
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
public class ImageServiceImpl implements ImageService {

    @Resource
    private CommonService commonService;
    @Override
    public String uploadImage(MultipartFile file) {
        try{
            // 对上传的文件重命名
            String oldFileName = file.getOriginalFilename();
            assert oldFileName != null;
            int index = oldFileName.lastIndexOf(".");
            String suffixName = oldFileName.substring(index);
            String newFileName = UUID.randomUUID() + suffixName;
            return commonService.upload(file);
        } catch (Exception e){
            throw new RuntimeException("上传图片失败");
        }

    }


}