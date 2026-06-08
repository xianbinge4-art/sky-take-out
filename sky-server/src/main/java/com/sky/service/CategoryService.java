package com.sky.service;

import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
import com.sky.mapper.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface CategoryService {

    public List<Category> list(Integer type);


    void add(CategoryDTO categoryDTO);

    void delete(Integer id);

    void startOrStop(Integer status, Integer id);

    void update(CategoryDTO categoryDTO);
}
