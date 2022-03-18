package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.TransactionRemark;

import java.util.List;

public interface TransactionRemarkService {
    List<TransactionRemark> queryRemarkByTranId(String id);
}
