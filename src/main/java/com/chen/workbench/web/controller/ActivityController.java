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
import com.chen.workbench.domain.ActivityRemark;
import com.chen.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping(value = "/update.do",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Boolean> updateActivity(Activity activity,HttpServletRequest request,HttpServletResponse response) {

        System.out.println("执行市场活动修改操作！");

        activity.setEditTime(DateTimeUtil.getSysTime());
        activity.setEditBy(((User)request.getSession().getAttribute("user")).getName());

        //System.out.println(activity);
        //List<User> users = activityService.addActivity();
        Map<String,Boolean> map = activityService.updateActivity(activity);
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

        System.out.println(vo.getTotal());

        return vo;
    }

    @RequestMapping(value = "/delete.do")
    @ResponseBody
    public Map<String,Boolean> doDelete(HttpServletRequest request){
        System.out.println("执行市场活动及备注删除操作！");

        String[] ids = request.getParameterValues("id");

        boolean success = activityService.deleteActivity(ids);
        Map<String,Boolean> map = new HashMap<>();
        map.put("success",success);
        return map;
    }

    @RequestMapping(value = "/searchOne.do")
    @ResponseBody
    public Activity doSearchOne(String id){
        Activity act = activityService.findOne2(id);
        System.out.println(act.getId());
        return act;
    }

    // 活动详情页
    @RequestMapping(value = "/detail.do")
    public ModelAndView toDetailPage(String id){
        System.out.println("进入详情页");
        ModelAndView mv = new ModelAndView();

        Activity act = activityService.findOne(id);

        mv.addObject("info", act);
        mv.setViewName("../workbench/activity/detail");
        //System.out.println(act);
        //System.out.println(act.getId());
        return mv;
    }

    @RequestMapping(value = "/getRemarkListById.do")
    @ResponseBody
    public List<ActivityRemark> doGetRemarkList(String activityId){
        //System.out.println(activityId);

        List<ActivityRemark> act = activityService.getRemarkList(activityId);
        //System.out.println(act);
        return act;
    }

    @RequestMapping(value = "/deleteRemark.do")
    @ResponseBody
    public Map<String,Boolean> doDeleteRemark(String remarkId){
        //System.out.println(remarkId);
        Map<String,Boolean> flag = activityService.deleteRemark(remarkId);
        //System.out.println(act);
        return flag;
    }

    @RequestMapping(value = "/addRemark.do")
    @ResponseBody
    public ActivityRemark doAddRemark(ActivityRemark remark, HttpServletRequest request){
        //System.out.println(remarkId);
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateTime(DateTimeUtil.getSysTime());
        remark.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        ActivityRemark remark2 = activityService.addRemark(remark);
        //System.out.println(act);
        return remark2;
    }

    @RequestMapping(value = "/updateRemark.do")
    @ResponseBody
    public ActivityRemark doUpdateRemark(String id, String noteContent,HttpServletRequest request){
        ActivityRemark remark = new ActivityRemark();
        remark.setId(id);
        remark.setNoteContent(noteContent);
        remark.setEditBy(((User)request.getSession().getAttribute("user")).getName());
        remark.setEditTime(DateTimeUtil.getSysTime());
        remark.setEditFlag("1");

        ActivityRemark mark = activityService.updateRemark(remark);
        return mark;
    }
}
