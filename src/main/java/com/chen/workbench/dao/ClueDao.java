package com.chen.workbench.dao;

import com.chen.workbench.domain.Activity;
import com.chen.workbench.domain.Clue;

import java.util.List;
import java.util.Map;

public interface ClueDao {

    int saveClue(Clue clue);

    int askTotalClueNumByCondition(Clue clue);

    List<Clue> askClueByCondition(Map<String,Object> map);

    Clue getOneClueById(String id);

    List<Activity> selectActivity(String clueId);

    int deleteRelationById(String car_id);

    int addActRelation(Map<String,String> val);

    Clue getById(String clueId);

    int delete(String clueId);
}
