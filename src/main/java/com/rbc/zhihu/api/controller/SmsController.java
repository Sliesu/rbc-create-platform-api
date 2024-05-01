package com.rbc.zhihu.api.controller;

import com.alibaba.fastjson2.JSONObject;

import com.rbc.zhihu.api.result.Result;
import com.rbc.zhihu.api.service.SmsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "短信服务接口")
@RequestMapping("/api/v1/sms")
public class SmsController {

    @Resource
    private SmsService service;
    @Operation(summary = "发送信息")
    @PostMapping("/send")
    public Result<Object> sendSms(@RequestParam("phone") String phone) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("phone", phone);
        service.sendSms(phone);
        return Result.ok();
    }
}
