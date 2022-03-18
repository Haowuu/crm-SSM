package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.Clue;
import com.wh.crm.workbench.domain.Transaction;

import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    List<Clue> pageListByCondition(Map<String, Object> map);

    int queryTotalByCondition(Map<String, Object> map);

    Clue queryClueById(String id);

    int saveEditClue(Clue clue);

    int deleteClueByIds(String[] id);

    Clue queryClueDetailById(String id);

    void saveConvertClueToContactsAndCustomer(Map<String, Object> map);
}
