package com.kingleaks.king_credits.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="payment_details")
public class PaymentDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name_bank_and_user")
    private String nameBankAndUser;

    @Column(name = "card_number")
    private String cardNumber;

    @Column(name = "other_payment_details")
    private String otherPaymentDetails;
}
