package com.chen.settings.web.controller;

import com.chen.exception.Login.LoginException;
import com.chen.exception.SystemException;
import com.chen.settings.domain.User;
import com.chen.settings.service.UserService;
import com.chen.utils.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 3:12
 */
@Controller
@RequestMapping(value = "/User")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/login.do",method = RequestMethod.POST)
    public ModelAndView doLogin(String loginAct, String loginPwd, HttpServletResponse response,
                         HttpServletRequest request) throws SystemException {

        System.out.println("进入后台密码验证环节！");

        // 转换密码为MD5密文形式
        String pwd = MD5Util.getMD5(loginPwd);

        // 接收浏览器端的ip地址
        String user_ip = request.getRemoteAddr();
        System.out.println("ip========================================="+user_ip);
        ModelAndView mv = new ModelAndView();

        // 若service验证登录失败，则将返回的User对象的name属性置为null，email属性设置为失败原因
        User user = userService.login(loginAct, pwd, user_ip);
        if(null==user.getName()){
            mv.setViewName("loginError");
            throw new LoginException(user.getEmail());
        }

        return mv;
    }

}
class Succ{
    private boolean success;

    public Succ() {
    }

    public Succ(boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "Succ{" +
                "success=" + success +
                '}';
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isSuccess() {
        return success;
    }
}
