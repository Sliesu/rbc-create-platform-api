package com.rbc.zhihu.api.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author DingYihang
 */
@Data
@Accessors(chain = true)
@TableName("image")
public class Image implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**原始图片名*/
    @ApiModelProperty(value = "原始图片名")
    @TableField("original_name")
    private String originalName;

    /**关联用户id*/
    @ApiModelProperty(value = "关联用户id")
    @TableField("user_id")
    private Integer userId;

    /**图片大小（字节B）*/
    @ApiModelProperty(value = "图片大小（字节B）")
    @TableField("size")
    private Integer size;

    /**图片的类型*/
    @ApiModelProperty(value = "图片的类型")
    @TableField("type")
    private Integer type;

    /**图片上传路径*/
    @ApiModelProperty(value = "图片上传路径")
    @TableField("url")
    private String url;

}
