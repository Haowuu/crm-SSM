package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.Contacts;

import java.util.List;

public interface ContactsService {
    List<Contacts> queryContactsByCustomerIdForCustomerDetail(String id);

    List<Contacts> queryContactsByNameForCreateTran(String fullname);
}
