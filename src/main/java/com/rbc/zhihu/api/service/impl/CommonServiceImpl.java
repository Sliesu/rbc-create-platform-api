package com.rbc.zhihu.api.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.rbc.zhihu.api.config.OssConfig;
import com.rbc.zhihu.api.service.CommonService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
//import top.ytq.boot.fi.exception.ServerException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @author DingYihang
 * @program: spring-boot-learning
 * @description:
 * @create: 2024-04-29 21:19
 **/
@Slf4j
@Service
public class CommonServiceImpl implements CommonService {
    @Resource
    private OssConfig ossConfig;
    //允许上传文件（图片）的格式
    private static final String[] IMAGE_TYPE = new String[]{".bmp",".jpg",",jpeg",".gif","png"};

    @Override
    public String upload(MultipartFile file) {
        String returnImgUrl;

        //校验图片格式
        boolean isLegal = false;
        for (String type : IMAGE_TYPE) {
            if (StringUtils.endsWithIgnoreCase(file.getOriginalFilename(),type)) {
                isLegal = true;
                break;
            }
        }
        if (!isLegal) {
            //如果图片格式不合法
            //throw new ServerException("图片格式不正确");
        }
        //获取文件原名称
        String originalFilename = file.getOriginalFilename();
        //获取文件类型
        String fileType = originalFilename.substring(originalFilename.lastIndexOf("."));
        //新文件名称
        String newFileName = UUID.randomUUID().toString() + fileType;
        //构建日期路径, 例如：OSS目标文件夹/2024/04/31 文件名
        String filePath = new SimpleDateFormat("yyyy/MM/dd").format(new Date());
        //文件上传的路径地址
        String uploadUrl = filePath + "/" + newFileName;

        //获取文件输入流
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /**
         * 现在阿里云OSS 默认图片上传ContentType是image/jpeg
         * 也就是说，获取图片链接后，图片是下载链接， 而并非在线浏览链接，
         * 因此，这里在上传的时候要解决ContentType的问题，将其改为image/jpg
         */
        ObjectMetadata meta = new ObjectMetadata();
        meta.setContentType("image/jpg");

        //读取配置文件中的属性
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();
        String bucketName = ossConfig.getBucketName();
        //创建 OssClient
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //文件上传至阿里云OSS
        ossClient.putObject(bucketName, uploadUrl, inputStream, meta);
        //获取文件上传后的图片返回地址
        returnImgUrl = "https://" + bucketName + "." + endpoint + "/" + uploadUrl;

        return returnImgUrl;
    }
}
