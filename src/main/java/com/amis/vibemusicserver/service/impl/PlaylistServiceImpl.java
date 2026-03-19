package com.amis.vibemusicserver.service.impl;


import com.amis.vibemusicserver.constant.JwtClaimsConstant;
import com.amis.vibemusicserver.constant.MessageConstant;
import com.amis.vibemusicserver.enumeration.LikeStatusEnum;
import com.amis.vibemusicserver.enumeration.RoleEnum;
import com.amis.vibemusicserver.exception.BusinessException;
import com.amis.vibemusicserver.mapper.PlaylistMapper;
import com.amis.vibemusicserver.mapper.UserFavoriteMapper;
import com.amis.vibemusicserver.model.dto.PlaylistAddDTO;
import com.amis.vibemusicserver.model.dto.PlaylistDTO;
import com.amis.vibemusicserver.model.dto.PlaylistUpdateDTO;
import com.amis.vibemusicserver.model.entity.Playlist;
import com.amis.vibemusicserver.model.entity.UserFavorite;
import com.amis.vibemusicserver.model.vo.PlaylistDetailVO;
import com.amis.vibemusicserver.model.vo.PlaylistVO;
import com.amis.vibemusicserver.model.vo.SongVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.service.IPlaylistService;
import com.amis.vibemusicserver.service.MinioService;
import com.amis.vibemusicserver.utils.JwtUtil;
import com.amis.vibemusicserver.utils.TypeConversionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : KwokChichung
 * @description : 歌单服务实现类
 * <p>
 * app自己有做缓存和数据库保存本地列表，这里提供的是云端播放列表和推荐歌单服务
 * <p/>
 * @createDate : 2026/1/28 19:28
 */
@Service
public class PlaylistServiceImpl extends ServiceImpl<PlaylistMapper, Playlist> implements IPlaylistService {

    @Autowired
    private PlaylistMapper playlistMapper;

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private MinioService minioService;


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
     *
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
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Map<String, Object> map = null;
        if (token != null && !token.isEmpty()) {
            map = JwtUtil.parseToken(token);
        }

        Long userId = null;
        if (map != null) {
            String role = (String) map.get(JwtClaimsConstant.ROLE);
            if (role.equals(RoleEnum.USER.getRole())) {
                Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
                userId = TypeConversionUtil.toLong(userIdObj);
            }
        }
        // 用户未登录，返回随机歌单
        if (userId == null) {
            return playlistMapper.getRandomPlaylists(10);
        }

        // 获取用户收藏的歌单 ID
        List<Long> favoritePlaylistIds = userFavoriteMapper.getFavoritePlaylistIdsByUserId(userId);
        if (favoritePlaylistIds.isEmpty()) {
            return playlistMapper.getRandomPlaylists(10); // 如果用户没有收藏歌单，返回随机歌单
        }

        // 查询用户收藏的歌单风格并统计频率
        List<String> favoriteStyles = playlistMapper.getFavoritePlaylistStyles(favoritePlaylistIds);
        List<Long> favoriteStyleIds = userFavoriteMapper.getFavoriteIdsByStyle(favoriteStyles);
        Map<Long, Long> styleFrequency = favoriteStyleIds.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        // 按风格出现次数降序排序
        List<Long> sortedStyleIds = styleFrequency.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        // 根据排序后的风格推荐歌单（排除已收藏歌单）
        List<PlaylistVO> recommendedPlaylists = playlistMapper.getRecommendedPlaylistsByStyles(sortedStyleIds, favoritePlaylistIds, 10);

        // 如果推荐的歌单不足 10 个，则用随机歌单填充
        if (recommendedPlaylists.size() < 10) {
            List<PlaylistVO> randomPlaylists = playlistMapper.getRandomPlaylists(10);
            Set<Long> addedPlaylistIds = recommendedPlaylists.stream().map(PlaylistVO::getPlaylistId).collect(Collectors.toSet());

            for (PlaylistVO playlist : randomPlaylists) {
                if (recommendedPlaylists.size() >= 10) break;
                if (!addedPlaylistIds.contains(playlist.getPlaylistId())) {
                    recommendedPlaylists.add(playlist);
                }
            }
        }


