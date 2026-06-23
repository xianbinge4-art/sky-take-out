package com.sky.exception;

/**
 * 套餐启用失败异常
 */
public class SetmealEnableFailedException extends BaseException {

    /**
     * 创建无错误信息的套餐启售失败异常。
     */
    public SetmealEnableFailedException(){}

    /**
     * 创建携带错误信息的套餐启售失败异常。
     *
     * @param msg 错误信息
     */
    public SetmealEnableFailedException(String msg){
        super(msg);
    }
}
