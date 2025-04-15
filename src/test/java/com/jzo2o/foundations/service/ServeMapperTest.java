package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName ServeMapperTest
 * @Description ServeMapper 单元测试类
 * @Author Lim
 * @Data 2025/4/15 14:28
 * @Version 1.0
 */
@SpringBootTest
@Slf4j
public class ServeMapperTest {

    @Resource
    ServeMapper serveMapper;

    @Test
    public void test_queryServeListByRegionId(){
        List<ServeResDTO> ServeResDTO = serveMapper.queryServeListByRegionId(1686303222843662337L);
        Assert.notEmpty(ServeResDTO, "查询结果为空！");
    }

}
