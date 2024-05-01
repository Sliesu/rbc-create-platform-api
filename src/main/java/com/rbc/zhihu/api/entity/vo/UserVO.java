package com.rbc.zhihu.api.entity.vo;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Slf4j
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="公文分页信息")
public class UserVO {
    /**用户id*/
    @ApiModelProperty(value = "id")
    private Integer id;

    /**用户姓名*/
    @ApiModelProperty(value = "用户信息")
    private String username;

    /**用户手机号*/
    @ApiModelProperty(value = "用户手机号")
    private String phone;

    /**性别*/
    @ApiModelProperty(value = "性别")
    private String gender;

    /**年龄*/
    @ApiModelProperty(value = "年龄")
    private Integer age;

    /**头像上传的url*/
    @ApiModelProperty(value = "头像上传的URL")
    private String avatarUrl;

}
