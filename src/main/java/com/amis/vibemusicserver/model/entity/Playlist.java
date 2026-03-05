package com.amis.vibemusicserver.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author : KwokChichung
 * @description : 歌单实体类
 * @createDate : 2026/1/3 22:48
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("tb_playlist")
public class Playlist implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 歌单 id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 歌单标题
     */
    @TableField("title")
    private String title;

    /**
     * 歌单封面 url
     */
    @TableField("cover_url")
    private String coverUrl;

    /**
     * 歌单简介
     */
    @TableField("introduction")
    private String introduction;

    /**
     * 歌单风格
     */
    @TableField("style")
    private String style;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private LocalDateTime updateTime;

}
