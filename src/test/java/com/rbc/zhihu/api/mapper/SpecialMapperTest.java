package com.rbc.zhihu.api.mapper;

import com.rbc.zhihu.api.entity.Special;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SpecialMapperTest {
    @Resource
    private SpecialMapper specialMapper;
    @Test
    void selectAll() {
        List<Special> specials = specialMapper.selectAll();
        specials.forEach(System.out::println);
    }
}