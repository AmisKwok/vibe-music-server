package com.amis.vibemusicserver.service.impl;

import com.amis.vibemusicserver.constant.JwtClaimsConstant;
import com.amis.vibemusicserver.constant.MessageConstant;
import com.amis.vibemusicserver.enumeration.LikeStatusEnum;
import com.amis.vibemusicserver.exception.BusinessException;
import com.amis.vibemusicserver.mapper.PlaylistMapper;
import com.amis.vibemusicserver.mapper.SongMapper;
import com.amis.vibemusicserver.mapper.UserFavoriteMapper;
import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.dto.SongDTO;
import com.amis.vibemusicserver.model.entity.UserFavorite;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.model.vo.SongVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.service.IUserFavoriteService;
import com.amis.vibemusicserver.utils.ThreadLocalUtil;
import com.amis.vibemusicserver.utils.TypeConversionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author : KwokChichung
 * @description : 用户收藏服务实现类，负责处理用户收藏歌曲和歌单的业务逻辑
 * @createDate : 2026/1/18 10:15
 */
@Service
@CacheConfig(cacheNames = "userFavoriteCache")
public class UserFavoriteServiceImpl extends ServiceImpl<UserFavoriteMapper, UserFavorite> implements IUserFavoriteService {

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;
    @Autowired
    private SongMapper songMapper;
    @Autowired
    private PlaylistMapper playlistMapper;

    /**
     * 获取用户收藏的歌曲列表
     *
     * @param songDTO 歌曲查询条件
     * @return 用户收藏的歌曲分页结果
     */
    @Override
    public PageResult<SongVO> getUserFavoriteSongs(SongDTO songDTO) {
        // 从ThreadLocal中获取当前用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
        Long userId = TypeConversionUtil.toLong(userIdObj);

        // 获取用户收藏的歌曲 ID 列表
        List<Long> favoriteSongIds = userFavoriteMapper.getUserFavoriteSongIds(userId);
        if (favoriteSongIds.isEmpty()) {
            return new PageResult<>(0L, Collections.emptyList());
        }

        // 分页查询收藏的歌曲，支持模糊查询
        Page<SongVO> page = new Page<>(songDTO.getPageNum(), songDTO.getPageSize());
        IPage<SongVO> songPage = songMapper.getSongsByIds(
                page,
                favoriteSongIds,
                songDTO.getSongName(),
                songDTO.getArtistName(),
                songDTO.getAlbum()
        );

        // 遍历结果，设置 likeStatus
        List<SongVO> songVOList = songPage.getRecords().stream()
                .peek(songVO -> songVO.setLikeStatus(LikeStatusEnum.LIKE.getCode())) // 设置为已收藏
                .toList();

        return new PageResult<>(songPage.getTotal(), songVOList);
    }

    /**
     * 收藏歌曲
     *
     * @param songId 歌曲 ID
     */
    @Override
    @CacheEvict(cacheNames = {"userFavoriteCache", "songCache", "artistCache", "playlistCache"}, allEntries = true)
    public void collectSong(Long songId) {
        // 从ThreadLocal中获取当前用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
        Long userId = TypeConversionUtil.toLong(userIdObj);

        // 检查是否已经收藏过该歌曲
        QueryWrapper<UserFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("type", 0).eq("song_id", songId);
        if (userFavoriteMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(MessageConstant.ADD + MessageConstant.FAILED);
        }

        // 创建收藏记录
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUserId(userId).setType(0).setSongId(songId).setCreateTime(LocalDateTime.now());
        userFavoriteMapper.insert(userFavorite);
    }

    /**
     * 取消收藏歌曲
     *
     * @param songId 歌曲 ID
     */
    @Override
    @CacheEvict(cacheNames = {"userFavoriteCache", "songCache", "artistCache", "playlistCache"}, allEntries = true)
    public void cancelCollectSong(Long songId) {
        // 从ThreadLocal中获取当前用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
        Long userId = TypeConversionUtil.toLong(userIdObj);

        // 删除收藏记录
        QueryWrapper<UserFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("type", 0).eq("song_id", songId);
        if (userFavoriteMapper.delete(queryWrapper) == 0) {
            throw new BusinessException(MessageConstant.DELETE + MessageConstant.FAILED);
        }
    }

    /**
     * 获取用户收藏的歌单列表
     *
     * @param playlistDTO 歌单查询条件
     * @return 用户收藏的歌单分页结果
     */
    @Override
    public PageResult<PlaylistVO> getUserFavoritePlaylists(PlaylistDTO playlistDTO) {
        // 从ThreadLocal中获取当前用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
        Long userId = TypeConversionUtil.toLong(userIdObj);

        // 获取用户收藏的歌单 ID 列表
        List<Long> favoritePlaylistIds = userFavoriteMapper.getUserFavoritePlaylistIds(userId);
        if (favoritePlaylistIds.isEmpty()) {
            return new PageResult<>(0L, Collections.emptyList());
        }

        // 分页查询收藏的歌单，支持模糊查询
        Page<PlaylistVO> page = new Page<>(playlistDTO.getPageNum(), playlistDTO.getPageSize());
        IPage<PlaylistVO> playlistPage = playlistMapper.getPlaylistsByIds(
                userId,
                page,
                favoritePlaylistIds,
                playlistDTO.getTitle(),
                playlistDTO.getStyle()
        );

        return new PageResult<>(playlistPage.getTotal(), playlistPage.getRecords());
    }

    /**
     * 收藏歌单
     *
     * @param playlistId 歌单 ID
     */
    @Override
    @CacheEvict(cacheNames = {"userFavoriteCache", "songCache", "artistCache", "playlistCache"}, allEntries = true)
    public void collectPlaylist(Long playlistId) {
        // 从ThreadLocal中获取当前用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
        Long userId = TypeConversionUtil.toLong(userIdObj);

        // 检查是否已经收藏过该歌单
        QueryWrapper<UserFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("type", 1).eq("playlist_id", playlistId);
        if (userFavoriteMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(MessageConstant.ADD + MessageConstant.FAILED);
        }

        // 创建收藏记录
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUserId(userId).setType(1).setPlaylistId(Math.toIntExact(playlistId)).setCreateTime(LocalDateTime.now());
        userFavoriteMapper.insert(userFavorite);
    }

    /**
     * 取消收藏歌单
     *
     * @param playlistId 歌单 ID
     */
    @Override
    @CacheEvict(cacheNames = {"userFavoriteCache", "songCache", "artistCache", "playlistCache"}, allEntries = true)
    public void cancelCollectPlaylist(Long playlistId) {
        // 从ThreadLocal中获取当前用户信息
        Map<String, Object> map = ThreadLocalUtil.get();
        Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
        Long userId = TypeConversionUtil.toLong(userIdObj);

        // 删除收藏记录
        QueryWrapper<UserFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("type", 1).eq("playlist_id", playlistId);
        if (userFavoriteMapper.delete(queryWrapper) == 0) {
            throw new BusinessException(MessageConstant.DELETE + MessageConstant.FAILED);
        }
    }

}
