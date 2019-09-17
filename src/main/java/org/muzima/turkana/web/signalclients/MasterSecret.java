package org.muzima.turkana.web.signalclients;

import java.util.Arrays;

import javax.crypto.spec.SecretKeySpec;

/**
 * When a user first initializes TextSecure, a few secrets
 * are generated.  These are:
 * <p>
 * 1) A 128bit symmetric encryption key.
 * 2) A 160bit symmetric MAC key.
 * 3) An ECC keypair.
 * <p>
 * The first two, along with the ECC keypair's private key, are
 * then encrypted on disk using PBE.
 * <p>
 * This class represents 1 and 2.
 *
 * @author Samuel Owino
 */

public class MasterSecret {

    private SecretKeySpec encryptionKey;
    private SecretKeySpec macKey;

    public MasterSecret(SecretKeySpec encryptionKey, SecretKeySpec macKey) {
        this.encryptionKey = encryptionKey;
        this.macKey = macKey;
    }

    public SecretKeySpec getEncryptionKey() {
        return this.encryptionKey;
    }

    public SecretKeySpec getMacKey() {
        return this.macKey;
    }

}

