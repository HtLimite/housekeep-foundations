package com.jzo2o.foundations.controller.operation;

import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName ServeController
 * @Description 区域服务管理相关接口
 * @Author Lim
 * @Data 2025/4/15 11:30
 * @Version 1.0
 */
@RestController("operationServeController")
@RequestMapping("/operation/serve")
@Api(tags = "运营端 - 区域服务管理相关接口")
public class ServeController {

    @Resource
    private IServeService serveService;


    //GET/foundations/operation/serve/page
    @GetMapping("/page")
    @ApiOperation("区域服务分页查询")
    // key: value 不加注解
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO){
        return serveService.page(servePageQueryReqDTO);
    }

   // POST/foundations/operation/serve/batch
    @PostMapping("/batch")
    @ApiOperation("批量添加区域服务")
    // json @RequestBody 注解
    public void add(@RequestBody List<ServeUpsertReqDTO> serveUpsertReqDTOS){
        serveService.batchAdd(serveUpsertReqDTOS);
    }

    // PUT/foundations/operation/serve/{id}
    @PutMapping("/{id}")
    @ApiOperation("更新区域服务价格")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataType = "Long"),
            @ApiImplicitParam(name = "price", value = "服务价格", required = true, dataType = "BigDecimal")
    })
    // 路径参数 @PathVariable
    public void update(@PathVariable("id") Long id, BigDecimal price) {
        serveService.update(id, price);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除区域服务")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "服务id", required = true, dataType = "Long")
    })
    public void delete(@PathVariable("id") Long id) {
        serveService.removeById(id);
    }

    @PutMapping("/onSale/{id}")
    @ApiOperation("上架区域服务")
    public Serve onSale(@PathVariable("id") Long id) {
        return serveService.onSale(id);
    }

    @PutMapping("/offSale/{id}")
    @ApiOperation("下架区域服务")
    public Serve offSale(@PathVariable("id") Long id) {
        return serveService.offSale(id);
    }

    @PutMapping("onHot/{id}")
    @ApiOperation("设置热门服务")
    public Serve onHot(@PathVariable Long id){
        return serveService.onHot(id);
    }

    @PutMapping("offHot/{id}")
    @ApiOperation("取消设置热门服务")
    public Serve offHot(@PathVariable Long id){
        return serveService.offHot(id);
    }

}
