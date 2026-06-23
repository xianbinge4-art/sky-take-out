package com.sky.exception;

/**
 * 登录失败
 */
public class LoginFailedException extends BaseException{
    /**
     * 创建携带错误信息的登录失败异常。
     *
     * @param msg 错误信息
     */
    public LoginFailedException(String msg){
        super(msg);
    }
}
