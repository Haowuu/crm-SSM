package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {

    List<ActivityRemark> queryActivityRemarkByActivityId(String id);

    int saveCreateActivityRemark(ActivityRemark activityRemark);

    int deleteActivityRemarkById(String id);

    int saveUpdateActivityRemark(ActivityRemark activityRemark);
}
