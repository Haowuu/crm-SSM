package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.FunnelVO;
import com.wh.crm.workbench.domain.Transaction;

import java.util.List;
import java.util.Map;

public interface TransactionService {
    List<Transaction> queryTranByCustomerIdForCustomerDetail(String id);

    List<Transaction> queryTranListByCondition(Map<String, Object> map);

    int queryTranTotalsByCondition(Map<String, Object> map);

    int saveCreateTran(Transaction transaction);

    Transaction queryTranDetailByTranId(String id);

    List<FunnelVO> queryCountOfTranGroupByStage();

}
