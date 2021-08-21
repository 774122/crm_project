package com.chen.workbench.dao;

import com.chen.workbench.domain.Tran;

public interface TranDao {

    int addTran(Tran tran);

    Tran selectTranById(String id);

    int changeStage(Tran tran);
}
