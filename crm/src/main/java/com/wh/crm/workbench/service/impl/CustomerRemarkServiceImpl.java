package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.CustomerRemark;
import com.wh.crm.workbench.mapper.CustomerRemarkMapper;
import com.wh.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/16  0:43
 * @description
 */
@Service
public class CustomerRemarkServiceImpl implements CustomerRemarkService {
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Override
    public List<CustomerRemark> queryCustomerRemarkByCustomerId(String id) {
        return customerRemarkMapper.queryCustomerRemarkByCustomerId(id);
    }

    @Override
    public int saveCreateCustomerRemark(CustomerRemark customerRemark) {
        return customerRemarkMapper.saveCreateCustomerRemark(customerRemark);
    }

}
