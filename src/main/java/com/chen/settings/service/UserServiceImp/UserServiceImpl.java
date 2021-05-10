package com.chen.settings.service.UserServiceImp;

import com.chen.exception.Login.LoginException;
import com.chen.settings.dao.UserDao;
import com.chen.settings.domain.User;
import com.chen.settings.service.UserService;
import com.chen.utils.DateTimeUtil;
import com.mysql.jdbc.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

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
        User user;
        String currentTime = DateTimeUtil.getSysTime();

        // 调用dao层查询用户，查到：返回user对象；查不到：返回的user对象各个属性为空值
        user = dao.selectUser(loginAct);  // 不给dao层传密码了
        if(null == user){ //.getLoginPwd() || null == user.getName() || null==user.getId()
            user = new User();
            user.setName(null);
            user.setEmail("用户不存在");
        }else if(!user.getLoginPwd().equals(pwd)){
            user.setName(null);
            user.setEmail("密码错误！");
        }else if(user.getExpireTime().compareTo(currentTime)<0){
            user.setName(null);
            user.setEmail("账号已失效！");
        }else if("0".equals(user.getLockState())){
            user.setName(null);
            user.setEmail("账号已锁定！");
        }else if(!user.getAllowIps().contains(user_ip)){
            user.setName(null);
            user.setEmail("ip地址受限！");
        }
        return user;
    }

    @Override
    public List<User> getUserList() {
        List<User> users = dao.getUserList();
        return users;
    }
}
