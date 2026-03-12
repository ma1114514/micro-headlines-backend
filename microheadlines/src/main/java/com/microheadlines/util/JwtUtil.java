package com.microheadlines.util;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * JWT 工具类 - 不依赖 Spring
 * 支持生成、验证、解析 JWT Token
 */
public class JwtUtil {

    // 签名算法
    private static final String ALGORITHM = "HmacSHA256";

    // Token 前缀
    public static final String TOKEN_PREFIX = "Bearer ";

    // 默认配置
    private static final long DEFAULT_EXPIRATION = 24 * 60 * 60 * 1000; // 24小时
    private static final long DEFAULT_REFRESH_EXPIRATION = 7 * 24 * 60 * 60 * 1000; // 7天

    // 声明常量
    private static final String CLAIM_KEY_USER_ID = "userId";
    private static final String CLAIM_KEY_USERNAME = "username";
    private static final String CLAIM_KEY_ROLES = "roles";
    private static final String CLAIM_KEY_TYPE = "type";
    private static final String CLAIM_KEY_EXP = "exp";
    private static final String CLAIM_KEY_IAT = "iat";

    // Token 类型
    public static final String TOKEN_TYPE_ACCESS = "access";
    public static final String TOKEN_TYPE_REFRESH = "refresh";

    // 单例模式
    private static JwtUtil instance;
    private final SecretKey secretKey;
    private final long expiration;
    private final long refreshExpiration;
    private final String issuer;

    /**
     * 私有构造方法
     */
    private JwtUtil(String secret, Long expiration, Long refreshExpiration, String issuer) {
        this.secretKey = generateSecretKey(secret);
        this.expiration = expiration != null ? expiration : DEFAULT_EXPIRATION;
        this.refreshExpiration = refreshExpiration != null ? refreshExpiration : DEFAULT_REFRESH_EXPIRATION;
        this.issuer = issuer != null ? issuer : "micro-headlines";
    }

    /**
     * 获取实例（懒加载单例）
     */
    public static synchronized JwtUtil getInstance() {
        return getInstance("your-secret-key-should-be-at-least-32-characters-long",
                null, null, null);
    }

    public static synchronized JwtUtil getInstance(String secret) {
        return getInstance(secret, null, null, null);
    }

    public static synchronized JwtUtil getInstance(String secret, Long expiration,
                                                   Long refreshExpiration, String issuer) {
        if (instance == null) {
            instance = new JwtUtil(secret, expiration, refreshExpiration, issuer);
        }
        return instance;
    }

    /**
     * 生成签名密钥
     */
    private SecretKey generateSecretKey(String secret) {
        byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);

        // 如果密钥太短，使用安全的方式扩展
        if (keyBytes.length < 32) {
            keyBytes = Arrays.copyOf(keyBytes, 32);
        }

