package com.rbc.zhihu.api;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;


/**
 * @author DingYihang
 */

@Slf4j
@MapperScan("com.rbc.zhihu.api.mapper")  // 扫描 Mapper 接口
@SpringBootApplication
public class ZhihuApiApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ZhihuApiApplication.class);
        Environment env = app.run(args).getEnvironment();
        log.info("启动成功");
        log.info("swagger文档地址：http://localhost:{}/swagger-ui/index.html#/", env.getProperty("server.port"));
        log.info("knife4j文档地址：http://localhost:{}/doc.html#/home", env.getProperty("server.port"));
    }
}
