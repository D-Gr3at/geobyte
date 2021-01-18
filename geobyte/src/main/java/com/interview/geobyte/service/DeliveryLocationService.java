package com.interview.geobyte.service;

import com.interview.geobyte.dto.DeliveryLocationDto;

import java.util.HashMap;
import java.util.List;

public interface DeliveryLocationService {

    DeliveryLocationDto createDeliveryLocation(DeliveryLocationDto deliveryLocationDto);

    List<DeliveryLocationDto> findByPortalUserId(Long id);

    List<DeliveryLocationDto> findByLocationName(String name);

    HashMap<String, String> deleteLocationById(Long id);

    DeliveryLocationDto updateDeliveryLocation(Long id, DeliveryLocationDto deliveryLocationDto);
}
