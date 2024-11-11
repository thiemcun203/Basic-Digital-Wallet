package com.example.Bank1.model;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@EqualsAndHashCode(of = {"type"})
public class Role {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_seq_gen"
    )
    @SequenceGenerator(
            name = "role_seq_gen",
            sequenceName = "role_seq",
            allocationSize = 5
    )
    private Long roleId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false, unique = true)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    private Set<Customer> users = new HashSet<>();

    public void addUser(Customer user) {
        users.add(user);
        user.getRoles().add(this);
    }

    public void removeUser(Customer user) {
        users.remove(user);
        user.getRoles().remove(this);
    }
}
