package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.TransactionHistory;
import com.wh.crm.workbench.mapper.TransactionHistoryMapper;
import com.wh.crm.workbench.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/17  13:56
 * @description
 */
@Service
public class TransactionHistoryServiceImpl implements TransactionHistoryService {

    @Autowired
    private TransactionHistoryMapper transactionHistoryMapper;

    @Override
    public int saveTransactionHistory(TransactionHistory transactionHistory) {
        return transactionHistoryMapper.saveTransactionHistory(transactionHistory);
    }

    @Override
    public List<TransactionHistory> queryHistoryByTranId(String id) {
        return transactionHistoryMapper.queryHistoryByTranId(id);
    }

}
