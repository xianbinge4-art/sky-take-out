package com.sky.exception;

/**
 * 业务异常
 */
public class BaseException extends RuntimeException {

    /**
     * 创建无错误信息的业务异常。
     */
    public BaseException() {
    }

    /**
     * 创建携带错误信息的业务异常。
     *
     * @param msg 错误信息
     */
    public BaseException(String msg) {
        super(msg);
    }

}
