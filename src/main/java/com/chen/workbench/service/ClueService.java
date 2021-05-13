package com.chen.workbench.service;

import com.chen.vo.PageNationVo;
import com.chen.workbench.domain.Activity;
import com.chen.workbench.domain.Clue;
import com.chen.workbench.domain.Tran;

import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:25
 */
public interface ClueService {

    Map<String,Boolean> saveClue(Clue clue);

    PageNationVo<Clue> selectPageClue(String index, String pageSize, Clue clue);

    Clue getOneClue(String id);

    List<Activity> selectActivityListByClueId(String clueId);

    Map<String,Boolean> deleteRelation(String car_id);

    Map<String,String> addRelation(String clueId,List<String> list);

    boolean convert(String clueId, Tran tran,String createBy);

}
