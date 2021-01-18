package com.interview.geobyte.service;


import com.interview.geobyte.dto.PortalUserResponse;

public interface PortalUserService {

    PortalUserResponse getPortalUserByEmail(String username);

    PortalUserResponse getPortalUserById(Long id);
}
