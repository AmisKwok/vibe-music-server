package com.amis.vibemusicserver.service.impl;


import com.amis.vibemusicserver.mapper.PlaylistMapper;
import com.amis.vibemusicserver.model.dto.PlaylistAddDTO;
import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.dto.PlaylistUpdateDTO;
import com.amis.vibemusicserver.model.entity.Playlist;
import com.amis.vibemusicserver.model.vo.PlaylistDetailVO;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.service.IPlaylistService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * @author : KwokChichung
 * @description : 歌单服务实现类
 * <p>
 * app自己有做缓存和数据库保存本地列表，这里提供的是云端播放列表
 * <p/>
 * @createDate : 2026/1/28 19:28
 */
@Service
@CacheConfig(cacheNames = "playlistCache")
public class PlaylistServiceImpl extends ServiceImpl<PlaylistMapper, Playlist> implements IPlaylistService {

    @Autowired
    private PlaylistMapper playlistMapper;


    /**
     * 获取所有歌单
     *
     * @param playlistDTO 歌单查询条件封装类
     * @return 分页查询结果
     */
    @Override
    public PageResult<PlaylistVO> getAllPlaylists(PlaylistDTO playlistDTO) {
        Page<Playlist> page = new Page<>(playlistDTO.getPageNum(), playlistDTO.getPageSize());
        QueryWrapper<Playlist> playlistQueryWrapper = new QueryWrapper<>();

        if (playlistDTO.getTitle() != null) {
            playlistQueryWrapper.like("title", playlistDTO.getTitle());
        }
        if (playlistDTO.getStyle() != null) {
            playlistQueryWrapper.eq("style", playlistDTO.getStyle());
        }

        IPage<Playlist> playlistPage = playlistMapper.selectPage(page, playlistQueryWrapper);

        if (playlistPage.getRecords().isEmpty()) {
            return new PageResult<>(0L, Collections.emptyList());
        }

        // 转换为PlaylistVO
        List<PlaylistVO> playlistVOList = playlistPage.getRecords().stream()
                .map(playlist -> {
                    PlaylistVO playlistVO = new PlaylistVO();
                    BeanUtils.copyProperties(playlist, playlistVO);
                    return playlistVO;
                }).toList();

        return new PageResult<>(playlistPage.getTotal(), playlistVOList);
    }

    /**
     * 获取所有歌单信息
     * @param playlistDTO 歌单查询条件
     * @return
     */
    @Override
    public PageResult<Playlist> getAllPlaylistsInfo(PlaylistDTO playlistDTO) {
        Page<Playlist> page = new Page<>(playlistDTO.getPageNum(), playlistDTO.getPageSize());
        QueryWrapper<Playlist> playlistQueryWrapper = new QueryWrapper<>();

        if (playlistDTO.getTitle() != null) {
            playlistQueryWrapper.like("title", playlistDTO.getTitle());
        }
        if (playlistDTO.getStyle() != null) {
            playlistQueryWrapper.eq("style", playlistDTO.getStyle());
        }

        // 管理员视图：按ID倒序排列
        playlistQueryWrapper.orderByDesc("id");

        IPage<Playlist> playlistPage = playlistMapper.selectPage(page, playlistQueryWrapper);

        if (playlistPage.getRecords().isEmpty()) {
            return new PageResult<>(0L, Collections.emptyList());
        }

        return new PageResult<>(playlistPage.getTotal(), playlistPage.getRecords());
    }

    @Override
    public List<PlaylistVO> getRecommendedPlaylists(HttpServletRequest request) {
        return null;
    }

    @Override
    public PlaylistDetailVO getPlaylistDetail(Long playlistId, HttpServletRequest request) {
        return null;
    }

    @Override
    public Long getAllPlaylistsCount(String style) {
        return null;
    }

    @Override
    public void addPlaylist(PlaylistAddDTO playlistAddDTO) {
        return;
    }

    @Override
    public void updatePlaylist(PlaylistUpdateDTO playlistUpdateDTO) {
        return;
    }

    @Override
    public void updatePlaylistCover(Long playlistId, String coverUrl) {
        return;
    }

    @Override
    public void deletePlaylist(Long playlistId) {
        return;
    }

    @Override
    public void deletePlaylists(List<Long> playlistIds) {
        return;
    }
}

