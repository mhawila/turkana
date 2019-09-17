package org.muzima.turkana.service;

import org.junit.Before;
import org.junit.BeforeClass;
import org.muzima.turkana.AbstractIntegrationTest;

import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.util.Base64;

import static org.muzima.turkana.utils.Constants.SECURITY.MESSAGE_DIGEST_ALGORITHM;
import static org.muzima.turkana.utils.Constants.SECURITY.SIGNATURE_ALGORITHM;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 8/19/19.
 */
public abstract class BaseSecurityVerificationTest extends AbstractIntegrationTest {
    protected static KeyPairGenerator keyGen;
    protected static KeyPair pair;
    protected static Signature signatureAlgorithm;
    protected static MessageDigest messageDigest;

    @BeforeClass
    public static void setup() throws NoSuchAlgorithmException, InvalidKeyException {
        messageDigest = MessageDigest.getInstance(MESSAGE_DIGEST_ALGORITHM);
        keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        pair = keyGen.generateKeyPair();
        signatureAlgorithm = Signature.getInstance(SIGNATURE_ALGORITHM);
        signatureAlgorithm.initSign(pair.getPrivate());
    }

    protected String getBase64PublicKeyFromKeyPair() {
        return new StringBuilder("-----BEGIN RSA PUBLIC KEY-----\n")
                .append(Base64.getEncoder().encodeToString(pair.getPublic().getEncoded()))
                .append("\n-----END RSA PUBLIC KEY-----\n").toString();
    }
}
