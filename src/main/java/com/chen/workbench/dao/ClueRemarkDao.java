package com.chen.workbench.dao;

import com.chen.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkDao {

    List<ClueRemark> getClueListById(String clueId);

    int delete(ClueRemark cr);
}
