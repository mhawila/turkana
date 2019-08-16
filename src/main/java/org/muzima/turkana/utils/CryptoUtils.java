package org.muzima.turkana.utils;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Logger;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 8/16/19.
 */
public class CryptoUtils {
    private static final Logger LOGGER = Logger.getLogger(CryptoUtils.class.getName());
    public static PublicKey getPublicKeyFromText(final String publicKey, String algorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
        if(algorithm == null) {
            LOGGER.info("No algorithm passed, using RSA instead.");
            algorithm = "RSA";
        }

        String pureKeyContent = stripInformativeLinesFromKeyText(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64.getDecoder().decode(pureKeyContent));
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        return keyFactory.generatePublic(spec);
    }

    public static PublicKey getPublicKeyFromText(final String publicKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        return getPublicKeyFromText(publicKey, null);
    }

    private static String stripInformativeLinesFromKeyText(String keyContent) {
        String pattern = "-+(BEGIN|END)\\s(RSA)?\\s?PUBLIC\\sKEY-+";
        return keyContent.replaceAll("\\n", "").replaceAll(pattern, "");
    }
}
