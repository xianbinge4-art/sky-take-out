package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.JwtProperties;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    // 微信登录接口地址
    public static final String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";
    @Autowired
    private WeChatProperties weChatProperties;
    @Autowired
    private UserMapper userMapper;



    @Override
    /**
     * 根据微信授权码完成用户登录，首次登录时自动创建用户。
     *
     * @param userLoginDTO 用户登录数据
     * @return 用户信息
     */
    public User login(UserLoginDTO userLoginDTO) {
        Map<String, String> map = new HashMap<>();;
       map.put("appid", weChatProperties.getAppid());
       map.put("secret", weChatProperties.getSecret());
       map.put("js_code", userLoginDTO.getCode());
       map.put("grant_type", "authorization_code");

       String json= HttpClientUtil.doGet(WX_LOGIN,map);
        String openid=JSON.parseObject(json).getString("openid");
        if(openid==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }
        User user=userMapper.getUserByOpenid(openid);
        if(user==null) {
            user=User.builder()
                    .openid(openid)
                    .createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
             log.info("注册用户，用户信息：{}", user);
             return user;

        }


        return user;
    }
}
