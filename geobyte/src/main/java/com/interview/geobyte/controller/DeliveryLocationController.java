package com.interview.geobyte.controller;

import com.interview.geobyte.dto.DeliveryLocationDto;
import com.interview.geobyte.service.DeliveryLocationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/delivery-locations")
@AllArgsConstructor
public class DeliveryLocationController {

    private final DeliveryLocationService deliveryLocationService;

    @PostMapping("/create")
    public ResponseEntity<DeliveryLocationDto> createLocation(@RequestBody DeliveryLocationDto deliveryLocationDto){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(deliveryLocationService.createDeliveryLocation(deliveryLocationDto));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryLocationDto>> getLocationsByName(@RequestParam("name") String name){
        return ResponseEntity.status(HttpStatus.OK)
                .body(deliveryLocationService.findByLocationName(name));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteLocation(Long id){
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(deliveryLocationService.deleteLocationById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DeliveryLocationDto> updateLocation(@PathVariable(value = "id") Long id, @RequestBody DeliveryLocationDto deliveryLocationDto){
        return ResponseEntity.status(HttpStatus.OK)
                .body(deliveryLocationService.updateDeliveryLocation(id, deliveryLocationDto));
    }

}
