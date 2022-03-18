package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.TransactionHistory;

import java.util.List;

public interface TransactionHistoryService {
    int saveTransactionHistory(TransactionHistory transactionHistory);

    List<TransactionHistory> queryHistoryByTranId(String id);
}
