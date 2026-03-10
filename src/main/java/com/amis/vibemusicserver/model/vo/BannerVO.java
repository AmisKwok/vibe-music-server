package com.amis.vibemusicserver.model.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author : AmisKwok
 * @description :
 * @createDate : 2026/3/11
 */
@Data
public class BannerVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 轮播图 id
     */
    private Long bannerId;

    /**
     * 轮播图 url
     */
    private String bannerUrl;

}

