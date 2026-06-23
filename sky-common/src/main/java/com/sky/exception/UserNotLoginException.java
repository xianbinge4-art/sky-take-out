package com.sky.exception;

public class UserNotLoginException extends BaseException {

    /**
     * 创建无错误信息的用户未登录异常。
     */
    public UserNotLoginException() {
    }

    /**
     * 创建携带错误信息的用户未登录异常。
     *
     * @param msg 错误信息
     */
    public UserNotLoginException(String msg) {
        super(msg);
    }

}
