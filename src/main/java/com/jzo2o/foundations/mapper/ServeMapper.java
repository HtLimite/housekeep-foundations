package com.jzo2o.foundations.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import feign.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author itcast
 * @since 2023-07-03
 */
public interface ServeMapper extends BaseMapper<Serve> {

    /**
     * 根据区域id查询服务列表
     * @param regionId 区域id
     * @return
     */
    List<ServeResDTO> queryServeListByRegionId(@Param("regionId") Long regionId);

    /**
     * 首页服务列表
     * @param regionId
     * @return
     */
    List<ServeCategoryResDTO>  findServeIconCategoryByRegionId(@Param("regionId") Long regionId);

    /**
     * 根据区域id查询服务类型列表
     * @param regionId
     * @return
     */

    List<ServeAggregationTypeSimpleResDTO> findServeTypeListByRegionId(@Param("regionId") Long regionId);


    /**
     * 根据区域id查询热门服务列表
     * @param regionId
     * @return
     */
    List<ServeAggregationSimpleResDTO> findHotServeListByRegionId(@Param("regionId") Long regionId);

}
