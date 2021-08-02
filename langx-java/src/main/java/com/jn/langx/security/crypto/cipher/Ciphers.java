package com.jn.langx.security.crypto.cipher;

import com.jn.langx.annotation.NonNull;
import com.jn.langx.annotation.NotEmpty;
import com.jn.langx.annotation.Nullable;
import com.jn.langx.exception.IllegalParameterException;
import com.jn.langx.security.SecurityException;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.CryptoException;
import com.jn.langx.security.crypto.key.supplier.bytesbased.BytesBasedKeySupplier;
import com.jn.langx.text.StringTemplates;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;

import javax.crypto.Cipher;
import java.security.*;
import java.security.cert.Certificate;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#Cipher
 */
public class Ciphers extends Securitys {
    protected Ciphers() {
    }

    protected static final Map<String, String> algorithmToTransformationMapping = new ConcurrentHashMap<String, String>();

    static {
        algorithmToTransformationMapping.put("AES", "AES/ECB/PKCS5Padding");
        algorithmToTransformationMapping.put("SM2", "SM2");
        algorithmToTransformationMapping.put("SM4", "SM4/CBC/PKCS7Padding");
        algorithmToTransformationMapping.put("RSA", "RSA/ECB/PKCS1Padding");
    }

    public static String getDefaultTransformation(String algorithm) {
        return algorithmToTransformationMapping.get(algorithm);
    }

    public static void addDefaultTransformation(String algorithm, String transformation) {
        algorithmToTransformationMapping.put(algorithm, transformation);
    }

    public static Cipher createEmptyCipher(@NonNull String algorithmTransformation, @Nullable Provider provider) {
        try {
            Cipher cipher = provider == null ? Cipher.getInstance(algorithmTransformation) : Cipher.getInstance(algorithmTransformation, provider);
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    /**
     * 如果在初始化 Cipher过程中，出现了java.security.InvalidKeyException: Illegal key size
     * 可以找到 ${JDK_HOME}/jre/lib/security/java.security, 将 crypto.policy 设置为 unlimited
     *
     * @return
     */
    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                cipher.init(operateMode, key);
            } else {
                cipher.init(operateMode, key, secureRandom);
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    /**
     * 如果在初始化 Cipher过程中，出现了java.security.InvalidKeyException: Illegal key size
     * 可以找到 ${JDK_HOME}/jre/lib/security/java.security, 将 crypto.policy 设置为 unlimited
     *
     * @return
     */
    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, @Nullable AlgorithmParameterSpec parameterSpec, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                cipher.init(operateMode, key, parameterSpec);
            } else {
                cipher.init(operateMode, key, parameterSpec, secureRandom);
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    /**
     * 如果在初始化 Cipher过程中，出现了java.security.InvalidKeyException: Illegal key size
     * 可以找到 ${JDK_HOME}/jre/lib/security/java.security, 将 crypto.policy 设置为 unlimited
     *
     * @return
     */
    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Key key, @Nullable AlgorithmParameters parameters, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                if (parameters != null) {
                    cipher.init(operateMode, key, parameters);
                } else {
                    cipher.init(operateMode, key);
                }
            } else {
                if (parameters != null) {
                    cipher.init(operateMode, key, parameters, secureRandom);
                } else {
                    cipher.init(operateMode, key, secureRandom);
                }
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static Cipher createCipher(@NonNull String algorithmTransformation, @Nullable Provider provider, int operateMode, Certificate certificate, SecureRandom secureRandom) {
        try {
            Cipher cipher = createEmptyCipher(algorithmTransformation, provider);
            if (secureRandom == null) {
                cipher.init(operateMode, certificate);
            } else {
                cipher.init(operateMode, certificate, secureRandom);
            }
            return cipher;
        } catch (Throwable ex) {
            throw new SecurityException(ex.getMessage(), ex);
        }
    }

    public static byte[] encrypt(Cipher cipher, byte[] data) {
        try {
            return cipher.doFinal(data);
        } catch (Throwable ex) {
            throw new CryptoException(ex.getMessage(), ex);
        }
    }

    public static byte[] decrypt(Cipher cipher, byte[] data) {
        try {
            return cipher.doFinal(data);
        } catch (Throwable ex) {
            throw new CryptoException(ex.getMessage(), ex);
        }
    }

    public static byte[] encrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, true);
    }

