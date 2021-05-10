package com.chen.workbench.web.controller;

import com.chen.exception.Login.LoginException;
import com.chen.exception.SystemException;
import com.chen.settings.domain.User;
import com.chen.settings.service.UserService;
import com.chen.utils.DateTimeUtil;
import com.chen.utils.MD5Util;
import com.chen.utils.UUIDUtil;
import com.chen.vo.PageNationVo;
import com.chen.workbench.domain.Activity;
import com.chen.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 3:12
 */
@Controller
@RequestMapping(value = "/Activity")
public class ActivityController {

    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(String loginAct, String loginPwd, HttpServletResponse response,
                                  HttpServletRequest request) throws SystemException {

        System.out.println("进入市场活动控制器！");

        List<User> users = userService.getUserList();

        return users;
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public Map<String,Boolean> saveActivity(Activity activity,HttpServletRequest request,HttpServletResponse response) {

        System.out.println("执行市场活动添加操作！");

        activity.setId(UUIDUtil.getUUID());
        activity.setCreateTime(DateTimeUtil.getSysTime());
        activity.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        //System.out.println(activity);
        //List<User> users = activityService.addActivity();
        Map<String,Boolean> map = activityService.addActivity(activity);
        return map;
    }

    @RequestMapping(value = "/pageSearch.do")
    @ResponseBody
    public PageNationVo<Activity> pageSearch(String index, String pageSize, HttpServletRequest request) {

        System.out.println("执行市场列表刷新操作！");

        String name = request.getParameter("name");
        String owner = request.getParameter("owner");
        String startDate = request.getParameter("startDate");
        String endDate = request.getParameter("endDate");

        Map<String,Object> map = new HashMap<>();
        map.put("index", Integer.valueOf(index));
        map.put("pageSize", Integer.valueOf(pageSize));
        map.put("name", name);
        map.put("owner", owner);
        map.put("startDate", startDate);
        map.put("endDate", endDate);

        PageNationVo<Activity> vo = activityService.doPageSearch(map);

        Integer total_Num = activityService.selectAll();
        System.out.println(vo.getTotal());

        return vo;
    }
}
