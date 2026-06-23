package com.sky.exception;

public class ShoppingCartBusinessException extends BaseException {

    /**
     * 创建携带错误信息的购物车业务异常。
     *
     * @param msg 错误信息
     */
    public ShoppingCartBusinessException(String msg) {
        super(msg);
    }

}
