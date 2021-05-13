package com.chen.workbench.dao;

import com.chen.workbench.domain.Activity;
import com.chen.workbench.domain.ActivityRemark;

import java.util.List;

/**
 * @author chenhongchang
 * @date 2021/5/9 0009 - 上午 10:26
 */
public interface ActivityRemarkDao {

    int getCountByAids(String[] ids);
    int deleteRemarkByAids(String[] ids);

    List<ActivityRemark> getRemarkList(String activityId);
    int deleteRemarkById(String remarkId);
    int addRemark(ActivityRemark remark);
    int updateRemark(ActivityRemark remark);

}
