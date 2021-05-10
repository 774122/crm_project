package com.chen.workbench.dao;

import com.chen.workbench.domain.Activity;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/9 0009 - 上午 8:55
 */
public interface ActivityDao {

    int addActivity(Activity act);
    int queryTotalNumByCondition();
    List<Activity> pageSearchByCondition(Map<String,Object> map);

}
