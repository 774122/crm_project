package com.chen.settings.service;

import com.chen.exception.Login.LoginException;
import com.chen.settings.domain.User;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 5:47
 */
public interface UserService {

    User login(String loginAct,String pwd,String user_ip);

}
