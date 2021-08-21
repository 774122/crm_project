package com.chen.workbench.service;

import com.chen.vo.PageNationVo;
import com.chen.workbench.domain.Activity;
import com.chen.workbench.domain.Clue;
import com.chen.workbench.domain.Tran;
import com.chen.workbench.domain.TranHistory;

import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:25
 */
public interface TranService {

    boolean save(Tran tran);

    Tran getTranById(String id);

    List<TranHistory> getTranHistoryByTranId(String tranId);

    String changeStage(Tran tran);
}
