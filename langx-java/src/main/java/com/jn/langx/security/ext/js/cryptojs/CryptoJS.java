package com.jn.langx.security.ext.js.cryptojs;

import com.jn.langx.codec.base64.Base64;
import com.jn.langx.security.Securitys;
import com.jn.langx.security.crypto.JCAEStandardName;
import com.jn.langx.security.crypto.cipher.CipherAlgorithmPadding;
import com.jn.langx.security.crypto.cipher.Ciphers;
import com.jn.langx.security.crypto.cipher.Symmetrics;
import com.jn.langx.security.crypto.pbe.PBEs;
import com.jn.langx.security.crypto.pbe.pbkdf.PBKDFKeySpec;
import com.jn.langx.util.Emptys;
import com.jn.langx.util.Objs;
import com.jn.langx.util.Preconditions;
import com.jn.langx.util.Strings;
import com.jn.langx.util.io.Charsets;
import com.jn.langx.util.struct.Holder;

public class CryptoJS {
    private CryptoJS(){}
    public static class SymmetricConfig {

        public int keyBitSize;
        public int ivBitSize;
        public int saltBitSize;
        public int iterations;
        public byte[] iv;
        public String cipherAlgorithm;
        public Symmetrics.MODE mode;
        public CipherAlgorithmPadding padding;

        public SymmetricConfig(int saltBitSize,
                               int keyBitSize,
                               int ivBitSize,
                               int iterations,
                               String cipherAlgorithm,
                               Symmetrics.MODE mode,
                               CipherAlgorithmPadding padding) {
            this(saltBitSize,keyBitSize,ivBitSize,iterations,cipherAlgorithm,mode,padding,null);
        }

        public SymmetricConfig(int saltBitSize,
                               int keyBitSize,
                               int ivBitSize,
                               int iterations,
                               String cipherAlgorithm,
                               Symmetrics.MODE mode,
                               CipherAlgorithmPadding padding, byte[] iv) {
            this.saltBitSize = saltBitSize;
            this.keyBitSize = keyBitSize;
            this.ivBitSize = ivBitSize;
            this.iterations = iterations;

            this.cipherAlgorithm = cipherAlgorithm;
            this.mode = mode;
            this.padding = padding;
            this.iv=iv;
        }
    }

    public static class PBEConfig extends SymmetricConfig {
        public String hashAlgorithm;
        public String pbeAlgorithm;

        public SaltedCipherTextHandler cipherTextHandler;

        public PBEConfig(
                int saltBitSize,
                int keyBitSize,
                int ivBitSize,
                int iterations,
                String cipherAlgorithm,
                Symmetrics.MODE mode,
                CipherAlgorithmPadding padding,
                String hashAlgorithm,
                String pbeAlgorithm,
                byte[] iv) {
            super(saltBitSize, keyBitSize, ivBitSize, iterations, cipherAlgorithm, mode, padding,iv);
            this.hashAlgorithm = hashAlgorithm;
            this.pbeAlgorithm = pbeAlgorithm;
            this.cipherTextHandler = new FixedPrefixSaltedCipherTextHandler("Salted__");
        }
    }

    public static class AESConfig extends PBEConfig {
        public AESConfig() {
            this(64, 256, 128, 1, Symmetrics.MODE.CBC, CipherAlgorithmPadding.PKCS5Padding, JCAEStandardName.MD5.getName(), "PBEWithMD5AndAES-OPENSSL_EVP", null);
        }

        public AESConfig(
                int saltBitSize,
                int keyBitSize,
                int ivBitSize,
                int iterations,
                Symmetrics.MODE mode,
                CipherAlgorithmPadding padding,
                String hashAlgorithm,
                String pbeAlgorithm, byte[] iv) {
            super(saltBitSize, keyBitSize, ivBitSize, iterations, JCAEStandardName.AES.getName(), mode, padding, hashAlgorithm, pbeAlgorithm, iv);
        }
    }

    public static interface SaltedCipherTextHandler {
        String stringify(byte[] salt, byte[] ciphertext, SymmetricConfig cfg);

        void extract(String saltedCipherText, SymmetricConfig cfg, Holder<byte[]> saltHolder, Holder<byte[]> ciphertextHolder);
    }

    public static class FixedPrefixSaltedCipherTextHandler implements SaltedCipherTextHandler {
        private String saltPrefix;

        public FixedPrefixSaltedCipherTextHandler(String saltPrefix) {
            this.saltPrefix = Objs.useValueIfNull(saltPrefix, "");
        }

