package com.amis.vibemusicserver.service.impl;

import com.amis.vibemusicserver.constant.JwtClaimsConstant;
import com.amis.vibemusicserver.constant.MessageConstant;
import com.amis.vibemusicserver.enumeration.LikeStatusEnum;
import com.amis.vibemusicserver.enumeration.RoleEnum;
import com.amis.vibemusicserver.exception.BusinessException;
import com.amis.vibemusicserver.mapper.GenreMapper;
import com.amis.vibemusicserver.mapper.SongMapper;
import com.amis.vibemusicserver.mapper.StyleMapper;
import com.amis.vibemusicserver.mapper.UserFavoriteMapper;
import com.amis.vibemusicserver.model.dto.SongAddDTO;
import com.amis.vibemusicserver.model.dto.SongAndArtistDTO;
import com.amis.vibemusicserver.model.dto.SongDTO;
import com.amis.vibemusicserver.model.dto.SongUpdateDTO;
import com.amis.vibemusicserver.model.entity.Genre;
import com.amis.vibemusicserver.model.entity.Song;
import com.amis.vibemusicserver.model.entity.Style;
import com.amis.vibemusicserver.model.entity.UserFavorite;
import com.amis.vibemusicserver.model.vo.SongAdminVO;
import com.amis.vibemusicserver.model.vo.SongDetailVO;
import com.amis.vibemusicserver.model.vo.SongVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.service.ISongService;
import com.amis.vibemusicserver.service.MinioService;
import com.amis.vibemusicserver.utils.JwtUtil;
import com.amis.vibemusicserver.utils.TypeConversionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author : KwokChichung
 * @description : 歌曲服务实现类
 * @createDate : 2026/1/9 2:11
 */
@Service
@CacheConfig(cacheNames = "songCache")
public class SongServiceImpl extends ServiceImpl<SongMapper, Song> implements ISongService {

    @Autowired
    private SongMapper songMapper;

    @Autowired
    private UserFavoriteMapper userFavoriteMapper;

    @Autowired
    private GenreMapper genreMapper;

    @Autowired
    private StyleMapper styleMapper;

    @Autowired
    private MinioService minioService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取所有歌曲
     *
     * @param songDTO songDTO
     * @param request HTTP请求对象
     * @return 歌曲列表
     */
    @Override
    public PageResult<SongVO> getAllSongs(SongDTO songDTO, HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Map<String, Object> map = null;
        if (token != null && !token.isEmpty()) {
            map = JwtUtil.parseToken(token);
        }

        Page<SongVO> page = new Page<>(songDTO.getPageNum(), songDTO.getPageSize());
        IPage<SongVO> songPage = songMapper.getSongsWithArtist(page, songDTO.getSongName(), songDTO.getArtistName(), songDTO.getAlbum());
        if (songPage.getRecords().isEmpty()) {
            return new PageResult<>(0L, null);
        }

        List<SongVO> songVOList = songPage.getRecords()
                .stream()
                .peek(songVO -> songVO.setLikeStatus(LikeStatusEnum.DEFAULT.getCode()))
                .toList();

        if (map != null) {
            String role = (String) map.get(JwtClaimsConstant.ROLE);

            if (role.equals(RoleEnum.USER.getRole())) {
                Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
                Long userId = TypeConversionUtil.toLong(userIdObj);

                List<UserFavorite> favoriteSongs = userFavoriteMapper.selectList(new QueryWrapper<UserFavorite>()
                        .eq("user_id", userId)
                        .eq("type", 0));

                Set<Long> favoriteSongIds = favoriteSongs.stream()
                        .map(UserFavorite::getSongId)
                        .collect(Collectors.toSet());

                for (SongVO songVO : songVOList) {
                    if (favoriteSongIds.contains(songVO.getSongId())) {
                        songVO.setLikeStatus(LikeStatusEnum.LIKE.getCode());
                    }
                }
            }
        }
        return new PageResult<>(page.getTotal(), songVOList);
    }

    /**
     * 获取歌手的所有歌曲
     *
     * @param songAndArtistDTO 歌手和歌曲DTO
     * @return 歌曲列表
     */
    @Override
    public PageResult<SongAdminVO> getAllSongsByArtist(SongAndArtistDTO songAndArtistDTO) {
        Page<SongAdminVO> page = new Page<>(songAndArtistDTO.getPageNum(), songAndArtistDTO.getPageSize());
        IPage<SongAdminVO> songPage = songMapper.getSongsWithArtistName(page, songAndArtistDTO.getArtistId(), songAndArtistDTO.getSongName(), songAndArtistDTO.getAlbum());

        if (songPage.getRecords().isEmpty()) {
            return new PageResult<>(0L, null);
        }

        return new PageResult<>(songPage.getTotal(), songPage.getRecords());
    }


