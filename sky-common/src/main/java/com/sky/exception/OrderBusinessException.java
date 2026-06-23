package com.sky.exception;

public class OrderBusinessException extends BaseException {

    /**
     * 创建携带错误信息的订单业务异常。
     *
     * @param msg 错误信息
     */
    public OrderBusinessException(String msg) {
        super(msg);
    }

}
