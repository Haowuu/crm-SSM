package com.wh.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author wu
 * @createTime 2022/3/4  13:19
 * @description
 */

@Controller
public class workbenchIndexController {

    @RequestMapping("/workbench/index.do")
    public String toWorkbenchIndex() {
        return "workbench/index";
    }

    @RequestMapping("/workbench/main/index.do")
    public String toWorkbenchMainIndex() {
        return "workbench/main/index";
    }

}
