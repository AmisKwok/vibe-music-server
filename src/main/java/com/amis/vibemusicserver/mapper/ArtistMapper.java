package com.amis.vibemusicserver.mapper;

import com.amis.vibemusicserver.model.entity.Artist;
import com.amis.vibemusicserver.model.vo.ArtistDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author : KwokChichung
 * @description : 歌手 Mapper 接口
 * @createDate : 2026/1/3 22:47
 */
@Mapper
public interface ArtistMapper extends BaseMapper<Artist> {

    // 根据id查询歌手详情
    ArtistDetailVO getArtistDetailById(Long artistId);
}