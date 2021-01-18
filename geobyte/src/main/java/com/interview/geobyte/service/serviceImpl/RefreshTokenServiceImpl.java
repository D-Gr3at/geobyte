package com.interview.geobyte.service.serviceImpl;


import com.interview.geobyte.dao.RefreshTokenRepository;
import com.interview.geobyte.exception.GeoByteException;
import com.interview.geobyte.model.RefreshToken;
import com.interview.geobyte.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    RefreshTokenRepository refreshTokenRepository;

    @Override
    public RefreshToken generateRefreshToken() {
        RefreshToken refreshToken =
                RefreshToken.builder()
                        .createdDate(Instant.now())
                        .refreshToken(UUID.randomUUID().toString())
                        .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public void validateRefreshToken(String token) {
         refreshTokenRepository.findByRefreshToken(token)
                .orElseThrow(()-> new GeoByteException("Invalid refresh token"));
    }

    @Override
    public void deleteRefreshToken(String token) {
        refreshTokenRepository.deleteByRefreshToken(token);
    }


}
