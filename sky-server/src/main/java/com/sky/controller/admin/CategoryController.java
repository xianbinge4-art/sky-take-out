package com.sky.controller.admin;


import com.github.pagehelper.PageInfo;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/admin/category")
@Api(tags = "分类管理相关代码")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;



    @ApiOperation("根据类型查询分类")
   @GetMapping("/list")
    /**
     * 根据分类类型查询分类列表。
     *
     * @param type 分类类型
     * @return 分类列表
     */
    public Result<List<Category>> list(Integer type){
     List<Category>  list= categoryService.list(type);
     return  Result.success(list);
    }



    @GetMapping("/page")
    @ApiOperation("分页查询相关代码")
    /**
     * 分页查询分类列表。
     *
     * @param categoryPageQueryDTO 分类分页查询条件
     * @return 分类分页结果
     */
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){
        PageResult pageResult=categoryService.page(categoryPageQueryDTO);
        return  Result.success(pageResult);




    }



@PostMapping()
    @ApiOperation("新增分类相关代码")
    /**
     * 新增分类。
     *
     * @param categoryDTO 分类提交数据
     * @return 通用成功结果
     */
    public Result add(@RequestBody CategoryDTO categoryDTO){
        log.info("新增的分类的类型{}",categoryDTO.getName());
        categoryService.add(categoryDTO);
        return Result.success();
}




@DeleteMapping()
    @ApiOperation("根据id删除分类相关代码")
    /**
     * 根据 id 删除分类。
     *
     * @param id 分类 id
     * @return 通用成功结果
     */
    public Result delete(@RequestParam("id") Integer id){
     log.info("需要删除的分类的id是{}",id);
     categoryService.delete(id);
     return Result.success();
}


@PostMapping("/status/{status}")
    @ApiOperation("根据id启用或者禁用分类状态")
    /**
     * 启用或禁用分类。
     *
     * @param status 目标状态
     * @param id 分类 id
     * @return 通用成功结果
     */
    public Result status(@PathVariable("status") Integer status,@RequestParam("id") Integer id){
        log.info("操作的分类的id是{}",id);
        categoryService.startOrStop(status,id);
        return Result.success();
}

@PutMapping()
    @ApiOperation("修改分类的相关代码")
    /**
     * 修改分类信息。
     *
     * @param categoryDTO 分类修改数据
     * @return 通用成功结果
     */
    public Result update(@RequestBody CategoryDTO categoryDTO){
  log.info("要修改的员工的id是{}",categoryDTO.getId());
  categoryService.update(categoryDTO);
    return Result.success();




}










}
