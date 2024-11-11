package com.example.Bank1.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Data Transfer Object used for authentication response
 */
@Getter
@Builder
public class JwtResponse {

    private String type;
    private String token;
    private Long customerId;
    private String customerName;
    private Long customerPhoneNumber;
    private String username;
    private List<String> roles;
}
