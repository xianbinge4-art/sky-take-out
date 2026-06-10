package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    @Override
    @Transactional
    public void saveWithFlavor(DishDTO dishDTO) {
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insert(dish);
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && !flavors.isEmpty()) {
            for (DishFlavor flavor : flavors) {
                flavor.setDishId(dish.getId());
            }
            dishFlavorMapper.insertBatch(flavors);
        }

    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        List<DishVO> dishes = dishMapper.page(dishPageQueryDTO);
        PageInfo<DishVO> pageInfo = new PageInfo<>(dishes);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }

    @Override
    @Transactional
    public void delete(List<Integer> ids) {

        //首先判断是否为空
        if(ids==null||ids.size()==0){
            throw new IllegalArgumentException("请选择要删除的菜品");
        }
        //再判断是否有菜品在售卖
        for (Integer id : ids) {
          Dish dish= dishMapper.getById(id);
          if(dish.getStatus()== StatusConstant.ENABLE) {
              throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
          }
        }
        //然后查询是否和套餐相关联
       List<Integer> setmealIds = setmealDishMapper.getSetmealIdsByDishIds(ids);
        if(setmealIds!=null&&setmealIds.size()>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //如果没有关联就删除菜品和口味
        dishMapper.delete(ids);
        dishFlavorMapper.delete(ids);


    }
}
