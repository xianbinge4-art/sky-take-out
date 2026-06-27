package com.sky.service;

import com.sky.entity.AddressBook;

import java.util.List;

public interface AddressBookService {
    List<AddressBook> list(AddressBook addressBook);

    void delete(Long ids);

    AddressBook getById(Long id);

    void updateById(AddressBook addressBook);

    void save(AddressBook addressBook);

    void setDefault(AddressBook addressBook);

    AddressBook getDefaultAddress();
}
