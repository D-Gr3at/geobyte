package com.interview.geobyte.service.serviceImpl;


import com.interview.geobyte.dao.PortalUserRepository;
import com.interview.geobyte.dto.PortalUserResponse;
import com.interview.geobyte.exception.GeoByteException;
import com.interview.geobyte.mapper.PortalUserMapper;
import com.interview.geobyte.service.PortalUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class PortalUserServiceImpl implements PortalUserService {

    private final PortalUserRepository portalUserRepository;

    private final PortalUserMapper portalUserMapper;

    @Transactional(readOnly = true)
    @Override
    public PortalUserResponse getPortalUserByEmail(String email) {
        return portalUserMapper.mapToDto(
                portalUserRepository.findByEmail(email)
                        .orElseThrow(
                                ()-> new GeoByteException(String.format("Portal user with email: %s does not exist", email)))
        );
    }

    @Transactional(readOnly = true)
    @Override
    public PortalUserResponse getPortalUserById(Long id) {
        return portalUserMapper.mapToDto(
                portalUserRepository.findById(id)
                        .orElseThrow(
                                ()-> new GeoByteException(String.format("Portal user with ID: %s does not exist", id))
                        )
        );
    }
}
