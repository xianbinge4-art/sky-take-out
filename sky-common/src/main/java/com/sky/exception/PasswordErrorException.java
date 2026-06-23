package com.sky.exception;

/**
 * 密码错误异常
 */
public class PasswordErrorException extends BaseException {

    /**
     * 创建无错误信息的密码错误异常。
     */
    public PasswordErrorException() {
    }

    /**
     * 创建携带错误信息的密码错误异常。
     *
     * @param msg 错误信息
     */
    public PasswordErrorException(String msg) {
        super(msg);
    }

}
