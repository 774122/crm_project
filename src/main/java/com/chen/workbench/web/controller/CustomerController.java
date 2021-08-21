package com.chen.workbench.web.controller;

import com.chen.workbench.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author chenhongchang
 * @date 2021/5/14 0014 - 下午 1:43
 */
@Controller
@RequestMapping("/Customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @RequestMapping(value = "/getCustomerName.do")
    @ResponseBody
    public List<String> doGetCustomerName(String name, HttpServletRequest request, HttpServletResponse response) throws Exception{
        // 取得客户名称列表：按照客户名称模糊查询
        List<String> list = customerService.getNameListByPartName(name);
        return list;
    }
}
