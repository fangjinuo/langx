package com.jn.langx.security.gm;

import com.jn.langx.security.gm.crypto.SM2CipherSpi;
import com.jn.langx.security.gm.crypto.SM2KeyFactorySpi;
import com.jn.langx.util.collection.Collects;
import com.jn.langx.util.function.Consumer2;
import com.jn.langx.util.reflect.Reflects;
import org.bouncycastle.jcajce.provider.asymmetric.ec.AlgorithmParametersSpi;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyPairGeneratorSpi;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.security.Provider;
import java.security.Security;
import java.util.HashMap;
import java.util.Map;

public class GmJceProvider extends Provider {
    private static final long serialVersionUID = 1443132961964116159L;
    private static final String INFO = "GM JCE provider";
    public static final String NAME = "langx-security-gm-provider";
    public static BouncyCastleProvider bouncyCastleProvider = new BouncyCastleProvider();

    static {
        Security.addProvider(bouncyCastleProvider);
    }

    public GmJceProvider() {
        super(NAME, 1.0, INFO);
        Map<String, String> map = new HashMap<String,String>();

        //    this.put("Signature.SM3WithSM2", Reflects.getFQNClassName(SM3WithSM2Signature.class));
        //    this.put("Signature.1.2.156.10197.1.501", Reflects.getFQNClassName(SM3WithSM2Signature.class));
        //    this.put("Signature.NoneWithSM2", Reflects.getFQNClassName(NoneWithSM2Signature.class));
        map.put("Cipher.SM2", Reflects.getFQNClassName(SM2CipherSpi.class));
        map.put("AlgorithmParameters.SM2", Reflects.getFQNClassName(AlgorithmParametersSpi.class));
        map.put("KeyFactory.ECDSA", Reflects.getFQNClassName(KeyFactorySpi.ECDSA.class));
        map.put("KeyFactory.EC", Reflects.getFQNClassName(KeyFactorySpi.EC.class));
        map.put("KeyFactory.SM2", Reflects.getFQNClassName(SM2KeyFactorySpi.class));
        map.put("KeyFactory.1.2.840.10045.2.1", Reflects.getFQNClassName(SM2KeyFactorySpi.class));
        map.put("KeyPairGenerator.SM2", Reflects.getFQNClassName(KeyPairGeneratorSpi.EC.class));

        Collects.forEach(map, new Consumer2<String, String>() {
            @Override
            public void accept(String key, String value) {
                if(!bouncyCastleProvider.containsKey(key)){
                    bouncyCastleProvider.addAlgorithm(key, value);
                }
            }
        });
/*
        this.put("MessageDigest.SM3", Reflects.getFQNClassName(SM3MessageDigest.class));
        this.put("Mac.HmacSM3", Reflects.getFQNClassName(HMacWithSM3.class));

        this.put("Cipher.SM4", Reflects.getFQNClassName(SM4EcbBlockCipher.class));
        this.put("Cipher.SM4/CBC", Reflects.getFQNClassName(SM4CbcBlockCipher.class));
        this.put("AlgorithmParameters.SM4", Reflects.getFQNClassName(SM4AlgorithmParameters.class));
        this.put("AlgorithmParameterGenerator.SM4", Reflects.getFQNClassName(SM4AlgorithmParameterGeneratorSpi.class));
        this.put("KeyGenerator.SM4", Reflects.getFQNClassName(SM4KeyGeneratorSpi.class));
    */
    }
}