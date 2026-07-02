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
import java.util.Map;

@Mapper
public interface DishMapper {

    /**
     * 新增菜品基础信息。
     *
     * @param dish 菜品实体
     */
@AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);


    /**
     * 根据条件分页查询菜品。
     *
     * @param dishPageQueryDTO 菜品分页查询条件
     * @return 菜品列表
     */
    List<DishVO> page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 根据条件查询菜品。
     *
     * @param dish 菜品查询条件
     * @return 菜品列表
     */
    List<Dish> list(Dish dish);

    /**
     * 根据 id 集合批量删除菜品。
     *
     * @param ids 菜品 id 集合
     */
    void delete( @Param("ids") List<Integer> ids);

    /**
     * 根据 id 查询菜品。
     *
     * @param id 菜品 id
     * @return 菜品实体
     */
    @Select("SELECT * FROM dish WHERE id IN (#{id})")
    Dish getById(Integer id);

    /**
     * 修改菜品基础信息。
     *
     * @param dish 菜品实体
     */
@AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);


   @Select("SELECT * FROM dish WHERE category_id = #{categoryId} AND status = 1")
    List<Dish> getByCategoryId(Integer categoryId);


    /**
     * 根据条件统计菜品数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
