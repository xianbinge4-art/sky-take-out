package com.sky.service;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.vo.DishVO;

import java.util.List;

public interface DishService {

    /**
     * 新增菜品并保存对应口味。
     *
     * @param dishDTO 菜品提交数据
     */
    void saveWithFlavor(DishDTO dishDTO);

    /**
     * 分页查询菜品。
     *
     * @param dishPageQueryDTO 菜品分页查询条件
     * @return 分页结果
     */
    PageResult page(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品。
     *
     * @param ids 菜品 id 集合
     */
    void delete(List<Integer> ids);

    /**
     * 根据 id 查询菜品详情。
     *
     * @param id 菜品 id
     * @return 菜品详情
     */
    DishVO getById(Integer id);

    /**
     * 修改菜品及其口味。
     *
     * @param dishDTO 菜品修改数据
     */
    void update(DishDTO dishDTO);
 /*根据分类id查询菜品信息*/

    List<DishVO> getByCategoryId(Integer categoryId);
}
