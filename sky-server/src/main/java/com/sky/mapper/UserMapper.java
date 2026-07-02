package com.sky.mapper;

import com.sky.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Map;

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


@Select("select * from user where id=#{userId}")
    User getById(Long userId);

    Integer getByMap(Map map);
}

