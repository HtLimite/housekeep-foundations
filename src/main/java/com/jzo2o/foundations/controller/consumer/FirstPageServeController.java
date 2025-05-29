package com.jzo2o.foundations.controller.consumer;

import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.service.IHomeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName FirstPageServeController
 * @Description 门户查询缓存类接口
 * @Author Lim
 * @Data 2025/5/28 14:38
 * @Version 1.0
 */
@RestController("consumerServeController")
@RequestMapping("/customer/serve")
@Api(tags = "用户端-首页查询缓存类接口")
public class FirstPageServeController {

    @Resource
    private IHomeService homeService;

    @GetMapping("/firstPageServeList")
    @ApiOperation("查询首页服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeCategoryResDTO> serveCategory(@RequestParam("regionId") Long regionId) {
        return homeService.queryServeIconCategoryByRegionId(regionId);
    }

}
