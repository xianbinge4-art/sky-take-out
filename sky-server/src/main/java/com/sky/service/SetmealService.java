package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetmealService {

    SetmealVO findById(Integer id);

    void saveSetmeal(SetmealDTO setmealDTO);

    void deleteSetmeal(List<Integer> ids);

    void updateStatus(Integer status, Integer id);

    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void updateSetmeal(SetmealDTO setmealDTO);
}
