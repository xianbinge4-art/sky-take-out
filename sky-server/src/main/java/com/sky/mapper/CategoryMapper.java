package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Category;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CategoryMapper {

    List<Category> list(Integer type);

    @AutoFill(OperationType.INSERT)
    void add(Category category);

    @Delete("delete from category where id = #{id}")
    void delete(Integer id);

    @AutoFill(OperationType.UPDATE)
    void updateStatus(Category category);

    @AutoFill(OperationType.UPDATE)
    void update(Category category);
}
