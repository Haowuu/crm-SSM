package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.FunnelVO;
import com.wh.crm.workbench.domain.Transaction;
import com.wh.crm.workbench.mapper.TransactionMapper;
import com.wh.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @createTime 2022/3/16  11:47
 * @description
 */
@Service
public class TransactionServiceImpl implements TransactionService {
    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public List<Transaction> queryTranByCustomerIdForCustomerDetail(String id) {
        return transactionMapper.queryTranByCustomerIdForCustomerDetail(id);
    }

    @Override
    public List<Transaction> queryTranListByCondition(Map<String, Object> map) {

        return transactionMapper.queryTranListByCondition(map);
    }

    @Override
    public int queryTranTotalsByCondition(Map<String, Object> map) {
        return transactionMapper.queryTranTotalsByCondition(map);
    }

    @Override
    public int saveCreateTran(Transaction transaction) {
        return transactionMapper.saveCreateTran(transaction);
    }

    @Override
    public Transaction queryTranDetailByTranId(String id) {
        return transactionMapper.queryTranDetailByTranId(id);
    }

    @Override
    public List<FunnelVO> queryCountOfTranGroupByStage() {
        return transactionMapper.queryCountOfTranGroupByStage();
    }

}
