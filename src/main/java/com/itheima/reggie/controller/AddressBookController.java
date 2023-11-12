package com.itheima.reggie.controller;

import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.R;
import com.itheima.reggie.pojo.AddressBook;
import com.itheima.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 地址管理
 */
@Slf4j
@RestController
@RequestMapping("addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 新增
     */
    @PostMapping
    public R save(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 设置默认地址
     */
    @PutMapping("default")
    public R setDefault(@RequestBody AddressBook addressBook) {
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update()
                .eq("user_id", BaseContext.getCurrentId())
                .set("is_default", 0)
                .update();
        //SQL:update address_book set is_default = 1 where id = ?
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    /**
     * 查询默认地址
     */
    @GetMapping("default")
    public R getDefault() {
        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.query()
                .eq("user_id", BaseContext.getCurrentId())
                .eq("is_default", 1)
                .one();
        return R.success(addressBook);
    }

    /**
     * 查询指定用户的全部地址
     */
    @GetMapping("/list")
    public R list(AddressBook addressBook) {
        //SQL:select * from address_book where user_id = ? order by update_time desc
        addressBook.setUserId(BaseContext.getCurrentId());
        List<AddressBook> list = addressBookService.query()
                .eq("user_id", addressBook.getUserId())
                .orderByDesc("update_time")
                .list();
        return R.success(list);
    }
}
