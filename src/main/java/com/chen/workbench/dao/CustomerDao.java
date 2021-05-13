package com.chen.workbench.dao;

import com.chen.workbench.domain.Customer;

public interface CustomerDao {

    Customer getCustomerByName(String company);

    int addCustomer(Customer customer);
}
