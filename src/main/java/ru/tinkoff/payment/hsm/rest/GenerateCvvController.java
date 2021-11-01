package ru.tinkoff.payment.hsm.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.payment.hsm.domain.Card;
import ru.tinkoff.payment.hsm.service.ReconstructCvvService;

@RestController
@RequiredArgsConstructor
public class GenerateCvvController {
    private final ReconstructCvvService reconstructCvvService;

    @PostMapping("/internal/api/card/cvv")
    Integer getCvv(@RequestBody Card card){
        return reconstructCvvService.generateCVV(card);
    }
}
