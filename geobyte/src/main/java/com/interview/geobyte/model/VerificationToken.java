package com.interview.geobyte.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationToken extends BaseIdEntity implements Serializable {

    private static final long serialVersionUID = 32574L;

    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    private PortalUser portalUser;

    private Instant expiryDate;
}
