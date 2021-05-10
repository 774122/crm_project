package com.chen.workbench.service.Impl;

import com.chen.vo.PageNationVo;
import com.chen.workbench.dao.ActivityDao;
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
        int totalNum = dao.queryTotalNumByCondition();
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
    public int selectAll() {
        int n = dao.queryTotalNumByCondition();
        return n;
    }
}
