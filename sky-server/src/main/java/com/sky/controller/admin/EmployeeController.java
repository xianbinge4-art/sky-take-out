package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

    EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                        .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }





    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }






@ApiOperation("新增员工")
    @PostMapping()
    /**
     * 新增员工账号。
     *
     * @param employeeDTO 员工提交数据
     * @return 通用成功结果
     */
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
    log.info("员工的信息{}", employeeDTO);
    employeeService.save(employeeDTO);
    return Result.success();
    }
   @ApiOperation("员工分页查询")
   @GetMapping("/page")
        /**
         * 分页查询员工列表。
         *
         * @param employeePageQueryDTO 员工分页查询条件
         * @return 员工分页结果
         */
        public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        PageResult pageResult = employeeService.page(employeePageQueryDTO);
        return Result.success(pageResult);
    }

@PostMapping("/status/{status}")
@ApiOperation("启用禁用员工账号")
    /**
     * 启用或禁用员工账号。
     *
     * @param status 目标状态
     * @param id 员工 id
     * @return 通用成功结果
     */
    public  Result startOrStop(@PathVariable Integer status ,Long id){
        log.info("启用禁用员工账号的状态为,{},员工id是{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }
    @GetMapping("{id}")
    @ApiOperation("根据id查询员工信息")
    /**
     * 根据 id 查询员工信息。
     *
     * @param id 员工 id
     * @return 员工信息
     */
    public Result<Employee> getById(@PathVariable Long id){
        log.info("要查询的员工的id是{}",id);
        Employee employee=employeeService.findById(id);
        return Result.success(employee);
    }


    @PutMapping()
    @ApiOperation("根据id修改员工")
    /**
     * 修改员工信息。
     *
     * @param employeeDTO 员工修改数据
     * @return 通用成功结果
     */
    public Result update(@RequestBody EmployeeDTO employeeDTO){
    log.info("需要修改的员工的id是{}",employeeDTO.getId());
        employeeService.update(employeeDTO);


        return Result.success();
    }



}
