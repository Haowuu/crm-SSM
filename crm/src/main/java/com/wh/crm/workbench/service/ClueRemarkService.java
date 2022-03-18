package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {

    List<ClueRemark> queryClueRemarkByClueId(String id);

    int saveCreateClueRemark(ClueRemark clueRemark);
}
