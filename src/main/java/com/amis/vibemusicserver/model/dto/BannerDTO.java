package com.amis.vibemusicserver.model.dto;

import com.amis.vibemusicserver.enumeration.BannerStatusEnum;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : AmisKwok
 * @description : 轮播图查询参数
 * @createDate : 2026/3/11
 */
@Data
public class BannerDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 页码
     */
    @NotNull
    private Integer pageNum;

    /**
     * 每页数量
     */
    @NotNull
    private Integer pageSize;

    /**
     * 轮播图状态：0-启用，1-禁用
     */
    private BannerStatusEnum bannerStatus;

}

