package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 服务表 服务类
 * </p>
 *
 * @author Lim
 * @since 2025-04-15
 */
public interface IServeService extends IService<Serve> {

    /**
     * @Author Lim
     * @Description 查询区域服务信息并进行缓存
     * @Date 2025/4/22 14:05
     * @Param [id]
     * @return com.jzo2o.foundations.model.domain.Serve
     **/
    Serve queryServeByIdCache(Long id);

    /**
     * @Author Lim
     * @Description 区域服务分页查询
     * @Date 2025/4/15 14:48
     * @Param [servePageQueryReqDTO]
     * @return java.util.List<com.jzo2o.foundations.model.dto.response.ServeResDTO>
     **/
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);

    /**
     * @Author Lim
     * @Description 区域服务批量新增
     * @Date 2025/4/16 13:57
     * @Param [serveUpsertReqDTO]
     * @return java.lang.Void
     **/
    void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOS);

    /**
     * @Author Lim
     * @Description 服务价格修改
     * @Date 2025/4/16 15:26
     * @Param [id, price]
     * @return com.jzo2o.foundations.model.domain.Serve
     **/
    Serve update(Long id, BigDecimal price);

    /**
     * @Author Lim
     * @Description 删除服务
     * @Date 2025/4/16 16:13
     * @Param [id]
     * @return com.jzo2o.foundations.model.domain.Serve
     **/
    void removeById(Long id);

    /**
     * @Author Lim
     * @Description 上架区域服务
     * @Date 2025/4/16 16:23
     * @Param [id]
     * @return void
     **/
    Serve onSale(Long id);

    Serve offSale(Long id);

    Serve onHot(Long id);

    Serve offHot(Long id);

    ServeAggregationSimpleResDTO findDetailById(Long id);
}
