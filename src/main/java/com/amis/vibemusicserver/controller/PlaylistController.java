package com.amis.vibemusicserver.controller;

import com.amis.vibemusicserver.constant.MessageConstant;
import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.entity.Playlist;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.result.Result;
import com.amis.vibemusicserver.service.IPlaylistService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author : KwokChichung
 * @description : 歌单控制器
 * @createDate : 2026/3/6 6:04
 */
@RestController
@RequestMapping("/playlist")
@CacheConfig(cacheNames = "playlistCache")
public class PlaylistController {

    @Autowired
    private IPlaylistService playlistService;

    /**
     * 获取所有歌单
     *
     * @param playlistDTO playlistDTO
     * @return 歌单列表
     */
    @PostMapping("/getAllPlaylists")
    @Cacheable(key = "#playlistDTO.pageNum + '-' + #playlistDTO.pageSize + '-' + #playlistDTO.title + '-' + #playlistDTO.style")
    public Result<PageResult<PlaylistVO>> getAllPlaylists(@RequestBody @Valid PlaylistDTO playlistDTO) {
        PageResult<PlaylistVO> allPlaylists = playlistService.getAllPlaylists(playlistDTO);

        // 处理查询结果为空的情况
        if (allPlaylists.getTotal() == 0) {
            return Result.success(MessageConstant.DATA_NOT_FOUND, new PageResult<>(0L, null));
        }
        // 返回查询结果
        return Result.success(new PageResult<>(allPlaylists.getTotal(), allPlaylists.getItems()));

    }

    /**
     * 管理员获取所有歌单（按ID倒序）
     *
     * @param playlistDTO playlistDTO
     * @return 歌单列表
     */
    @PostMapping("/getAllPlaylistsInfo")
    @Cacheable(key = "#playlistDTO.pageNum + '-' + #playlistDTO.pageSize + '-' + #playlistDTO.title + '-' + #playlistDTO.style + '-admin'")
    public Result<PageResult<Playlist>> getAllPlaylistsInfo(@RequestBody @Valid PlaylistDTO playlistDTO) {
        PageResult<Playlist> allPlaylists = playlistService.getAllPlaylistsInfo(playlistDTO);

        // 处理查询结果为空的情况
        if (allPlaylists.getTotal() == 0) {
            return Result.success(MessageConstant.DATA_NOT_FOUND, new PageResult<>(0L, null));
        }
        // 返回查询结果
        return Result.success(new PageResult<>(allPlaylists.getTotal(), allPlaylists.getItems()));
    }
}

