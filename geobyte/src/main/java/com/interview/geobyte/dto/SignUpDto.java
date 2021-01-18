package com.interview.geobyte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDto {

    @NotBlank(message = "username is required")
    private String name;

    @NotBlank(message = "username is required")
    private String password;

    @Email
    @NotBlank(message = "Email is required")
    private String email;
}
