package com.interview.geobyte.service.serviceImpl;

import com.interview.geobyte.dao.PortalUserRepository;
import com.interview.geobyte.dao.VerificationTokenRepository;
import com.interview.geobyte.dto.*;
import com.interview.geobyte.exception.GeoByteAsyncExceptionHandler;
import com.interview.geobyte.exception.GeoByteException;
import com.interview.geobyte.model.PortalUser;
import com.interview.geobyte.model.VerificationToken;
import com.interview.geobyte.security.JwtProvider;
import com.interview.geobyte.service.AuthService;
import com.interview.geobyte.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final PortalUserRepository portalUserRepository;


    private final VerificationTokenRepository verificationTokenRepository;

    private final MailService mailService;

    private final AuthenticationManager authenticationManager;

    private final JwtProvider jwtProvider;

    private final RefreshTokenService refreshTokenService;

    @Transactional
    @Override
    public void signUp(SignUpDto signUpDto) throws GeoByteAsyncExceptionHandler {
        Optional<PortalUser> user = portalUserRepository.findByEmail(signUpDto.getEmail().trim());
        if (user.isPresent()){
            throw new GeoByteException(String.format("User with this email %s already exist", signUpDto.getEmail().trim()));
        }
        PortalUser portalUser = new PortalUser();
        portalUser.setName(signUpDto.getName());
        portalUser.setEmail(signUpDto.getEmail().trim());
        portalUser.setCreatedDate(Instant.now());
        portalUser.setEnabled(false);
        portalUser.setPassword(bCryptPasswordEncoder.encode(signUpDto.getPassword().trim()));
       String token =  generateVerificationToken(portalUser);

       mailService.sendMail(new NotificationEmail("Account Verification", signUpDto.getEmail(),
               "Thank you for signing up to Spring Reddit, Please click the url below to activate your account:"+
               "http://localhost:8080/api/v1/auth/accountVerification/"+token));
        portalUserRepository.save(portalUser);
    }

    @Override
    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new GeoByteException("Invalid token"));
        fetchPortalUserAndEnable(verificationToken.get());
    }

    @Override
    public AuthenticationResponse login(LoginDto loginDto) {
        PortalUser portalUser = portalUserRepository.findByEmail(loginDto.getEmail().trim())
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s does not exist", loginDto.getEmail().trim())));
        if (!portalUser.isEnabled()){
            throw new GeoByteException(String.format("Account with email %s has not been verified", loginDto.getEmail().trim()));
        }
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtProvider.generateToken(authentication);
        return AuthenticationResponse.builder()
                .token(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getRefreshToken())
                .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTimeInMillis()).toString())
                .email(loginDto.getEmail())
                .build();
    }

    @Transactional(readOnly = true)
    @Override
    public PortalUser getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return portalUserRepository.findByEmail(authentication.getPrincipal().toString())
                .orElseThrow(()-> new GeoByteException("User has not been authenticated."));
    }

    @Override
    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
         refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
         String refreshToken = jwtProvider.generateTokenWithEmail(refreshTokenRequest.getEmail().trim());
         return AuthenticationResponse.builder()
                 .token(refreshToken)
                 .refreshToken(refreshTokenRequest.getRefreshToken())
                 .email(refreshTokenRequest.getEmail().trim())
                 .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationTimeInMillis()).toString())
                 .build();
    }

    @Transactional
    protected void fetchPortalUserAndEnable(VerificationToken verificationToken) {
        String email = verificationToken.getPortalUser().getEmail();
        PortalUser portalUser = portalUserRepository.findByEmail(email)
                .orElseThrow(() -> new GeoByteException(String.format("Email %s does not exist", email)));
        portalUser.setEnabled(true);
        portalUserRepository.save(portalUser);
    }

    @Transactional
    protected String generateVerificationToken(PortalUser portalUser){
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setPortalUser(portalUser);

        verificationTokenRepository.save(verificationToken);
        return token;
    }
}
