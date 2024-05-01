package com.rbc.zhihu.api.controller;

import com.rbc.zhihu.api.common.ResponseResult;
import com.rbc.zhihu.api.entity.Special;
import com.rbc.zhihu.api.service.SpecialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author DingYihang
 */
@RestController
@Tag(name = "知乎访问数据接口")
@RequestMapping("/api/v1/special")
public class SpecialController {
    @Resource
    private SpecialService specialService;
    @GetMapping("all")
    @Operation(summary = "获取首页信息")
    public ResponseResult getAll() {
        List<Special> all = specialService.getAll();
        return ResponseResult.builder()
                .code(200)
                .msg(" 数据获取成功 ")
                .data(all)
                .build();
    }
}