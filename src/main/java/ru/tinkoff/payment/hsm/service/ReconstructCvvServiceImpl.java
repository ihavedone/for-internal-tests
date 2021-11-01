package ru.tinkoff.payment.hsm.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.tinkoff.payment.hsm.domain.Card;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReconstructCvvServiceImpl implements ReconstructCvvService {

    public static final String ALGORITHM = "AES";
    public static final String HASH_ALGORITHM = "MD5";
    public static final int INITIAL_VALUE = 2006;
    @Value("${application.key}")
    private String masterKey;

    @Value("${application.iv}")
    private String iv;

    @Value("${application.encKeyForCvvCalc}")
    private String encryptedSecretKey;

    private final HsmService hsmService;

    @Override
    public Integer generateCVV(Card card) {

        byte[] masterKeyBytes = Base64.getDecoder().decode(this.masterKey);
        SecretKey originalKey = new SecretKeySpec(
                masterKeyBytes,
                0,
                masterKeyBytes.length,
                ALGORITHM);

        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));

        String decryptedMasterKey = hsmService.decrypt(encryptedSecretKey, originalKey, ivSpec);

        String pan = card.getPan();
        AtomicInteger counter = new AtomicInteger(INITIAL_VALUE);

        try {
            MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
            md.update( (decryptedMasterKey+pan).getBytes());
            byte[] digest = md.digest();

            return pan.chars()
                    .map((s) -> {
                        return s +
                                pan.charAt( counter.incrementAndGet() % (pan.length() - 1)) |
                                digest[counter.get() % (digest.length-1)];
                    })
                    .sum() % 1000;

        } catch (Exception e) {
            log.error("Bad value, check the request");
            throw new RuntimeException("Bad value");
        }
    }
}
