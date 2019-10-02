package org.muzima.turkana.utils;

import org.apache.http.util.TextUtils;
import org.muzima.turkana.crypto.AsymmetricMasterSecret;
import org.muzima.turkana.crypto.InvalidPassphraseException;
import org.muzima.turkana.data.PreferenceRepository;
import org.muzima.turkana.model.Preference;
import org.muzima.turkana.web.signalclients.MasterCipher;
import org.muzima.turkana.web.signalclients.MasterSecret;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.ecc.ECPublicKey;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

public class MasterSecretUtil {

    private static Logger logger = LoggerFactory.getLogger(MasterSecretUtil.class);
    public static final String UNENCRYPTED_PASSPHRASE = "unencrypted";
    public static final String PREFERENCES_NAME = "SecureSMS-Preferences";

    private static final String ASYMMETRIC_LOCAL_PUBLIC_DJB = "asymmetric_master_secret_curve25519_public";
    private static final String ASYMMETRIC_LOCAL_PRIVATE_DJB = "asymmetric_master_secret_curve25519_private";

    @Autowired
    private static PreferenceRepository preferenceRepository;

    public static MasterSecret changeMasterSecretPassphrase(MasterSecret masterSecret,
                                                            String newPassphrase) {
        try {
            byte[] combinedSecrets = Util.combine(masterSecret.getEncryptionKey().getEncoded(),
                masterSecret.getMacKey().getEncoded());

            byte[] encryptionSalt = generateSalt();
            int iterations = generateIterationCount(newPassphrase, encryptionSalt);
            byte[] encryptedMasterSecret = encryptWithPassphrase(encryptionSalt, iterations, combinedSecrets, newPassphrase);
            byte[] macSalt = generateSalt();
            byte[] encryptedAndMacdMasterSecret = macWithPassphrase(macSalt, iterations, encryptedMasterSecret, newPassphrase);

            save("encryption_salt", encryptionSalt);
            save("mac_salt", macSalt);
            save("passphrase_iterations", iterations);
            save("master_secret", encryptedAndMacdMasterSecret);
            save("passphrase_initialized", true);

            return masterSecret;
        } catch (GeneralSecurityException gse) {
            throw new AssertionError(gse);
        }
    }

    public static MasterSecret changeMasterSecretPassphrase(String originalPassphrase,
                                                            String newPassphrase)
        throws InvalidPassphraseException {
        MasterSecret masterSecret = getMasterSecret(originalPassphrase);
        changeMasterSecretPassphrase(masterSecret, newPassphrase);

        return masterSecret;
    }

    public static MasterSecret getMasterSecret(String passphrase)
        throws InvalidPassphraseException {
        try {
            byte[] encryptedAndMacdMasterSecret = retrieve("master_secret");
            byte[] macSalt = retrieve("mac_salt");
            int iterations = retrieve("passphrase_iterations", 100);
            byte[] encryptedMasterSecret = verifyMac(macSalt, iterations, encryptedAndMacdMasterSecret, passphrase);
            byte[] encryptionSalt = retrieve("encryption_salt");
            byte[] combinedSecrets = decryptWithPassphrase(encryptionSalt, iterations, encryptedMasterSecret, passphrase);
            byte[] encryptionSecret = Util.split(combinedSecrets, 16, 20)[0];
            byte[] macSecret = Util.split(combinedSecrets, 16, 20)[1];

            return new MasterSecret(new SecretKeySpec(encryptionSecret, "AES"),
                new SecretKeySpec(macSecret, "HmacSHA1"));
        } catch (GeneralSecurityException e) {
            logger.info("keyutil", e);
            return null; //XXX
        } catch (IOException e) {
            logger.info("keyutil", e);
            return null; //XXX
        }
    }

