package com.jzo2o.foundations.controller.consumer;

import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.service.IHomeService;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
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

    @Resource
    private IServeService serveService;

    @GetMapping("/firstPageServeList")
    @ApiOperation("查询首页服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeCategoryResDTO> serveCategory(@RequestParam("regionId") Long regionId) {
        return homeService.queryServeIconCategoryByRegionId(regionId);
    }

    @GetMapping("/serveTypeList")
    @ApiOperation("服务分类列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeAggregationTypeSimpleResDTO> serveTypeList(@RequestParam("regionId") Long regionId) {
        return homeService.queryServeTypeListByRegionIdCache(regionId);
    }

    @GetMapping("/hotServeList")
    @ApiOperation("首页热门服务列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "regionId", value = "区域id", required = true, dataTypeClass = Long.class)
    })
    public List<ServeAggregationSimpleResDTO> serveTypeHotList(@RequestParam("regionId") Long regionId) {
        return homeService.findHotServeListByRegionIdCache(regionId);
    }

    @GetMapping("/{id}")
    @ApiOperation("根据id查询服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataTypeClass = Long.class)
    })
    public ServeAggregationSimpleResDTO findById(@NotNull(message = "id不能为空") @PathVariable("id") Long id) {
        return serveService.findDetailById(id);
    }

}
