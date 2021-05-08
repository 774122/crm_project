package com.chen.settings.dao;

import com.chen.settings.domain.User;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 5:35
 */
public interface UserDao {

    User selectUser(String loginAct);

}
