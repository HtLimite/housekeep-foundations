package com.jzo2o.foundations.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jzo2o.common.expcetions.CommonException;
import com.jzo2o.common.expcetions.ForbiddenOperationException;
import com.jzo2o.common.model.PageResult;
import com.jzo2o.common.utils.BeanUtils;
import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.RegionMapper;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.request.ServePageQueryReqDTO;
import com.jzo2o.foundations.model.dto.request.ServeUpsertReqDTO;
import com.jzo2o.foundations.model.dto.response.ServeResDTO;
import com.jzo2o.foundations.service.IServeService;
import com.jzo2o.mysql.utils.PageHelperUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

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


    @Resource
    private ServeItemMapper serveItemMapper;

    @Resource
    private RegionMapper regionMapper;
    private Serve serve;

    // 缓存key，支持SpEL表达式(spring boot 提供 ，支持 取对象属性及执行属性方法)，上述代码表示取参数id的值作为key
    // 最终缓存key为：缓存名称+“::”+key
    @Cacheable(value = RedisConstants.CacheName.SERVE, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    @Override
    public Serve queryServeByIdCache(Long id) {
        Serve serve = baseMapper.selectById(id);
        return serve;
    }

    /**
     * @return java.util.List<com.jzo2o.foundations.model.dto.response.ServeResDTO>
     * @Author Lim
     * @Description //TODO
     * @Date 2025/4/15 14:54
     * @Param [servePageQueryReqDTO]
     **/
    @Override
    public PageResult<ServeResDTO> page(ServePageQueryReqDTO servePageQueryReqDTO){
        return PageHelperUtils.selectPage(servePageQueryReqDTO,
                () -> baseMapper.queryServeListByRegionId(servePageQueryReqDTO.getRegionId()));

    }

    @Override
    public void batchAdd(List<ServeUpsertReqDTO> serveUpsertReqDTOS) {
        for (ServeUpsertReqDTO serveUpsertReqDTO : serveUpsertReqDTOS) {
            // 合法校验
            // 1.serve_item 是否启用
            // 未启用不能添加
            Long serveItemId = serveUpsertReqDTO.getServeItemId();
            ServeItem serveItem = serveItemMapper.selectById(serveItemId);
            if (ObjectUtils.isNull(serveItem) || serveItem.getActiveStatus()!= FoundationStatusEnum.ENABLE.getStatus()) {
                throw new ForbiddenOperationException("服务项不存在或服务项未启用，不允许添加！");
            }

            // 2.同一个区域下 不能添加相同的服务 region_id + serve_item_id
            // sql: select count(*) from serve where region_id = #{regionId} and serve_item_id = #{serveItemId}
            // lambdaQuery() == new LambdaQueryWrapper<Serve>()
            Integer count = lambdaQuery().
                    eq(Serve::getRegionId, serveUpsertReqDTO.getRegionId()).
                    eq(Serve::getServeItemId, serveUpsertReqDTO.getServeItemId()).
                    count();
            if (count > 0) {
                throw new ForbiddenOperationException("该区域下已经添加过该服务，不允许重复添加！");
            }

            // 校验通过 插入表
            // Serve serve = new Serve();
            // serve.setServeItemId(serveUpsertReqDTO.getServeItemId());
            // serve.setRegionId(serveUpsertReqDTO.getRegionId());
            Serve serve = BeanUtils.toBean(serveUpsertReqDTO, Serve.class);
            Region region = regionMapper.selectById(serve.getRegionId());
            String cityCode = region.getCityCode();
            serve.setCityCode(cityCode);

            baseMapper.insert(serve);
        }
    }

    @Override
    public Serve update(Long id, BigDecimal price) {
        boolean update = lambdaUpdate().
                eq(Serve::getId, id).
                set(Serve::getPrice, price).
                update();
        if (!update){
            throw new ForbiddenOperationException("修改服务价格失败！");
        }

        // 查询最新数据返回
        return baseMapper.selectById(id);
    }

    @Override
    public void removeById(Long id) {
        int delete = baseMapper.deleteById(id);
        if (delete == 0) {
            throw new ForbiddenOperationException("删除服务失败！");
        }
    }

    @Override
    public Serve onSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }

        // 业务逻辑校验
        // 1.售卖状态
        if (serve.getSaleStatus() == FoundationStatusEnum.ENABLE.getStatus()) {
            throw new ForbiddenOperationException("草稿或下架状态方可上架！");
        }
        // 2.服务项表的状态，是否启用
        ServeItem serveItem = serveItemMapper.selectById(serve.getServeItemId());
        if (serveItem.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            throw new ForbiddenOperationException("服务项状态未启用！");
        }

        // 校验通过更新
        boolean update = lambdaUpdate().
                eq(Serve::getId, id).
                set(Serve::getSaleStatus, FoundationStatusEnum.ENABLE.getStatus()).
                update();
        if (!update){
            throw new CommonException("上架服务失败！");
        }

        return baseMapper.selectById(id);
    }

    @Override
    public Serve offSale(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }
        if (serve.getSaleStatus() == FoundationStatusEnum.DISABLE.getStatus()){
            throw new ForbiddenOperationException("该服务已下架，不允许重复下架！");
        }
        // 校验通过更新
        boolean update = lambdaUpdate().
                eq(Serve::getId, id).
                set(Serve::getSaleStatus, FoundationStatusEnum.DISABLE.getStatus()).
                update();
        if (!update){
            throw new CommonException("下架服务失败！");
        }
        return baseMapper.selectById(id);
    }

    @Override
    public Serve onHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }
        if (serve.getIsHot() == FoundationStatusEnum.DISABLE.getStatus()){
            throw new ForbiddenOperationException("该服务已设为热门服务，不允许重复设置！");
        }
        boolean update = lambdaUpdate().
                eq(Serve::getId, id).
                set(Serve::getIsHot, FoundationStatusEnum.DISABLE.getStatus()).
                update();
        if (!update){
            throw new CommonException("设置热门服务失败！");
        }
        return baseMapper.selectById(id);
    }

    @Override
    public Serve offHot(Long id) {
        Serve serve = baseMapper.selectById(id);
        if (ObjectUtils.isNull(serve)){
            throw new ForbiddenOperationException("区域服务不存在！");
        }
        if (serve.getIsHot() == FoundationStatusEnum.INIT.getStatus()){
            throw new ForbiddenOperationException("该服务已不是热门服务，不允许重复设置！");
        }
        boolean update = lambdaUpdate().
                eq(Serve::getId, id).
                set(Serve::getIsHot, FoundationStatusEnum.INIT.getStatus()).
                update();
        if (!update){
            throw new CommonException("设置热门服务失败！");
        }
        return baseMapper.selectById(id);    }


}
