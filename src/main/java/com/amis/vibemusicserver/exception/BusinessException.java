package com.amis.vibemusicserver.exception;

import com.amis.vibemusicserver.enumeration.ResultCodeEnum;
import lombok.Getter;

/**
 * 业务异常类
 * 用于处理业务逻辑中的异常情况
 *
 * @author : KwokChichung
 * @description : 业务异常
 * @createDate : 2026/3/5
 */
@Getter
public class BusinessException extends RuntimeException {

    /**
     * 业务状态码
     */
    private Integer code;

    /**
     * 使用自定义错误消息构造业务异常
     * 默认使用 BUSINESS_ERROR 状态码
     *
     * @param message 错误消息
     */
    public BusinessException(String message) {
        super(message);
        this.code = ResultCodeEnum.BUSINESS_ERROR.getCode();
    }

    /**
     * 使用 ResultCodeEnum 构造业务异常
     *
     * @param resultCode 结果状态码枚举
     */
    public BusinessException(ResultCodeEnum resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }

    /**
     * 使用 ResultCodeEnum 和自定义消息构造业务异常
     *
     * @param resultCode 结果状态码枚举
     * @param message    自定义错误消息
     */
    public BusinessException(ResultCodeEnum resultCode, String message) {
        super(message);
        this.code = resultCode.getCode();
    }

    /**
     * 使用自定义状态码和消息构造业务异常
     *
     * @param code    自定义状态码
     * @param message 自定义错误消息
     */
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }
}
