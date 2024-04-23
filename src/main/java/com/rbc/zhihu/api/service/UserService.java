package com.rbc.zhihu.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.rbc.zhihu.api.entity.User;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DingYihang
 */
public interface UserService {

    public void addUser(User user);

    public Integer deleteUser(Integer id);

    public void updateUser(User user);

    public User queryUser(Integer id);

    String updateUserAvatar(Integer userId, MultipartFile file);

    public JSONObject loginByPhone(String phone);

}
