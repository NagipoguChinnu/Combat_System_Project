package com.combatsystem.www.service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class EncryptionUtils {

    private static final String SECRET_KEY = "MySecretKey12345"; 
    private static final String ALGORITHM = "AES";

    public String encrypt(String message) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encryptedBytes = cipher.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(encryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String encrypted) {
        try {
            SecretKeySpec key = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decodedBytes = Base64.getDecoder().decode(encrypted);
            byte[] decryptedBytes = cipher.doFinal(decodedBytes);
            return new String(decryptedBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Map<String, String> splitMessage(String encrypted) {
        int length = encrypted.length();
        int partSize = length / 3;

        Map<String, String> blocks = new HashMap<>();
        blocks.put("block1", encrypted.substring(0, partSize));
        blocks.put("block2", encrypted.substring(partSize, partSize * 2));
        blocks.put("block3", encrypted.substring(partSize * 2));

        return blocks;
    }

    public String combineBlocks(String block1, String block2, String block3) {
        return block1 + block2 + block3;
    }

    public Map<String, String> encryptAndSplit(String data) {
        String encrypted = encrypt(data);
        return splitMessage(encrypted);
    }

    public String decryptFromBlocks(String block1, String block2, String block3) {
        return decrypt(combineBlocks(block1, block2, block3));
    }
}
