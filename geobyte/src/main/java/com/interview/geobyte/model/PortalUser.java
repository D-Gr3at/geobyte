package com.interview.geobyte.model;


import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PortalUser extends BaseIdEntity implements Serializable {

    private static final long serialVersionUID = 7592876L;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false)
    private String email;

    private Instant createdDate;

    private boolean enabled;
}
