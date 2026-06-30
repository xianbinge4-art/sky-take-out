package com.sky.controller.user;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.sky.context.BaseContext;
import com.sky.entity.AddressBook;
import com.sky.result.Result;
import com.sky.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/addressBook")
@Slf4j
@Api(tags = "c端-地址簿管理接口")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;

    /**
     * 查询当前登录用户的所有收货地址
     * @return  当前用户的地址列表
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询地址簿")
    public Result<List<AddressBook>> list() {
        log.info("查询地址簿");
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId()); // 从线程上下文取出当前登录用户 id
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }

    /**
     * 根据 id 删除一条收货地址
     * @param ids  地址 id（参数名按前端约定写为 ids）
     */
    @DeleteMapping
    @ApiOperation(value = "删除地址簿")
    public Result<String> delete(Long ids) {
        log.info("删除地址簿");
        addressBookService.delete(ids);
        return Result.success("删除成功");
    }

    /**
     * 修改一条收货地址的信息
     * @param addressBook  地址 id + 修改后的字段
     */
    @PutMapping
    @ApiOperation(value = "修改地址簿")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("修改地址簿的信息是{}", addressBook);
        addressBookService.updateById(addressBook);
        return Result.success("修改成功");
    }

    /**
     * 根据 id 查询单条地址（用于修改时的回显）
     * @param id  地址 id
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询地址簿")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据id查询地址簿");
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);
    }

    /**
     * 新增一条收货地址，会自动与当前登录用户关联
     * @param addressBook  收货人、手机号、省市区、详细地址、标签、是否默认等字段
     */
    @PostMapping
    @ApiOperation(value = "新增地址簿")
    public Result<String> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址簿");
        addressBook.setUserId(BaseContext.getCurrentId()); // 从线程上下文取出当前登录用户 id
        addressBookService.save(addressBook);
        return Result.success("新增地址簿成功");
    }

    /**
     * 设置默认收货地址
     * 业务逻辑：先把当前用户所有地址的 isDefault 置为 0，再把指定地址置为 1
     * @param addressBook  要设为默认的地址 id
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 查询当前登录用户的默认收货地址（下单时优先使用）
     */
    @GetMapping("/default")
    @ApiOperation("查询默认收货地址")
    public Result<AddressBook> getDefaultAddress() {
        log.info("查询当前用户默认地址");
        AddressBook addressBook = addressBookService.getDefaultAddress();
        return Result.success(addressBook);
    }

}