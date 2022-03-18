package com.wh.crm.workbench.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.User;
import com.wh.crm.workbench.domain.CustomerRemark;
import com.wh.crm.workbench.service.CustomerRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author wu
 * @createTime 2022/3/17  10:39
 * @description
 */
@Controller
public class CustomerRemarkController {
    @Autowired
    private CustomerRemarkService customerRemarkService;


    @RequestMapping("/workbench/customer/saveCreateCustomerRemark.do")
    @ResponseBody
    public Object saveCreateCustomerRemark(CustomerRemark customerRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        customerRemark.setId(UUIDUtils.getUUID());
        customerRemark.setCreateBy(user.getId());
        customerRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        customerRemark.setEditFlag(Contants.EDIT_FLAG_NO);

        ReturnObject returnObject = new ReturnObject();
        try {
            int i = customerRemarkService.saveCreateCustomerRemark(customerRemark);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setReturnData(customerRemark);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("添加备注失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("添加备注失败，请重试");
        }
        return returnObject;

    }
}
