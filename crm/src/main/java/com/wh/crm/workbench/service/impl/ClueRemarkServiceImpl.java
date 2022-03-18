package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.ClueRemark;
import com.wh.crm.workbench.mapper.ClueRemarkMapper;
import com.wh.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/12  18:07
 * @description
 */
@Service
public class ClueRemarkServiceImpl implements ClueRemarkService {

    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Override
    public List<ClueRemark> queryClueRemarkByClueId(String id) {
        return clueRemarkMapper.queryClueRemarkByClueId(id);
    }

    @Override
    public int saveCreateClueRemark(ClueRemark clueRemark) {
        return clueRemarkMapper.saveCreateClueRemark(clueRemark);
    }
}
