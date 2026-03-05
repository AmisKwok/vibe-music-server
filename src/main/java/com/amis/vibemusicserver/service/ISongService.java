package com.amis.vibemusicserver.service;

import com.amis.vibemusicserver.model.dto.SongAddDTO;
import com.amis.vibemusicserver.model.dto.SongAndArtistDTO;
import com.amis.vibemusicserver.model.dto.SongDTO;
import com.amis.vibemusicserver.model.dto.SongUpdateDTO;
import com.amis.vibemusicserver.model.entity.Song;
import com.amis.vibemusicserver.model.vo.SongAdminVO;
import com.amis.vibemusicserver.model.vo.SongDetailVO;
import com.amis.vibemusicserver.model.vo.SongVO;
import com.amis.vibemusicserver.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * @author : KwokChichung
 * @description : 歌曲服务接口
 * @createDate : 2026/1/9 22:49
 */
public interface ISongService extends IService<Song> {

    /**
     * 获取所有歌曲
     *
     * @param songDTO 歌曲查询条件
     * @param request HTTP请求对象
     * @return 歌曲分页结果
     */
    PageResult<SongVO> getAllSongs(SongDTO songDTO, HttpServletRequest request);

    /**
     * 获取歌手的所有歌曲
     *
     * @param songAndArtistDTO 歌手和歌曲查询条件
     * @return 歌曲分页结果
     */
    PageResult<SongAdminVO> getAllSongsByArtist(SongAndArtistDTO songAndArtistDTO);

    /**
     * 获取推荐歌曲
     *
     * @param request HTTP请求对象
     * @return 推荐歌曲列表
     */
    List<SongVO> getRecommendedSongs(HttpServletRequest request);

    /**
     * 根据id获取歌曲详情
     *
     * @param songId  歌曲id
     * @param request HTTP请求对象
     * @return 歌曲详情
     */
    SongDetailVO getSongDetail(Long songId, HttpServletRequest request);

    /**
     * 获取所有歌曲数量
     *
     * @param style 歌曲风格
     * @return 歌曲数量
     */
    Long getAllSongsCount(String style);

    /**
     * 添加歌曲信息
     *
     * @param songAddDTO 歌曲信息
     */
    void addSong(SongAddDTO songAddDTO);

    /**
     * 更新歌曲信息
     *
     * @param songUpdateDTO 歌曲信息
     */
    void updateSong(SongUpdateDTO songUpdateDTO);

    /**
     * 更新歌曲封面
     *
     * @param songId   歌曲id
     * @param coverUrl 封面url
     */
    void updateSongCover(Long songId, String coverUrl);

    /**
     * 更新歌曲音频
     *
     * @param songId   歌曲id
     * @param audioUrl 音频url
     * @param duration 音频时长
     */
    void updateSongAudio(Long songId, String audioUrl, String duration);

    /**
     * 删除歌曲
     *
     * @param songId 歌曲id
     */
    void deleteSong(Long songId);

    /**
     * 批量删除歌曲
     *
     * @param songIds 歌曲id列表
     */
    void deleteSongs(List<Long> songIds);
}
