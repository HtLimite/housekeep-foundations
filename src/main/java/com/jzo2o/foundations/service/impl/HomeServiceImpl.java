package com.jzo2o.foundations.service.impl;

import com.jzo2o.common.utils.ObjectUtils;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.enums.FoundationStatusEnum;
import com.jzo2o.foundations.mapper.ServeItemMapper;
import com.jzo2o.foundations.mapper.ServeMapper;
import com.jzo2o.foundations.model.domain.Region;
import com.jzo2o.foundations.model.domain.Serve;
import com.jzo2o.foundations.model.domain.ServeItem;
import com.jzo2o.foundations.model.dto.response.ServeAggregationSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeAggregationTypeSimpleResDTO;
import com.jzo2o.foundations.model.dto.response.ServeCategoryResDTO;
import com.jzo2o.foundations.model.dto.response.ServeSimpleResDTO;
import com.jzo2o.foundations.service.IHomeService;
import com.jzo2o.foundations.service.IRegionService;
import com.jzo2o.foundations.service.IServeItemService;
import com.jzo2o.foundations.service.IServeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @ClassName HomeServiceImpl
 * @Description 门户查询接口
 * @Author Lim
 * @Data 2025/5/28 16:23
 * @Version 1.0
 */
@Service
@Slf4j
public class HomeServiceImpl implements IHomeService {

    @Resource
    private ServeMapper serveMapper;

    @Resource
    private ServeItemMapper serveItemMapper;

    @Resource
    private IRegionService regionService;

    @Resource
    private IServeService serveService;

    @Resource
    private IServeItemService serveItemService;

    // 返回数据为空 缓存
    // unless true 不缓存
    // @Cacheable(value = "", key = "", cacheManager = "", unless = "#result.size() != 0")
    // 返回数据不为空 缓存
    // @Cacheable(value = "", key = "", cacheManager = "", unless = "#result.size() == 0")
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.SERVE_ICON, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    @Override
    public List<ServeCategoryResDTO> queryServeIconCategoryByRegionId(Long regionId) {
        // 查询区域
        Region region = regionService.getById(regionId);
        if (ObjectUtils.isNull(region) || region.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            return Collections.emptyList();
        }
        // 请求数据库查询服务列表
        List<ServeCategoryResDTO> list = serveMapper.findServeIconCategoryByRegionId(regionId);
        if (ObjectUtils.isNull(list)){
            return Collections.emptyList();
        }
        // 查询数据处理
        // 截取前两个
        // 每一个最多四个
        int endIndex = Math.min(list.size(), 2);
        List<ServeCategoryResDTO> serveCategoryResDTO = new ArrayList<>(list.subList(0, endIndex));
        serveCategoryResDTO.forEach(item -> {
            List<ServeSimpleResDTO> serveResDTOList = item.getServeResDTOList();
            List<ServeSimpleResDTO> serveResDTO = new ArrayList<>(serveResDTOList.subList(0, Math.min(serveResDTOList.size(), 4)));
            item.setServeResDTOList(serveResDTO);

        });
        return serveCategoryResDTO;
    }

    @Override
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.SERVE_TYPE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeAggregationTypeSimpleResDTO> queryServeTypeListByRegionIdCache(Long regionId) {
        // 查询区域
        Region region = regionService.getById(regionId);
        // 判断区域是否启用 || 为空
        if (ObjectUtils.isNull(region) || region.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            return Collections.emptyList();
        }

        // 通过区域id查询服务类型
        List<ServeAggregationTypeSimpleResDTO> list = serveMapper.findServeTypeListByRegionId(regionId);
        if (ObjectUtils.isNull(list)){
            return Collections.emptyList();
        }

        return list;
    }

    @Override
    @Caching(
            cacheable = {
                    //result为null时,属于缓存穿透情况，缓存时间30分钟
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() != 0", cacheManager = RedisConstants.CacheManager.THIRTY_MINUTES),
                    //result不为null时,永久缓存
                    @Cacheable(value = RedisConstants.CacheName.HOT_SERVE, key = "#regionId", unless = "#result.size() == 0", cacheManager = RedisConstants.CacheManager.FOREVER)
            }
    )
    public List<ServeAggregationSimpleResDTO> findHotServeListByRegionIdCache(Long regionId) {
        // 查询区域
        Region region = regionService.getById(regionId);
        // 判断区域是否启用 || 为空
        if (ObjectUtils.isNull(region) || region.getActiveStatus() != FoundationStatusEnum.ENABLE.getStatus()) {
            return Collections.emptyList();
        }

        // 根据城市编码查询热门服务
        List<ServeAggregationSimpleResDTO> hotList = serveMapper.findHotServeListByRegionId(regionId);
        if (ObjectUtils.isNull(hotList)){
            return Collections.emptyList();
        }

        return hotList;
    }

    /**
     * 根据id查询区域服务信息
     *
     * @param id 服务id
     * @return 服务
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.SERVE, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public Serve queryServeByIdCache(Long id) {
        return serveService.getById(id);
    }

    /**
     * 根据id查询服务项
     *
     * @param id 服务项id
     * @return 服务项
     */
    @Override
    @Cacheable(value = RedisConstants.CacheName.SERVE_ITEM, key = "#id", cacheManager = RedisConstants.CacheManager.ONE_DAY)
    public ServeItem queryServeItemByIdCache(Long id) {
        return serveItemService.getById(id);
    }


}
