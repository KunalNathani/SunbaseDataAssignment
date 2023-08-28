package com.kunal.SunbaseDataAssignment.services.customer;

import com.kunal.SunbaseDataAssignment.dao.customer.CustomerDAO;
import com.kunal.SunbaseDataAssignment.models.Customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    @Autowired
    private CustomerDAO customerDAO;

    public Customer[] fetchAllRecords(String token) {
        return customerDAO.fetchAllRecords(token);
    }

    public String login(String login_id, String password) {
        return customerDAO.getLoginToken(login_id, password);
    }

    public boolean deleteCustomer(String uuid, String token) {
        return customerDAO.delete(uuid, token);
    }

    public boolean addCustomer(Customer customer, String token) {
        return customerDAO.create(customer, token);
    }

    public Customer getCustomerByUUID(String uuid, String token) {
        return customerDAO.getCustomerByUUID(uuid, token);
    }

    public boolean updateCustomer(Customer customer, String token) {
        return customerDAO.update(customer, token);
    }
}
