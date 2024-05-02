package com.rbc.zhihu.api.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rbc.zhihu.api.entity.DefaultAvatar;
import com.rbc.zhihu.api.entity.User;
import com.rbc.zhihu.api.entity.dto.UserLoginDto;
import com.rbc.zhihu.api.entity.vo.UserVO;
import com.rbc.zhihu.api.mapper.DefaultAvatarMapper;
import com.rbc.zhihu.api.mapper.UserMapper;
import com.rbc.zhihu.api.service.CommonService;
import com.rbc.zhihu.api.service.UserService;
import com.rbc.zhihu.api.utils.JwtUtil;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * @author DingYihang
 */
@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private CommonService commonService;

    @Resource
    private DefaultAvatarMapper defaultAvatarMapper;

    @Override
    public void addUser(User user) {
        userMapper.insert(user);
    }

    @Override
    public Integer deleteUser(Integer id) {
        return userMapper.deleteById(id);
    }

    @Override
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    @Override
    public User queryUser(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public String updateUserAvatar(Integer userId, MultipartFile file) {
        try{
            // 对上传的文件重命名
            String url = commonService.upload(file);
            userMapper.updateById(new User().setId(userId).setAvatarUrl(url));
            return url;
        } catch (Exception e){
            throw new RuntimeException("上传头像时失败");
        }

    }

    @Override
    public JSONObject loginByPhone(String phone) {
        JSONObject jsonObject = new JSONObject();
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>().eq(User::getPhone, phone));
        if (user == null){
            try {
                jsonObject.put("isRegister", false);
                User newUser = new User();
                newUser.setPhone(phone);
                newUser.setUsername("用户"+phone);
                Random random = new Random();
                DefaultAvatar defaultAvatar = defaultAvatarMapper.selectById(random.nextInt(3) + 1);
                newUser.setAvatarUrl(defaultAvatar.getUrl());
                newUser.setDeleted(0);
                userMapper.insert(newUser);
                jsonObject.put("user",newUser);
            } catch (Exception e) {
                throw new RuntimeException("创建新用户时失败");
            }
        }else{
            jsonObject.put("isRegister", true);
            jsonObject.put("user",user);
        }
        return jsonObject;
    }

    /**
     * 用户登录
     * @param userLoginDto 用户登录信息
     * @return jsonObject 返回用户信息
     */
    @Override
    public JSONObject login(UserLoginDto userLoginDto) {
        if (userLoginDto.getId() == null || userLoginDto.getPassword() == null) {
            throw new RuntimeException("用户名或密码不能为空");
        }
        try {
            User getUser = this.queryUser(userLoginDto.getId());
            if(!Objects.equals(userLoginDto.getPassword(), getUser.getPassword())) {
                throw new RuntimeException("登录失败，检查登录账号或密码");
            }
            // 查询成功后，返回查询到的用户信息
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("user", getUser);
            UserVO user = new UserVO();
            BeanUtils.copyProperties(getUser, user);
            jsonObject.put("token", JwtUtil.createToken(getUser.getId(), getUser.getUsername()));
            return jsonObject;

        } catch (Exception e) {
            // 返回查询用户失败的错误信息
            throw new RuntimeException("查询用户失败");
        }

    }

    /**
     * 修改密码
     * @param userId 用户id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return ResponseResult 返回体
     */
    @Override
    public boolean updatePassword(String userId, String oldPassword, String newPassword) {
        User getUser = userMapper.selectById(userId);
        if(!Objects.equals(oldPassword, getUser.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }else{
            getUser.setPassword(newPassword);
            userMapper.updateById(getUser);
            return true;
        }
    }
}

