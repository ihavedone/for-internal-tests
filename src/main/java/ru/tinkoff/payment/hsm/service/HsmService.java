package ru.tinkoff.payment.hsm.service;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

public interface HsmService {
    SecretKey generateKey(int n);
    IvParameterSpec generateIv();
    String encrypt(String input, SecretKey key, IvParameterSpec iv);
    String decrypt(String cipherText, SecretKey key, IvParameterSpec iv);
}
