package com.rbc.zhihu.api.mapper;

import com.rbc.zhihu.api.entity.Special;

import java.util.List;

/**
 * @author DingYihang
 */
public interface SpecialMapper {
    List<Special> selectAll();
}