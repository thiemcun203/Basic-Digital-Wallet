package com.example.Bank1.controller;

import com.example.Bank1.model.Customer;
import com.example.Bank1.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public ResponseEntity<List<Customer>> getAllCustomer() {
        List<Customer> customers = customerService.getAllCustomer();
        return ResponseEntity.ok(customers);
    }

    @PostMapping("/createCustomer")
    public ResponseEntity<Customer> createCustomer(
            @RequestParam Long customerId,
            @RequestParam String customerName,
            @RequestParam Long customerPhoneNumber,
            @RequestParam String username,
            @RequestParam String password) {
        Customer customer = customerService.createCustomer(customerId, customerName, customerPhoneNumber, username, password);
        return ResponseEntity.ok(customer);
    }
}
