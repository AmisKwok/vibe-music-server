package com.amis.vibemusicserver.controller;

import com.amis.vibemusicserver.constant.MessageConstant;
import com.amis.vibemusicserver.model.dto.SongDTO;
import com.amis.vibemusicserver.model.vo.SongDetailVO;
import com.amis.vibemusicserver.model.vo.SongVO;
import com.amis.vibemusicserver.result.PageResult;
import com.amis.vibemusicserver.result.Result;
import com.amis.vibemusicserver.service.ISongService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author : KwokChichung
 * @description :
 * @createDate : 2026/1/9 23:53
 */
@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private ISongService songService;

    /**
     * 获取所有歌曲
     *
     * @param songDTO 歌曲查询条件
     * @param request 请求对象
     * @return 歌曲分页结果
     */
    @PostMapping("/getAllSongs")
    @Cacheable(cacheNames = "songCache", key = "#songDTO.pageNum.toString() + '-' + #songDTO.pageSize.toString() + '-' + #songDTO.songName + '-' + #songDTO.artistName + '-' + #songDTO.album")
    public Result<PageResult<SongVO>> getAllSongs(@RequestBody SongDTO songDTO, HttpServletRequest request) {
        PageResult<SongVO> pageResult = songService.getAllSongs(songDTO, request);
        if (pageResult.getTotal() == 0) {
            return Result.success(MessageConstant.DATA_NOT_FOUND, pageResult);
        }
        return Result.success(pageResult);
    }

    /**
     * 获取推荐歌曲
     * 推荐歌曲的数量为 20
     *
     * @param request 请求
     * @return 推荐歌曲列表
     */
    @GetMapping("/getRecommendedSongs")
    public Result<List<SongVO>> getRecommendedSongs(HttpServletRequest request) {
        List<SongVO> songList = songService.getRecommendedSongs(request);
        return Result.success(songList);
    }

    /**
     * 获取歌曲详情
     *
     * @param songId 歌曲id
     * @return 歌曲详情
     */
    @GetMapping("/getSongDetail/{id}")
    @Cacheable(cacheNames = "songCache", key = "#songId")
    public Result<SongDetailVO> getSongDetail(@PathVariable("id") Long songId, HttpServletRequest request) {
        SongDetailVO songDetailVO = songService.getSongDetail(songId, request);
        return Result.success(songDetailVO);
    }
}

