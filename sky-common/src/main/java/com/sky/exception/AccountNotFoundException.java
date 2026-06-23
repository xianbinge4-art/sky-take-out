package com.sky.exception;

/**
 * 账号不存在异常
 */
public class AccountNotFoundException extends BaseException {

    /**
     * 创建无错误信息的账号不存在异常。
     */
    public AccountNotFoundException() {
    }

    /**
     * 创建携带错误信息的账号不存在异常。
     *
     * @param msg 错误信息
     */
    public AccountNotFoundException(String msg) {
        super(msg);
    }

}
