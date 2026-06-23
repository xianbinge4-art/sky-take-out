package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    /**
     * 根据类型查询分类列表。
     *
     * @param type 分类类型
     * @return 分类列表
     */
    List<Category> list(Integer type);

    /**
     * 新增分类。
     *
     * @param category 分类实体
     */
    @AutoFill(OperationType.INSERT)
    void add(Category category);

    /**
     * 根据 id 删除分类。
     *
     * @param id 分类 id
     */
    @Delete("delete from category where id = #{id}")
    void delete(Integer id);

    /**
     * 修改分类状态。
     *
     * @param category 分类实体
     */
    @AutoFill(OperationType.UPDATE)
    void updateStatus(Category category);

    /**
     * 修改分类信息。
     *
     * @param category 分类实体
     */
    @AutoFill(OperationType.UPDATE)
    void update(Category category);

    /**
     * 分页查询分类列表。
     *
     * @param categoryPageQueryDTO 分类分页查询条件
     * @return 分类列表
     */
    List<Category>  page(CategoryPageQueryDTO categoryPageQueryDTO);
}
