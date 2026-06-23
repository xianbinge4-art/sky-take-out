package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 新增员工。
     *
     * @param employeeDTO 员工提交数据
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 分页查询员工。
     *
     * @param employeePageQueryDTO 员工分页查询条件
     * @return 分页结果
     */
    PageResult page(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 启用或禁用员工账号。
     *
     * @param status 目标状态
     * @param id 员工 id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据 id 查询员工。
     *
     * @param id 员工 id
     * @return 员工信息
     */
    Employee findById(Long id);

    /**
     * 修改员工信息。
     *
     * @param employeeDTO 员工修改数据
     */
       void update(EmployeeDTO employeeDTO);
}
