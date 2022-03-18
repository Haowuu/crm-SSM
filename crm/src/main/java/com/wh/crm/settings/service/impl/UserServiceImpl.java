package com.wh.crm.settings.service.impl;

import com.wh.crm.settings.domain.User;
import com.wh.crm.settings.mapper.UserMapper;
import com.wh.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author wu
 * @createTime 2022/3/4  1:16
 * @description
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    /**
     * 根据登录参数查询用户信息
    * */
    @Override
    public User queryUserByLoginActAndPwd(Map<String, String> map) {
        System.out.println("login.service执行了");
        User user = userMapper.selectUserByActAndPwd(map);
        System.out.println("返回的user对象："+user);
        return user;
    }

    @Override
    public List<User> queryAllUser() {
        List<User> users = userMapper.selectAllUser();
        return users;
    }

}
