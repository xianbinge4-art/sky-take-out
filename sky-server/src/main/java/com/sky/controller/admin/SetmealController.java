package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.exception.BaseException;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/setmeal")
@Slf4j
@Api(tags = "管理端-套餐相关接口")
public class SetmealController {


    @Autowired
    private SetmealService setmealService;

    @GetMapping("/{id}")
    @ApiOperation("根据 id 查询套餐信息接口")
    /**
     * 根据套餐 id 查询套餐信息。
     *
     * @param id 套餐 id
     * @return 套餐信息
     */
    public Result<SetmealVO> getSetmealById(@PathVariable Integer id) {
        SetmealVO setmealVO = setmealService.findById(id);

        return Result.success(setmealVO);
    }


    @PostMapping()
    @ApiOperation("新增套餐接口")
    @CacheEvict(value = "setmealCache", key = "#setmealDTO.categoryId")
    public Result<String> saveSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.saveSetmeal(setmealDTO);
        return Result.success("新增套餐成功");
    }


    @DeleteMapping()
    @ApiOperation("删除套餐接口")
    @CacheEvict(value = "setmealCache",allEntries = true)
    public Result<String> deleteSetmeal(@RequestParam("ids") List<Integer> ids) {
        setmealService.deleteSetmeal(ids);
        return Result.success("删除套餐成功");
    }


    @PostMapping("/status/{status}")
    @ApiOperation("修改套餐起售停售接口")
    @CacheEvict(value = "setmealCache",allEntries = true)
    public Result<String> updateSetmealStatus(@PathVariable Integer status, @RequestParam("id") Integer id) {
        setmealService.updateStatus(status, id);
        return Result.success("修改套餐状态成功");
    }


    @GetMapping("/page")
    @ApiOperation("分页查询套餐接口")
    public Result<PageResult> pageSetmeal(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageResult pageResult = setmealService.page(setmealPageQueryDTO);
        return Result.success(pageResult);
    }


    @PutMapping()
    @ApiOperation("修改套餐接口")
    @CacheEvict(value = "setmealCache",allEntries = true)
    public Result<String> updateSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.updateSetmeal(setmealDTO);
        return Result.success("修改套餐成功");


    }
}
