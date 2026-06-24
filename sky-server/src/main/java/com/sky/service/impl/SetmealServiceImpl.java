package com.sky.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.BaseException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.SetmealEnableFailedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
@Autowired
SetmealMapper setmealMapper;
@Autowired
SetmealDishMapper setmealDishMapper;
@Autowired
DishMapper dishMapper;


    @Override
    public SetmealVO findById(Integer id) {
        Setmeal setmeal=setmealMapper.findById(id);
        if (setmeal == null) {
            throw new BaseException("套餐不存在");
        }
        SetmealVO setmealVO=new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);
          List<SetmealDish> setmealDishes = setmealDishMapper.getDishBySetmealId(id);
        setmealVO.setSetmealDishes(setmealDishes);
        return setmealVO;
    }

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void saveSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        //保存套餐信息
        setmealMapper.insert(setmeal);
        //保存套餐菜品关联信息
        List<SetmealDish> setmealDishes=setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.insert(setmealDish);
        }
    }

    @Override
    public void deleteSetmeal(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) {
            throw new BaseException("套餐id不能为空");
        }

        //查询套餐是否正在起售
        for (Integer id : ids) {
            Setmeal setmeal=setmealMapper.findById(id);
            if (setmeal.getStatus()==1) {
                throw  new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        for (Integer id : ids) {
            setmealMapper.deleteById(id);
            //删除套餐菜品关联信息
            List<SetmealDish> setmealDishes = setmealDishMapper.getDishBySetmealId(id);
            for (SetmealDish setmealDish : setmealDishes) {
                setmealDishMapper.deleteById(setmealDish.getId());
            }
        }



    }

    @Override
    public void updateStatus(Integer status, Integer id) {
        Setmeal setmeal=setmealMapper.findById(id);
        if (setmeal == null) {
            throw new BaseException("套餐不存在");
        }
        if (StatusConstant.ENABLE.equals(status)) {
            List<SetmealDish> setmealDishes = setmealDishMapper.getDishBySetmealId(id);
            for (SetmealDish setmealDish : setmealDishes) {
                Dish dish = dishMapper.getById(setmealDish.getDishId().intValue());
                if (dish == null || StatusConstant.DISABLE.equals(dish.getStatus())) {
                    throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            }
        }
        setmeal.setStatus(status);
        setmealMapper.updateStatus(setmeal);
    }

    @Override
    public PageResult page(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());
        List<Setmeal> setmealList=setmealMapper.page(setmealPageQueryDTO);
        PageInfo<Setmeal> pageInfo = new PageInfo<>(setmealList);
        PageResult pageResult=new PageResult(pageInfo.getTotal(),pageInfo.getList());
        return pageResult;
    }

    @Override
    @Transactional(rollbackFor = BaseException.class)
    public void updateSetmeal(SetmealDTO setmealDTO) {
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setmealMapper.update(setmeal);
        //删除原有的套餐菜品关联信息
        setmealDishMapper.deleteBySetmealId(setmeal.getId());
        //保存新的套餐菜品关联信息
        List<SetmealDish> newSetmealDishes=setmealDTO.getSetmealDishes();
        for (SetmealDish newSetmealDish : newSetmealDishes) {
            newSetmealDish.setSetmealId(setmeal.getId());
            setmealDishMapper.insert(newSetmealDish);
        }
    }



    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }


    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }
}
