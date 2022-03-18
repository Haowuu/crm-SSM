package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {
    int saveActivityAndClueRelation(List<ClueActivityRelation> clueActivityRelationList);

    int deleteClueAndActivityBind(ClueActivityRelation clueActivityRelation);
}
