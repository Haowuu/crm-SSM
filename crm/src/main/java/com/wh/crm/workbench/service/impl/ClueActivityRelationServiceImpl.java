package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.ClueActivityRelation;
import com.wh.crm.workbench.mapper.ClueActivityRelationMapper;
import com.wh.crm.workbench.service.ClueActivityRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/12  18:08
 * @description
 */
@Service
public class ClueActivityRelationServiceImpl implements ClueActivityRelationService {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int saveActivityAndClueRelation(List<ClueActivityRelation> clueActivityRelationList) {
        return clueActivityRelationMapper.saveActivityAndClueRelation(clueActivityRelationList);
    }

    @Override
    public int deleteClueAndActivityBind(ClueActivityRelation clueActivityRelation) {

        return clueActivityRelationMapper.deleteClueAndActivityBind(clueActivityRelation);
    }

}
