package com.wh.crm.workbench.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.commons.utils.UUIDUtils;
import com.wh.crm.settings.domain.User;
import com.wh.crm.workbench.domain.ClueRemark;
import com.wh.crm.workbench.service.ClueRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * @author wu
 * @createTime 2022/3/12  18:09
 * @description
 */
@Controller
public class ClueRemarkController {

    @Autowired
    private ClueRemarkService clueRemarkService;

    /**
    * 保存新的备注
     * */
    @RequestMapping("/workbench/clue/saveCreateClueRemark.do")
    @ResponseBody
    public Object saveCreateClueRemark(ClueRemark clueRemark, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        //ClueRemark clueRemark = new ClueRemark();
        /*clueRemark.setClueId(clueId);
        clueRemark.setNoteContent(noteContent);*/
        clueRemark.setId(UUIDUtils.getUUID());
        clueRemark.setCreateTime(DateUtils.formatDateTime(new Date()));
        clueRemark.setCreateBy(user.getId());
        clueRemark.setEditFlag("0");

        ReturnObject returnObject = new ReturnObject();
        try {
            int i = clueRemarkService.saveCreateClueRemark(clueRemark);
            if (i > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                returnObject.setReturnData(clueRemark);
            } else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("创建失败，请重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("创建失败，请重试");
        }

        return returnObject;

    }
}
