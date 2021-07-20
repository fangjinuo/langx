package com.jn.langx.security.crypto.digest.provider.sha3;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.SecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class SHA3MessageProviderConfigurer implements SecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.SHA-3", Reflects.getFQNClassName(SHA3MessageDigest.class));
        provider.addAlgorithm("MessageDigest.SHA3", Reflects.getFQNClassName(SHA3MessageDigest.class));
        provider.addAlgorithm("MessageDigest.SHA3-224", Reflects.getFQNClassName(SHA3_224MessageDigest.class));
        provider.addAlgorithm("MessageDigest.SHA3-256", Reflects.getFQNClassName(SHA3_256MessageDigest.class));
        provider.addAlgorithm("MessageDigest.SHA3-384", Reflects.getFQNClassName(SHA3_384MessageDigest.class));
        provider.addAlgorithm("MessageDigest.SHA3-512", Reflects.getFQNClassName(SHA3_512MessageDigest.class));
        provider.addAlgorithm("MessageDigest", "2.16.840.1.101.3.4.2.7", Reflects.getFQNClassName(SHA3_224MessageDigest.class));
        provider.addAlgorithm("MessageDigest", "2.16.840.1.101.3.4.2.8", Reflects.getFQNClassName(SHA3_256MessageDigest.class));
        provider.addAlgorithm("MessageDigest", "2.16.840.1.101.3.4.2.9", Reflects.getFQNClassName(SHA3_384MessageDigest.class));
        provider.addAlgorithm("MessageDigest", "2.16.840.1.101.3.4.2.10", Reflects.getFQNClassName(SHA3_512MessageDigest.class));
    }
}