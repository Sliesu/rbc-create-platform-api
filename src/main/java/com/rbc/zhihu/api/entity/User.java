package com.rbc.zhihu.api.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author DingYihang
 */
@Data
@Accessors(chain = true)
@TableName("user")
@EqualsAndHashCode
public class User implements Serializable {

    /**序列化版本号 */
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty("用户id")
    private Integer id;

    /**用户名*/
    @TableField("username")
    @ExcelProperty("用户名")
    private String username;

    /**密码*/
    @TableField("password")
    @ExcelProperty("密码")
    private String password;

    /**手机号*/
    @TableField("phone")
    @ExcelProperty("手机号")
    private String phone;

    /**性别*/
    @TableField("gender")
    @ExcelProperty("性别")
    private String gender;

    /**年龄*/
    @TableField("age")
    @ExcelProperty("年龄")
    private Integer age;

    /** 创建时间*/
    @ExcelProperty("创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**更新时间*/
    @ExcelProperty("更新时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**头像上传路径*/
    @ExcelProperty("头像上传路径")
    @TableField("avatar_url")
    private String avatarUrl;

    /**1、删除 0、未删除*/
    @ExcelProperty("删除标志")
    @TableField("deleted")
    private Integer deleted;


}
