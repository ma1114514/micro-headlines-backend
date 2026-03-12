package com.microheadlines.util;


import javax.crypto.SecretKey;
import java.security.SecureRandom;
import javax.crypto.*;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
public class AESUtil {
    private static final String ALGORITHM = "AES/GCM/NoPadding";
    private static final int KEY_SIZE = 256; // AES-256
    private static final int GCM_TAG_LENGTH = 128; // 128位认证标签
    private static final int GCM_IV_LENGTH = 12;   // 12字节IV

    // 从安全配置获取，实际项目中应该从环境变量或配置中心获取
    private static final String SECRET_KEY;

    static {
        // 实际项目中应该从环境变量获取
        String envKey = System.getenv("DB_ENCRYPTION_KEY");
        if (envKey == null || envKey.trim().isEmpty()) {
            // 开发环境默认密钥（生产环境必须使用环境变量）
            envKey = "YourDefaultEncryptionKeyForDevelopment123!";
        }
        SECRET_KEY = envKey;
    }

    private void ASEUtil() {
        // 工具类，防止实例化
    }

    /**
     * 加密文本数据
     * @param plaintext 明文
     * @return Base64编码的加密数据
     */
    public static String encrypt(String plaintext) {
        try {
            if (plaintext == null || plaintext.trim().isEmpty()) {
                return plaintext;
            }

            // 生成随机IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);

            // 创建密钥
            SecretKey secretKey = generateKey();

            // 初始化加密器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);

            // 加密
            byte[] encryptedData = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));

            // 组合IV和加密数据: IV + 加密数据
            byte[] combined = new byte[iv.length + encryptedData.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encryptedData, 0, combined, iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(combined);

        } catch (Exception e) {
            throw new RuntimeException("加密失败", e);
        }
    }

    /**
     * 解密文本数据
     * @param encryptedText Base64编码的加密数据
     * @return 明文
     */
    public static String decrypt(String encryptedText) {
        try {
            if (encryptedText == null || encryptedText.trim().isEmpty()) {
                return encryptedText;
            }

            // 解码Base64
            byte[] combined = Base64.getDecoder().decode(encryptedText);

            // 分离IV和加密数据
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encryptedData = new byte[combined.length - GCM_IV_LENGTH];
            System.arraycopy(combined, 0, iv, 0, iv.length);
            System.arraycopy(combined, iv.length, encryptedData, 0, encryptedData.length);

            // 创建密钥
            SecretKey secretKey = generateKey();

            // 初始化解密器
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, parameterSpec);

            // 解密
            byte[] decryptedData = cipher.doFinal(encryptedData);
            return new String(decryptedData, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("解密失败", e);
        }
    }

    /**
     * 生成AES密钥
     */
    private static SecretKey generateKey() throws NoSuchAlgorithmException {
        // 使用SHA-256生成固定长度的密钥
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = digest.digest(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(keyBytes, "AES");
    }

    /**
     * 检查文本是否已加密（启发式检查）
     */
    public static boolean isEncrypted(String text) {
        if (text == null || text.length() < 20) {
            return false;
        }
        try {
            // 尝试Base64解码
            byte[] decoded = Base64.getDecoder().decode(text);
            // 如果解码后长度足够包含IV和加密数据，则可能是加密数据
            return decoded.length >= GCM_IV_LENGTH + 1;
        } catch (IllegalArgumentException e) {
            // 如果不是Base64格式，肯定不是加密数据
            return false;
        }
    }

    /**
     * 批量加密（用于数据迁移）
     */
    public static String[] batchEncrypt(String[] plaintexts) {
        if (plaintexts == null) return null;

        String[] results = new String[plaintexts.length];
        for (int i = 0; i < plaintexts.length; i++) {
            results[i] = encrypt(plaintexts[i]);
        }
        return results;
    }

    /**
     * 批量解密（用于数据读取）
     */
    public static String[] batchDecrypt(String[] encryptedTexts) {
        if (encryptedTexts == null) return null;

        String[] results = new String[encryptedTexts.length];
        for (int i = 0; i < encryptedTexts.length; i++) {
            results[i] = decrypt(encryptedTexts[i]);
        }
        return results;
    }
}
