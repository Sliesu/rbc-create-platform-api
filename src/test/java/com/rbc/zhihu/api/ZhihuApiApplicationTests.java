package com.rbc.zhihu.api;

import com.rbc.zhihu.api.entity.Special;
import com.rbc.zhihu.api.mapper.SpecialMapper;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ZhihuApiApplicationTests {
    @Resource
    private SpecialMapper specialMapper;
    @Test
    void selectAll() {
        List<Special> specials = specialMapper.selectAll();
        specials.forEach(System.out::println);
    }
}
