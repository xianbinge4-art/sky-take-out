package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insertBatch( @Param("list")  List<DishFlavor> flavors);

    void delete(@Param("ids") List<Integer> ids);


    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{id}")
    List<DishFlavor> getByDishId(Integer id);
}
