package org.amis.vibemusicserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jakarta.servlet.http.HttpServletRequest;
import org.amis.vibemusicserver.model.dto.ArtistAddDTO;
import org.amis.vibemusicserver.model.dto.ArtistDTO;
import org.amis.vibemusicserver.model.dto.ArtistUpdateDTO;
import org.amis.vibemusicserver.model.entity.Artist;
import org.amis.vibemusicserver.model.vo.ArtistDetailVO;
import org.amis.vibemusicserver.model.vo.ArtistNameVO;
import org.amis.vibemusicserver.model.vo.ArtistVO;
import org.amis.vibemusicserver.result.PageResult;

import java.util.List;

public interface IArtistService extends IService<Artist> {

    /**
     * 获取所有歌手
     *
     * @param artistDTO 歌手查询条件
     * @return 歌手分页结果
     */
    PageResult<ArtistVO> getAllArtists(ArtistDTO artistDTO);

    /**
     * 获取所有歌手（含详情）
     *
     * @param artistDTO 歌手查询条件
     * @return 歌手分页结果
     */
    PageResult<Artist> getAllArtistsAndDetail(ArtistDTO artistDTO);

    /**
     * 获取所有歌手id和名字
     *
     * @return 歌手名称列表
     */
    List<ArtistNameVO> getAllArtistNames();

    /**
     * 获取随机歌手
     *
     * @return 歌手列表
     */
    List<ArtistVO> getRandomArtists();

    /**
     * 根据id获取歌手详情
     *
     * @param artistId 歌手id
     * @param request  HTTP请求对象
     * @return 歌手详情
     */
    ArtistDetailVO getArtistDetail(Long artistId, HttpServletRequest request);

    /**
     * 获取所有歌手数量
     *
     * @param gender 性别
     * @param area   地区
     * @return 歌手数量
     */
    Long getAllArtistsCount(Integer gender, String area);

    /**
     * 添加歌手
     *
     * @param artistAddDTO 歌手信息
     */
    void addArtist(ArtistAddDTO artistAddDTO);

    /**
     * 更新歌手
     *
     * @param artistUpdateDTO 歌手信息
     */
    void updateArtist(ArtistUpdateDTO artistUpdateDTO);

    /**
     * 更新歌手头像
     *
     * @param artistId 歌手id
     * @param avatar   头像url
     */
    void updateArtistAvatar(Long artistId, String avatar);

    /**
     * 删除歌手
     *
     * @param artistId 歌手id
     */
    void deleteArtist(Long artistId);

    /**
     * 批量删除歌手
     *
     * @param artistIds 歌手id列表
     */
    void deleteArtists(List<Long> artistIds);

}
