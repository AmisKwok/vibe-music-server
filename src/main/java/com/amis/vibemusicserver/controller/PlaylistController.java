package com.amis.vibemusicserver.controller;

import com.amis.vibemusicserver.constant.MessageConstant;
import com.amis.vibemusicserver.model.dto.PlaylistAddDTO;
import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.dto.PlaylistUpdateDTO;
import com.amis.vibemusicserver.model.entity.Playlist;
import com.amis.vibemusicserver.model.vo.PlaylistDetailVO;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.result.Result;
import com.amis.vibemusicserver.service.IPlaylistService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * @author : KwokChichung
 * @description : 歌单控制器
 * @createDate : 2026/3/6 6:04
 */
@RestController
@RequestMapping("/playlist")
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
    @Cacheable(cacheNames = "playlistCache", key = "#playlistDTO.pageNum + '-' + #playlistDTO.pageSize + '-' + #playlistDTO.title + '-' + #playlistDTO.style")
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
     * 获取推荐歌单
     *
     * @param request HTTP请求
     * @return 推荐歌单列表
     */
    @PostMapping("/getRecommendedPlaylists")
    public Result<List<PlaylistVO>> getRecommendedPlaylists(HttpServletRequest request) {
        List<PlaylistVO> recommendedPlaylists = playlistService.getRecommendedPlaylists(request);

        // 处理查询结果为空的情况
        if (recommendedPlaylists == null || recommendedPlaylists.isEmpty()) {
            return Result.success(MessageConstant.DATA_NOT_FOUND, Collections.emptyList());
        }

        return Result.success(recommendedPlaylists);
    }

    /**
     * 获取歌单详情
     *
     * @param playlistId 歌单id
     * @param request    HTTP请求
     * @return 歌单详情
     */
    @GetMapping("/detail/{playlistId}")
    @Cacheable(cacheNames = "playlistCache", key = "#playlistId")
    public Result<PlaylistDetailVO> getPlaylistDetail(@PathVariable Long playlistId, HttpServletRequest request) {
        PlaylistDetailVO playlistDetailVO = playlistService.getPlaylistDetail(playlistId, request);
        return Result.success(playlistDetailVO);
    }

    /**
     * 获取所有歌单数量
     *
     * @param style 歌单风格
     * @return 歌单数量
     */
    @GetMapping("/count")
    public Result<Long> getAllPlaylistsCount(@RequestParam(required = false) String style) {
        Long count = playlistService.getAllPlaylistsCount(style);
        return Result.success(count);
    }

    // ==================== 管理员操作 ====================

    /**
     * 管理员获取所有歌单（按ID倒序）
     *
     * @param playlistDTO playlistDTO
     * @return 歌单列表
     */
    @PostMapping("/admin/getAllPlaylistsInfo")
    @Cacheable(cacheNames = "playlistCache", key = "#playlistDTO.pageNum + '-' + #playlistDTO.pageSize + '-' + #playlistDTO.title + '-' + #playlistDTO.style + '-admin'")
    public Result<PageResult<Playlist>> getAllPlaylistsInfo(@RequestBody @Valid PlaylistDTO playlistDTO) {
        PageResult<Playlist> allPlaylists = playlistService.getAllPlaylistsInfo(playlistDTO);

        // 处理查询结果为空的情况
        if (allPlaylists.getTotal() == 0) {
            return Result.success(MessageConstant.DATA_NOT_FOUND, new PageResult<>(0L, null));
        }
        // 返回查询结果
        return Result.success(new PageResult<>(allPlaylists.getTotal(), allPlaylists.getItems()));
    }

    /**
     * 添加歌单
     *
     * @param playlistAddDTO 歌单信息
     * @return 添加结果
     */
    @PostMapping("/admin/add")
    @CacheEvict(cacheNames = "playlistCache", allEntries = true)
    public Result addPlaylist(@RequestBody @Valid PlaylistAddDTO playlistAddDTO) {
        playlistService.addPlaylist(playlistAddDTO);
        return Result.success(MessageConstant.ADD + MessageConstant.SUCCESS);
    }

    /**
     * 更新歌单
     *
     * @param playlistUpdateDTO 歌单信息
     * @return 更新结果
     */
    @PostMapping("/admin/update")
    @CacheEvict(cacheNames = "playlistCache", allEntries = true)
    public Result updatePlaylist(@RequestBody @Valid PlaylistUpdateDTO playlistUpdateDTO) {
        playlistService.updatePlaylist(playlistUpdateDTO);
        return Result.success(MessageConstant.UPDATE + MessageConstant.SUCCESS);
    }

    /**
     * 更新歌单封面
     *
     * @param playlistId 歌单id
     * @param coverUrl   封面url
     * @return 更新结果
     */
    @PostMapping("/admin/updateCover")
    @CacheEvict(cacheNames = "playlistCache", allEntries = true)
    public Result updatePlaylistCover(@RequestParam Long playlistId, @RequestParam String coverUrl) {
        playlistService.updatePlaylistCover(playlistId, coverUrl);
        return Result.success(MessageConstant.UPDATE + MessageConstant.SUCCESS);
    }

    /**
     * 删除歌单
     *
     * @param playlistId 歌单id
     * @return 删除结果
     */
    @DeleteMapping("/admin/delete/{playlistId}")
    @CacheEvict(cacheNames = "playlistCache", allEntries = true)
    public Result deletePlaylist(@PathVariable Long playlistId) {
        playlistService.deletePlaylist(playlistId);
        return Result.success(MessageConstant.DELETE + MessageConstant.SUCCESS);
    }

    /**
     * 批量删除歌单
     *
     * @param playlistIds 歌单id列表
     * @return 删除结果
     */
    @DeleteMapping("/admin/deleteBatch")
    @CacheEvict(cacheNames = "playlistCache", allEntries = true)
    public Result deletePlaylists(@RequestBody List<Long> playlistIds) {
        playlistService.deletePlaylists(playlistIds);
        return Result.success(MessageConstant.DELETE + MessageConstant.SUCCESS);
    }
}