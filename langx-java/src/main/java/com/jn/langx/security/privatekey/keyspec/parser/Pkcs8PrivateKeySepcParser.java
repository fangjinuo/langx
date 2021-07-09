package com.jn.langx.security.privatekey.keyspec.parser;

import java.security.spec.PKCS8EncodedKeySpec;

public class Pkcs8PrivateKeySepcParser implements PrivateKeySpecParser<PKCS8EncodedKeySpec>{
    @Override
    public PKCS8EncodedKeySpec get(byte[] derEncodedBytes) {
        return new PKCS8EncodedKeySpec(derEncodedBytes);
    }
}
