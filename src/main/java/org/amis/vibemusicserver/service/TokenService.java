package org.amis.vibemusicserver.service;

import org.amis.vibemusicserver.model.dto.TokenDTO;
import org.amis.vibemusicserver.model.dto.TokenRefreshDTO;

public interface TokenService {
    /**
     * 生成刷新Token
     *
     * @param refreshDTO 刷新Token请求
     * @return TokenDTO
     */
    TokenDTO generateRefreshToken(TokenRefreshDTO refreshDTO);
}
