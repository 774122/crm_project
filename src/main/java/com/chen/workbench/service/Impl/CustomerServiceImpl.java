package com.chen.workbench.service.Impl;

import com.chen.workbench.dao.CustomerDao;
import com.chen.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chenhongchang
 * @date 2021/5/14 0014 - 下午 1:42
 */
@Service
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private CustomerDao customerDao;


    @Override
    public List<String> getNameListByPartName(String name) {

        List<String> list = customerDao.getNameList(name);

        return list;
    }
}
