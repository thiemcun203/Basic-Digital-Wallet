package com.example.Bank1.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * Data Transfer Object for Signup request
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {

    private Long customerId;

    @Size(min = 3, max = 50, message = "{customerName.size}")
    @NotBlank(message = "{customerName.notblank}")
    private String customerName;

    @Size(min = 10, max = 10, message = "{customerPhoneNumber.size}")
    @NotBlank(message = "{customerPhoneNumber.notblank}")
    private Long customerPhoneNumber;

    @Size(min = 3, max = 20, message = "{username.size}")
    @NotBlank(message = "{username.notblank}")
    private String username;

    @Size(min = 6, max = 100, message = "{password.size}")
    @NotBlank(message = "password.notblank}")
    private String password;

    private Set<String> roles;
}
