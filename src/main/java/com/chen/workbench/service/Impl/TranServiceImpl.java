package com.chen.workbench.service.Impl;

import com.alibaba.druid.util.Histogram;
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
    @Autowired
    private CustomerDao customerDao;

    @Override
    public boolean save(Tran tran) {
        boolean flag = true;

        String customerName = tran.getCustomerId();
        Customer customer = customerDao.getCustomerByName(customerName);
        if(customer==null){
            customer = new Customer();
            customer.setId(UUIDUtil.getUUID());
            customer.setOwner(customer.getOwner());
            customer.setNextContactTime(customer.getNextContactTime());
            customer.setName(customerName);
            customer.setContactSummary(tran.getContactSummary());
            customer.setCreateTime(tran.getCreateTime());
            customer.setCreateBy(tran.getCreateBy());
            // 添加客户
            int num = customerDao.addCustomer(customer);
            if(num!=1) flag = false;
        }

        tran.setCustomerId(customer.getId());
        int res = tranDao.addTran(tran);
        if(res!=1) flag = false;

        // 添加交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setCreateBy(tran.getCreateBy());
        tranHistory.setCreateTime(tran.getCreateTime());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setStage(tran.getStage());
            // 添加进数据库
        int count6 = tranHistoryDao.addHistory(tranHistory);
        if(count6!=1) flag = false;

        return flag;
    }

    @Override
    public Tran getTranById(String id) {
        Tran tran = tranDao.selectTranById(id);
        return tran;
    }

    @Override
    public List<TranHistory> getTranHistoryByTranId(String tranId) {
        List<TranHistory> list = tranHistoryDao.getHistoryListByTranId(tranId);
        return list;
    }

    @Override
    public String changeStage(Tran tran) {
        String flag = "true";
        int count1 = tranDao.changeStage(tran);
        if(count1!=1) flag = "false";

        // 生成交易历史
        TranHistory tranHistory = new TranHistory();
        tranHistory.setId(UUIDUtil.getUUID());
        tranHistory.setTranId(tran.getId());
        tranHistory.setCreateBy(tran.getEditBy());
        tranHistory.setCreateTime(DateTimeUtil.getSysTime());
        tranHistory.setStage(tran.getStage());
        tranHistory.setMoney(tran.getMoney());
        tranHistory.setExpectedDate(tran.getExpectedDate());
        int count2 = tranHistoryDao.addHistory(tranHistory);
        if(count2!=1) flag = "false";

        return flag;
    }
}