package com.wh.crm.workbench.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.DicValue;
import com.wh.crm.settings.domain.User;
import com.wh.crm.settings.service.DicValueService;
import com.wh.crm.settings.service.UserService;
import com.wh.crm.workbench.domain.*;
import com.wh.crm.workbench.service.ActivityService;
import com.wh.crm.workbench.service.ClueActivityRelationService;
import com.wh.crm.workbench.service.ClueRemarkService;
import com.wh.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

/**
 * @author wu
 * @createTime 2022/3/10  23:07
 * @description
 */
@Controller
public class ClueController {
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    /**
     * 跳转到线索页面，加载下拉框内容
     */
    @RequestMapping("/workbench/clue/index.do")
    public String toClueIndex(HttpServletRequest request) {
        List<User> userList = userService.queryAllUser();
        request.setAttribute("userList", userList);

        List<DicValue> appellationList = dicValueService.queryDicValueByDiCType("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByDiCType("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByDiCType("source");
        request.setAttribute("appellationList", appellationList);
        request.setAttribute("clueStateList", clueStateList);
        request.setAttribute("sourceList", sourceList);
        return "workbench/clue/index";
    }

    /**
     * 保存新创建的线索
     */
    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formatDateTime(new Date()));
        clue.setCreateBy(user.getId());

        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueService.saveCreateClue(clue);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("系统繁忙，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("系统繁忙，请稍后重试");
        }
        return returnObject;
    }


    /**
     * 分页查询
     */

    @RequestMapping("/workbench/clue/pageList.do")
    @ResponseBody
    public Object pageList(int pageNo, int pageSize, String fullname, String company, String phone,
                           String source, String owner, String mphone, String state) {
        int limitIndex = (pageNo - 1) * pageSize;

        Map<String, Object> map = new HashMap<>();
        map.put("pageSize", pageSize);
        map.put("limitIndex", limitIndex);
        map.put("fullname", fullname);
        map.put("company", company);
        map.put("phone", phone);
        map.put("source", source);
        map.put("owner", owner);
        map.put("mphone", mphone);
        map.put("state", state);

        List<Clue> clueList = clueService.pageListByCondition(map);
        int total = clueService.queryTotalByCondition(map);

        Map<String, Object> retMap = new HashMap<>();

        retMap.put("clueList", clueList);
        retMap.put("total", total);

        return retMap;
    }

    /**
     * 查询要修改的线索信息
     */
    @RequestMapping("/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id) {
        Clue clue = clueService.queryClueById(id);
        return clue;
    }

    /**
     * 保存修改线索
     */
    @RequestMapping("/workbench/clue/saveEditClue.do")
    @ResponseBody
    public Object saveEditClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clue.setEditBy(user.getId());
        clue.setEditTime(DateUtils.formatDateTime(new Date()));
        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueService.saveEditClue(clue);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
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


    /**
     * 根据id删除线索(批量)
     */
    @RequestMapping("/workbench/clue/deleteClueByIds.do")
    @ResponseBody
    public Object deleteClueByIds(String[] id) {

        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueService.deleteClueByIds(id);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setReturnData(i);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("删除失败");
        }
        return returnObject;
    }

    /*
    打开线索的详情页*/
    @RequestMapping("/workbench/clue/toClueDetail.do")
    public String toClueDetail(String id,HttpServletRequest request) {
        Clue clue = clueService.queryClueDetailById(id);
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkByClueId(id);
        List<Activity> activityList = activityService.quertActivityListForClueDetailByClueId(id);

        request.setAttribute("clue", clue);
        request.setAttribute("clueRemarkList", clueRemarkList);
        request.setAttribute("activityList", activityList);

        return "workbench/clue/detail";
    }

    /*
    *   动态搜索有关输入内容的市场活动
    * */
    @RequestMapping("/workbench/clue/queryActivityForBindClue.do")
    @ResponseBody
    public Object queryActivityForBindClue(String activityName,String clueId) {

        System.out.println("传递的活动名称为："+activityName);
        List<Activity> activityList = activityService.queryActivityForBindClue(activityName,clueId);

        return activityList;
    }


    @RequestMapping("/workbench/clue/saveActivityAndClueRelation.do")
    @ResponseBody
    public Object saveActivityAndClueRelation(String[] activityId,String clueId) {

        System.out.println(activityId);

        List<ClueActivityRelation> clueActivityRelationList = new ArrayList<>();
        for (String ai : activityId) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setActivityId(ai);
            clueActivityRelation.setClueId(clueId);
            clueActivityRelation.setId(UUIDUtils.getUUID());

            clueActivityRelationList.add(clueActivityRelation);
        }
        ReturnObject returnObject = new ReturnObject();

        try {
            int i = clueActivityRelationService.saveActivityAndClueRelation(clueActivityRelationList);

            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                List<Activity> activityList = activityService.queryActivitysForClueDetailByIds(activityId);
                returnObject.setReturnData(activityList);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("关联失败，请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("关联失败，请稍后重试");
        }


        return returnObject;
    }

    /*
     * 解除关联关系
     * */
    @RequestMapping("/workbench/clue/disBind.do")
    @ResponseBody
    public Object disBind(ClueActivityRelation clueActivityRelation) {

        ReturnObject returnObject = new ReturnObject();
       try {
            int i = clueActivityRelationService.deleteClueAndActivityBind(clueActivityRelation);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("解除失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("解除失败，请重试");
        }

        return returnObject;

    }

    /**
    * 跳转到线索转换页面
     * */
    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {

        Clue clue = clueService.queryClueDetailById(id);
        List<DicValue> stageList = dicValueService.queryDicValueByDiCType("stage");

        request.setAttribute("clue", clue);
        request.setAttribute("stageList", stageList);

        return "workbench/clue/convert";
    }


    @RequestMapping("/workbench/clue/selectActivityByActivityNameAndClueIdForConvert.do")
    @ResponseBody
    public Object selectActivityByActivityAndClueIdForConvert(String name, String clueId) {
        Map<String, String> map = new HashMap<>();
        map.put("name", name);
        map.put("clueId", clueId);

        List<Activity> activityList = activityService.queryActivityByActivityNameAndClueIdForConvert(map);

        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(Transaction transaction, String clueId, boolean isCreateTransaction, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);

        Map<String, Object> map = new HashMap<>();
        map.put("transaction", transaction);
        map.put("clueId", clueId);
        map.put("isCreateTransaction", isCreateTransaction);
        map.put(Contants.SESSION_USER, user);

        ReturnObject returnObject = new ReturnObject();
        try {
            clueService.saveConvertClueToContactsAndCustomer(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("转换失败，请稍后重试");

        }

        return returnObject;
    }
}
