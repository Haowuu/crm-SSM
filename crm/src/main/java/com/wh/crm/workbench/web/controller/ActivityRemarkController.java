package com.wh.crm.workbench.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.User;
import com.wh.crm.workbench.domain.ActivityRemark;
import com.wh.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @createTime 2022/3/9  22:48
 * @description
 */
@Controller
public class ActivityRemarkController {

    @Autowired
    private ActivityRemarkService activityRemarkService;

    @RequestMapping("/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(String noteContent, String activityId, HttpSession session) {


        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(UUIDUtils.getUUID());
        activityRemark.setNoteContent(noteContent);
        activityRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setCreateBy(user.getId());
        activityRemark.setEditFlag(Contants.EDIT_FLAG_NO);
        activityRemark.setActivityId(activityId);

        ReturnObject returnObject = new ReturnObject();

        try {
            int i = activityRemarkService.saveCreateActivityRemark(activityRemark);


            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setReturnData(activityRemark);
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("系统繁忙，请稍后再试");
        }
        return returnObject;
    }
    /**
     * 根据市场活动备注id删除备注
     * */
    @RequestMapping("/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityRemarkService.deleteActivityRemarkById(id);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("删除失败，请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("删除失败，请稍后再试");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/saveUpdateActivityRemark.do")
    @ResponseBody
    public Object saveUpdateActivityRemark(String id, String noteContent, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        ActivityRemark activityRemark = new ActivityRemark();
        activityRemark.setId(id);
        activityRemark.setNoteContent(noteContent);
        activityRemark.setEditTime(DateUtils.formatDateTime(new Date()));
        activityRemark.setEditBy(user.getId());
        activityRemark.setEditFlag("1");

        ReturnObject returnObject = new ReturnObject();
        try {
            int i = activityRemarkService.saveUpdateActivityRemark(activityRemark);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setReturnData(activityRemark);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("修改失败，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("修改失败，请稍后重试");
        }

        return returnObject;
    }


}

