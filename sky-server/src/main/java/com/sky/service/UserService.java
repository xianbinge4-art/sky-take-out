package com.sky.service;

import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;

public interface UserService {


    /**
     * 用户登录。
     *
     * @param userLoginDTO 用户登录数据
     * @return 用户信息
     */
    User login(UserLoginDTO   userLoginDTO);

}
