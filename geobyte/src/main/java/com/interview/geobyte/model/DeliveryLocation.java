package com.interview.geobyte.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryLocation extends BaseIdEntity implements Serializable {

    private static final long serialVersionUID = 167L;

    private String locationName;

    private Instant createdDate;

    @OneToOne(fetch = FetchType.LAZY)
    private PortalUser createdBy;

    private BigDecimal clearingCost;

    private Double longitude;

    private Double latitude;
}
