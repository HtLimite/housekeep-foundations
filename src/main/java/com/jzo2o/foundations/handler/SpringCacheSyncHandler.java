package com.jzo2o.foundations.handler;

import com.jzo2o.api.foundations.dto.response.RegionSimpleResDTO;
import com.jzo2o.foundations.constants.RedisConstants;
import com.jzo2o.foundations.service.IHomeService;
import com.jzo2o.foundations.service.IRegionService;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @ClassName SpringCacheSyncHandler
 * @Description TODO
 * @Author Lim
 * @Data 2025/5/22 14:01
 * @Version 1.0
 */
@Slf4j
@Component
public class SpringCacheSyncHandler {


    @Resource
    private IRegionService regionService;

    @Resource
    private IHomeService homeService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate ;


    // 定时更新已启用区域缓存
    @XxlJob(value = "activeRegionCacheSync") // 指定任务名称
    public void activeRegionCacheSync() {
        log.info(">>>>>>>>开始进行缓存同步，更新已启用区域");
        // 1.删除原来的缓存
        String key = RedisConstants.CacheName.JZ_CACHE + "::ACTIVE_REGIONS";
        redisTemplate.delete(key);
        // 2.查询数据库，更新缓存
        // 查询无缓存，缓存更新---spring cache
        List<RegionSimpleResDTO> regionSimpleResDTOS = regionService.queryActiveRegionListCache();

        regionSimpleResDTOS.forEach(item -> {
            // 删除缓存
            String key1 = RedisConstants.CacheName.SERVE_ICON + "::" + item.getId();
            redisTemplate.delete(key1);
            // 查询无缓存，缓存更新---spring cache
            homeService.queryServeIconCategoryByRegionId(item.getId());

        });

        log.info(">>>>>>>>更新已启用区域完成");
    }

}
