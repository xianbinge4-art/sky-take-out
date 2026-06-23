package com.sky.exception;

/**
 * 账号被锁定异常
 */
public class AccountLockedException extends BaseException {

    /**
     * 创建无错误信息的账号锁定异常。
     */
    public AccountLockedException() {
    }

    /**
     * 创建携带错误信息的账号锁定异常。
     *
     * @param msg 错误信息
     */
    public AccountLockedException(String msg) {
        super(msg);
    }

}
