package com.wh.crm.workbench.service.impl;

import com.sun.tools.doclets.internal.toolkit.util.ClassUseMapper;
import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.DicValue;
import com.wh.crm.settings.domain.User;
import com.wh.crm.settings.mapper.DicValueMapper;
import com.wh.crm.workbench.domain.*;
import com.wh.crm.workbench.mapper.*;
import com.wh.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @createTime 2022/3/11  17:42
 * @description
 */
@Service
public class ClueServiceImpl implements ClueService {
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private DicValueMapper dicValueMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;
    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;


    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.saveCreateClue(clue);
    }

    @Override
    public List<Clue> pageListByCondition(Map<String, Object> map) {
        return clueMapper.pageListByCondition(map);
    }

    @Override
    public int queryTotalByCondition(Map<String, Object> map) {

        return clueMapper.queryTotalByCondition(map);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.queryClueById(id);
    }

    @Override
    public int saveEditClue(Clue clue) {
        return clueMapper.saveEditClue(clue);
    }

    @Override
    public int deleteClueByIds(String[] id) {

        return clueMapper.deleteClueByIds(id);
    }

    @Override
    public Clue queryClueDetailById(String id) {
        return clueMapper.queryClueDetailById(id);
    }

    @Override
    @Transactional(readOnly = false)
    public void saveConvertClueToContactsAndCustomer(Map<String, Object> map) {

        String clueId = (String) map.get("clueId");
        User user= (User) map.get(Contants.SESSION_USER);
        boolean isCreateTransaction= (boolean) map.get("isCreateTransaction");

        //????????????id???????????????????????????
        Clue clue = clueMapper.queryClueForConvertById(clueId);
        //????????????id???????????????????????????
        List<ClueRemark> clueRemarkList = clueRemarkMapper.queryClueRemarkForConvertByClueId(clueId);
        //????????????id????????????????????????????????????
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.queryClueActivityRelationMapperByClueId(clueId);


        //????????????????????????????????????????????????
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(user.getId());
        customer.setName(clue.getCompany());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateUtils.formatDateTime(new Date()));
        customer.setContactSummary(clue.getContactSummary());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setDescription(clue.getDescription());
        customer.setAddress(clue.getAddress());
        int saveCustomerNums = customerMapper.saveCustomer(customer);

        //???????????????????????????????????????????????????
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setAppellation(clue.getAppellation());
        contacts.setEmail(clue.getEmail());
        contacts.setMphone(clue.getMphone());
        contacts.setJob(clue.getJob());
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateUtils.formatDateTime(new Date()));
        contacts.setDescription(clue.getDescription());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setAddress(clue.getAddress());
        int saveContactsNum = contactsMapper.saveContacts(contacts);

        //?????????????????????????????????????????????????????????
        if (clueRemarkList != null&&clueRemarkList.size()!=0) {
            CustomerRemark customerRemark = null;
            List<CustomerRemark> customerRemarkList = new ArrayList<>();
            for (ClueRemark clueRemark : clueRemarkList) {
                customerRemark = new CustomerRemark();
                customerRemark.setId(UUIDUtils.getUUID());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setEditTime(clueRemark.getEditTime());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setCustomerId(customer.getId());
                customerRemarkList.add(customerRemark);
            }
            System.out.println("???????????????????????????"+customerRemarkList);
            int saveCustomerRemarksNum = customerRemarkMapper.saveCustomerRemarks(customerRemarkList);
        }


        //????????????????????????????????????????????????????????????
        if (clueRemarkList != null&&clueRemarkList.size()!=0) {
            ContactsRemark contactsRemark = null;
            List<ContactsRemark> contactsRemarkList = new ArrayList<>();
            for (ClueRemark clueRemark : clueRemarkList) {
                contactsRemark = new ContactsRemark();
                contactsRemark.setId(UUIDUtils.getUUID());
                contactsRemark.setNoteContent(clueRemark.getNoteContent());
                contactsRemark.setCreateBy(clueRemark.getCreateBy());
                contactsRemark.setCreateTime(clueRemark.getCreateTime());
                contactsRemark.setEditBy(clueRemark.getEditBy());
                contactsRemark.setEditTime(clueRemark.getEditTime());
                contactsRemark.setEditFlag(clueRemark.getEditFlag());
                contactsRemark.setContactsId(contacts.getId());
                contactsRemarkList.add(contactsRemark);
            }

            int saveContactsRemarkNum = contactsRemarkMapper.saveContactsRemark(contactsRemarkList);
        }


        //?????????????????????????????????????????????????????????????????????????????????????????????
        if (clueActivityRelationList != null&&clueActivityRelationList.size()!=0) {
            ContactsActivityRelation contactsActivityRelation = null;
            List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
            for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
                contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtils.getUUID());
                contactsActivityRelation.setContactsId(contacts.getId());
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                contactsActivityRelationList.add(contactsActivityRelation);
            }
            int saveContactsActivityRelationNum = contactsActivityRelationMapper.saveContactsActivityRelation(contactsActivityRelationList);
        }



        //??????????????????????????????????????????????????????????????????
        //???????????????????????????????????????????????????????????????
        //??????????????????
        int saveTransactionNum = 0;
        int saveTransactionRemarksNum = 0;
        if (isCreateTransaction) {
            List<DicValue> transactionTypeList = dicValueMapper.queryDicValueByDiCType("transactionType");
            String transactionTypeId = null;
            for (DicValue dicValue : transactionTypeList) {
                if ("?????????".equals(dicValue.getValue())) {
                    transactionTypeId = dicValue.getId();
                }
            }

            Transaction transaction = (Transaction) map.get("transaction");
            transaction.setId(UUIDUtils.getUUID());
            transaction.setOwner(user.getId());
            transaction.setCustomerId(customer.getId());
            transaction.setType(transactionTypeId);
            transaction.setSource(clue.getSource());
            transaction.setContactsId(contacts.getId());
            transaction.setCreateBy(user.getId());
            transaction.setCreateTime(DateUtils.formatDateTime(new Date()));
            transaction.setDescription(clue.getDescription());
            transaction.setContactSummary(clue.getContactSummary());
            transaction.setNextContactTime(clue.getNextContactTime());
            saveTransactionNum = transactionMapper.saveTransaction(transaction);

            //????????????????????????
            TransactionHistory transactionHistory = new TransactionHistory();
            transactionHistory.setId(UUIDUtils.getUUID());
            transactionHistory.setStage(transaction.getStage());
            transactionHistory.setMoney(transaction.getMoney());
            transactionHistory.setExpectedDate(transaction.getExpectedDate());
            transactionHistory.setCreateTime(DateUtils.formatDateTime(new Date()));
            transactionHistory.setCreateBy(user.getId());
            transactionHistory.setTranId(transaction.getId());
            transactionHistoryMapper.saveTransactionHistory(transactionHistory);


            if (clueRemarkList != null&&clueRemarkList.size()!=0) {
                TransactionRemark transactionRemark = null;
                List<TransactionRemark> transactionRemarkList = new ArrayList<>();
                for (ClueRemark clueRemark : clueRemarkList) {
                    transactionRemark = new TransactionRemark();
                    transactionRemark.setId(UUIDUtils.getUUID());
                    transactionRemark.setNoteContent(clueRemark.getNoteContent());
                    transactionRemark.setCreateBy(clueRemark.getCreateBy());
                    transactionRemark.setCreateTime(clueRemark.getCreateTime());
                    transactionRemark.setEditBy(clueRemark.getEditBy());
                    transactionRemark.setEditTime(clueRemark.getEditTime());
                    transactionRemark.setEditFlag(clueRemark.getEditFlag());
                    transactionRemark.setTranId(transaction.getId());
                    transactionRemarkList.add(transactionRemark);
                }
                saveTransactionRemarksNum = transactionRemarkMapper.saveTransactionRemarks(transactionRemarkList);
            }


        }

        //?????????????????????
        //??????????????????????????????????????????
        //????????????
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
        clueActivityRelationMapper.deleteClueActivityRelationByClueId(clueId);
        clueMapper.deleteClueById(clueId);


    }

}