    public static AsymmetricMasterSecret getAsymmetricMasterSecret(MasterSecret masterSecret) {
        try {
            byte[] djbPublicBytes = retrieve(ASYMMETRIC_LOCAL_PUBLIC_DJB);
            byte[] djbPrivateBytes = retrieve(ASYMMETRIC_LOCAL_PRIVATE_DJB);

            ECPublicKey djbPublicKey = null;
            ECPrivateKey djbPrivateKey = null;

            if (djbPublicBytes != null) {
                djbPublicKey = Curve.decodePoint(djbPublicBytes, 0);
            }

            if (masterSecret != null) {
                MasterCipher masterCipher = new MasterCipher(masterSecret);

                if (djbPrivateBytes != null) {
                    djbPrivateKey = masterCipher.decryptKey(djbPrivateBytes);
                }
            }

            return new AsymmetricMasterSecret(djbPublicKey, djbPrivateKey);
        } catch (InvalidKeyException | IOException ike) {
            throw new AssertionError(ike);
        }
    }

    public static AsymmetricMasterSecret generateAsymmetricMasterSecret(MasterSecret masterSecret) {
        MasterCipher masterCipher = new MasterCipher(masterSecret);
        ECKeyPair keyPair = Curve.generateKeyPair();

        save(ASYMMETRIC_LOCAL_PUBLIC_DJB, keyPair.getPublicKey().serialize());
        save(ASYMMETRIC_LOCAL_PRIVATE_DJB, masterCipher.encryptKey(keyPair.getPrivateKey()));

        return new AsymmetricMasterSecret(keyPair.getPublicKey(), keyPair.getPrivateKey());
    }

    public static MasterSecret generateMasterSecret(String passphrase) {
        try {
            byte[] encryptionSecret = generateEncryptionSecret();
            byte[] macSecret = generateMacSecret();
            byte[] masterSecret = Util.combine(encryptionSecret, macSecret);
            byte[] encryptionSalt = generateSalt();
            int iterations = generateIterationCount(passphrase, encryptionSalt);
            byte[] encryptedMasterSecret = encryptWithPassphrase(encryptionSalt, iterations, masterSecret, passphrase);
            byte[] macSalt = generateSalt();
            byte[] encryptedAndMacdMasterSecret = macWithPassphrase(macSalt, iterations, encryptedMasterSecret, passphrase);

            save("encryption_salt", encryptionSalt);
            save("mac_salt", macSalt);
            save("passphrase_iterations", iterations);
            save("master_secret", encryptedAndMacdMasterSecret);
            save("passphrase_initialized", true);

            return new MasterSecret(new SecretKeySpec(encryptionSecret, "AES"),
                new SecretKeySpec(macSecret, "HmacSHA1"));
        } catch (GeneralSecurityException e) {
            logger.info("keyutil", e);
            return null;
        }
    }

    private static void save(String key, int value) {
        preferenceRepository.save(new Preference(key, value));
    }

    private static void save(String key, byte[] value) {
        preferenceRepository.save(new Preference(key, Base64.encodeBytes(value)));
    }

    private static void save(String key, boolean value) {
        preferenceRepository.save(new Preference(key,value));
    }

    private static byte[] retrieve(String key) throws IOException {
        String encodedValue = preferenceRepository.findByKey(key);

        if (TextUtils.isEmpty(encodedValue)) return null;
        else return Base64.decode(encodedValue);
    }

    private static int retrieve(String key, int defaultValue) throws IOException {
        return Integer.parseInt(preferenceRepository.findByKey(key));
    }

    private static byte[] generateEncryptionSecret() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("AES");
            generator.init(128);

