package com.rbc.zhihu.api.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rbc.zhihu.api.common.ResponseResult;
import com.rbc.zhihu.api.entity.User;
import com.rbc.zhihu.api.entity.dto.UserLoginDto;
import com.rbc.zhihu.api.result.Result;
import com.rbc.zhihu.api.service.AuthService;
import com.rbc.zhihu.api.service.UserService;
import com.rbc.zhihu.api.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

/**
 * @author DingYihang
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "用户接口")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private AuthService authService;

    /**
     * 添加用户
     * @param user 用户信息
     * @return ResponseResult 返回体
     */
    @Operation(summary = "添加用户")
    @PutMapping("/add")
    public ResponseResult addUser(@RequestBody User user) {
        if (user == null) {
            return ResponseResult.error("用户信息不能为空!");
        }
        if(userService.queryUser(user.getId())!=null){
            return ResponseResult.error("用户已存在!");
        }
        try {
            userService.addUser(user);
            // 查询成功后，返回查询到的用户信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", user);
            return ResponseResult.ok(jsonObject);
        } catch (Exception e) {
            // 返回添加用户失败的错误信息
            return ResponseResult.error("添加用户失败!");
        }
    }

    /**
     * 修改用户信息
     * @param user 用户信息
     * @return ResponseResult 返回体
     */
    @Operation(summary = "修改用户基本信息")
    @PostMapping("/update")
    public ResponseResult updateUser(@RequestBody User user) {
        if (user == null) {
            return ResponseResult.error("用户信息不能为空!");
        }
        try {
            userService.updateUser(user);
            // 查询成功后，返回查询到的用户信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", user);
            return ResponseResult.ok(jsonObject);
        } catch (Exception e) {
            // 返回更新用户失败的错误信息
            return ResponseResult.error("更新用户失败!");
        }
    }

    /**
     * 删除用户
     * @param id 用户id
     * @return ResponseResult 返回体
     */
    @Operation(summary = "根据id删除用户")
    @DeleteMapping("/delete/{id}")
    public ResponseResult deleteUser(@PathVariable("id") Integer id) {
        if (id == null) {
            return ResponseResult.error("用户ID不能为空!");
        }
        try {
            if(userService.deleteUser(id) == 1) {
                return ResponseResult.ok();
            }else{
                return ResponseResult.error("用户不存在！");
            }
        } catch (Exception e) {
            // 返回删除用户失败的错误信息
            return ResponseResult.error("删除用户失败!");
        }
    }

    /**
     * 用户登录接口
     * @param userLoginInfo 用户信息
     * @return ResponseResult 返回体
     */
    @Operation(summary = "用户登录")
    @PostMapping("/login")
    public ResponseResult login(@RequestBody UserLoginDto userLoginInfo) {
        JSONObject jsonObject = null;
        try {
            jsonObject = userService.login(userLoginInfo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseResult.ok(jsonObject);
    }

    /**
     * 查询用户信息
     * @param id 用户id
     * @return ResponseResult 返回体
     */
    @Operation(summary = "查询用户信息")
    @GetMapping("/query")
    public ResponseResult query(@RequestParam("id")Integer id) {
        if (id == null) {
            return ResponseResult.error("用户ID不能为空!");

        }
        try {

            // 查询成功后，返回查询到的用户信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", userService.queryUser(id));
            return ResponseResult.ok(jsonObject);
        } catch (Exception e) {
            // 返回查询用户失败的错误信息
            return ResponseResult.error("查询用户失败!");
        }
    }

    /**
     * 修改用户头像
     * @param id 用户ID
     * @param file 用户头像文件
     * @return ResponseResult 返回体
     */
    @Operation(summary = "修改用户头像")
    @PostMapping("/update-avatar")
    public ResponseResult updateUserAvatar(@RequestParam("id") Integer id, @RequestParam("file") MultipartFile file) {
        if (id == null || file == null) {
            return ResponseResult.error("用户ID或头像文件不能为空");
        }
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("avatarUrl", userService.updateUserAvatar(id, file));
            return ResponseResult.ok(jsonObject);
        } catch (Exception e) {
            return ResponseResult.error("修改用户头像失败!");
        }
    }

    /**
     * 手机验证码登录
     * @param phone 手机号
     * @param code 验证码
     * @return ResponseResult 返回体
     */
    @Operation(summary = "手机验证码登录")
    @PostMapping("/login/message")
    public Result<JSONObject> loginByPhone(@RequestParam("phone") String phone, @RequestParam("code") String code) {
        try {
            authService.loginByPhone(phone, code);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return Result.ok(userService.loginByPhone(phone));
    }


    /**
     * 修改密码
     * @param oldPaw 旧密码
     * @param newPsw 新密码
     * @return ResponseResult 返回体
     */
    @Operation(summary = "修改密码")
    @PostMapping("/upload-password")
    public Result<String> loginByPhone(@RequestParam("oldPaw") String oldPaw,@RequestParam("newPsw") String newPsw, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        cn.hutool.json.JSONObject tokenPayload = JwtUtil.getPayload(token);
        String userId = tokenPayload.getStr("userId");
        boolean success = userService.updatePassword(userId, oldPaw,newPsw);
        if(success){
            return Result.ok("密码修改成功");
        } else {
            return Result.error("密码修改失败");
        }
    }
}
