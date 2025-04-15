package com.jzo2o.foundations.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;

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
     * @Description 区域服务分页查询
     * @Date 2025/4/15 14:48
     * @Param [servePageQueryReqDTO]
     * @return java.util.List<com.jzo2o.foundations.model.dto.response.ServeResDTO>
     **/
    PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO);
}
