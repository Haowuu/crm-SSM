package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.TransactionRemark;
import com.wh.crm.workbench.mapper.TransactionRemarkMapper;
import com.wh.crm.workbench.service.TransactionRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/17  16:29
 * @description
 */
@Service
public class TransactionRemarkServiceImpl implements TransactionRemarkService {

    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public List<TransactionRemark> queryRemarkByTranId(String id) {
        return transactionRemarkMapper.queryRemarkByTranId(id);
    }
}
