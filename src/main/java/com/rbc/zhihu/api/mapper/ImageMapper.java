package com.rbc.zhihu.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rbc.zhihu.api.entity.Image;
import org.apache.ibatis.annotations.Mapper;


/**
 * @author DingYihang
 */
@Mapper
public interface ImageMapper extends BaseMapper<Image> {
    // MyBatis Plus会为你提供CRUD方法，不需要手动添加。
}