            SecretKey key = generator.generateKey();
            return key.getEncoded();
        } catch (NoSuchAlgorithmException ex) {
            logger.info("keyutil", ex);
            return null;
        }
    }

    private static byte[] generateMacSecret() {
        try {
            KeyGenerator generator = KeyGenerator.getInstance("HmacSHA1");
            return generator.generateKey().getEncoded();
        } catch (NoSuchAlgorithmException e) {
            logger.info("keyutil", e);
            return null;
        }
    }

    private static byte[] generateSalt() throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        random.nextBytes(salt);

        return salt;
    }

    private static int generateIterationCount(String passphrase, byte[] salt) {
        int TARGET_ITERATION_TIME = 50;   //ms
        int MINIMUM_ITERATION_COUNT = 100;   //default for low-end devices
        int BENCHMARK_ITERATION_COUNT = 10000; //baseline starting iteration count

        try {
            PBEKeySpec keyspec = new PBEKeySpec(passphrase.toCharArray(), salt, BENCHMARK_ITERATION_COUNT);
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWITHSHA1AND128BITAES-CBC-BC");

            long startTime = System.currentTimeMillis();
            skf.generateSecret(keyspec);
            long finishTime = System.currentTimeMillis();

            int scaledIterationTarget = (int) (((double) BENCHMARK_ITERATION_COUNT / (double) (finishTime - startTime)) * TARGET_ITERATION_TIME);

            if (scaledIterationTarget < MINIMUM_ITERATION_COUNT) return MINIMUM_ITERATION_COUNT;
            else return scaledIterationTarget;
        } catch (NoSuchAlgorithmException e) {
            logger.info("MasterSecretUtil", e);
            return MINIMUM_ITERATION_COUNT;
        } catch (InvalidKeySpecException e) {
            logger.info("MasterSecretUtil", e);
            return MINIMUM_ITERATION_COUNT;
        }
    }

    private static SecretKey getKeyFromPassphrase(String passphrase, byte[] salt, int iterations)
        throws GeneralSecurityException {
        PBEKeySpec keyspec = new PBEKeySpec(passphrase.toCharArray(), salt, iterations);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBEWITHSHA1AND128BITAES-CBC-BC");
        return skf.generateSecret(keyspec);
    }

    private static Cipher getCipherFromPassphrase(String passphrase, byte[] salt, int iterations, int opMode)
        throws GeneralSecurityException {
        SecretKey key = getKeyFromPassphrase(passphrase, salt, iterations);
        Cipher cipher = Cipher.getInstance(key.getAlgorithm());
        cipher.init(opMode, key, new PBEParameterSpec(salt, iterations));

        return cipher;
    }

    private static byte[] encryptWithPassphrase(byte[] encryptionSalt, int iterations, byte[] data, String passphrase)
        throws GeneralSecurityException {
        Cipher cipher = getCipherFromPassphrase(passphrase, encryptionSalt, iterations, Cipher.ENCRYPT_MODE);
        return cipher.doFinal(data);
    }

    private static byte[] decryptWithPassphrase(byte[] encryptionSalt, int iterations, byte[] data, String passphrase)
        throws GeneralSecurityException, IOException {
        Cipher cipher = getCipherFromPassphrase(passphrase, encryptionSalt, iterations, Cipher.DECRYPT_MODE);
        return cipher.doFinal(data);
    }

    private static Mac getMacForPassphrase(String passphrase, byte[] salt, int iterations)
        throws GeneralSecurityException {
        SecretKey key = getKeyFromPassphrase(passphrase, salt, iterations);
        byte[] pbkdf2 = key.getEncoded();
        SecretKeySpec hmacKey = new SecretKeySpec(pbkdf2, "HmacSHA1");
        Mac hmac = Mac.getInstance("HmacSHA1");
        hmac.init(hmacKey);

        return hmac;
    }

    private static byte[] verifyMac(byte[] macSalt, int iterations, byte[] encryptedAndMacdData, String passphrase) throws InvalidPassphraseException, GeneralSecurityException, IOException {
        Mac hmac = getMacForPassphrase(passphrase, macSalt, iterations);

        byte[] encryptedData = new byte[encryptedAndMacdData.length - hmac.getMacLength()];
        System.arraycopy(encryptedAndMacdData, 0, encryptedData, 0, encryptedData.length);

        byte[] givenMac = new byte[hmac.getMacLength()];
        System.arraycopy(encryptedAndMacdData, encryptedAndMacdData.length - hmac.getMacLength(), givenMac, 0, givenMac.length);

        byte[] localMac = hmac.doFinal(encryptedData);

        if (Arrays.equals(givenMac, localMac)) return encryptedData;
        else throw new InvalidPassphraseException("MAC Error");
    }

    private static byte[] macWithPassphrase(byte[] macSalt, int iterations, byte[] data, String passphrase) throws GeneralSecurityException {
        Mac hmac = getMacForPassphrase(passphrase, macSalt, iterations);
        byte[] mac = hmac.doFinal(data);
        byte[] result = new byte[data.length + mac.length];

        System.arraycopy(data, 0, result, 0, data.length);
        System.arraycopy(mac, 0, result, data.length, mac.length);

        return result;
    }
}
