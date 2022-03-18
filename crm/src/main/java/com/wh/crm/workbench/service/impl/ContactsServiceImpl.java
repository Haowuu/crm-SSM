package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.Contacts;
import com.wh.crm.workbench.mapper.ContactsMapper;
import com.wh.crm.workbench.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/16  10:42
 * @description
 */
@Service
public class ContactsServiceImpl implements ContactsService {
    @Autowired
    private ContactsMapper contactsMapper;

    @Override
    public List<Contacts> queryContactsByCustomerIdForCustomerDetail(String id) {
        return contactsMapper.queryContactsByCustomerIdForCustomerDetail(id);
    }

    @Override
    public List<Contacts> queryContactsByNameForCreateTran(String fullname) {
        return contactsMapper.queryContactsByNameForCreateTran(fullname);
    }

}
