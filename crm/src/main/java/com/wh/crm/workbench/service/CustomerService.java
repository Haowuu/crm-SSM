package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {

    List<Customer> queryCustomerByConditionForPageList(Map<String, Object> map);

    int queryCustomerTotalByCondition(Map<String, Object> map);

    int saveCreateCustomer(Customer customer);

    int deleteCustomerByIds(String[] id);

    Customer queryCustomerByIdToEdit(String id);

    int saveEditCustomer(Customer customer);

    Customer queryCustomerForDetail(String id);

    List<String> queryCustomerNameByName(String name);

    Customer queryCustomerByNameForcreateTran(String customerName);
}
