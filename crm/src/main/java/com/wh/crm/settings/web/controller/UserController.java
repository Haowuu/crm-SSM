package com.wh.crm.settings.web.controller;

import com.wh.crm.commons.contants.Contants;
import com.wh.crm.commons.domain.ReturnObject;
import com.wh.crm.commons.utils.DateUtils;
import com.wh.crm.settings.domain.User;
import com.wh.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wu
 * @createTime 2022/3/3  21:52
 * @description
 */
@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 跳转到登录界面
     * */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin() {
        return "settings/qx/user/login";
    }

    /**
     * 封装参数信息
     * 接收返回值
     * 判断用户登录是否成功
     */
    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("login.do执行了");
        //封装参数信息，调用service
        Map<String, String> map = new HashMap<>();
        map.put("loginAct", loginAct);
        map.put("loginPwd", loginPwd);
        User user = userService.queryUserByLoginActAndPwd(map);
        ReturnObject returnObject = new ReturnObject();

        System.out.println("返回的user对象："+user);

        if (user == null) {
            //登录失败，用户名或密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMassage("用户名或密码错误");
        } else {
            if (DateUtils.formatDateTime(new Date()).compareTo(user.getExpireTime()) > 0) {
                //登录失败，用户以过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("用户已过期");

            } else if ("0".equals(user.getLockState())) {
                //登录失败，用户状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("用户状态被锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                //登录失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMassage("ip受限");
            } else {
                //登录成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                request.getSession().setAttribute(Contants.SESSION_USER, user);


                if ("true".equals(isRemPwd)) {
                    Cookie cookieAct = new Cookie("loginAct", loginAct);
                    Cookie cookiePwd = new Cookie("loginPwd", loginPwd);
                    cookieAct.setMaxAge(10 * 24 * 60 * 60);
                    cookiePwd.setMaxAge(10 * 24 * 60 * 60);
                    response.addCookie(cookieAct);
                    response.addCookie(cookiePwd);
                } else {
                    Cookie cookieAct = new Cookie("loginAct", "1");
                    Cookie cookiePwd = new Cookie("loginPwd", "1");
                    cookieAct.setMaxAge(0);
                    cookiePwd.setMaxAge(0);
                    response.addCookie(cookieAct);
                    response.addCookie(cookiePwd);
                }
            }
        }

        return returnObject;
    }

    /**
     * 安全退出系统
     * 清除cookie
     * 删除session
     * 跳转登录页
     * */
    @RequestMapping("/settings/qx/user/loginOut.do")
    public String loginOut(HttpSession session,HttpServletResponse response) {
        //清除cookie
        Cookie cookieAct = new Cookie("loginAct", "1");
        Cookie cookiePwd = new Cookie("loginPwd", "1");
        cookieAct.setMaxAge(0);
        cookiePwd.setMaxAge(0);
        response.addCookie(cookieAct);
        response.addCookie(cookiePwd);

        //删除session
        session.invalidate();

        return "redirect:/";
    }
}
