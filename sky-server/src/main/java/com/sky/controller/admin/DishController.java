package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Slf4j
@Api(tags = "菜品相关接口")
public class DishController {
    @Autowired
    private DishService dishService;

    @ApiOperation("新增菜品")
    @PostMapping
     /**
      * 新增菜品及其口味信息。
      *
      * @param dishDTO 菜品提交数据
      * @return 通用成功结果
      */
     public Result save( @RequestBody DishDTO dishDTO){
         log.info("新增菜品的信息是:{}", dishDTO);
         dishService.saveWithFlavor(dishDTO);
         return Result.success();
     }
     @GetMapping("/page")
     @ApiOperation("菜品分页查询")
    /**
     * 分页查询菜品列表。
     *
     * @param dishPageQueryDTO 菜品分页查询条件
     * @return 菜品分页结果
     */
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
            log.info("菜品分页查询的参数是:{}", dishPageQueryDTO);
         PageResult pageResult=dishService.page(dishPageQueryDTO);
         return Result.success(pageResult);
     }
     @ApiOperation("菜品删除")
    @DeleteMapping
     /**
      * 根据菜品 id 批量删除菜品。
      *
      * @param ids 待删除的菜品 id 集合
      * @return 通用成功结果
      */
     public Result delete( @RequestParam("ids") List<Integer> ids){
         log.info("删除菜单的id是:{}", ids);
         dishService.delete(ids);
         return Result.success();
     }


     @ApiOperation("菜品修改")
    @PutMapping
     /**
      * 修改菜品及其口味信息。
      *
      * @param dishDTO 菜品修改数据
      * @return 通用成功结果
      */
     public Result update(@RequestBody DishDTO dishDTO){
        dishService.update(dishDTO);
          return Result.success();



     }

    @ApiOperation("根据菜品id查询菜品")
    @GetMapping("{id}")
     /**
      * 根据菜品 id 查询菜品详情。
      *
      * @param id 菜品 id
      * @return 菜品详情
      */
     public Result<DishVO> getById(@PathVariable Integer id){
         log.info("根据id查询菜品，id是:{}", id);
         DishVO dish=dishService.getById(id);
         return Result.success(dish);
     }


    @ApiOperation("根据分类id查询")
    @GetMapping("/list")
    public Result<List<DishVO>> getByCategoryId(@RequestParam("categoryId") Integer categoryId){
        log.info("根据分类id查询菜品，id是:{}", categoryId);
        List<DishVO> dish=dishService.getByCategoryId(categoryId);
        return Result.success(dish);



    }





}
