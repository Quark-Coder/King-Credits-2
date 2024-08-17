package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="selling_rate")
public class SellingRate {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "zero_to_thousands")
    private double zeroToThousands;

    @Column(name = "thousand_to_five_thousand")
    private double thousandToFiveThousand;

    @Column(name = "five_thousand_to_ten_thousand")
    private double fiveThousandToTenThousand;

    @Column(name = "ten_thousand_or_more")
    private double tenThousandOrMore;
}
