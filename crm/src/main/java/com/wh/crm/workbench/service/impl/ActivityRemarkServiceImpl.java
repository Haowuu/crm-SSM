package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.ActivityRemark;
import com.wh.crm.workbench.mapper.ActivityRemarkMapper;
import com.wh.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/9  18:26
 * @description
 */
@Service
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;

    @Override
    public List<ActivityRemark> queryActivityRemarkByActivityId(String id) {


        return activityRemarkMapper.queryActivityRemarkByActivityId(id);
    }

    @Override
    public int saveCreateActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.saveCreateActivityRemark(activityRemark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {

        return activityRemarkMapper.deleteActivityRemarkById(id);
    }

    @Override
    public int saveUpdateActivityRemark(ActivityRemark activityRemark) {
        return activityRemarkMapper.saveUpdateActivityRemark(activityRemark);
    }

}
