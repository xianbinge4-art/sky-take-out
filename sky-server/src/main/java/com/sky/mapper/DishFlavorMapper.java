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

    /**
     * 批量新增菜品口味。
     *
     * @param flavors 菜品口味列表
     */
    void insertBatch( @Param("list")  List<DishFlavor> flavors);

    /**
     * 根据菜品 id 集合批量删除口味。
     *
     * @param ids 菜品 id 集合
     */
    void delete(@Param("ids") List<Integer> ids);


    /**
     * 根据菜品 id 查询口味列表。
     *
     * @param id 菜品 id
     * @return 口味列表
     */
    @Select("SELECT * FROM dish_flavor WHERE dish_id = #{id}")
    List<DishFlavor> getByDishId(Integer id);
}
