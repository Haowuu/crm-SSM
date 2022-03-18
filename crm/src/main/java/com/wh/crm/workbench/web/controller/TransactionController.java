package com.wh.crm.workbench.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.DicValue;
import com.wh.crm.settings.domain.User;
import com.wh.crm.settings.service.DicValueService;
import com.wh.crm.settings.service.UserService;
import com.wh.crm.workbench.domain.*;
import com.wh.crm.workbench.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wu
 * @createTime 2022/3/16  16:13
 * @description
 */
@Controller
public class TransactionController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private TransactionHistoryService transactionHistoryService;
    @Autowired
    private TransactionRemarkService transactionRemarkService;

    /*
     * 跳转交易主页
     * */
    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request) {

        List<DicValue> stageList = dicValueService.queryDicValueByDiCType("stage");
        List<DicValue> typeList = dicValueService.queryDicValueByDiCType("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByDiCType("source");

        request.setAttribute("stageList", stageList);
        request.setAttribute("typeList", typeList);
        request.setAttribute("sourceList", sourceList);

        return "workbench/transaction/index";
    }

    /*
     * 跳转交易创建页
     * */
    @RequestMapping("/workbench/transaction/toSave.do")
    public String save(HttpServletRequest request) {

        List<User> userList = userService.queryAllUser();
        List<DicValue> stageList = dicValueService.queryDicValueByDiCType("stage");
        List<DicValue> typeList = dicValueService.queryDicValueByDiCType("transactionType");
        List<DicValue> sourceList = dicValueService.queryDicValueByDiCType("source");

        request.setAttribute("userList", userList);
        request.setAttribute("stageList", stageList);
        request.setAttribute("typeList", typeList);
        request.setAttribute("sourceList", sourceList);

        return "workbench/transaction/save";
    }

    /**
     * 分页查询
     */
    @RequestMapping("/workbench/transaction/tranPageList.do")
    @ResponseBody
    public Object tranPageList(String pageNo, String pageSize, String owner, String name, String customerName,
                               String stage, String type, String source, String contactName) {
        Integer limitCount = (Integer.valueOf(pageNo) - 1) * Integer.valueOf(pageSize);
        Map<String, Object> map = new HashMap<>();
        map.put("limitCount", limitCount);
        map.put("pageSize", Integer.valueOf(pageSize));
        map.put("owner", owner);
        map.put("name", name);
        map.put("customerId", customerName);
        map.put("stage", stage);
        map.put("type", type);
        map.put("source", source);
        map.put("contactId", contactName);

        List<Transaction> transactionList = transactionService.queryTranListByCondition(map);
        int total = transactionService.queryTranTotalsByCondition(map);

        Map<String, Object> retObject = new HashMap<>();
        retObject.put("transactionList", transactionList);
        retObject.put("total", total);
        return retObject;
    }

    /**
     * 根据阶段判断交易成功的可能性
     */
    @RequestMapping("/workbench/transaction/getPossibility.do")
    @ResponseBody
    public Object getPossibility(String stageText) {
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");

        String possibility = bundle.getString(stageText);
        return possibility;
    }

    /**
     * 自动补全客户姓名
     */
    @RequestMapping("/workbench/transaction/queryCustomerNameByName.do")
    @ResponseBody
    public Object queryCustomerNameByName(String name) {
        List<String> customerNameList = customerService.queryCustomerNameByName(name);

        return customerNameList;
    }

    /*
     * 根据名称模糊查询市场活动
     * */
    @RequestMapping("/workbench/transaction/queryActivityByNameForCreateTran.do")
    @ResponseBody
    public Object queryActivityByNameForCreateTran(String name) {

        List<Activity> activityList = activityService.queryActivityByNameForCreateTran(name);

        return activityList;
    }

    /*
     * 根据联系人姓名模糊查询联系人
     * */
    @RequestMapping("/workbench/transaction/queryContactsByNameForCreateTran.do")
    @ResponseBody
    public Object queryContactsByNameForCreateTran(String fullname) {
        List<Contacts> contactsList = contactsService.queryContactsByNameForCreateTran(fullname);
        return contactsList;
    }

    /*
     * 保存新创建的交易
     * */
    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(Transaction transaction, String customerName, HttpSession session) {

        User user= (User) session.getAttribute(Contants.SESSION_USER);
        Customer customer = customerService.queryCustomerByNameForcreateTran(customerName);
        ReturnObject returnObject = new ReturnObject();

        try {
            //如果客户不存在则新建客户并保存
            if (customer == null) {
                customer = new Customer();
                customer.setId(UUIDUtils.getUUID());
                customer.setOwner(user.getId());
                customer.setName(customerName);
                customer.setCreateBy(user.getId());
                customer.setCreateTime(DateUtils.formatDateTime(new Date()));
                customerService.saveCreateCustomer(customer);
            }

            //保存创建的交易信息
            transaction.setCustomerId(customer.getId());
            transaction.setId(UUIDUtils.getUUID());
            transaction.setCreateBy(user.getId());
            transaction.setCreateTime(DateUtils.formatDateTime(new Date()));
            int i = transactionService.saveCreateTran(transaction);

            //保存一条交易记录
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setId(UUIDUtils.getUUID());
            transactionHistory.setStage(transaction.getStage());
            transactionHistory.setMoney(transaction.getMoney());
            transactionHistory.setExpectedDate(transaction.getExpectedDate());
            transactionHistory.setCreateBy(user.getId());
            transactionHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
            transactionHistory.setTranId(transaction.getId());
            int j = transactionHistoryService.saveTransactionHistory(transactionHistory);


            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("创建失败，请稍后重试");
        }
        return returnObject;
    }

    /**
     * 点击跳转到交易详情页
     */
    @RequestMapping("/workbench/transaction/toDetail.do")
    public String toDetail(String id, HttpServletRequest request) {
        //查询交易的详细信息
        Transaction transaction = transactionService.queryTranDetailByTranId(id);
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");//获取可能性
        String possibility = bundle.getString(transaction.getStage());
        transaction.setPossibility(possibility);
        request.setAttribute("transaction", transaction);
        System.out.println("查询到的编号=======================："+transaction.getStageOrderNo());

        //查询交易备注
        List<TransactionRemark> tranRemarkList = transactionRemarkService.queryRemarkByTranId(id);
        request.setAttribute("tranRemarkList",tranRemarkList);

        //查询交易阶段历史
        List<TransactionHistory> tranHistoryList = transactionHistoryService.queryHistoryByTranId(id);
        request.setAttribute("tranHistoryList", tranHistoryList);

        //查询交易阶段名称和编号等
        List<DicValue> stageList = dicValueService.queryDicValueByDiCType("stage");
        request.setAttribute("stageList", stageList);


        return "workbench/transaction/detail";
    }
}
