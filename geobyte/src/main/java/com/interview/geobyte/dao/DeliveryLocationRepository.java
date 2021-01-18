package com.interview.geobyte.dao;

import com.interview.geobyte.model.DeliveryLocation;
import com.interview.geobyte.model.PortalUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryLocationRepository extends JpaRepository<DeliveryLocation, Long> {

    List<DeliveryLocation> findByClearingCost(BigDecimal clearingCost);

    List<DeliveryLocation> findByLocationNameContains(String locationName);

    Optional<DeliveryLocation> findByLocationName(String locationName);

    List<DeliveryLocation> findByCreatedBy(PortalUser portalUser);
}
