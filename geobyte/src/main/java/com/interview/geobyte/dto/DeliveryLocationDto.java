package com.interview.geobyte.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryLocationDto {

    private Long id;

    @NotBlank(message = "Location name is required")
    private String locationName;

    @NotBlank(message = "User id is required")
    private Long userId;

    @NotBlank(message = "Longitude is required")
    private Double longitude;

    @NotBlank(message = "Latitude is required")
    private Double latitude;

    @NotBlank(message = "Clearing cost is required")
    private BigDecimal clearingCost;

    private String createdDate;
}
