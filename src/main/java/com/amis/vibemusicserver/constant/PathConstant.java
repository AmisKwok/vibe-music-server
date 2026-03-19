package com.amis.vibemusicserver.constant;

/**
 * @author : KwokChichung
 * @description : 路径常量类
 * @createDate : 2026/1/7 5:41
 */
public class PathConstant {

    // ==================== Playlist 路径 ====================
    public static final String PLAYLIST_ALL_PATH = "/playlist/getAllPlaylists";
    public static final String PLAYLIST_RECOMMENDED_PATH = "/playlist/getRecommendedPlaylists";
    public static final String PLAYLIST_DETAIL_PATH = "/playlist/detail/**";
    public static final String PLAYLIST_COUNT_PATH = "/playlist/count";

    // ==================== Song 路径 ====================
    public static final String SONG_LIST_PATH = "/song/getAllSongs";
    public static final String SONG_RECOMMENDED_PATH = "/song/getRecommendedSongs";
    public static final String SONG_DETAIL_PATH = "/song/getSongDetail/**";

    // ==================== Admin 路径 ====================
    public static final String ADMIN_LOGIN_PATH = "/admin/login";
    public static final String ADMIN_LOGOUT_PATH = "/admin/logout";
    public static final String ADMIN_REGISTER_PATH = "/admin/register";

    // ==================== User 路径 ====================
    public static final String USER_LOGIN_PATH = "/user/login";
    public static final String USER_LOGOUT_PATH = "/user/logout";
    public static final String USER_REGISTER_PATH = "/user/register";
    public static final String USER_SEND_VERIFICATION_CODE_PATH = "/user/sendVerificationCode";
    public static final String USER_RESET_PASSWORD_PATH = "/user/resetUserPassword";
    public static final String USER_VERIFY_VERIFICATION_CODE_PATH = "/user/verifyVerificationCode";

    // ==================== Token 路径 ====================
    public static final String TOKEN_REFRESH_PATH = "/token/refresh";

    // ==================== Banner 路径 ====================
    public static final String BANNER_LIST_PATH = "/banner/getBannerList";

    // ==================== Artist 路径 ====================
    public static final String ARTIST_ALL_PATH = "/artist/getAllArtists";
    public static final String ARTIST_DETAIL_PATH = "/artist/getArtistDetail/**";

}
