package com.amis.vibemusicserver.service;

import com.amis.vibemusicserver.model.dto.PlaylistAddDTO;
import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.dto.PlaylistUpdateDTO;
import com.amis.vibemusicserver.model.entity.Playlist;
import com.amis.vibemusicserver.model.vo.PlaylistDetailVO;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface IPlaylistService extends IService<Playlist> {

    /**
     * 获取所有歌单
     *
     * @param playlistDTO 歌单查询条件
     * @return 歌单分页结果
     */
    PageResult<PlaylistVO> getAllPlaylists(PlaylistDTO playlistDTO);

    /**
     * 获取所有歌单（含详情）
     *
     * @param playlistDTO 歌单查询条件
     * @return 歌单分页结果
     */
    PageResult<Playlist> getAllPlaylistsInfo(PlaylistDTO playlistDTO);

    /**
     * 获取推荐歌单
     *
     * @param request HTTP请求对象
     * @return 推荐歌单列表
     */
    List<PlaylistVO> getRecommendedPlaylists(HttpServletRequest request);

    /**
     * 根据id获取歌单详情
     *
     * @param playlistId 歌单id
     * @param request    HTTP请求对象
     * @return 歌单详情
     */
    PlaylistDetailVO getPlaylistDetail(Long playlistId, HttpServletRequest request);

    /**
     * 获取所有歌单数量
     *
     * @param style 风格
     * @return 歌单数量
     */
    Long getAllPlaylistsCount(String style);

    /**
     * 添加歌单
     *
     * @param playlistAddDTO 歌单信息
     */
    void addPlaylist(PlaylistAddDTO playlistAddDTO);

    /**
     * 更新歌单
     *
     * @param playlistUpdateDTO 歌单信息
     */
    void updatePlaylist(PlaylistUpdateDTO playlistUpdateDTO);

    /**
     * 更新歌单封面
     *
     * @param playlistId 歌单id
     * @param coverUrl   封面url
     */
    void updatePlaylistCover(Long playlistId, String coverUrl);

    /**
     * 删除歌单
     *
     * @param playlistId 歌单id
     */
    void deletePlaylist(Long playlistId);

    /**
     * 批量删除歌单
     *
     * @param playlistIds 歌单id列表
     */
    void deletePlaylists(List<Long> playlistIds);

}
