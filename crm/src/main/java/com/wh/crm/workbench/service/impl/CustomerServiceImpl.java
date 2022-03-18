package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.Customer;
import com.wh.crm.workbench.mapper.CustomerMapper;
import com.wh.crm.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @createTime 2022/3/15  9:48
 * @description
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public List<Customer> queryCustomerByConditionForPageList(Map<String, Object> map) {
        return customerMapper.queryCustomerByConditionForPageList(map);
    }

    @Override
    public int queryCustomerTotalByCondition(Map<String, Object> map) {
        return customerMapper.queryCustomerTotalByCondition(map);
    }

    @Override
    public int saveCreateCustomer(Customer customer) {
        return customerMapper.saveCreateCustomer(customer);
    }

    @Override
    public int deleteCustomerByIds(String[] id) {

        return customerMapper.deleteCustomerByIds(id);
    }

    @Override
    public Customer queryCustomerByIdToEdit(String id) {
        return customerMapper.queryCustomerByIdToEdit(id);
    }

    @Override
    public int saveEditCustomer(Customer customer) {
        return customerMapper.saveEditCustomer(customer);
    }

    @Override
    public Customer queryCustomerForDetail(String id) {
        return customerMapper.queryCustomerForDetail(id);
    }

    @Override
    public List<String> queryCustomerNameByName(String name) {

        return customerMapper.queryCustomerNameByName(name);
    }

    @Override
    public Customer queryCustomerByNameForcreateTran(String customerName) {
        return customerMapper.queryCustomerByNameForcreateTran(customerName);
    }

}