        return recommendedPlaylists;
    }


    /**
     * 获取歌单详情
     *
     * @param playlistId 歌单id
     * @param request    HttpServletRequest，用于获取请求头中的 token
     * @return 歌单详情
     */
    @Override
    public PlaylistDetailVO getPlaylistDetail(Long playlistId, HttpServletRequest request) {
        PlaylistDetailVO playlistDetailVO = playlistMapper.getPlaylistDetailById(playlistId);

        // 设置默认状态
        List<SongVO> songVOList = playlistDetailVO.getSongs();
        songVOList.forEach(songVO -> songVO.setLikeStatus(LikeStatusEnum.DEFAULT.getCode()));
        playlistDetailVO.setLikeStatus(LikeStatusEnum.DEFAULT.getCode());

        // 获取请求头中的 token
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);  // 去掉 "Bearer " 前缀
        }

        Map<String, Object> map = null;
        if (token != null && !token.isEmpty()) {
            map = JwtUtil.parseToken(token);
        }

        // 如果 token 解析成功且用户为登录状态，进一步操作
        if (map != null) {
            String role = (String) map.get(JwtClaimsConstant.ROLE);
            if (role.equals(RoleEnum.USER.getRole())) {
                Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
                Long userId = TypeConversionUtil.toLong(userIdObj);

                // 获取用户收藏的歌单
                UserFavorite favoritePlaylist = userFavoriteMapper.selectOne(new QueryWrapper<UserFavorite>()
                        .eq("user_id", userId)
                        .eq("type", 1)
                        .eq("playlist_id", playlistId));
                if (favoritePlaylist != null) {
                    playlistDetailVO.setLikeStatus(LikeStatusEnum.LIKE.getCode());
                }

                // 获取用户收藏的歌曲
                List<UserFavorite> favoriteSongs = userFavoriteMapper.selectList(new QueryWrapper<UserFavorite>()
                        .eq("user_id", userId)
                        .eq("type", 0));

                // 获取用户收藏的歌曲 id
                Set<Long> favoriteSongIds = favoriteSongs.stream()
                        .map(UserFavorite::getSongId)
                        .collect(Collectors.toSet());

                // 检查并更新状态
                for (SongVO songVO : songVOList) {
                    if (favoriteSongIds.contains(songVO.getSongId())) {
                        songVO.setLikeStatus(LikeStatusEnum.LIKE.getCode());
                    }
                }
            }
        }

        return playlistDetailVO;
    }

    /**
     * 获取所有歌单数量
     *
     * @param style 歌单风格
     * @return 歌单数量
     */
    @Override
    public Long getAllPlaylistsCount(String style) {
        QueryWrapper<Playlist> queryWrapper = new QueryWrapper<>();
        if (style != null) {
            queryWrapper.eq("style", style);
        }

        return playlistMapper.selectCount(queryWrapper);
    }

    /**
     * 添加歌单
     *
     * @param playlistAddDTO 歌单DTO
     */
    @Override
    public void addPlaylist(PlaylistAddDTO playlistAddDTO) {
        QueryWrapper<Playlist> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("title", playlistAddDTO.getTitle());
        if (playlistMapper.selectCount(queryWrapper) > 0) {
            throw new BusinessException(MessageConstant.PLAYLIST + MessageConstant.ALREADY_EXISTS);
        }

        Playlist playlist = new Playlist();
        BeanUtils.copyProperties(playlistAddDTO, playlist);
        playlistMapper.insert(playlist);
    }

    /**
     * 更新歌单
     *
     * @param playlistUpdateDTO 歌单更新DTO
     */
    @Override
    public void updatePlaylist(PlaylistUpdateDTO playlistUpdateDTO) {
        Long playlistId = playlistUpdateDTO.getPlaylistId();

        Playlist playlistByTitle = playlistMapper.selectOne(new QueryWrapper<Playlist>().eq("title", playlistUpdateDTO.getTitle()));
        if (playlistByTitle != null && !playlistByTitle.getId().equals(playlistId)) {
            throw new BusinessException(MessageConstant.PLAYLIST + MessageConstant.ALREADY_EXISTS);
        }

        Playlist playlist = new Playlist();
        BeanUtils.copyProperties(playlistUpdateDTO, playlist);
        if (playlistMapper.updateById(playlist) == 0) {
            throw new BusinessException(MessageConstant.UPDATE + MessageConstant.FAILED);
        }
    }

    /**
     * 更新歌单封面
     *
     * @param playlistId 歌单id
     * @param coverUrl   歌单封面url
     */
    @Override
    public void updatePlaylistCover(Long playlistId, String coverUrl) {
        Playlist playlist = playlistMapper.selectById(playlistId);
        String cover = playlist.getCoverUrl();
        if (cover != null && !cover.isEmpty()) {
            minioService.deleteFile(cover);
        }

        playlist.setCoverUrl(coverUrl);
        if (playlistMapper.updateById(playlist) == 0) {
            throw new BusinessException(MessageConstant.UPDATE + MessageConstant.FAILED);
        }
    }

    /**
     * 删除歌单
     *
     * @param playlistId 歌单id
     */
    @Override
    public void deletePlaylist(Long playlistId) {
        // 1. 查询歌单信息，获取封面 URL
        Playlist playlist = playlistMapper.selectById(playlistId);
        if (playlist == null) {
            throw new BusinessException(MessageConstant.PLAYLIST + MessageConstant.NOT_FOUND);
        }
        String coverUrl = playlist.getCoverUrl();

        // 2. 先删除 MinIO 里的封面文件
        if (coverUrl != null && !coverUrl.isEmpty()) {
            minioService.deleteFile(coverUrl);
        }

        // 3. 删除数据库中的歌单信息
        if (playlistMapper.deleteById( playlistId) == 0) {
            throw new BusinessException(MessageConstant.DELETE + MessageConstant.FAILED);
        }
    }

    /**
     * 批量删除歌单
     *
     * @param playlistIds 歌单id列表
     */
    @Override
    public void deletePlaylists(List<Long> playlistIds) {
        List<Playlist> playlists = playlistMapper.selectBatchIds(playlistIds);
        List<String> coverUrlList = playlists.stream()
                .map(Playlist::getCoverUrl)
                .filter(coverUrl -> coverUrl != null && !coverUrl.isEmpty())
                .toList();

        // 2. 先删除 MinIO 里的封面文件
        for (String coverUrl : coverUrlList) {
            minioService.deleteFile(coverUrl);
        }

        // 3. 删除数据库中的歌单信息
        if (playlistMapper.deleteBatchIds(playlistIds) == 0) {
            throw new BusinessException(MessageConstant.DELETE + MessageConstant.FAILED);
        }
    }
}