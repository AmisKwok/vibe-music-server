package com.amis.vibemusicserver.service;

import com.amis.vibemusicserver.model.dto.*;
import com.amis.vibemusicserver.model.entity.User;
import com.amis.vibemusicserver.model.vo.UserManagementVO;
import com.amis.vibemusicserver.model.vo.UserVO;
import com.amis.vibemusicserver.result.PageResult;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface IUserService extends IService<User> {
    /**
     * 发送验证码
     *
     * @param email 用户邮箱
     */
    void sendVerificationCode(String email);

    /**
     * 验证验证码
     *
     * @param email            用户邮箱
     * @param verificationCode 验证码
     * @return 验证结果
     */
    Boolean verifyVerificationCode(String email, String verificationCode);

    /**
     * 用户注册
     *
     * @param userRegisterDTO 用户注册信息
     */
    void register(UserRegisterDTO userRegisterDTO);

    /**
     * 用户登录
     *
     * @param userLoginDTO 用户登录信息
     * @return TokenDTO
     */
    TokenDTO login(UserLoginDTO userLoginDTO);

    /**
     * 用户信息
     *
     * @return 用户VO
     */
    UserVO userInfo();

    /**
     * 更新用户信息
     *
     * @param userDTO 用户信息DTO对象
     */
    void updateUserInfo(UserDTO userDTO);

    /**
     * 更新用户头像
     *
     * @param avatarUrl 用户头像URL地址
     */
    void updateUserAvatar(String avatarUrl);

    /**
     * 更新用户密码
     *
     * @param userPasswordDTO 用户密码信息
     * @param token           JWT token
     */
    void updateUserPassword(UserPasswordDTO userPasswordDTO, String token);

    /**
     * 重置用户密码
     *
     * @param userResetPasswordDTO 用户密码信息
     */
    void resetUserPassword(UserResetPasswordDTO userResetPasswordDTO);

    /**
     * 用户登出
     *
     * @param token 认证token
     */
    void logout(String token);

    /**
     * 注销账户
     */
    void deleteAccount();

    /**
     * 获取所有用户数量
     *
     * @return 用户数量
     */
    Long getAllUsersCount();

    /**
     * 获取所有用户
     *
     * @param userSearchDTO 用户搜索条件
     * @return 用户分页结果
     */
    PageResult<UserManagementVO> getAllUsers(UserSearchDTO userSearchDTO);

    /**
     * 添加用户
     *
     * @param userAddDTO 用户添加DTO对象
     */
    void addUser(UserAddDTO userAddDTO);

    /**
     * 更新用户
     *
     * @param userDTO 用户信息DTO对象
     */
    void updateUser(UserDTO userDTO);

    /**
     * 更新用户状态
     *
     * @param userId     用户ID
     * @param userStatus 用户状态
     */
    void updateUserStatus(Long userId, Integer userStatus);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     */
    void deleteUsers(List<Long> userIds);
}
