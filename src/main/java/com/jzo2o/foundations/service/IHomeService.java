package com.jzo2o.foundations.service;

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
}
