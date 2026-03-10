package com.amis.vibemusicserver.controller;

import com.amis.vibemusicserver.model.dto.BannerDTO;
import com.amis.vibemusicserver.model.entity.Banner;
import com.amis.vibemusicserver.model.vo.BannerVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.result.Result;
import com.amis.vibemusicserver.service.IBannerService;
import com.amis.vibemusicserver.service.MinioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author : AmisKwok
 * @description : 轮播图控制器
 * @createDate : 2026/3/11
 */
@RestController
@RequestMapping("/banner")
@CacheConfig(cacheNames = "bannerCache")
public class BannerController {

    @Autowired
    private IBannerService bannerService;
    @Autowired
    private MinioService minioService;

    /**
     * 获取轮播图列表
     *
     * @return 轮播图列表
     */
    @PostMapping("/admin/getAllBanners")
    @Cacheable(key = "'bannerList' + #bannerDTO.pageNum + #bannerDTO.pageSize + #bannerDTO.bannerStatus")
    public Result<PageResult<Banner>> getAllBanners(@RequestBody BannerDTO bannerDTO) {
        return Result.success(bannerService.getAllBanners(bannerDTO));
    }

    /**
     * 添加轮播图
     *
     * @param banner 轮播图
     * @return 结果
     */
    @PostMapping("/admin/addBanner")
    @CacheEvict(allEntries = true)
    public Result addBanner(@RequestParam("banner") MultipartFile banner) {
        String bannerUrl = minioService.uploadFile(banner, "banners");
        bannerService.addBanner(bannerUrl);
        return Result.success();
    }

    /**
     * 更新轮播图
     *
     * @param banner 轮播图
     * @return 结果
     */
    @PatchMapping("/admin/updateBanner/{id}")
    @CacheEvict(allEntries = true)
    public Result updateBanner(@PathVariable("id") Long bannerId, @RequestParam("banner") MultipartFile banner) {
        String bannerUrl = minioService.uploadFile(banner, "banners");
        bannerService.updateBanner(bannerId, bannerUrl);
        return Result.success();
    }

    /**
     * 更新轮播图状态
     *
     * @param bannerStatus 轮播图状态
     * @return 结果
     */
    @PatchMapping("/admin/updateBannerStatus/{id}")
    @CacheEvict(allEntries = true)
    public Result updateBannerStatus(@PathVariable("id") Long bannerId, @RequestParam("status") Integer bannerStatus) {
        bannerService.updateBannerStatus(bannerId, bannerStatus);
        return Result.success();
    }

    /**
     * 删除轮播图
     *
     * @param bannerId 轮播图id
     * @return 结果
     */
    @DeleteMapping("/admin/deleteBanner/{id}")
    @CacheEvict(allEntries = true)
    public Result deleteBanner(@PathVariable("id") Long bannerId) {
        bannerService.deleteBanner(bannerId);
        return Result.success();
    }

    /**
     * 批量删除轮播图
     *
     * @param bannerIds 轮播图id列表
     * @return 结果
     */
    @DeleteMapping("/admin/deleteBanners")
    @CacheEvict(allEntries = true)
    public Result deleteBanners(@RequestBody List<Long> bannerIds) {
        bannerService.deleteBanners(bannerIds);
        return Result.success();
    }

    /**
     * 获取轮播图列表（用户端）
     *
     * @return 轮播图列表
     */
    @GetMapping("/getBannerList")
    @Cacheable(key = "'bannerList'")
    public Result<List<BannerVO>> getBannerList() {
        return Result.success(bannerService.getBannerList());
    }
}

