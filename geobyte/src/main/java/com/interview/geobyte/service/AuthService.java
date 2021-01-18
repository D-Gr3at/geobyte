package com.interview.geobyte.service;


import com.interview.geobyte.dto.AuthenticationResponse;
import com.interview.geobyte.dto.LoginDto;
import com.interview.geobyte.dto.RefreshTokenRequest;
import com.interview.geobyte.dto.SignUpDto;
import com.interview.geobyte.exception.GeoByteAsyncExceptionHandler;
import com.interview.geobyte.model.PortalUser;

public interface AuthService {

    void signUp(SignUpDto signUpDto) throws GeoByteAsyncExceptionHandler;

    void verifyAccount(String token);

    AuthenticationResponse login(LoginDto loginDto);

    PortalUser getCurrentUser();

    AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
