package com.interview.geobyte.controller;


import com.interview.geobyte.dto.AuthenticationResponse;
import com.interview.geobyte.dto.LoginDto;
import com.interview.geobyte.dto.RefreshTokenRequest;
import com.interview.geobyte.dto.SignUpDto;
import com.interview.geobyte.exception.GeoByteAsyncExceptionHandler;
import com.interview.geobyte.service.AuthService;
import com.interview.geobyte.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody SignUpDto signUpDto) throws GeoByteAsyncExceptionHandler {
        authService.signUp(signUpDto);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/accountVerification/{token}")
    public ResponseEntity<?> verifyAccount(@PathVariable("token") String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody LoginDto loginDto){
        return authService.login(loginDto);
    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
       return  authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.status(HttpStatus.OK)
                .body(response);
    }
}
