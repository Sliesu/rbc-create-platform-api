package com.rbc.zhihu.api.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 默认头像实体类
 * @author DingYihang
 */
@Data
@Accessors(chain = true)
@TableName("default_avatar")
public class DefaultAvatar implements Serializable {

    /**
     * 序列化版本号
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 主键，自增
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 头像名称
     */
    @TableField("name")
    private String name;

    /**
     * 头像地址
     */
    @TableField("url")
    private String url;

    /**
     * 头像类型
     */
    @TableField("type")
    private String type;

    /**
     * 头像大小
     */
    @TableField("size")
    private String size;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 逻辑删除标志
     */
    @TableField("deleted")
    private Integer deleted;
}
