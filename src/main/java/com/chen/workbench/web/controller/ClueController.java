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
import com.sun.net.httpserver.HttpPrincipal;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/7 0007 - 下午 3:12
 */
@Controller
@RequestMapping(value = "/Clue")
public class ClueController {

    @Autowired
    private ClueService clueService;
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/getUserList.do")
    @ResponseBody
    public List<User> getUserList(){
        List<User> list = userService.getUserList();
        return list;
    }

    @RequestMapping(value = "/saveClue.do")
    @ResponseBody
    public Map<String,Boolean> saveClue(Clue clue, HttpServletRequest request){
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DateTimeUtil.getSysTime());
        clue.setCreateBy(((User)request.getSession().getAttribute("user")).getName());

        Map<String,Boolean> map = clueService.saveClue(clue);
        return map;
    }

    @RequestMapping(value = "/selectClue.do")
    @ResponseBody
    public PageNationVo<Clue> selectClue(Clue clue,HttpServletRequest request){
        System.out.println("进入线索查询控制模块");
        String index = request.getParameter("index");
        String pageSize = request.getParameter("pageSize");
        //System.out.println(index);
        //System.out.println(pageSize);
        //System.out.println(clue);

        PageNationVo<Clue> clues = clueService.selectPageClue(index,pageSize,clue);

        //System.out.println(clues.getDataList().size());

        return clues;
    }

    @RequestMapping(value = "/detail.do")
    public ModelAndView goDetail(String id, HttpServletRequest request){

        Clue clue = clueService.getOneClue(id);

        ModelAndView mv = new ModelAndView();
        mv.addObject("clue",clue);
        mv.setViewName("../workbench/clue/detail");

        return mv;
    }

    @RequestMapping(value = "/selectActivityListById.do")
    @ResponseBody
    public List<Activity> selectActivityList(String clueId, HttpServletRequest request){
        System.out.println("进入活动详细信息查询控制模块");

        List<Activity> activityList = clueService.selectActivityListByClueId(clueId);

        return activityList;
    }

    @RequestMapping(value = "/deleteRelation.do")
    @ResponseBody
    public Map<String,Boolean> deleteRelation(String car_id, HttpServletRequest request){

        Map<String,Boolean> map = clueService.deleteRelation(car_id);

        return map;
    }

    @RequestMapping(value = "/findAllActivity.do")
    @ResponseBody
    public List<Activity> findAllActivity(String partName,String clueId, HttpServletRequest request){

        List<Activity> list = activityService.findActivityByName(partName, clueId);

        return list;
    }

    @RequestMapping(value = "/bundActivity.do")
    @ResponseBody
    public Map<String,String> bundActivity(String clueId, String activityId){

        Map<String,String> map;

        System.out.println(clueId);
        String[] list1 = activityId.split(",");
        List<String> list = new ArrayList<>();
        for(String s : list1){
            list.add(s);
        }
        map = clueService.addRelation(clueId, list);

        //List<Activity> list = activityService.findActivityByName(partName, clueId);

        return map;
    }

    @RequestMapping(value = "/getActivityListByName.do")
    @ResponseBody
    public List<Activity> getActivityListByName(String aname){

        List<Activity> list = activityService.getActivityListByPartName(aname);

        return list;
    }

    @RequestMapping(value = "/convert.do")
    @ResponseBody
    public void doConvertNoTransaction(Tran tran, HttpServletRequest request,
                                                 HttpServletResponse response) throws IOException {
        String clueId;
        String createBy = ((User)request.getSession().getAttribute("user")).getName();

        String flag = request.getParameter("flag");
        if(null==flag||"".equals(flag)){
            clueId = request.getParameter("clueId");
            tran = null;
        }else{
            clueId = request.getParameter("clueId");
            /*String money = request.getParameter("money");
            String name = request.getParameter("name");
            String expectedDate = request.getParameter("expectedDate");
            String stage = request.getParameter("stage");
            String activityId = request.getParameter("activityId");*/
            tran.setId(UUIDUtil.getUUID());
            tran.setCreateTime(DateTimeUtil.getSysTime());
            tran.setCreateBy(createBy);
        }
        boolean flag2 = clueService.convert(clueId,tran,createBy);

        if(flag2){
            response.sendRedirect(request.getContextPath()+"/workbench/clue/index.jsp");
        }
    }

}
