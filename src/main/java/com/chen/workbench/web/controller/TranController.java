package com.chen.workbench.web.controller;

import com.chen.settings.domain.User;
import com.chen.settings.service.UserService;
import com.chen.utils.DateTimeUtil;
import com.chen.utils.UUIDUtil;
import com.chen.vo.PageNationVo;
import com.chen.workbench.domain.Activity;
import com.chen.workbench.domain.Clue;
import com.chen.workbench.domain.Tran;
import com.chen.workbench.service.ActivityService;
import com.chen.workbench.service.ClueService;
import com.chen.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 3:12
 */
@Controller
@RequestMapping(value = "/Tran")
public class TranController {

    @Autowired
    private TranService tranService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/add.do")
    public void doAddTran(HttpServletRequest request,HttpServletResponse response) throws Exception{
        List<User> users = userService.getUserList();

        request.setAttribute("userList",users);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);


    }

}