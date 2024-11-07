package com.example.Bank2.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.Bank2.model.Customer;
import com.example.Bank2.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getAllCustomer() {
        List<Customer> customers = customerRepository.findAll();
        return customers;
    }

    public Customer createCustomer(Long customerId, String customerName, Long customerPhoneNumber) {
        Customer customer = new Customer(customerId, customerName, customerPhoneNumber);
        return customerRepository.save(customer);
    }

    public Optional<Customer> updateCustomerInformation(Long customerId, String customerName, Long customerPhoneNumber) {
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isPresent()) {
            Customer customer = customerOpt.get();
            customer.setCustomerName(customerName);
            customer.setCustomerPhoneNumber(customerPhoneNumber);
            customerRepository.save(customer);
        }
        return customerOpt;
    }

    public void deleteCustomer(Long customerId) {
        customerRepository.deleteById(customerId);
    }

    public Optional<Customer> findCustomer(Long customerId) {
        return customerRepository.findById(customerId);
    }
}
