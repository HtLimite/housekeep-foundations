package com.jzo2o.foundations.service;

import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;

import java.util.List;

/***
 * @Author Lim
 * @Description 门户查询
 * @Date 2025/5/28 16:21
 * @Param
 * @return
 **/
public interface IHomeService {
    /**
     * @Author Lim
     * @Description 查询区域服务分类
     * @param regionId
     * @return
     */
    List<ServeCategoryResDTO> queryServeIconCategoryByRegionId(Long regionId);

    /**、
     * @Author Lim
     * @Description 查询区域服务分类列表
     * @param regionId
     * @return
     */
    List<ServeAggregationTypeSimpleResDTO> queryServeTypeListByRegionIdCache(Long regionId);

    /**
     * @Author Lim
     * @Description 查询区域服务热门列表
     * @param regionId
     * @return
     */
    List<ServeAggregationSimpleResDTO> findHotServeListByRegionIdCache(Long regionId);

    /**
     * 根据id查询区域服务信息
     *
     * @param id 服务id
     * @return 服务
     */
    Serve queryServeByIdCache(Long id);

    /**
     * 根据id查询服务项
     *
     * @param id 服务项id
     * @return 服务项
     */
    ServeItem queryServeItemByIdCache(Long id);

}
