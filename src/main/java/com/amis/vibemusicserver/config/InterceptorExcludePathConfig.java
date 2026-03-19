package com.amis.vibemusicserver.config;

import com.amis.vibemusicserver.constant.PathConstant;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @author : KwokChichung
 * @description : 拦截器排除路径配置类
 * @createDate : 2026/1/8
 */
@Configuration
public class InterceptorExcludePathConfig {

    /**
     * 获取所有不需要拦截的路径列表
     */
    public static List<String> getExcludePaths() {
        return Arrays.asList(
                // 管理员相关
                PathConstant.ADMIN_LOGIN_PATH,
                PathConstant.ADMIN_LOGOUT_PATH,
                PathConstant.ADMIN_REGISTER_PATH,

                // 用户认证相关
                PathConstant.USER_LOGIN_PATH,
                PathConstant.USER_LOGOUT_PATH,
                PathConstant.USER_REGISTER_PATH,
                PathConstant.USER_SEND_VERIFICATION_CODE_PATH,
                PathConstant.USER_RESET_PASSWORD_PATH,
                PathConstant.USER_VERIFY_VERIFICATION_CODE_PATH,

                //token
                PathConstant.TOKEN_REFRESH_PATH,

                // 公共内容相关
                PathConstant.BANNER_LIST_PATH,

                // 歌单相关 - 允许所有非admin路径
                PathConstant.PLAYLIST_ALL_PATH,
                PathConstant.PLAYLIST_RECOMMENDED_PATH,
                PathConstant.PLAYLIST_DETAIL_PATH,
                PathConstant.PLAYLIST_COUNT_PATH,

                // 歌手相关
                PathConstant.ARTIST_ALL_PATH,
                PathConstant.ARTIST_DETAIL_PATH,

                // 歌曲相关
                PathConstant.SONG_LIST_PATH,
                PathConstant.SONG_RECOMMENDED_PATH,
                PathConstant.SONG_DETAIL_PATH
        );
    }

    /**
     * 获取管理员相关排除路径
     */
    public static List<String> getAdminExcludePaths() {
        return Arrays.asList(
                PathConstant.ADMIN_LOGIN_PATH,
                PathConstant.ADMIN_LOGOUT_PATH,
                PathConstant.ADMIN_REGISTER_PATH
        );
    }

    /**
     * 获取用户相关排除路径
     */
    public static List<String> getUserExcludePaths() {
        return Arrays.asList(
                PathConstant.USER_LOGIN_PATH,
                PathConstant.USER_LOGOUT_PATH,
                PathConstant.USER_REGISTER_PATH,
                PathConstant.USER_SEND_VERIFICATION_CODE_PATH,
                PathConstant.USER_RESET_PASSWORD_PATH,
                PathConstant.USER_VERIFY_VERIFICATION_CODE_PATH
        );
    }

    /**
     * 获取公共内容排除路径
     */
    public static List<String> getPublicContentExcludePaths() {
        return Arrays.asList(
                PathConstant.BANNER_LIST_PATH,
                PathConstant.PLAYLIST_ALL_PATH,
                PathConstant.PLAYLIST_RECOMMENDED_PATH,
                PathConstant.PLAYLIST_DETAIL_PATH,
                PathConstant.ARTIST_ALL_PATH,
                PathConstant.ARTIST_DETAIL_PATH,
                PathConstant.SONG_LIST_PATH,
                PathConstant.SONG_RECOMMENDED_PATH,
                PathConstant.SONG_DETAIL_PATH
        );
    }
}