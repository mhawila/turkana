package org.muzima.turkana.utils;

import org.junit.Before;
import org.junit.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import static junit.framework.TestCase.assertEquals;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 8/16/19.
 */
public class CryptoUtilsTest {
    private static String ALG = "RSA";

    private KeyPairGenerator keyGen;

    @Before
    public void setup() throws NoSuchAlgorithmException {
        keyGen = KeyPairGenerator.getInstance(ALG);
        keyGen.initialize(1024);
    }

    @Test
    public void getPublicKeyFromTextShouldReturnTheCorrectKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyPair pair = keyGen.generateKeyPair();

        PublicKey publicKey = pair.getPublic();
        String textKeyFormat1 = new StringBuilder("-----BEGIN RSA PUBLIC KEY-----\n")
                .append(Base64.getEncoder().encodeToString(publicKey.getEncoded()))
                .append("\n-----END RSA PUBLIC KEY-----\n").toString();

        String textKeyFormat2 = new StringBuilder("-----BEGIN PUBLIC KEY-----\n")
                .append(Base64.getEncoder().encodeToString(publicKey.getEncoded()))
                .append("\n-----END PUBLIC KEY-----\n").toString();

        // Read it back.
        PublicKey retrievedKey = CryptoUtils.getPublicKeyFromText(textKeyFormat1, ALG);
        assertEquals(publicKey, retrievedKey);

        retrievedKey = CryptoUtils.getPublicKeyFromText(textKeyFormat2, ALG);
        assertEquals(publicKey, retrievedKey);
    }

}