        @Override
        public String stringify(byte[] salt, byte[] ciphertext, SymmetricConfig cfg) {
            byte[] resultBytes;
            if (Objs.isNull(salt)) {
                resultBytes = ciphertext;
            } else {
                byte[] saltPrefixBytes = this.saltPrefix.getBytes(Charsets.UTF_8);
                resultBytes = new byte[saltPrefixBytes.length + salt.length + ciphertext.length];

                System.arraycopy(saltPrefixBytes, 0, resultBytes, 0, saltPrefixBytes.length);
                System.arraycopy(salt, 0, resultBytes, saltPrefixBytes.length, salt.length);
                System.arraycopy(ciphertext, 0, resultBytes, saltPrefixBytes.length + salt.length, ciphertext.length);
            }
            String result = Base64.encodeBase64String(resultBytes);
            return result;
        }

        @Override
        public void extract(String saltedCipherText, SymmetricConfig cfg, Holder<byte[]> saltHolder, Holder<byte[]> ciphertextHolder) {
            byte[] salt;
            byte[] encryptedBytes;

            boolean hasSalt = Objs.isNotEmpty(this.saltPrefix) && Base64.decodeBase64ToString(saltedCipherText).startsWith(this.saltPrefix);
            int saltBitSize= cfg.saltBitSize;
            byte[] saltAndEncryptedBytes = Base64.decodeBase64(saltedCipherText);
            if (!hasSalt) {
                salt = Emptys.EMPTY_BYTES;
                encryptedBytes = saltAndEncryptedBytes;
            } else {
                int saltBytesLength = Securitys.getBytesLength(saltBitSize);
                salt = new byte[saltBytesLength];
                System.arraycopy(saltAndEncryptedBytes, this.saltPrefix.getBytes(Charsets.UTF_8).length, salt, 0, saltBytesLength);

                int startOffsetOfEncryptedBytes = this.saltPrefix.getBytes(Charsets.UTF_8).length + saltBytesLength;
                int encryptedBytesLength = saltAndEncryptedBytes.length - startOffsetOfEncryptedBytes;
                encryptedBytes = new byte[encryptedBytesLength];
                System.arraycopy(saltAndEncryptedBytes, startOffsetOfEncryptedBytes, encryptedBytes, 0, encryptedBytesLength);
            }

            saltHolder.set(salt);
            ciphertextHolder.set(encryptedBytes);

        }
    }

    public static abstract class Symmetric{

        private Symmetric(){}

        protected static String encryptWithPBE(String message, String passphrase, CryptoJS.PBEConfig cfg) {
            Preconditions.checkNotNull(cfg);

            byte[] salt = Securitys.randomBytes(cfg.saltBitSize);
            PBKDFKeySpec pbeKeySpec = new PBKDFKeySpec(passphrase.toCharArray(), salt, cfg.keyBitSize, cfg.ivBitSize, cfg.iterations, cfg.hashAlgorithm);
            String transformation = Ciphers.createAlgorithmTransformation(cfg.cipherAlgorithm, cfg.mode.name(), cfg.padding);

            byte[] encryptedBytes = PBEs.encrypt(
                    Strings.getBytesUtf8(message),
                    cfg.pbeAlgorithm,
                    pbeKeySpec,
                    transformation,
                    cfg.iv,
                    null, null
            );


            return cfg.cipherTextHandler.stringify(salt, encryptedBytes, cfg);
        }

        protected static String decryptWithPBE(String encryptedText, String passphrase, CryptoJS.AESConfig cfg) {
            Preconditions.checkNotNull(cfg);

            Holder<byte[]> saltHolder = new Holder<byte[]>();
            Holder<byte[]> ciphertextHolder = new Holder<byte[]>();
            cfg.cipherTextHandler.extract(encryptedText, cfg, saltHolder, ciphertextHolder);

            byte[] salt = saltHolder.get();
            byte[] encryptedBytes = ciphertextHolder.get();

            PBKDFKeySpec pbeKeySpec = new PBKDFKeySpec(passphrase.toCharArray(), salt, cfg.keyBitSize, cfg.ivBitSize, cfg.iterations, cfg.hashAlgorithm);
            String transformation = Ciphers.createAlgorithmTransformation(cfg.cipherAlgorithm, cfg.mode.name(), cfg.padding);

            byte[] rawBytes = PBEs.decrypt(
                    encryptedBytes,
                    cfg.pbeAlgorithm,
                    pbeKeySpec,
                    transformation,
                    cfg.iv,
                    null, null
            );

            String result = Strings.newStringUtf8(rawBytes);
            return result;
        }
    }

    public static class AES extends Symmetric{
        public static String encrypt(String message, String passphrase, CryptoJS.AESConfig cfg) {
            if (cfg == null) {
                cfg = new CryptoJS.AESConfig();
            }
            return encryptWithPBE(message,passphrase,cfg);
        }

        public static String decrypt(String encryptedText, String passphrase, CryptoJS.AESConfig cfg) {
            if (cfg == null) {
                cfg = new CryptoJS.AESConfig();
            }
            return decryptWithPBE(encryptedText,passphrase, cfg);
        }

    }


}
