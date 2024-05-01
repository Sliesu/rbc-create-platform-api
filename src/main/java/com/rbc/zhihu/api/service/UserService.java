package com.rbc.zhihu.api.service;

import com.alibaba.fastjson2.JSONObject;
import com.rbc.zhihu.api.entity.User;
import com.rbc.zhihu.api.entity.dto.UserLoginDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author DingYihang
 */
public interface UserService {

    void addUser(User user);

    Integer deleteUser(Integer id);

    void updateUser(User user);

    User queryUser(Integer id);

    String updateUserAvatar(Integer userId, MultipartFile file);

    JSONObject loginByPhone(String phone);

    JSONObject login(UserLoginDto userLoginDto);

}
