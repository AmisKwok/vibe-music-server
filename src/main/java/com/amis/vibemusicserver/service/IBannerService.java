package com.amis.vibemusicserver.service;

import com.amis.vibemusicserver.model.dto.BannerDTO;
import com.amis.vibemusicserver.model.entity.Banner;
import com.amis.vibemusicserver.model.vo.BannerVO;
import com.amis.vibemusicserver.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @author : AmisKwok
 * @description : 轮播图服务接口
 */
public interface IBannerService extends IService<Banner> {

    // 获取轮播图列表
    PageResult<Banner> getAllBanners(BannerDTO bannerDTO);

    // 添加轮播图
    void addBanner(String bannerUrl);

    // 更新轮播图
    void updateBanner(Long bannerId, String bannerUrl);

    // 更新轮播图状态
    void updateBannerStatus(Long bannerId, Integer bannerStatus);

    // 删除轮播图
    void deleteBanner(Long bannerId);

    // 批量删除轮播图
    void deleteBanners(List<Long> bannerIds);

    // 获取轮播图列表（用户端）
    List<BannerVO> getBannerList();
}
