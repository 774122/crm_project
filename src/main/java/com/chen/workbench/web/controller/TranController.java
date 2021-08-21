package com.chen.workbench.web.controller;

import com.chen.settings.domain.User;
import com.chen.settings.service.UserService;
import com.chen.utils.DateTimeUtil;
import com.chen.utils.UUIDUtil;
import com.chen.workbench.dao.TranHistoryDao;
import com.chen.workbench.domain.Tran;
import com.chen.workbench.domain.TranHistory;
import com.chen.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
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
    public void doAddTran(HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<User> users = userService.getUserList();

        request.setAttribute("userList",users);
        request.getRequestDispatcher("/workbench/transaction/save.jsp").forward(request,response);
    }

    @RequestMapping(value = "/save.do")
    @ResponseBody
    public ModelAndView doSave(Tran tran, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        tran.setId(UUIDUtil.getUUID());
        tran.setCreateBy(((User)request.getSession().getAttribute("user")).getName());
        tran.setCreateTime(DateTimeUtil.getSysTime());

        boolean result = tranService.save(tran);
        mv.addObject("saveResult", result);
        mv.setViewName("redirect:/workbench/transaction/index.jsp");

        return mv;
    }

    @RequestMapping(value = "/detail.do")
    @ResponseBody
    public void toDetail(String id,HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException {

        Tran tran = tranService.getTranById(id);// owner属性转换为用户名

        ServletContext application = request.getServletContext();
        Map<String,String> map = (Map<String, String>)application.getAttribute("pMap");
        tran.setPossibility(map.get(tran.getStage()));

        request.setAttribute("tran", tran);
        request.getRequestDispatcher("/workbench/transaction/detail.jsp").forward(request,response);
    }

    @RequestMapping(value = "/getHistoryList.do")
    @ResponseBody
    public List<TranHistory> getHistory(String tranId,HttpServletRequest request){

        List<TranHistory> list = tranService.getTranHistoryByTranId(tranId);
        ServletContext application = request.getServletContext();
        Map<String,String> map = (Map<String, String>)application.getAttribute("pMap");
        for(TranHistory th : list){
            th.setPossbility(map.get(th.getStage()));
        }

        return list;
    }

    @RequestMapping(value = "/changeStage.do")
    @ResponseBody
    public Map<String,String> doChangeStage(Tran tran ,HttpServletRequest request){
        tran.setEditTime(DateTimeUtil.getSysTime());
        tran.setEditBy(((User)request.getSession().getAttribute("user")).getName());

        String stage = tran.getStage();
        ServletContext application = request.getServletContext();
        Map<String,String> map = (Map<String, String>)application.getAttribute("pMap");
        tran.setPossibility(map.get(stage));

        String success = tranService.changeStage(tran);

        Map<String,String> map2 = new HashMap<>();

        map2.put("success",success);
        map2.put("tranStage",tran.getStage());
        map2.put("possibility",tran.getPossibility());
        map2.put("editor",tran.getEditBy());
        map2.put("editTime",tran.getEditTime());

        return map2;
    }

}