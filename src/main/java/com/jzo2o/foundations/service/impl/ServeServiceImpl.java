package com.jzo2o.foundations.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务表 服务实现类
 * </p>
 *
 * @author Lim
 * @since 2025-04-15
 */
@Service
public class ServeServiceImpl extends ServiceImpl<ServeMapper, Serve> implements IServeService {
    
    
    /**
     * @Author Lim
     * @Description //TODO
     * @Date 2025/4/15 14:54
     * @Param [servePageQueryReqDTO]
     * @return java.util.List<com.jzo2o.foundations.model.dto.response.ServeResDTO>
     **/
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO){
        return PageHelperUtils.selectPage(servePageQueryReqDTO,
                () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));

    }


}
