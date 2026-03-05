package com.amis.vibemusicserver.controller;

import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.dto.SongDTO;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.model.vo.SongVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.result.Result;
import com.amis.vibemusicserver.service.IUserFavoriteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

/**
 * @author : KwokChichung
 * @description : 用户收藏控制器类，负责处理用户收藏相关的请求
 * @createDate : 2026/1/18 10:28
 */
@RestController
@RequestMapping("/favorite")
public class UserFavoriteController {

    @Autowired
    private IUserFavoriteService userFavoriteService;

    /**
     * 获取用户收藏的歌曲列表
     *
     * @return 用户收藏的歌曲列表
     */
    @PostMapping("/getFavoriteSongs")
    @Cacheable(cacheNames = "userFavoriteCache", key = "#songDTO.pageNum + '-' + #songDTO.pageSize + '-' + #songDTO.songName + '-' + #songDTO.artistName + '-' + #songDTO.album")
    public Result<PageResult<SongVO>> getUserFavoriteSongs(@RequestBody @Valid SongDTO songDTO) {
        PageResult<SongVO> pageResult = userFavoriteService.getUserFavoriteSongs(songDTO);
        return Result.success(pageResult);
    }

    /**
     * 收藏歌曲
     *
     * @param songId 歌曲id
     * @return 收藏结果
     */
    @PostMapping("/collectSong")
    public Result collectSong(@RequestParam Long songId) {
        userFavoriteService.collectSong(songId);
        return Result.success();
    }

    /**
     * 取消收藏歌曲
     *
     * @param songId 歌曲id
     * @return 取消收藏结果
     */
    @DeleteMapping("/cancelCollectSong")
    public Result cancelCollectSong(@RequestParam Long songId) {
        userFavoriteService.cancelCollectSong(songId);
        return Result.success();
    }

    /**
     * 获取用户收藏的歌单列表
     *
     * @return 用户收藏的歌单列表
     */
    @PostMapping("/getFavoritePlaylists")
    @Cacheable(cacheNames = "userFavoriteCache", key = "#playlistDTO.pageNum + '-' + #playlistDTO.pageSize + '-' + #playlistDTO.title + '-' + #playlistDTO.style")
    public Result<PageResult<PlaylistVO>> getFavoritePlaylists(@RequestBody @Valid PlaylistDTO playlistDTO) {
        PageResult<PlaylistVO> pageResult = userFavoriteService.getUserFavoritePlaylists(playlistDTO);
        return Result.success(pageResult);
    }

    /**
     * 收藏歌单
     *
     * @param playlistId 歌单id
     * @return 收藏结果
     */
    @PostMapping("/collectPlaylist")
    public Result collectPlaylist(@RequestParam Long playlistId) {
        userFavoriteService.collectPlaylist(playlistId);
        return Result.success();
    }

    /**
     * 取消收藏歌单
     *
     * @param playlistId 歌单id
     * @return 取消收藏结果
     */
    @DeleteMapping("/cancelCollectPlaylist")
    public Result cancelCollectPlaylist(@RequestParam Long playlistId) {
        userFavoriteService.cancelCollectPlaylist(playlistId);
        return Result.success();
    }
}

