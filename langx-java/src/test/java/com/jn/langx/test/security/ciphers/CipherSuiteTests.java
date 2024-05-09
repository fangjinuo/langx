package com.jn.langx.test.security.ciphers;

import com.jn.langx.security.ssl.SSLs;
import com.jn.langx.util.Strings;
import org.junit.Test;


public class CipherSuiteTests {
    @Test
    public void testInferSuite(){
        String[] cipherSuiteNames= Strings.split("SSL_NULL_WITH_NULL_NULL,TLS_AES_256_GCM_SHA384,TLS_AES_128_GCM_SHA256,TLS_CHACHA20_POLY1305_SHA256,TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,TLS_DHE_RSA_WITH_AES_256_GCM_SHA384,TLS_DHE_RSA_WITH_CHACHA20_POLY1305_SHA256,TLS_DHE_DSS_WITH_AES_256_GCM_SHA384,TLS_DHE_RSA_WITH_AES_128_GCM_SHA256,TLS_DHE_DSS_WITH_AES_128_GCM_SHA256,TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,TLS_DHE_RSA_WITH_AES_256_CBC_SHA256,TLS_DHE_DSS_WITH_AES_256_CBC_SHA256,TLS_DHE_RSA_WITH_AES_128_CBC_SHA256,TLS_DHE_DSS_WITH_AES_128_CBC_SHA256,TLS_ECDH_ECDSA_WITH_AES_256_GCM_SHA384,TLS_ECDH_RSA_WITH_AES_256_GCM_SHA384,TLS_ECDH_ECDSA_WITH_AES_128_GCM_SHA256,TLS_ECDH_RSA_WITH_AES_128_GCM_SHA256,TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA384,TLS_ECDH_RSA_WITH_AES_256_CBC_SHA384,TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256,TLS_ECDH_RSA_WITH_AES_128_CBC_SHA256,TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,TLS_DHE_RSA_WITH_AES_256_CBC_SHA,TLS_DHE_DSS_WITH_AES_256_CBC_SHA,TLS_DHE_RSA_WITH_AES_128_CBC_SHA,TLS_DHE_DSS_WITH_AES_128_CBC_SHA,TLS_ECDH_ECDSA_WITH_AES_256_CBC_SHA,TLS_ECDH_RSA_WITH_AES_256_CBC_SHA,TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA,TLS_ECDH_RSA_WITH_AES_128_CBC_SHA,TLS_RSA_WITH_AES_256_GCM_SHA384,TLS_RSA_WITH_AES_128_GCM_SHA256,TLS_RSA_WITH_AES_256_CBC_SHA256,TLS_RSA_WITH_AES_128_CBC_SHA256,TLS_RSA_WITH_AES_256_CBC_SHA,TLS_RSA_WITH_AES_128_CBC_SHA,TLS_ECDHE_ECDSA_WITH_3DES_EDE_CBC_SHA,TLS_ECDHE_RSA_WITH_3DES_EDE_CBC_SHA,TLS_DHE_RSA_WITH_3DES_EDE_CBC_SHA,TLS_DHE_DSS_WITH_3DES_EDE_CBC_SHA,TLS_ECDH_ECDSA_WITH_3DES_EDE_CBC_SHA,TLS_ECDH_RSA_WITH_3DES_EDE_CBC_SHA,TLS_RSA_WITH_3DES_EDE_CBC_SHA,TLS_EMPTY_RENEGOTIATION_INFO_SCSV,TLS_DH_anon_WITH_AES_256_GCM_SHA384,TLS_DH_anon_WITH_AES_128_GCM_SHA256,TLS_DH_anon_WITH_AES_256_CBC_SHA256,TLS_ECDH_anon_WITH_AES_256_CBC_SHA,TLS_DH_anon_WITH_AES_256_CBC_SHA,TLS_DH_anon_WITH_AES_128_CBC_SHA256,TLS_ECDH_anon_WITH_AES_128_CBC_SHA,TLS_DH_anon_WITH_AES_128_CBC_SHA,TLS_ECDH_anon_WITH_3DES_EDE_CBC_SHA,TLS_DH_anon_WITH_3DES_EDE_CBC_SHA,TLS_ECDHE_ECDSA_WITH_RC4_128_SHA,TLS_ECDHE_RSA_WITH_RC4_128_SHA,TLS_RSA_WITH_RC4_128_SHA,TLS_ECDH_ECDSA_WITH_RC4_128_SHA,TLS_ECDH_RSA_WITH_RC4_128_SHA,TLS_RSA_WITH_RC4_128_MD5,TLS_ECDH_anon_WITH_RC4_128_SHA,TLS_DH_anon_WITH_RC4_128_MD5,TLS_RSA_WITH_DES_CBC_SHA,TLS_DHE_RSA_WITH_DES_CBC_SHA,TLS_DHE_DSS_WITH_DES_CBC_SHA,TLS_DH_anon_WITH_DES_CBC_SHA,TLS_RSA_EXPORT_WITH_DES40_CBC_SHA,TLS_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA,TLS_DHE_DSS_EXPORT_WITH_DES40_CBC_SHA,TLS_DH_anon_EXPORT_WITH_DES40_CBC_SHA,TLS_RSA_EXPORT_WITH_RC4_40_MD5,TLS_DH_anonEXPORT_WITH_RC4_40_MD5,TLS_RSA_WITH_NULL_SHA256,TLS_ECDHE_ECDSA_WITH_NULL_SHA,TLS_ECDHE_RSA_WITH_NULL_SHA,TLS_RSA_WITH_NULL_SHA,TLS_ECDH_ECDSA_WITH_NULL_SHA,TLS_ECDH_RSA_WITH_NULL_SHA,TLS_ECDH_anon_WITH_NULL_SHA,TLS_RSA_WITH_NULL_MD5",",");

        for (int i = 0; i < cipherSuiteNames.length; i++) {
            System.out.println(SSLs.inferCipherSuite(cipherSuiteNames[i]));
        }
    }
}