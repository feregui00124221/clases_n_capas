package com.renegz.pnccontroller.utils;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Converter
public class Encrypter implements AttributeConverter<String, String> {

    private static final Dotenv dotenv = Dotenv.load();
    private static final String SECRET_KEY = dotenv.get("SECRET_KEY");
    private static final String ALGORITHM = "AES/ECB/PKCS5Padding";

    @Override
    public String convertToDatabaseColumn(String attribute) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            assert SECRET_KEY != null;
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), "AES"));
            return Base64.getEncoder().encodeToString(cipher.doFinal(attribute.getBytes()));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to encrypt data", e);
        }
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            assert SECRET_KEY != null;
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(SECRET_KEY.getBytes(), "AES"));
            return new String(cipher.doFinal(Base64.getDecoder().decode(dbData)));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to decrypt data", e);
        }
    }
}
