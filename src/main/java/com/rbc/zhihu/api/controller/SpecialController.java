package com.rbc.zhihu.api.controller;

import com.rbc.zhihu.api.common.ResponseResult;
import com.rbc.zhihu.api.entity.Special;
import com.rbc.zhihu.api.service.SpecialService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author DingYihang
 */
@RestController
@RequestMapping("/api/v1/special")
public class SpecialController {
    @Resource
    private SpecialService specialService;
    @GetMapping("all")
    public ResponseResult getAll() {
        List<Special> all = specialService.getAll();
        return ResponseResult.builder()
                .code(200)
                .msg(" 数据获取成功 ")
                .data(all)
                .build();
    }
}