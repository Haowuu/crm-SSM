package com.wh.crm.workbench.service;

import com.wh.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {

    int saveCreateActivity(Activity activity);

    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

    int  queryTotalByCondition(Map<String, Object> map) ;

    int deleteActivityByIds(String[] id);

    Activity queryActivityById(String id);

    int saveUpdateActivity(Activity activity);

    List<Activity> queryAllActivitys();


    List<Activity> exportChangeActivity(String[] id);

    int saveActivityFromFile(List<Activity> activityList);

    Activity queryActivityDetailById(String id);

    List<Activity> quertActivityListForClueDetailByClueId(String id);

    List<Activity> queryActivityForBindClue(String activityName, String clueId);

    List<Activity> queryActivitysForClueDetailByIds(String[] activityId);

    List<Activity> queryActivityByActivityNameAndClueIdForConvert(Map<String, String> map);

    List<Activity> queryActivityByNameForCreateTran(String name);
}