    /**
     * 获取推荐歌曲
     * 默认推荐数量：20
     *
     * @param request 请求对象，用来获取请求头中的token
     * @return 推荐歌曲列表
     */
    @Override
    public List<SongVO> getRecommendedSongs(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Map<String, Object> map = null;
        if (token != null && !token.isEmpty()) {
            map = JwtUtil.parseToken(token);
        }

        if (map == null) {
            return songMapper.getRandomSongsWithArtist();
        }

        Long userId = TypeConversionUtil.toLong(map.get(JwtClaimsConstant.USER_ID));

        List<Long> favoriteSongIds = userFavoriteMapper.getFavoriteSongIdsByUserId(userId);
        if (favoriteSongIds.isEmpty()) {
            return songMapper.getRandomSongsWithArtist();
        }

        List<Long> favoriteSongStylesId = songMapper.getFavoriteSongStyles(favoriteSongIds);
        Map<Long, Long> styleFrequency = favoriteSongStylesId.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<Long> sortedStyleId = styleFrequency.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue(), a.getValue()))
                .map(Map.Entry::getKey)
                .toList();

        String redisKey = "recommended_songs_" + userId;
        List<SongVO> cachedSongs = redisTemplate.opsForList().range(redisKey, 0, -1);

        if (cachedSongs == null || cachedSongs.isEmpty()) {
            cachedSongs = songMapper.getRecommendedSongsByStyles(sortedStyleId, favoriteSongIds, 80);
            redisTemplate.opsForList().rightPushAll(redisKey, cachedSongs);
            redisTemplate.expire(redisKey, 30, TimeUnit.MINUTES);
        }

        Collections.shuffle(cachedSongs);
        List<SongVO> recommendedSongs = cachedSongs.subList(0, Math.min(20, cachedSongs.size()));

        if (recommendedSongs.size() < 20) {
            List<SongVO> randomSongs = songMapper.getRandomSongsWithArtist();
            Set<Long> addSongIds = recommendedSongs.stream()
                    .map(SongVO::getSongId)
                    .collect(Collectors.toSet());

            for (SongVO songVO : randomSongs) {
                if (randomSongs.size() >= 20) {
                    break;
                }
                if (!addSongIds.contains(songVO.getSongId())) {
                    recommendedSongs.add(songVO);
                }
            }
        }
        return recommendedSongs;
    }


    /**
     * 获取歌曲详情
     *
     * @param songId  歌曲id
     * @param request HttpServletRequest，用于获取请求头中的 token
     * @return 歌曲详情
     */
    @Override
    public SongDetailVO getSongDetail(Long songId, HttpServletRequest request) {
        SongDetailVO songDetailVO = songMapper.getSongDetailById(songId);

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        Map<String, Object> map = null;
        if (token != null && !token.isEmpty()) {
            map = JwtUtil.parseToken(token);
        }

        if (map != null) {
            String role = (String) map.get(JwtClaimsConstant.ROLE);
            if (role.equals(RoleEnum.USER.getRole())) {
                Object userIdObj = map.get(JwtClaimsConstant.USER_ID);
                Long userId = TypeConversionUtil.toLong(userIdObj);

                UserFavorite favoriteSong = userFavoriteMapper.selectOne(new QueryWrapper<UserFavorite>()
                        .eq("user_id", userId)
                        .eq("type", 0)
                        .eq("song_id", songId));
                if (favoriteSong != null) {
                    songDetailVO.setLikeStatus(LikeStatusEnum.LIKE.getCode());
                }
            }
        }

        return songDetailVO;
    }

    /**
     * 获取所有歌曲数量
     *
     * @param style 歌曲风格
     * @return 歌曲数量
     */
    @Override
    public Long getAllSongsCount(String style) {
        QueryWrapper<Song> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(style)) {
            queryWrapper.eq("style", style);
        }
        return songMapper.selectCount(queryWrapper);
    }

    /**
     * 添加歌曲信息
     *
     * @param songAddDTO 歌曲信息
     */
    @Override
    @CacheEvict(cacheNames = "songCache", allEntries = true)
    public void addSong(SongAddDTO songAddDTO) {
        Song song = new Song();
        BeanUtils.copyProperties(songAddDTO, song);

        if (songMapper.insert(song) == 0) {
            throw new BusinessException(MessageConstant.ADD + MessageConstant.FAILED);
        }

        Song songInDB = songMapper.selectOne(new QueryWrapper<Song>()
                .eq("artist_id", songAddDTO.getArtistId())
                .eq("name", songAddDTO.getSongName())
                .eq("album", songAddDTO.getAlbum())
                .orderByDesc("id")
                .last("LIMIT 1"));

        if (songInDB == null) {
            throw new BusinessException(MessageConstant.SONG + MessageConstant.NOT_FOUND);
        }

        Long songId = songInDB.getSongId();

        String styleStr = songAddDTO.getStyle();
        if (styleStr != null && !styleStr.isEmpty()) {
            List<String> styles = Arrays.asList(styleStr.split(","));

            List<Style> styleList = styleMapper.selectList(new QueryWrapper<Style>().in("name", styles));

            for (Style style : styleList) {
                Genre genre = new Genre();
                genre.setSongId(songId);
                genre.setStyleId(style.getStyleId());
                genreMapper.insert(genre);
            }
        }
    }

    /**
     * 更新歌曲信息
     *
     * @param songUpdateDTO 歌曲信息
     */
    @Override
    @CacheEvict(cacheNames = "songCache", allEntries = true)
    public void updateSong(SongUpdateDTO songUpdateDTO) {
        Song songInDB = songMapper.selectById(songUpdateDTO.getSongId());
        if (songInDB == null) {
            throw new BusinessException(MessageConstant.SONG + MessageConstant.NOT_FOUND);
        }

        Song song = new Song();
        BeanUtils.copyProperties(songUpdateDTO, song);
        if (songMapper.updateById(song) == 0) {
            throw new BusinessException(MessageConstant.UPDATE + MessageConstant.FAILED);
        }

        Long songId = songUpdateDTO.getSongId();

        genreMapper.delete(new QueryWrapper<Genre>().eq("song_id", songId));

        String styleStr = songUpdateDTO.getStyle();
        if (styleStr != null && !styleStr.isEmpty()) {
            List<String> styles = Arrays.asList(styleStr.split(","));

            List<Style> styleList = styleMapper.selectList(new QueryWrapper<Style>().in("name", styles));

            for (Style style : styleList) {
                Genre genre = new Genre();
                genre.setSongId(songId);
                genre.setStyleId(style.getStyleId());
                genreMapper.insert(genre);
            }
        }
    }

    /**
     * 更新歌曲封面
     *
     * @param songId   歌曲id
     * @param coverUrl 封面url
     */
    @Override
    @CacheEvict(cacheNames = "songCache", allEntries = true)
    public void updateSongCover(Long songId, String coverUrl) {
        Song song = songMapper.selectById(songId);
        String cover = song.getCoverUrl();
        if (cover != null && !cover.isEmpty()) {
            minioService.deleteFile(cover);
        }

        song.setCoverUrl(coverUrl);
        if (songMapper.updateById(song) == 0) {
            throw new BusinessException(MessageConstant.UPDATE + MessageConstant.FAILED);
        }
    }

    /**
     * 更新歌曲音频
     *
     * @param songId   歌曲id
     * @param audioUrl 音频url
     * @param duration 音频时长
     */
    @Override
    @CacheEvict(cacheNames = "songCache", allEntries = true)
    public void updateSongAudio(Long songId, String audioUrl, String duration) {
        Song song = songMapper.selectById(songId);
        String audio = song.getAudioUrl();
        if (audio != null && !audio.isEmpty()) {
            minioService.deleteFile(audio);
        }

        song.setAudioUrl(audioUrl).setDuration(duration);
        if (songMapper.updateById(song) == 0) {
            throw new BusinessException(MessageConstant.UPDATE + MessageConstant.FAILED);
        }
    }

    /**
     * 删除歌曲
     *
     * @param songId 歌曲id
     */
    @Override
    @CacheEvict(cacheNames = "songCache", allEntries = true)
    public void deleteSong(Long songId) {
        Song song = songMapper.selectById(songId);
        if (song == null) {
            throw new BusinessException(MessageConstant.SONG + MessageConstant.NOT_FOUND);
        }
        String cover = song.getCoverUrl();
        String audio = song.getAudioUrl();

        if (cover != null && !cover.isEmpty()) {
            minioService.deleteFile(cover);
        }
        if (audio != null && !audio.isEmpty()) {
            minioService.deleteFile(audio);
        }

        if (songMapper.deleteById(songId) == 0) {
            throw new BusinessException(MessageConstant.DELETE + MessageConstant.FAILED);
        }
    }

    /**
     * 批量删除歌曲
     *
     * @param songIds 歌曲id列表
     */
    @Override
    @CacheEvict(cacheNames = "songCache", allEntries = true)
    public void deleteSongs(List<Long> songIds) {
        List<Song> songs = songMapper.selectByIds(songIds);
        List<String> coverUrlList = songs.stream()
                .map(Song::getCoverUrl)
                .filter(coverUrl -> coverUrl != null && !coverUrl.isEmpty())
                .toList();
        List<String> audioUrlList = songs.stream()
                .map(Song::getAudioUrl)
                .filter(audioUrl -> audioUrl != null && !audioUrl.isEmpty())
                .toList();

        for (String coverUrl : coverUrlList) {
            minioService.deleteFile(coverUrl);
        }
        for (String audioUrl : audioUrlList) {
            minioService.deleteFile(audioUrl);
        }

        if (songMapper.deleteByIds(songIds) == 0) {
            throw new BusinessException(MessageConstant.DELETE + MessageConstant.FAILED);
        }
    }

}
