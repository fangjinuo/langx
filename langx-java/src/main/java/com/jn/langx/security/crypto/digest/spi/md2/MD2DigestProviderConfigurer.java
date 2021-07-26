package com.jn.langx.security.crypto.digest.spi.md2;

import com.jn.langx.security.crypto.provider.LangxSecurityProvider;
import com.jn.langx.security.crypto.provider.LangxSecurityProviderConfigurer;
import com.jn.langx.util.reflect.Reflects;

public class MD2DigestProviderConfigurer implements LangxSecurityProviderConfigurer {
    @Override
    public void configure(LangxSecurityProvider provider) {
        provider.addAlgorithm("MessageDigest.MD2", MD2MessageDigestSpi.class);
        provider.addAlgorithm("Alg.Alias.MessageDigest.1.2.840.113549.2.2", "MD2");
    }
}
