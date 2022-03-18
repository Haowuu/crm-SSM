package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.CustomerRemark;

import java.util.List;

public interface CustomerRemarkService {
    List<CustomerRemark> queryCustomerRemarkByCustomerId(String id);

    int saveCreateCustomerRemark(CustomerRemark customerRemark);
}
