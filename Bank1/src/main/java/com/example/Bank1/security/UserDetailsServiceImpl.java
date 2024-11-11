package com.example.Bank1.security;

import com.example.Bank1.model.Customer;
import com.example.Bank1.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

import static com.example.Bank1.common.Constants.NOT_FOUND_USERNAME;

/**
 * Service used for UserDetails related operations
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final CustomerRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final Customer user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(MessageFormat.format(NOT_FOUND_USERNAME, username)));
        return UserDetailsImpl.build(user);
    }
}
