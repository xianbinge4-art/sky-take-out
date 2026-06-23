package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.StatusConstant;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    /**
     * 根据类型查询分类列表。
     *
     * @param type 分类类型
     * @return 分类列表
     */
    public List<Category> list(Integer type) {
        return categoryMapper.list(type);
    }

    @Override
    /**
     * 新增分类并设置默认状态。
     *
     * @param categoryDTO 分类提交数据
     */
    public void add(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setStatus(StatusConstant.ENABLE);
        categoryMapper.add(category);
    }

    @Override
    /**
     * 根据 id 删除分类。
     *
     * @param id 分类 id
     */
    public void delete(Integer id) {
        categoryMapper.delete(id);
    }

    @Override
    /**
     * 修改分类启用或禁用状态。
     *
     * @param status 目标状态
     * @param id 分类 id
     */
    public void startOrStop(Integer status, Integer id) {
        Category category = Category.builder()
                .id(id.longValue())
                .status(status)
                .build();
        categoryMapper.updateStatus(category);
    }

    @Override
    /**
     * 修改分类信息。
     *
     * @param categoryDTO 分类修改数据
     */
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        categoryMapper.update(category);
    }

    @Override
    /**
     * 分页查询分类列表。
     *
     * @param categoryPageQueryDTO 分类分页查询条件
     * @return 分类分页结果
     */
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());
        List<Category> empList=categoryMapper.page(categoryPageQueryDTO);
        PageInfo <Category> pageInfo = new PageInfo<>(empList);
        PageResult pageResult = new PageResult(pageInfo.getTotal(),pageInfo.getList());
        return pageResult;
    }
}
