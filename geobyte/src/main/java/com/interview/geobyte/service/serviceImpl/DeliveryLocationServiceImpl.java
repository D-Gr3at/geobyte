package com.interview.geobyte.service.serviceImpl;

import com.interview.geobyte.dao.DeliveryLocationRepository;
import com.interview.geobyte.dao.PortalUserRepository;
import com.interview.geobyte.dto.DeliveryLocationDto;
import com.interview.geobyte.exception.GeoByteException;
import com.interview.geobyte.exception.LocationNotFoundException;
import com.interview.geobyte.exception.PortalUserNotFoundException;
import com.interview.geobyte.model.DeliveryLocation;
import com.interview.geobyte.model.PortalUser;
import com.interview.geobyte.service.DeliveryLocationService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DeliveryLocationServiceImpl implements DeliveryLocationService {

    private final DeliveryLocationRepository deliveryLocationRepository;

    private final PortalUserRepository portalUserRepository;

    @Transactional
    @Override
    public DeliveryLocationDto createDeliveryLocation(DeliveryLocationDto deliveryLocationDto) {
        PortalUser portalUser = portalUserRepository.findById(deliveryLocationDto.getUserId())
                .orElseThrow(()-> new PortalUserNotFoundException(String.format("Portal user with ID: %d does not exist", deliveryLocationDto.getUserId())));
        Optional<DeliveryLocation> optionalDeliveryLocation = deliveryLocationRepository.findByLocationName(deliveryLocationDto.getLocationName().toLowerCase());
        if (optionalDeliveryLocation.isPresent()){
            throw new GeoByteException(String.format("Delivery location with name %s already exist.", deliveryLocationDto.getLocationName()));
        }
        DeliveryLocation deliveryLocation = DeliveryLocation.builder()
                        .clearingCost(deliveryLocationDto.getClearingCost())
                        .createdBy(portalUser)
                        .createdDate(Instant.now())
                        .latitude(deliveryLocationDto.getLatitude())
                        .locationName(deliveryLocationDto.getLocationName().toLowerCase())
                        .longitude(deliveryLocationDto.getLongitude())
                        .build();
        deliveryLocationRepository.save(deliveryLocation);

        return DeliveryLocationDto.builder().id(deliveryLocation.getId()).build();
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeliveryLocationDto> findByPortalUserId(Long id) {
       return deliveryLocationRepository.findByCreatedBy(
                portalUserRepository.findById(id)
                        .orElseThrow(()-> new PortalUserNotFoundException(String.format("User with UD: %d not found.", id)))
        ).stream()
               .map(this::mapToDto)
               .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    @Override
    public List<DeliveryLocationDto> findByLocationName(String name) {
        return deliveryLocationRepository.findByLocationNameContains(name.toLowerCase())
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public HashMap<String, String> deleteLocationById(Long id) {
        deliveryLocationRepository.deleteById(id);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "Deleted successfully");
        return response;
    }

    @Override
    public DeliveryLocationDto updateDeliveryLocation(Long id, DeliveryLocationDto deliveryLocationDto) {
        DeliveryLocation deliveryLocation = deliveryLocationRepository.findById(id)
                .orElseThrow(()-> new LocationNotFoundException(String.format("Delivery location with ID: %d not found.", id)));
        deliveryLocation.setClearingCost(deliveryLocationDto.getClearingCost());
        deliveryLocation.setClearingCost(deliveryLocationDto.getClearingCost());
        deliveryLocation.setLatitude(deliveryLocationDto.getLatitude());
        deliveryLocation.setLocationName(deliveryLocationDto.getLocationName());
        deliveryLocation.setLongitude(deliveryLocationDto.getLongitude());
        deliveryLocationRepository.save(deliveryLocation);
        return DeliveryLocationDto.builder().id(deliveryLocation.getId()).build();
    }

    private DeliveryLocationDto mapToDto(DeliveryLocation deliveryLocation){
        return DeliveryLocationDto.builder()
                .userId(deliveryLocation.getCreatedBy().getId())
                .longitude(deliveryLocation.getLongitude())
                .locationName(deliveryLocation.getLocationName())
                .latitude(deliveryLocation.getLatitude())
                .createdDate(deliveryLocation.getCreatedDate().toString())
                .clearingCost(deliveryLocation.getClearingCost())
                .id(deliveryLocation.getId())
                .build();
    }

}
