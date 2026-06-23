package com.sky.exception;

public class AddressBookBusinessException extends BaseException {

    /**
     * 创建携带错误信息的地址簿业务异常。
     *
     * @param msg 错误信息
     */
    public AddressBookBusinessException(String msg) {
        super(msg);
    }

}
