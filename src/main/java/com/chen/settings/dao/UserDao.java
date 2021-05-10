package com.chen.settings.dao;

import com.chen.settings.domain.User;

import java.util.List;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 5:35
 */
public interface UserDao {

    User selectUser(String loginAct);
    List<User> getUserList();

}
