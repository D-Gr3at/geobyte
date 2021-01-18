package com.interview.geobyte.model;

import lombok.Data;

import javax.persistence.*;

@Data
@MappedSuperclass
public class BaseIdEntity {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, updatable = false)
    @Id
    private Long id;
}
