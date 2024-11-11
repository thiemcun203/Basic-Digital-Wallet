package com.example.Bank1.service;

import com.example.Bank1.dto.mapper.SignupRequestMapper;
import com.example.Bank1.dto.request.LoginRequest;
import com.example.Bank1.dto.request.SignupRequest;
import com.example.Bank1.dto.response.CommandResponse;
import com.example.Bank1.dto.response.JwtResponse;
import com.example.Bank1.exception.ElementAlreadyExistsException;
import com.example.Bank1.model.Customer;
import com.example.Bank1.repository.CustomerRepository;
import com.example.Bank1.security.JwtUtils;
import com.example.Bank1.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.Bank1.common.Constants.*;

/**
 * Service used for Authentication related operations
 */
@Slf4j(topic = "AuthService")
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomerRepository userRepository;
    private final SignupRequestMapper signupRequestMapper;

    /**
     * Authenticates users by their credentials
     *
     * @param request
     * @return JwtResponse
     */
    public JwtResponse login(LoginRequest request) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername().trim(), request.getPassword().trim()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        final UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        final List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .toList();

        log.info(LOGGED_IN_USER, new Object[]{request.getUsername()});
        return JwtResponse
                .builder()
                .token(jwt)
                .customerId(userDetails.getCustomerId())
                .customerName(userDetails.getCustomerName())
                .customerPhoneNumber(userDetails.getCustomerPhoneNumber())
                .username(userDetails.getUsername())
                .roles(roles).build();
    }

    /**
     * Registers a user by provided credentials and user info
     *
     * @param request
     * @return id of the registered user
     */
    public CommandResponse signup(SignupRequest request) {
        if (userRepository.existsByUsernameIgnoreCase(request.getUsername().trim()))
            throw new ElementAlreadyExistsException(ALREADY_EXISTS_USER_NAME);

        final Customer user = signupRequestMapper.toEntity(request);
        userRepository.save(user);
        log.info(CREATED_USER, new Object[]{user.getUsername()});
        return CommandResponse.builder().id(user.getCustomerId()).build();
    }
}
