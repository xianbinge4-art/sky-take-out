package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryDTO;
import com.sky.entity.Category;
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
    public Result<List<Category>> list(Integer type){
     List<Category>  list= categoryService.list(type);
     return  Result.success(list);
    }



@PostMapping()
    @ApiOperation("新增分类相关代码")
    public Result add(@RequestBody CategoryDTO categoryDTO){
        log.info("新增的分类的类型{}",categoryDTO.getName());
        categoryService.add(categoryDTO);
        return Result.success();
}




@DeleteMapping()
    @ApiOperation("根据id删除分类相关代码")
    public Result delete(@RequestParam("id") Integer id){
     log.info("需要删除的分类的id是{}",id);
     categoryService.delete(id);
     return Result.success();
}


@PostMapping("/status/{status}")
    @ApiOperation("根据id启用或者禁用分类状态")
    public Result status(@PathVariable("status") Integer status,@RequestParam("id") Integer id){
        log.info("操作的分类的id是{}",id);
        categoryService.startOrStop(status,id);
        return Result.success();
}

@PutMapping()
    @ApiOperation("修改分类的相关代码")
    public Result update(@RequestBody CategoryDTO categoryDTO){
  log.info("要修改的员工的id是{}",categoryDTO.getId());
  categoryService.update(categoryDTO);
    return Result.success();




}








}
