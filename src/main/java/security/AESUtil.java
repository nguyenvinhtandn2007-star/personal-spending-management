package security;

import config.AppConfig;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class AESUtil {
    private static final String TRANSFORMATION = "AES";

    private AESUtil() {
    }

    public static String encrypt(String plainText) {
        if (plainText == null || plainText.isBlank()) {
            return plainText;
        }
        try {
            // Ghi chú giao dịch được xem là thông tin riêng tư nên mã hoá trước khi lưu DB.
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, key());
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
            return "AES:" + Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new IllegalStateException("Không mã hoá được dữ liệu", e);
        }
    }

    public static String decrypt(String cipherText) {
        if (cipherText == null || !cipherText.startsWith("AES:")) {
            return cipherText;
        }
        try {
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, key());
            byte[] decoded = Base64.getDecoder().decode(cipherText.substring(4));
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            return cipherText;
        }
    }

    private static SecretKeySpec key() {
        String key = AppConfig.get("security.aesKey", "1234567890123456");
        return new SecretKeySpec(key.substring(0, 16).getBytes(StandardCharsets.UTF_8), TRANSFORMATION);
    }
}
