package com.chen.web;

import com.chen.settings.domain.DicValue;
import com.chen.settings.service.DicService;
import com.chen.settings.service.UserServiceImp.DicServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.*;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 6:48
 */
public class SysInitListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        // 获取当前spring容器
        WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
        // 获取DicService对象
        DicService service = wac.getBean("dicServiceImpl", DicServiceImpl.class);

        Map<String, List<DicValue>> map = service.getDicValueMap();
        ServletContext application = servletContextEvent.getServletContext();
        for(String s:map.keySet()){
            application.setAttribute(s,map.get(s));
        }

        System.out.println("全局作用域对象创建了");
        //System.out.println(application.getAttribute("map"));

        //==========================================================
        //处理Stage2Possibility.properties文件，转换成键值对形式
        // Map<String(阶段),String(可能性)> pMap = ...
        Map<String,String> pMap = new HashMap<>();
        ResourceBundle rb = ResourceBundle.getBundle("Stage2Possibility");
        Enumeration<String> e = rb.getKeys();
        while (e.hasMoreElements()){
            String key = e.nextElement();
            String value = rb.getString(key);
            pMap.put(key,value);
        }
        application.setAttribute("pMap",pMap);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}
