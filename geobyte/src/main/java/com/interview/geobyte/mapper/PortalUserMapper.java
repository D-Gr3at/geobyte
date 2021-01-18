package com.interview.geobyte.mapper;

import com.interview.geobyte.dto.PortalUserResponse;
import com.interview.geobyte.model.PortalUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PortalUserMapper {

    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "id", target = "id")
    PortalUserResponse mapToDto(PortalUser portalUser);
}
