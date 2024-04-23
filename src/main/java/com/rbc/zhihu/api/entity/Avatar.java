package com.rbc.zhihu.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author DingYihang
 */
@Data
@Accessors(chain = true)
@TableName("avatar")
public class Avatar implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 图片上传路径
     */
    @TableField("url")
    private String url;

    /**
     * 用户id
     */
    @TableField("user_id")
    private Integer userId;


    /**
     * 图片大小
     */
    @TableField("size")
    private Integer size;

    /**
     * 图片类型
     */
    @TableField("type")
    private Integer type;

}
