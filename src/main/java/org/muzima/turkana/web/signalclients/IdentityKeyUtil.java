package org.muzima.turkana.web.signalclients;

import org.muzima.turkana.data.TurkanaKeyStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyException;
import org.whispersystems.libsignal.ecc.Curve;
import org.whispersystems.libsignal.ecc.ECKeyPair;
import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.signalservice.internal.util.Base64;

import java.io.IOException;
import java.util.HashMap;

import static org.apache.http.client.methods.RequestBuilder.delete;

public class IdentityKeyUtil {


    @SuppressWarnings("unused")
    private static final String TAG = IdentityKeyUtil.class.getSimpleName();

    private static final String IDENTITY_PUBLIC_KEY_CIPHERTEXT_LEGACY_PREF = "pref_identity_public_curve25519";
    private static final String IDENTITY_PRIVATE_KEY_CIPHERTEXT_LEGACY_PREF = "pref_identity_private_curve25519";
    private static final HashMap<String,String> KEY_PREF_STORE = new HashMap<>();
    private static final String IDENTITY_PUBLIC_KEY_PREF = "pref_identity_public_v3";
    private static final String IDENTITY_PRIVATE_KEY_PREF = "pref_identity_private_v3";

    @Autowired
    TurkanaKeyStore turkanaKeyStore;

    public static boolean hasIdentityKey() {
        return true;
    }

    public static @NonNull
    IdentityKey getIdentityKey() {

        try {
            byte[] publicKeyBytes = Base64.decode(retrieve(IDENTITY_PUBLIC_KEY_PREF));
            return new IdentityKey(publicKeyBytes, 0);
        } catch (IOException | InvalidKeyException e) {
            throw new AssertionError(e);
        }
    }

    public static @NonNull
    IdentityKeyPair getIdentityKeyPair() {

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


    private static boolean hasLegacyIdentityKeys() {
        return true;
    }

    private static void save(String key, String value) {
        KEY_PREF_STORE.put(key,value);
    }

    private static String retrieve(String key) {
        return KEY_PREF_STORE.get(key);
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

}
