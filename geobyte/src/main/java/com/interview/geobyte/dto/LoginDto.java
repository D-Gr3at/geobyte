package com.interview.geobyte.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "password is required")
    private String password;
}
