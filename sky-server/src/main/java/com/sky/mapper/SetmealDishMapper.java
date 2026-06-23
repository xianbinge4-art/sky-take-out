package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 根据菜品 id 集合查询关联的套餐 id。
     *
     * @param ids 菜品 id 集合
     * @return 套餐 id 集合
     */
    List<Integer> getSetmealIdsByDishIds(@Param("ids") List<Integer> ids);;

    List<SetmealDish> getDishBySetmealId(long id);

@Insert("INSERT INTO setmeal_dish (setmeal_id, dish_id, name, price, copies) " +
        "VALUES (#{setmealId}, #{dishId}, #{name}, #{price}, #{copies})")
    void insert(SetmealDish setmealDish);



@Delete("DELETE FROM setmeal_dish WHERE id = #{id}")
    void deleteById(Long id);

@Delete("DELETE FROM setmeal_dish WHERE setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);


}
