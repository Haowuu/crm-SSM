package com.wh.crm.workbench.mapper;

import com.wh.crm.workbench.domain.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Tue Mar 15 01:04:27 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Tue Mar 15 01:04:27 CST 2022
     */
    int insert(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Tue Mar 15 01:04:27 CST 2022
     */
    int insertSelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Tue Mar 15 01:04:27 CST 2022
     */
    Customer selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Tue Mar 15 01:04:27 CST 2022
     */
    int updateByPrimaryKeySelective(Customer record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_customer
     *
     * @mbggenerated Tue Mar 15 01:04:27 CST 2022
     */
    int updateByPrimaryKey(Customer record);

    int saveCustomer(Customer customer);

    List<Customer> queryCustomerByConditionForPageList(Map<String, Object> map);

    int queryCustomerTotalByCondition(Map<String, Object> map);

    int saveCreateCustomer(Customer customer);

    int deleteCustomerByIds(String[] id);

    Customer queryCustomerByIdToEdit(String id);

    int saveEditCustomer(Customer customer);

    Customer queryCustomerForDetail(String id);

    List<String> queryCustomerNameByName(String name);

    Customer queryCustomerByNameForcreateTran(String name);
}