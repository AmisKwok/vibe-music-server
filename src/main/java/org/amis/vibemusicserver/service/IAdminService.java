package org.amis.vibemusicserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.amis.vibemusicserver.model.dto.AdminDTO;
import org.amis.vibemusicserver.model.dto.TokenDTO;
import org.amis.vibemusicserver.model.entity.Admin;

public interface IAdminService extends IService<Admin> {

    /**
     * 管理员注册
     *
     * @param adminDTO 管理员信息
     */
    void register(AdminDTO adminDTO);

    /**
     * 管理员登录
     *
     * @param adminDTO 管理员信息
     * @return TokenDTO
     */
    TokenDTO login(AdminDTO adminDTO);

    /**
     * 退出登录
     *
     * @param token 认证token
     */
    void logout(String token);
}
