package org.amis.vibemusicserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.amis.vibemusicserver.mapper.ArtistMapper;
import org.amis.vibemusicserver.model.dto.ArtistAddDTO;
import org.amis.vibemusicserver.model.dto.ArtistDTO;
import org.amis.vibemusicserver.model.dto.ArtistUpdateDTO;
import org.amis.vibemusicserver.model.entity.Artist;
import org.amis.vibemusicserver.model.vo.ArtistDetailVO;
import org.amis.vibemusicserver.model.vo.ArtistNameVO;
import org.amis.vibemusicserver.model.vo.ArtistVO;
import org.amis.vibemusicserver.result.PageResult;
import org.amis.vibemusicserver.service.IArtistService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author : KwokChichung
 * @description : 歌手服务实现类，负责处理歌手相关的业务逻辑
 * @createDate : 2026/1/17 11:32
 */
@Service
@CacheConfig(cacheNames = "artistCache")
public class ArtistServiceImpl extends ServiceImpl<ArtistMapper, Artist> implements IArtistService {

    @Override
    public PageResult<ArtistVO> getAllArtists(ArtistDTO artistDTO) {
        return null;
    }

    @Override
    public PageResult<Artist> getAllArtistsAndDetail(ArtistDTO artistDTO) {
        return null;
    }

    @Override
    public List<ArtistNameVO> getAllArtistNames() {
        return null;
    }

    @Override
    public List<ArtistVO> getRandomArtists() {
        return null;
    }

    @Override
    public ArtistDetailVO getArtistDetail(Long artistId, HttpServletRequest request) {
        return null;
    }

    @Override
    public Long getAllArtistsCount(Integer gender, String area) {
        return null;
    }

    @Override
    public void addArtist(ArtistAddDTO artistAddDTO) {
        return;
    }

    @Override
    public void updateArtist(ArtistUpdateDTO artistUpdateDTO) {
        return;
    }

    @Override
    public void updateArtistAvatar(Long artistId, String avatar) {
        return;
    }

    @Override
    public void deleteArtist(Long ArtistId) {
        return;
    }

    @Override
    public void deleteArtists(List<Long> artistIds) {
        return;
    }
}

