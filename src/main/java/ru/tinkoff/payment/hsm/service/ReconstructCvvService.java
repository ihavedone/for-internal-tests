package ru.tinkoff.payment.hsm.service;

import ru.tinkoff.payment.hsm.domain.Card;

public interface ReconstructCvvService {
    Integer generateCVV(Card card);
}
