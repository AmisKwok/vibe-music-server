package com.amis.vibemusicserver.mapper;

import com.amis.vibemusicserver.model.entity.Genre;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GenreMapper extends BaseMapper<Genre> {
}
