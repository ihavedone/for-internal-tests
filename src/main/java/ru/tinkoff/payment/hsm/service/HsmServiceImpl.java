package ru.tinkoff.payment.hsm.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
@Slf4j
public class HsmServiceImpl implements HsmService {

    public static final String ALGORITHM = "AES";
    public static final int ITERATION_COUNT = 65536;
    public static final int KEY_LENGTH = 256;
    public static final String ALGORITHM_CIPHER_PADDING = "AES/CBC/PKCS5Padding";

    @Override
    public SecretKey generateKey(int n) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(n);
            SecretKey key = keyGenerator.generateKey();
            return key;
        } catch (NoSuchAlgorithmException e) {
            log.error(e.toString());
            throw new SomethingWentWrongException(e);
        }
    }

    @Override
    public IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    @Override
    public String encrypt(String input, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER_PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] cipherText = cipher.doFinal(input.getBytes());
            return Base64.getEncoder().encodeToString(cipherText);
        } catch (Exception e) {
            log.error(e.toString());
            throw new SomethingWentWrongException(e);
        }
    }

    @Override
    public String decrypt(String cipherText, SecretKey key, IvParameterSpec iv) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM_CIPHER_PADDING);
            cipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] plainText = cipher.doFinal(Base64.getDecoder().decode(cipherText));
            return new String(plainText);
        } catch (Exception exception) {
            log.error(exception.toString());
            throw new SomethingWentWrongException(exception);
        }
    }

    public static class SomethingWentWrongException extends RuntimeException {
        public SomethingWentWrongException(Throwable cause) {
            super(cause);
        }
    }
}
