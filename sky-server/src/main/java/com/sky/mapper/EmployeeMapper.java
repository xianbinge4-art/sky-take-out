package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username 用户名
     * @return 员工信息
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 新增员工。
     *
     * @param employee 员工实体
     */
    @AutoFill(OperationType.INSERT)
    @Insert("insert into employee(name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status) " +
            "values(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void saveEmp(Employee employee);

    /**
     * 根据条件查询员工列表。
     *
     * @param employeePageQueryDTO 员工分页查询条件
     * @return 员工列表
     */
    List<Employee> list(@Param("dto") EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 修改员工状态。
     *
     * @param employee 员工实体
     */
    @AutoFill(OperationType.UPDATE)
    void startOrStop(Employee employee);

    /**
     * 根据 id 查询员工。
     *
     * @param id 员工 id
     * @return 员工实体
     */
    @Select("select id,name,username,password,phone,sex,id_number,create_time,update_time,create_user,update_user,status from employee where id=#{id}")
    Employee findById(Long id);

    /**
     * 修改员工信息。
     *
     * @param employee 员工实体
     */
    @AutoFill(OperationType.UPDATE)
    void update(Employee employee);
}
