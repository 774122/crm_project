package com.chen.workbench.dao;

import com.chen.workbench.domain.TranHistory;

import java.util.List;

public interface TranHistoryDao {

    int addHistory(TranHistory tranHistory);

    List<TranHistory> getHistoryListByTranId(String tranId);
}
