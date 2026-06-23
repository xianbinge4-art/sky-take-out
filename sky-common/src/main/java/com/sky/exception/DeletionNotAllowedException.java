package com.sky.exception;

public class DeletionNotAllowedException extends BaseException {

    /**
     * 创建携带错误信息的删除不允许异常。
     *
     * @param msg 错误信息
     */
    public DeletionNotAllowedException(String msg) {
        super(msg);
    }

}
