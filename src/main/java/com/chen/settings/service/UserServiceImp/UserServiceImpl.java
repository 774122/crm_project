package com.chen.settings.service.UserServiceImp;

import com.chen.exception.Login.LoginException;
import com.chen.settings.dao.UserDao;
import com.chen.settings.domain.User;
import com.chen.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 5:47
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao dao;


    @Override
    public User login(String loginAct,String pwd,String user_ip){

        //test
        User user = new User();
        user.setName(null);
        user.setEmail("登录权限过期！");
        return user;
    }
}
