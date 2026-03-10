package com.amis.vibemusicserver.service.impl;

import com.amis.vibemusicserver.enumeration.BannerStatusEnum;
import com.amis.vibemusicserver.mapper.BannerMapper;
import com.amis.vibemusicserver.model.dto.BannerDTO;
import com.amis.vibemusicserver.model.entity.Banner;
import com.amis.vibemusicserver.model.vo.BannerVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.service.IBannerService;
import com.amis.vibemusicserver.service.MinioService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : AmisKwok
 * @description : 轮播图服务实现类
 * @createDate : 2026/3/11
 */
@Service
@CacheConfig(cacheNames = "bannerCache")
public class BannerServiceImpl extends ServiceImpl<BannerMapper, Banner> implements IBannerService {

    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private MinioService minioService;

    @Override
    public PageResult<Banner> getAllBanners(BannerDTO bannerDTO) {
        Page<Banner> page = new Page<>(bannerDTO.getPageNum(), bannerDTO.getPageSize());
        QueryWrapper<Banner> queryWrapper = new QueryWrapper<>();
        if (bannerDTO.getBannerStatus() != null) {
            queryWrapper.eq("status", bannerDTO.getBannerStatus().getId());
        }
        queryWrapper.orderByDesc("id");

        IPage<Banner> bannerPage = bannerMapper.selectPage(page, queryWrapper);
        return new PageResult<>(bannerPage.getTotal(), bannerPage.getRecords());
    }

    @Override
    public void addBanner(String bannerUrl) {
        Banner banner = new Banner();
        banner.setBannerUrl(bannerUrl).setBannerStatus(BannerStatusEnum.ENABLE);
        bannerMapper.insert(banner);
    }

    @Override
    public void updateBanner(Long bannerId, String bannerUrl) {
        Banner banner = bannerMapper.selectById(bannerId);
        String oldBannerUrl = banner.getBannerUrl();
        if (oldBannerUrl != null && !oldBannerUrl.isEmpty()) {
            minioService.deleteFile(oldBannerUrl);
        }
        banner.setBannerUrl(bannerUrl);
        bannerMapper.updateById(banner);
    }

    @Override
    public void updateBannerStatus(Long bannerId, Integer bannerStatus) {
        Banner banner = new Banner();
        banner.setBannerId(bannerId).setBannerStatus(
            bannerStatus == 0 ? BannerStatusEnum.ENABLE : BannerStatusEnum.DISABLE
        );
        bannerMapper.updateById(banner);
    }

    @Override
    public void deleteBanner(Long bannerId) {
        Banner banner = bannerMapper.selectById(bannerId);
        String bannerUrl = banner.getBannerUrl();
        if (bannerUrl != null && !bannerUrl.isEmpty()) {
            minioService.deleteFile(bannerUrl);
        }
        bannerMapper.deleteById(bannerId);
    }

    @Override
    public void deleteBanners(List<Long> bannerIds) {
        List<Banner> banners = bannerMapper.selectByIds(bannerIds);
        List<String> bannerUrlList = banners.stream()
                .map(Banner::getBannerUrl)
                .filter(url -> url != null && !url.isEmpty())
                .toList();
        bannerUrlList.forEach(url -> minioService.deleteFile(url));
        bannerMapper.deleteByIds(bannerIds);
    }

    @Override
    public List<BannerVO> getBannerList() {
        List<Banner> banners = bannerMapper.selectList(new QueryWrapper<Banner>()
                .eq("status", BannerStatusEnum.ENABLE.getId())
                .orderByDesc("id")
                .last("limit 9"));

        return banners.stream()
                .map(banner -> {
                    BannerVO bannerVO = new BannerVO();
                    BeanUtils.copyProperties(banner, bannerVO);
                    return bannerVO;
                }).toList();
    }
}

