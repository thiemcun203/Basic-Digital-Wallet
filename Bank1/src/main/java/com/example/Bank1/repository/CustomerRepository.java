package com.example.Bank1.repository;

import com.example.Bank1.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    Optional<Customer> findByUsername(String username);
    
    boolean existsByUsernameIgnoreCase(String name);
}
