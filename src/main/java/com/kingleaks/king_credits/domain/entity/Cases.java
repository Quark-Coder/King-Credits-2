package com.kingleaks.king_credits.domain.entity;

import com.kingleaks.king_credits.domain.enums.CaseName;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "cases")
public class Cases {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private CaseName name;

    @Column(name = "price")
    private Double price;
}
