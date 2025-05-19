package com.jzo2o.foundations.service;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import javax.annotation.Resource;

@SpringBootTest
@Slf4j
class IServeServiceTest {
    @Resource
    private IServeService serveService;

    // 区域服务查询
    @Test
    public void test_queryServeByIdCache() {
        Serve serve = serveService.queryServeByIdCache(1693543106233835521L);
        Assert.notNull(serve,"查询失败");

    }


    //分页测试
    @Test
    public void test_page(){
        ServePageQueryReqDTO servePageQueryReqDTO = new ServePageQueryReqDTO();
        servePageQueryReqDTO.setRegionId(1677152267410149378L);
        servePageQueryReqDTO.setPageNo(1L);
        servePageQueryReqDTO.setPageSize(3L);
        PageResult<ServeResDTO> page = serveService.page(servePageQueryReqDTO);
        log.info("page : {}", page);
        Assert.notEmpty(page.getList(),"列表为空");
    }
}