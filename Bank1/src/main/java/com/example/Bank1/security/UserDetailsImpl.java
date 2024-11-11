package com.example.Bank1.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.example.Bank1.model.Customer;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@EqualsAndHashCode(of = {"username"})
public class UserDetailsImpl implements UserDetails {

    private static final long serialVersionUID = 1L;

    private Long customerId;

    private String customerName;

    private Long customerPhoneNumber;

    private String username;

    @JsonIgnore
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    public static UserDetailsImpl build(Customer customer) {
        List<GrantedAuthority> authorities = customer.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getType().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                customer.getCustomerId(),
                customer.getCustomerName(),
                customer.getCustomerPhoneNumber(),
                customer.getUsername(),
                customer.getPassword(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
