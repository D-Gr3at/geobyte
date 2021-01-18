package com.interview.geobyte.model;

import lombok.*;

import javax.persistence.Entity;
import java.io.Serializable;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RefreshToken extends BaseIdEntity implements Serializable {

    private static final long serialVersionUID = 42081L;

    private String refreshToken;

    private Instant createdDate;
}
