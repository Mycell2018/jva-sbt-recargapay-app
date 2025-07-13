package com.recargapay.app.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.recargapay.app.config.LocalDateAdapter;
import com.recargapay.app.config.LocalDateTimeAdapter;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Base64;
import java.util.Random;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StringUtils {

    private StringUtils() {}

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new LocalDateAdapter())
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public static String objectToString(Object obj) {
        try {
            MAPPER.registerModule(new JavaTimeModule())
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            return MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateRandomString(
            int length, boolean useLetters, boolean useNumbers, boolean useSpecialChars) {
        String allowedChars = "";

        if (useLetters) {
            allowedChars += "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        }

        if (useNumbers) {
            allowedChars += "0123456789";
        }

        if (useSpecialChars) {
            allowedChars += "!@#$%^&*()_+";
        }

        Random random = new Random();

        StringBuilder randomString = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            randomString.append(allowedChars.charAt(index));
        }

        return randomString.toString();
    }

    public static String encrypt(String secretKey, String data) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedData = cipher.doFinal(data.getBytes());

            return Base64.getEncoder().encodeToString(encryptedData);
        } catch (Exception e) {
            log.error("Erro ao criptografar os dados", e);
            return null;
        }
    }

    public static String decrypt(String secretKey, String encryptedData) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);

            byte[] decodedData = Base64.getDecoder().decode(encryptedData);
            byte[] decryptedData = cipher.doFinal(decodedData);

            return new String(decryptedData);
        } catch (Exception e) {
            log.error("Erro ao descriptografar os dados", e);
            return null;
        }
    }

    public static String toJsonString(Object obj) {
        return GSON.toJson(obj);
    }
}
