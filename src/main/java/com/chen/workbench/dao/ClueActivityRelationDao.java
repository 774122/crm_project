package com.chen.workbench.dao;

import com.chen.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationDao {


    List<ClueActivityRelation> getRelationListByClueId(String clueId);

    int delete(ClueActivityRelation car);
}