        return new SecretKeySpec(keyBytes, ALGORITHM);
    }

    /**
     * 生成访问令牌
     */
    public String generateAccessToken(Long userId, String username, String roles) {
        return generateToken(userId, username, roles, TOKEN_TYPE_ACCESS, expiration);
    }

    /**
     * 生成刷新令牌
     */
    public String generateRefreshToken(Long userId, String username, String roles) {
        return generateToken(userId, username, roles, TOKEN_TYPE_REFRESH, refreshExpiration);
    }

    /**
     * 生成 JWT Token
     */
    private String generateToken(Long userId, String username, String roles,
                                 String tokenType, long expirationMillis) {
        // 头部
        Map<String, Object> header = new LinkedHashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        // 载荷
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USER_ID, userId);
        claims.put(CLAIM_KEY_USERNAME, username);
        claims.put(CLAIM_KEY_ROLES, roles);
        claims.put(CLAIM_KEY_TYPE, tokenType);
        claims.put("iss", issuer);
        claims.put(CLAIM_KEY_IAT, System.currentTimeMillis());
        claims.put(CLAIM_KEY_EXP, System.currentTimeMillis() + expirationMillis);

        // 编码头部和载荷
        String headerJson = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(toJson(header).getBytes(StandardCharsets.UTF_8));
        String payloadJson = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(toJson(claims).getBytes(StandardCharsets.UTF_8));

        // 生成签名
        String signingInput = headerJson + "." + payloadJson;
        String signature = Base64.getUrlEncoder().withoutPadding()
                .encodeToString(createSignature(signingInput));

        return headerJson + "." + payloadJson + "." + signature;
    }

    /**
     * 验证 Token 是否有效
     */
    public boolean validateToken(String token) {
        try {
            String[] parts = parseToken(token);
            if (parts.length != 3) {
                return false;
            }

            // 验证签名
            String signingInput = parts[0] + "." + parts[1];
            byte[] signature = Base64.getUrlDecoder().decode(parts[2]);
            byte[] expectedSignature = createSignature(signingInput);

            if (!Arrays.equals(signature, expectedSignature)) {
                return false;
            }

            // 验证过期时间
            Map<String, Object> claims = getClaimsFromToken(token);
            Long exp = (Long) claims.get(CLAIM_KEY_EXP);
            return exp != null && exp > System.currentTimeMillis();

        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 从 Token 中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            Object userId = claims.get(CLAIM_KEY_USER_ID);
            if (userId instanceof Integer) {
                return ((Integer) userId).longValue();
            }
            return (Long) userId;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            return (String) claims.get(CLAIM_KEY_USERNAME);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取用户角色
     */
    public String getRolesFromToken(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            return (String) claims.get(CLAIM_KEY_ROLES);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取 Token 类型
     */
    public String getTokenTypeFromToken(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            return (String) claims.get(CLAIM_KEY_TYPE);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 从 Token 中获取过期时间
     */
    public Date getExpirationDateFromToken(String token) {
        try {
            Map<String, Object> claims = getClaimsFromToken(token);
            Long exp = (Long) claims.get(CLAIM_KEY_EXP);
            return exp != null ? new Date(exp) : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 检查 Token 是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration == null || expiration.before(new Date());
    }

    /**
     * 刷新访问令牌
     */
    public String refreshAccessToken(String refreshToken) {
        if (!validateToken(refreshToken)) {
            return null;
        }

        String tokenType = getTokenTypeFromToken(refreshToken);
        if (!TOKEN_TYPE_REFRESH.equals(tokenType)) {
            return null;
        }

        Long userId = getUserIdFromToken(refreshToken);
        String username = getUsernameFromToken(refreshToken);
        String roles = getRolesFromToken(refreshToken);

        if (userId == null || username == null || roles == null) {
            return null;
        }

        return generateAccessToken(userId, username, roles);
    }

    /**
     * 解析 Token
     */
    private String[] parseToken(String token) {
        // 移除前缀
        if (token.startsWith(TOKEN_PREFIX)) {
            token = token.substring(TOKEN_PREFIX.length());
        }

        // 分割 Token
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid JWT token format");
        }

        return parts;
    }

    /**
     * 从 Token 中获取声明
     */
    public Map<String, Object> getClaimsFromToken(String token) {
        try {
            String[] parts = parseToken(token);
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]),
                    StandardCharsets.UTF_8);

            return parseJson(payloadJson);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid JWT token", e);
        }
    }

    /**
     * 创建签名
     */
    private byte[] createSignature(String data) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance(ALGORITHM);
            mac.init(secretKey);
            return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create signature", e);
        }
    }

    /**
     * 对象转 JSON
     */
    private String toJson(Object obj) {
        // 简单的 JSON 序列化
        if (obj instanceof Map) {
            Map<?, ?> map = (Map<?, ?>) obj;
            StringBuilder sb = new StringBuilder("{");
            boolean first = true;

            for (Map.Entry<?, ?> entry : map.entrySet()) {
                if (!first) {
                    sb.append(",");
                }
                sb.append("\"").append(entry.getKey()).append("\":");

                Object value = entry.getValue();
                if (value instanceof String) {
                    sb.append("\"").append(escapeJson((String) value)).append("\"");
                } else if (value instanceof Number || value instanceof Boolean) {
                    sb.append(value);
                } else if (value == null) {
                    sb.append("null");
                } else {
                    sb.append("\"").append(value.toString()).append("\"");
                }
                first = false;
            }
            sb.append("}");
            return sb.toString();
        }
        return "{}";
    }

    /**
     * 解析 JSON
     */
    private Map<String, Object> parseJson(String json) {
        Map<String, Object> map = new HashMap<>();

        // 移除大括号
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1).trim();
        }

        if (json.isEmpty()) {
            return map;
        }

        // 简单的 JSON 解析
        String[] pairs = json.split(",");
        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();

                // 移除引号
                if (key.startsWith("\"") && key.endsWith("\"")) {
                    key = key.substring(1, key.length() - 1);
                }
                if (value.startsWith("\"") && value.endsWith("\"")) {
                    value = value.substring(1, value.length() - 1);
                }

                map.put(key, parseValue(value));
            }
        }

        return map;
    }

    /**
     * 解析值
     */
    private Object parseValue(String value) {
        if ("null".equals(value)) {
            return null;
        }
        if ("true".equals(value)) {
            return true;
        }
        if ("false".equals(value)) {
            return false;
        }

        try {
            // 尝试解析为 Long
            return Long.parseLong(value);
        } catch (NumberFormatException e1) {
            try {
                // 尝试解析为 Double
                return Double.parseDouble(value);
            } catch (NumberFormatException e2) {
                // 作为字符串返回
                return value;
            }
        }
    }

    /**
     * 转义 JSON 字符串
     */
    private String escapeJson(String str) {
        if (str == null) {
            return "";
        }
        return str.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * 获取剩余有效时间（毫秒）
     */
    public long getRemainingMillis(String token) {
        Date expiration = getExpirationDateFromToken(token);
        if (expiration == null) {
            return 0;
        }
        return expiration.getTime() - System.currentTimeMillis();
    }

    /**
     * 验证 Token 是否为访问令牌
     */
    public boolean isAccessToken(String token) {
        try {
            String tokenType = getTokenTypeFromToken(token);
            return TOKEN_TYPE_ACCESS.equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 验证 Token 是否为刷新令牌
     */
    public boolean isRefreshToken(String token) {
        try {
            String tokenType = getTokenTypeFromToken(token);
            return TOKEN_TYPE_REFRESH.equals(tokenType);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 生成安全的随机密钥
     */
    public static String generateSecureSecret() {
        byte[] randomBytes = new byte[32];
        new Random().nextBytes(randomBytes);
        return Base64.getEncoder().encodeToString(randomBytes);
    }
}