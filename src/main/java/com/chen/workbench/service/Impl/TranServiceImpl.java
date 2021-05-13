package com.chen.workbench.service.Impl;

import com.chen.utils.DateTimeUtil;
import com.chen.utils.UUIDUtil;
import com.chen.vo.PageNationVo;
import com.chen.workbench.dao.*;
import com.chen.workbench.domain.*;
import com.chen.workbench.service.ClueService;
import com.chen.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenhongchang
 * @date 2021/5/11 0011 - 下午 4:26
 */
@Service
public class TranServiceImpl implements TranService {

    @Autowired
    private TranDao tranDao;
    @Autowired
    private TranHistoryDao tranHistoryDao;

}