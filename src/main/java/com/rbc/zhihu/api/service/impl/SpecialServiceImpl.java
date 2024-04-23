package com.rbc.zhihu.api.service.impl;


import com.rbc.zhihu.api.entity.Special;
import com.rbc.zhihu.api.mapper.SpecialMapper;
import com.rbc.zhihu.api.service.SpecialService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author DingYihang
 */
@Service
public class SpecialServiceImpl implements SpecialService {
    @Resource
    private SpecialMapper specialMapper;
    @Override
    public List<Special> getAll() {
        List<Special> specials = specialMapper.selectAll();
        specials.forEach(special -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    //时间戳单位为秒，加上000转为毫秒，然后⽣成Date，再格式化
            String format = sdf.format(new Date(Long.parseLong(special.getUpdated() + "000")));
            special.setUpdated(format);
        });
        return specials;
    }
}