package com.amis.vibemusicserver.service;

import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.dto.SongDTO;
import com.amis.vibemusicserver.model.entity.UserFavorite;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.model.vo.SongVO;
import com.amis.vibemusicserver.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

public interface IUserFavoriteService extends IService<UserFavorite> {

    /**
     * 获取用户收藏的歌曲列表
     *
     * @param songDTO 歌曲查询条件
     * @return 歌曲分页结果
     */
    PageResult<SongVO> getUserFavoriteSongs(SongDTO songDTO);

    /**
     * 收藏歌曲
     *
     * @param songId 歌曲id
     */
    void collectSong(Long songId);

    /**
     * 取消收藏歌曲
     *
     * @param songId 歌曲id
     */
    void cancelCollectSong(Long songId);

    /**
     * 获取用户收藏的歌单列表
     *
     * @param playlistDTO 歌单查询条件
     * @return 歌单分页结果
     */
    PageResult<PlaylistVO> getUserFavoritePlaylists(PlaylistDTO playlistDTO);

    /**
     * 收藏歌单
     *
     * @param playlistId 歌单id
     */
    void collectPlaylist(Long playlistId);

    /**
     * 取消收藏歌单
     *
     * @param playlistId 歌单id
     */
    void cancelCollectPlaylist(Long playlistId);

}
