package com.rbc.zhihu.api.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author DingYihang
 */
@Data
@Slf4j
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="用户登录信息")
public class UserLoginDto {
    /**用户id*/
    @ApiModelProperty(value = "id")
    private Integer id;
    /**用户名*/
    @ApiModelProperty(value = "登录密码")
    private String password;
}
