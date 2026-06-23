package com.sky.exception;

/**
 * 密码修改失败异常
 */
public class PasswordEditFailedException extends BaseException{

    /**
     * 创建携带错误信息的密码修改失败异常。
     *
     * @param msg 错误信息
     */
    public PasswordEditFailedException(String msg){
        super(msg);
    }

}
