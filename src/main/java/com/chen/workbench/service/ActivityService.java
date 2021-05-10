package com.chen.workbench.service;

import com.chen.vo.PageNationVo;
import com.chen.workbench.domain.Activity;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/9 0009 - 上午 8:59
 */
public interface ActivityService {

    Map<String,Boolean> addActivity(Activity activity);
    PageNationVo<Activity> doPageSearch(Map<String,Object> map);
    boolean deleteActivity(String[] ids);
    Activity findOne(String id);
    Map<String,Boolean> updateActivity(Activity activity);


}
