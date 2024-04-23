package com.rbc.zhihu.api.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author DingYihang
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Special {
    private String id;
    private String title;
    private String banner;
    private Long viewCount;
    private Long followersCount;
    private String isFollowing;
    private String updated;
}