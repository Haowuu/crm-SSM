package com.wh.crm.settings.service;

import com.wh.crm.settings.domain.User;

import java.util.List;
import java.util.Map;


public interface UserService {


    User queryUserByLoginActAndPwd(Map<String,String> map);

    List<User> queryAllUser();
}