    public static byte[] encrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable AlgorithmParameterSpec parameterSpec) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, parameterSpec, true);
    }

    public static byte[] encrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable final AlgorithmParameters parameters) {
        return encrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, new AlgorithmParameterSupplier() {
            @Override
            public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
                return parameters;
            }
        });
    }

    public static byte[] encrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable AlgorithmParameterSupplier parameterSupplier) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, parameterSupplier, true);
    }


    public static byte[] decrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, false);
    }

    public static byte[] decrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable AlgorithmParameterSpec parameterSpec) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, parameterSpec, false);
    }

    public static byte[] decrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable final AlgorithmParameters parameters) {
        return decrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, new AlgorithmParameterSupplier() {
            @Override
            public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
                return parameters;
            }
        });
    }


    public static byte[] decrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable final AlgorithmParameterSupplier parameterSupplier) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, parameterSupplier, false);
    }


    public static byte[] doEncryptOrDecrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, boolean encrypt) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, (AlgorithmParameterSpec) null, encrypt);
    }

    public static byte[] doEncryptOrDecrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable final AlgorithmParameterSpec parameterSpec, boolean encrypt) {
        return doEncryptOrDecrypt(bytes, keyBytes, algorithm, algorithmTransformation, provider, secureRandom, keySupplier, new AlgorithmParameterSupplier() {
            @Override
            public Object get(Key key, String algorithm, String transform, Provider provider, SecureRandom secureRandom) {
                return parameterSpec;
            }
        }, encrypt);
    }

    public static byte[] doEncryptOrDecrypt(byte[] bytes, byte[] keyBytes, String algorithm, String algorithmTransformation, Provider provider, SecureRandom secureRandom, @NonNull BytesBasedKeySupplier keySupplier, @Nullable AlgorithmParameterSupplier parameterSupplier, boolean encrypt) {
        Preconditions.checkNotEmpty(keyBytes, "{} key is empty", algorithm);
        Preconditions.checkArgument(!Emptys.isAllEmpty(algorithm, algorithmTransformation), "the algorithm and algorithmTransformation is empty");
        Preconditions.checkNotNull(keySupplier, "the key supplier is null");
        if (Emptys.isEmpty(algorithm)) {
            algorithm = Ciphers.extractAlgorithm(algorithmTransformation);
        }
        if (Emptys.isEmpty(algorithmTransformation)) {
            algorithmTransformation = getDefaultTransformation(algorithm);
            if (Emptys.isEmpty(algorithmTransformation)) {
                algorithmTransformation = Ciphers.createAlgorithmTransformation(algorithm, "ECB", "PKCS5Padding");
            }
        }

        Key key = keySupplier.get(keyBytes, algorithm, provider);

        AlgorithmParameterSpec parameterSpec = null;
        AlgorithmParameters parameters = null;
        Object parameter = null;
        if (parameterSupplier != null) {
            parameter = parameterSupplier.get(key, algorithm, algorithmTransformation, provider, secureRandom);
            if (parameter != null) {
                if (parameter instanceof AlgorithmParameterSpec) {
                    parameterSpec = (AlgorithmParameterSpec) parameter;
                } else if (parameter instanceof AlgorithmParameters) {
                    parameters = (AlgorithmParameters) parameter;
                } else if (parameter instanceof AlgorithmParameterGenerator) {
                    AlgorithmParameterGenerator parameterGenerator = (AlgorithmParameterGenerator) parameter;
                    parameters = parameterGenerator.generateParameters();
                }
            }
        }
        if (parameterSpec == null && parameters == null && provider != null) {
            try {
                parameters = AlgorithmParameters.getInstance(algorithm, provider);
            } catch (NoSuchAlgorithmException ex) {
                // ignore it
            }
        }
        try {

            Cipher cipher = null;
            if (parameters != null) {
                cipher = Ciphers.createCipher(algorithmTransformation, provider, encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key, parameters, secureRandom);
            } else {
                cipher = Ciphers.createCipher(algorithmTransformation, provider, encrypt ? Cipher.ENCRYPT_MODE : Cipher.DECRYPT_MODE, key, parameterSpec, secureRandom);
            }
            if (encrypt) {
                return Ciphers.encrypt(cipher, bytes);
            } else {
                return Ciphers.decrypt(cipher, bytes);
            }
        } catch (Throwable ex) {
            throw new CryptoException(ex.getMessage(), ex);
        }
    }

    /**
     * https://docs.oracle.com/javase/8/docs/technotes/guides/security/crypto/CryptoSpec.html#trans
     * <p>
     * transformation 有两种形式：
     * <pre>
     *    1) 只有algorithm name
     *    2) 由3部分构成：{algorithm name}/{mode}/padding
     * </pre>
     */
    public static String createAlgorithmTransformation(String transformation) {
        Preconditions.checkNotEmpty(transformation, "the cipher algorithm transformation is null or empty");
        String[] components = Strings.split(transformation, "/");
        if (components.length == 0) {
            throw new IllegalParameterException(StringTemplates.formatWithPlaceholder("the cipher algorithm transformation is illegal: {}", transformation));
        }
        if (components.length < 3) {
            return components[0];
        }
        return createAlgorithmTransformation(components[0], components[1], components[2]);
    }

    public static String createAlgorithmTransformation(@NotEmpty String algorithm, @NotEmpty String mode, @NotEmpty String padding) {
        Preconditions.checkNotEmpty(algorithm, "the algorithm is null or empty");
        Preconditions.checkNotEmpty(mode, "the mode is null or empty");
        Preconditions.checkNotEmpty(padding, "the padding is null or empty");

        return StringTemplates.formatWithPlaceholder("{}/{}/{}", algorithm, mode, padding);
    }

    public static String extractAlgorithm(String transformation) {
        Preconditions.checkNotEmpty(transformation, "the cipher algorithm transformation is null or empty");
        String[] segments = Strings.split(transformation, "/");
        Preconditions.checkNotEmpty(segments, "invalid transformation: {}", transformation);
        return segments[0];
    }

    /**
     * 获取用于密钥生成的算法<br>
     * 获取XXXwithXXX算法，这种算法通常是 签名算法，with 前面部分是摘要算法或者None，with 后面部分是 加密算法，通常是非对称加密算法。
     *
     * @param algorithm XXXwithXXX算法
     * @return 算法
     */
    public static String extractCipherAlgorithm(String algorithm) {
        Preconditions.checkNotNull(algorithm, "algorithm must be not null !");
        if (algorithm.contains("SM2") || algorithm.contains("EC")) {
            int indexOfWith = Strings.lastIndexOfIgnoreCase(algorithm, "with");
            if (indexOfWith > 0) {
                algorithm = Strings.substring(algorithm, indexOfWith + "with".length());
            }
        }
        if (Strings.contains(algorithm, "with", true)) {
            int indexOfWith = Strings.lastIndexOfIgnoreCase(algorithm, "with");
            algorithm = Strings.substring(algorithm, indexOfWith + "with".length());
        } else if ("ECDSA".equalsIgnoreCase(algorithm)) {
            algorithm = "EC";
        }
        if (Strings.startsWith(algorithm, "EC", true)) {
            algorithm = "EC";
        }
        return algorithm;
    }

}
