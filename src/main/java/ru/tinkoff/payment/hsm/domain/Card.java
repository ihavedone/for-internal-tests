package ru.tinkoff.payment.hsm.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private Integer cvv;
    private String cardHolderName;
    private Integer expirationYear;
    private Integer expirationMonth;
    private String pan;
    private Integer internalCardId;
    private Byte[] salt;
    private String pin;
    private PaymentSystem paymentSystem;
}
