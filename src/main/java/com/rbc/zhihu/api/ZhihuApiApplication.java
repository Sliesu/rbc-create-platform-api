package com.rbc.zhihu.api;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author DingYihang
 */
@MapperScan("com.rbc.zhihu.api.mapper")  // 扫描 Mapper 接口
@SpringBootApplication
public class ZhihuApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZhihuApiApplication.class, args);
    }

}
