package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {
@AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


    List<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    void delete( @Param("ids") List<Integer> ids);

    @Select("SELECT * FROM dish WHERE id IN (#{id})")
    Dish getById(Integer id);
}

