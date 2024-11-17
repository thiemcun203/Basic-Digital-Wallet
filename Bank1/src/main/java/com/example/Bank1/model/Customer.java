package com.example.Bank1.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = {"customerId", "username"})
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "customer_seq_gen"
    )
    @SequenceGenerator(
            name = "customer_seq_gen",
            sequenceName = "customer_seq",
            allocationSize = 5
    )
    private Long customerId;

    @Column(length = 100, nullable = false)
    private String customerName;
    
    @Column(length = 10, nullable = false)
    private Long customerPhoneNumber;
    
    @Column(length = 20, unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "customer_role",
            joinColumns = @JoinColumn(name = "customer_id", referencedColumnName = "customerId", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "roleId")
    )
    private Set<Role> roles = new HashSet<>();

    public Customer() {
    }

    public Customer(Long customerId, String customerName, Long customerPhoneNumber, String username, String password) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.username = username;
        this.password = password;
    }
}
