package com.wh.crm.workbench.service.impl;

import com.wh.crm.workbench.domain.Activity;
import com.wh.crm.workbench.mapper.ActivityMapper;
import com.wh.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @createTime 2022/3/5  17:23
 * @description
 */
@Service
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Override
    public int saveCreateActivity(Activity activity) {
        return activityMapper.insertCreateActivity(activity);
    }

    //根据条件分页查询市场活动
    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {

        return  activityMapper.selectActivityByCondition(map);
    }

    //根据条件查询市场活动条数
    @Override
    public int  queryTotalByCondition(Map<String, Object> map) {
        return  activityMapper.selectTotalByCondition(map);
    }

    @Override
    public int deleteActivityByIds(String[] id) {
        return activityMapper.deleteActivityById(id);
    }

    @Override
    public Activity queryActivityById(String id) {

        return activityMapper.queryActivityById(id);
    }

    @Override
    public int saveUpdateActivity(Activity activity) {

        return  activityMapper.saveUpdateActivity(activity);
    }

    @Override
    public List<Activity> queryAllActivitys() {

        return activityMapper.queryAllActivitys();
    }

    @Override
    public List<Activity> exportChangeActivity(String[] id) {

        return activityMapper.exportChangeActivity(id);
    }

    @Override
    public int saveActivityFromFile(List<Activity> activityList) {
        return activityMapper.saveActivityFromFile(activityList);
    }

    @Override
    public Activity queryActivityDetailById(String id) {
        return activityMapper.queryActivityDetailById(id);
    }

    @Override
    public List<Activity> quertActivityListForClueDetailByClueId(String id) {
        return activityMapper.quertActivityListForClueDetailByClueId(id);
    }

    @Override
    public List<Activity> queryActivityForBindClue(String activityName, String clueId) {
        return activityMapper.queryActivityForBindClue(activityName,clueId);
    }

    @Override
    public List<Activity> queryActivitysForClueDetailByIds(String[] activityId) {
        return activityMapper.queryActivitysForClueDetailByIds(activityId);
    }

    @Override
    public List<Activity> queryActivityByActivityNameAndClueIdForConvert(Map<String, String> map) {

        return activityMapper.queryActivityByActivityNameAndClueIdForConvert(map);
    }

    @Override
    public List<Activity> queryActivityByNameForCreateTran(String name) {
        return activityMapper.queryActivityByNameForCreateTran(name);
    }


}
