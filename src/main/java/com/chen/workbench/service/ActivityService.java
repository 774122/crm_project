package com.chen.workbench.service;

import com.chen.vo.PageNationVo;
import com.chen.workbench.domain.Activity;
import com.chen.workbench.domain.ActivityRemark;

import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/9 0009 - 上午 8:59
 */
public interface ActivityService {

    // 市场活动相关
    Map<String,Boolean> addActivity(Activity activity);
    PageNationVo<Activity> doPageSearch(Map<String,Object> map);
    boolean deleteActivity(String[] ids);
    Activity findOne(String id);
    Map<String,Boolean> updateActivity(Activity activity);

    // 市场活动备注相关
    List<ActivityRemark> getRemarkList(String activityId);
    Map<String,Boolean> deleteRemark(String remarkId);
    ActivityRemark addRemark(ActivityRemark remark);

}
