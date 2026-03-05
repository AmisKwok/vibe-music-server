package com.amis.vibemusicserver.service;

import com.amis.vibemusicserver.result.Result;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

/**
 * @author : KwokChichung
 * @description : 设备信息服务接口
 * @createDate : 2026/1/21
 */
public interface DeviceInfoService {

    /**
     * 处理客户端IP和设备信息请求
     *
     * @param request HttpServletRequest对象
     * @param requestData 请求数据
     * @return 处理结果
     */
    Result processClientInfoRequest(HttpServletRequest request, Map<String, Object> requestData);
}