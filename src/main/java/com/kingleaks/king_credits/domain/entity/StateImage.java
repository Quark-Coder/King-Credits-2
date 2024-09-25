package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="state_image")
public class StateImage {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "name_state")
    private String nameState;

    @Column(name = "photo_data", columnDefinition="bytea")
    private byte[] photoData;
}
