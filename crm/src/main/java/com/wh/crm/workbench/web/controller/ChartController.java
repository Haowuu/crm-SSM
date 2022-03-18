package com.wh.crm.workbench.web.controller;

import com.wh.crm.workbench.domain.FunnelVO;
import com.wh.crm.workbench.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author wu
 * @createTime 2022/3/17  22:07
 * @description
 */
@Controller
public class ChartController {
    @Autowired
    private TransactionService transactionService;


    @RequestMapping("/workbench/chart/transaction/index.do")
    public String index() {
        return "workbench/chart/transaction/index";
    }


    @RequestMapping("/workbench/chart/transaction/queryCountOfTranGroupByStage.do")
    @ResponseBody
    public Object queryCountOfTranGroupByStage() {

        List<FunnelVO> funnelVOList =transactionService.queryCountOfTranGroupByStage();
        return funnelVOList;
    }
}
