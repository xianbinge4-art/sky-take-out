package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SetmealMapper {

  @Select("SELECT * FROM setmeal WHERE id = #{id}")
    Setmeal findById(Integer id);
  @AutoFill(
         OperationType.INSERT
  )
  @Insert("INSERT INTO setmeal (category_id, name, price, status, description, image, create_time, update_time, create_user, update_user) " +
          "VALUES (#{categoryId}, #{name}, #{price}, #{status}, #{description}, #{image}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Setmeal setmeal);
@Delete("DELETE FROM setmeal WHERE id = #{id}")
    void deleteById(Integer id);
    @AutoFill(OperationType.UPDATE)
    @Update("UPDATE setmeal SET status = #{status}, update_time = #{updateTime}, update_user = #{updateUser} WHERE id = #{id}")
    void updateStatus(Setmeal setmeal);

    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    List<Setmeal> page(SetmealPageQueryDTO setmealPageQueryDTO);
    /**
     * 动态条件查询套餐
     * @param setmeal
     * @return
     */
    List<Setmeal> list(Setmeal setmeal);




    /**
     * 根据套餐id查询菜品选项
     * @param setmealId
     * @return
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
