package org.muzima.turkana.utils;

import org.muzima.turkana.data.PreferenceRepository;
import org.muzima.turkana.model.Preference;
import org.muzima.turkana.web.signalclients.MasterCipher;
import org.muzima.turkana.web.signalclients.MasterSecret;
import org.springframework.beans.factory.annotation.Autowired;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPrivateKey;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Utility class for working with identity keys.
 *
 * @author Samuel Owino
 */

public class IdentityKeyUtil {

    @SuppressWarnings("unused")
    private static final String TAG = IdentityKeyUtil.class.getSimpleName();

    private static final String IDENTITY_PUBLIC_KEY_CIPHERTEXT_LEGACY_PREF = "pref_identity_public_curve25519";
    private static final String IDENTITY_PRIVATE_KEY_CIPHERTEXT_LEGACY_PREF = "pref_identity_private_curve25519";

    private static final String IDENTITY_PUBLIC_KEY_PREF = "pref_identity_public_v3";
    private static final String IDENTITY_PRIVATE_KEY_PREF = "pref_identity_private_v3";

    @Autowired
    private static PreferenceRepository preferenceRepository;

    public static boolean hasIdentityKey() {
        return true;
    }

    public static IdentityKey getIdentityKey() {
        if (!hasIdentityKey()) throw new AssertionError("There isn't one!");

        try {
            byte[] publicKeyBytes = Base64.decode(retrieve(IDENTITY_PUBLIC_KEY_PREF));
            return new IdentityKey(publicKeyBytes, 0);
        } catch (IOException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }

    public static IdentityKeyPair getIdentityKeyPair() {
        if (!hasIdentityKey()) throw new AssertionError("There isn't one!");

        try {
            IdentityKey publicKey = getIdentityKey();
            ECPrivateKey privateKey = Curve.decodePrivatePoint(Base64.decode(retrieve(IDENTITY_PRIVATE_KEY_PREF)));

            return new IdentityKeyPair(publicKey, privateKey);
        } catch (IOException e) {
            throw new AssertionError(e);
        }
    }

    public static void generateIdentityKeys() {
        ECKeyPair djbKeyPair = Curve.generateKeyPair();
        IdentityKey djbIdentityKey = new IdentityKey(djbKeyPair.getPublicKey());
        ECPrivateKey djbPrivateKey = djbKeyPair.getPrivateKey();

        save(IDENTITY_PUBLIC_KEY_PREF, Base64.encodeBytes(djbIdentityKey.serialize()));
        save(IDENTITY_PRIVATE_KEY_PREF, Base64.encodeBytes(djbPrivateKey.serialize()));
    }

    public static void migrateIdentityKeys(MasterSecret masterSecret) {
        if (!hasIdentityKey()) {
            if (hasLegacyIdentityKeys()) {
                IdentityKeyPair legacyPair = getLegacyIdentityKeyPair(masterSecret);

                save(IDENTITY_PUBLIC_KEY_PREF, Base64.encodeBytes(legacyPair.getPublicKey().serialize()));
                save(IDENTITY_PRIVATE_KEY_PREF, Base64.encodeBytes(legacyPair.getPrivateKey().serialize()));

                delete(IDENTITY_PUBLIC_KEY_CIPHERTEXT_LEGACY_PREF);
                delete(IDENTITY_PRIVATE_KEY_CIPHERTEXT_LEGACY_PREF);
            } else {
                generateIdentityKeys();
            }
        }
    }

    private static boolean hasLegacyIdentityKeys() {
        return
            retrieve(IDENTITY_PUBLIC_KEY_CIPHERTEXT_LEGACY_PREF) != null &&
                retrieve(IDENTITY_PRIVATE_KEY_CIPHERTEXT_LEGACY_PREF) != null;
    }

    private static IdentityKeyPair getLegacyIdentityKeyPair(MasterSecret masterSecret) {
        try {
            MasterCipher masterCipher = new MasterCipher(masterSecret);
            byte[] publicKeyBytes = Base64.decode(retrieve(IDENTITY_PUBLIC_KEY_CIPHERTEXT_LEGACY_PREF));
            IdentityKey identityKey = new IdentityKey(publicKeyBytes, 0);
            ECPrivateKey privateKey = masterCipher.decryptKey(Base64.decode(retrieve(IDENTITY_PRIVATE_KEY_CIPHERTEXT_LEGACY_PREF)));

            return new IdentityKeyPair(identityKey, privateKey);
        } catch (IOException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }

    private static String retrieve(String key) {
        return preferenceRepository.findByKey(key);
    }

    private static void save(String key, String value) {
        preferenceRepository.save(new Preference(key,value));
    }

    private static void delete(String key) {
        preferenceRepository.deleteByKey(key);
    }

}

