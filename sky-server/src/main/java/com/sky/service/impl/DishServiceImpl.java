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
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl implements DishService {
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 新增菜品并批量保存菜品口味。
     *
     * @param dishDTO 菜品提交数据
     */
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

    /**
     * 根据查询条件分页查询菜品列表。
     *
     * @param dishPageQueryDTO 菜品分页查询条件
     * @return 菜品分页结果
     */
    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        List<DishVO> dishes = dishMapper.page(dishPageQueryDTO);
        PageInfo<DishVO> pageInfo = new PageInfo<>(dishes);
        PageResult pageResult = new PageResult(pageInfo.getTotal(), pageInfo.getList());
        return pageResult;
    }

    /**
     * 批量删除菜品，删除前校验售卖状态和套餐关联关系。
     *
     * @param ids 待删除的菜品 id 集合
     */
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

    /**
     * 查询菜品详情并组装口味列表。
     *
     * @param id 菜品 id
     * @return 菜品详情
     */
    @Override
    public DishVO getById(Integer id) {
        Dish dish = dishMapper.getById(id);
        if (dish == null) {
            throw new IllegalArgumentException("没有查询到对应的菜品");
        }
        DishVO dishVO = new DishVO();
        BeanUtils.copyProperties(dish, dishVO);
        List<DishFlavor> flavors = dishFlavorMapper.getByDishId(id);
        dishVO.setFlavors(flavors);
        return dishVO;
    }

    /**
     * 修改菜品基础信息并重建口味列表。
     *
     * @param dishDTO 菜品修改数据
     */
    @Override
    @Transactional
    public void update(DishDTO dishDTO) {
       Dish dish=dishMapper.getById( dishDTO.getId().intValue());
         if(dish==null){
              throw new IllegalArgumentException("没有查询到对应的菜品");
         }
          BeanUtils.copyProperties(dishDTO, dish);
        dishFlavorMapper.delete(List.of(dishDTO.getId().intValue()));
         List<DishFlavor> flavors = dishDTO.getFlavors();
         if (flavors != null && !flavors.isEmpty()) {
             for (DishFlavor flavor : flavors) {
                 flavor.setDishId(dish.getId());
             }
         }
         dishFlavorMapper.insertBatch(flavors);
         dishMapper.update(dish);


    }

    @Override
    public List<DishVO> getByCategoryId(Integer categoryId) {

      List<Dish> dish=dishMapper.getByCategoryId(categoryId);
      List<DishVO> dishVOS=new ArrayList<>();
      for (Dish dish1 : dish) {
          DishVO dishVO = new DishVO();
          BeanUtils.copyProperties(dish1, dishVO);
          List<DishFlavor> list=dishFlavorMapper.getByDishId(dish1.getId().intValue());
          dishVO.setFlavors(list);
          dishVOS.add(dishVO);
      }
      return dishVOS;
    }



    /**
     * 条件查询菜品和口味
     * @param dish
     * @return
     */
    public List<DishVO> listWithFlavor(Dish dish) {
        List<Dish> dishList = dishMapper.list(dish);

        List<DishVO> dishVOList = new ArrayList<>();

        for (Dish d : dishList) {
            DishVO dishVO = new DishVO();
            BeanUtils.copyProperties(d,dishVO);

            //根据菜品id查询对应的口味
            List<DishFlavor> flavors = dishFlavorMapper.getByDishId(d.getId().intValue());

            dishVO.setFlavors(flavors);
            dishVOList.add(dishVO);
        }

        return dishVOList;
    }
}
