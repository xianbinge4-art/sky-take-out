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

    @GetMapping("/list")
    @ApiOperation(value = "查询地址簿")
    public Result<List<AddressBook>> list() {
        log.info("查询地址簿");
        AddressBook addressBook = new AddressBook();
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.list(addressBook);
        return Result.success(list);
    }


    @DeleteMapping
    @ApiOperation(value = "删除地址簿")
    public Result<String> delete(Long ids) {
        log.info("删除地址簿");
        addressBookService.delete(ids);
        return Result.success("删除成功");
    }

    @PutMapping
    @ApiOperation(value = "修改地址簿")
    public Result update(@RequestBody AddressBook addressBook) {
        log.info("修改地址簿的信息是{}", addressBook);
        addressBookService.updateById(addressBook);

        return Result.success("修改成功");
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询地址簿")
    public Result<AddressBook> getById(@PathVariable Long id) {
        log.info("根据id查询地址簿");
        AddressBook addressBook = addressBookService.getById(id);
        return Result.success(addressBook);

    }

    @PostMapping
    @ApiOperation(value = "新增地址簿")
    public Result<String> save(@RequestBody AddressBook addressBook) {
        log.info("新增地址簿");
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return Result.success("新增地址簿成功");
    }

    /**
     * 设置默认地址
     *
     * @param addressBook
     * @return
     */
    @PutMapping("/default")
    @ApiOperation("设置默认地址")
    public Result setDefault(@RequestBody AddressBook addressBook) {
        addressBookService.setDefault(addressBook);
        return Result.success();
    }

    /**
     * 查询当前用户默认地址
     */
    @GetMapping("/default")
    @ApiOperation("查询默认收货地址")
    public Result<AddressBook> getDefaultAddress() {
        log.info("查询当前用户默认地址");
        AddressBook addressBook = addressBookService.getDefaultAddress();
        return Result.success(addressBook);
    }



}
