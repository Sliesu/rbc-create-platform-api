package com.rbc.zhihu.api.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.rbc.zhihu.api.entity.DefaultAvatar;
import com.rbc.zhihu.api.entity.User;
import com.rbc.zhihu.api.mapper.DefaultAvatarMapper;
import com.rbc.zhihu.api.mapper.UserMapper;
import com.rbc.zhihu.api.service.UserService;
import com.rbc.zhihu.api.utils.OssTemplate;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private OssTemplate ossTemplate;

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
            String oldFileName = file.getOriginalFilename();
            assert oldFileName != null;
            int index = oldFileName.lastIndexOf(".");
            String suffixName = oldFileName.substring(index);
            String newFileName = UUID.randomUUID() + suffixName;
            String url = ossTemplate.ossUpload(file,"images/avatar/"+newFileName);
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
}

