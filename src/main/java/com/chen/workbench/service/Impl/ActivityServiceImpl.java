package com.chen.workbench.service.Impl;

import com.chen.vo.PageNationVo;
import com.chen.workbench.dao.ActivityDao;
import com.chen.workbench.dao.ActivityRemarkDao;
import com.chen.workbench.domain.Activity;
import com.chen.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/9 0009 - 上午 8:59
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao dao;
    @Autowired
    private ActivityRemarkDao r_dao;

    @Override
    public Map<String,Boolean> addActivity(Activity activity) {
        int num = dao.addActivity(activity);
        Map<String,Boolean> map = new HashMap<>();
        if(num==1){
            map.put("success", true);
        }else{
            map.put("success", false);
        }
        return map;
    }



    @Override
    public PageNationVo<Activity> doPageSearch(Map<String,Object> map) {

        // 将参数转为Integer类型
        Integer page_Size = (Integer) map.get("pageSize");
        Integer page_Num = (Integer) map.get("index");
        // 获取总记录条数，总页数
        int totalNum = dao.queryTotalNumByCondition(map);
        int totalPage = (totalNum%page_Size == 0) ? totalNum/page_Size : totalNum/page_Size +1;
        // 获取开始索引
        int begin = (page_Num-1)*page_Size;
        map.put("index",begin);
        List<Activity> actList = new ArrayList<>();
        if(page_Num <= totalPage){
            actList = dao.pageSearchByCondition(map);
        }

        PageNationVo<Activity> vo = new PageNationVo<>();
        vo.setTotal(totalNum);
        vo.setDataList(actList);
        return vo;
    }

    @Override
    public boolean deleteActivity(String[] ids) {
        boolean flag = true;

        //先查找出要删除的备注的数量
        int count1 = r_dao.getCountByAids(ids);

        // 删除备注，返回受影响的条数
        int count2 = r_dao.deleteRemarkByAids(ids);
        if(count1 != count2){flag = false;}

        // 删除市场活动
        int count3 = dao.delete(ids);
        if(count3!=ids.length){ flag=false; }

        return flag;
    }

    @Override
    public Activity findOne(String id) {
        Activity act = dao.findOneActivity(id);
        return act;
    }

    @Override
    public Map<String, Boolean> updateActivity(Activity activity) {
        Map<String,Boolean> map = new HashMap<>();
        // 先根据id查询，若没有变动则不进行更新操作
        Activity activity1 = dao.selectOneAllField(activity.getId());
        if( activity1.getId()         .equals(activity.getId()) &&
            activity1.getOwner()      .equals(activity.getOwner()) &&
            activity1.getName()       .equals(activity.getName()) &&
            activity1.getStartDate()  .equals(activity.getStartDate()) &&
            activity1.getEndDate()    .equals( activity.getEndDate()) &&
            activity1.getCost()       .equals(activity.getCost()) &&
            activity1.getDescription().equals(activity.getDescription()) &&
            activity1.getEditBy()     .equals(activity.getEditBy()))
        {
            map.put("success", null);
            return map;
        }

        System.out.println(activity + "\n" +activity1);
        int result = dao.updateActivityById(activity);
        if(result==1){
            map.put("success",true);
        }else{map.put("success", false);}
        return map;
    }
}
