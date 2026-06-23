package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    /**
     * 根据 openid 查询用户。
     *
     * @param openid 微信用户 openid
     * @return 用户实体
     */
    @Select("SELECT * FROM user WHERE openid = #{openid}")
    User getUserByOpenid(String openid);

    /**
     * 新增用户。
     *
     * @param user 用户实体
     */
    void insert(User user);
}
