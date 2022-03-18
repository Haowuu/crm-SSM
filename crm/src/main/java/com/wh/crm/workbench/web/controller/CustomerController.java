package com.wh.crm.workbench.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.DicValue;
import com.wh.crm.settings.domain.User;
import com.wh.crm.settings.service.DicValueService;
import com.wh.crm.settings.service.UserService;
import com.wh.crm.workbench.domain.Contacts;
import com.wh.crm.workbench.domain.Customer;
import com.wh.crm.workbench.domain.CustomerRemark;
import com.wh.crm.workbench.domain.Transaction;
import com.wh.crm.workbench.service.ContactsService;
import com.wh.crm.workbench.service.CustomerRemarkService;
import com.wh.crm.workbench.service.CustomerService;
import com.wh.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wu
 * @createTime 2022/3/15  18:21
 * @description
 */
@Controller
public class CustomerController {

    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRemarkService customerRemarkService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private TransactionService transactionService;



    /*
    * 跳转客户页
    * */
    @RequestMapping("/workbench/customer/index.do")
    public String index(HttpServletRequest request) {
        List<User> userList = userService.queryAllUser();
        request.setAttribute("userList", userList);

        return "workbench/customer/index";
    }

    /*
    * 分页查询
    * */
    @RequestMapping("/workbench/customer/pageList.do")
    @ResponseBody
    public Object pageList(String name, String owner, String phone, String website, String pageNo, String pageSize) {

        Integer pageCount = Integer.valueOf(pageSize);
        Integer limitCount = (Integer.valueOf(pageNo) - 1) * pageCount;

        Map<String, Object> map = new HashMap<>();
        map.put("pageCount",pageCount);
        map.put("limitCount",limitCount);
        map.put("name",name);
        map.put("owner",owner);
        map.put("phone",phone);
        map.put("website",website);
        List<Customer> customerList = customerService.queryCustomerByConditionForPageList(map);
        int total = customerService.queryCustomerTotalByCondition(map);

        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("customerList", customerList);
        returnMap.put("total", total);

        return returnMap;
    }

    /*
     * 保存新创建的客户信息
     * */
    @RequestMapping("/workbench/customer/saveCreateCustomer.do")
    @ResponseBody
    public Object saveCreateCustomer(Customer customer, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customer.setId(UUIDUtils.getUUID());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int i = customerService.saveCreateCustomer(customer);
            System.out.println("i======================================================="+i);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("创建失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("创建失败");
        }
        return returnObject;
    }

    /*
     * 删除客户信息
     * */
    @RequestMapping("/workbench/customer/deleteCustomerByIds.do")
    @ResponseBody
    public Object deleteCustomerById(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = customerService.deleteCustomerByIds(id);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("删除失败");
        }
        return returnObject;

    }

    /*
     * 根据id查询客户信息用于修改页
     * */
    @RequestMapping("/workbench/customer/queryCustomerByIdToEdit.do")
    @ResponseBody
    public Object queryCustomerByIdToEdit(String id) {
        Customer customer = customerService.queryCustomerByIdToEdit(id);
        return customer;
    }

    /*
     * 保存修改的用户信息
     * */
    @RequestMapping("/workbench/customer/saveEditCustomer.do")
    @ResponseBody
    public Object saveEditCustomer(Customer customer,HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customer.setEditBy(user.getId());
        customer.setEditTime(DateUtils.formatDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();

        try {
            int i = customerService.saveEditCustomer(customer);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("修改失败，请重试");

            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("修改失败，请重试");
        }
        return returnObject;
    }

    /*
     * 跳转到客户详情页
     * */
    @RequestMapping("/workbench/customer/toCustomerDetail.do")
    public String toCustomerDetail(String id,HttpServletRequest request) {
        //查询客户详细信息
        Customer customer = customerService.queryCustomerForDetail(id);
        request.setAttribute("customer", customer);

        //查询客户备注
        List<CustomerRemark> customerRemarkList = customerRemarkService.queryCustomerRemarkByCustomerId(id);
        request.setAttribute("customerRemarkList",customerRemarkList);


        //查询此客户的交易
        List<Transaction> transactionList = transactionService.queryTranByCustomerIdForCustomerDetail(id);
        for (Transaction transaction : transactionList) {
            String stageValue = transaction.getStage();
            ResourceBundle bundle = ResourceBundle.getBundle("possibility");
            String possibility = bundle.getString(stageValue);
            transaction.setPossibility(possibility);
        }
        request.setAttribute("transactionList", transactionList);


        //查询客户的联系人
        List<Contacts> contactsList = contactsService.queryContactsByCustomerIdForCustomerDetail(id);
        request.setAttribute("contactsList", contactsList);

        //来源下拉框
        List<DicValue> sourceList = dicValueService.queryDicValueByDiCType("source");
        request.setAttribute("sourceList",sourceList);
        //称呼下拉框
        List<DicValue> appellationList = dicValueService.queryDicValueByDiCType("appellation");
        request.setAttribute("appellationList",appellationList);
        //所有者下拉框
        List<User> userList = userService.queryAllUser();
        request.setAttribute("userList", userList);

        return "workbench/customer/detail";
    }


}
