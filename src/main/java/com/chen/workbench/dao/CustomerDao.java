package com.chen.workbench.dao;

import com.chen.workbench.domain.Customer;

import java.util.List;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int addCustomer(Customer customer);

    List<String> getNameList(String name);

}
