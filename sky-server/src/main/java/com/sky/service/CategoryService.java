package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    /**
     * 根据类型查询分类列表。
     *
     * @param type 分类类型
     * @return 分类列表
     */
    public List<Category> list(Integer type);


    /**
     * 新增分类。
     *
     * @param categoryDTO 分类提交数据
     */
    void add(CategoryDTO categoryDTO);

    /**
     * 根据 id 删除分类。
     *
     * @param id 分类 id
     */
    void delete(Integer id);

    /**
     * 启用或禁用分类。
     *
     * @param status 目标状态
     * @param id 分类 id
     */
    void startOrStop(Integer status, Integer id);

    /**
     * 修改分类。
     *
     * @param categoryDTO 分类修改数据
     */
    void update(CategoryDTO categoryDTO);

    /**
     * 分页查询分类。
     *
     * @param categoryPageQueryDTO 分类分页查询条件
     * @return 分页结果
     */
    PageResult page(CategoryPageQueryDTO categoryPageQueryDTO);
}
