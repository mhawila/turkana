package org.muzima.turkana.crypto;

import org.whispersystems.libsignal.ecc.ECPrivateKey;
import org.whispersystems.libsignal.ecc.ECPublicKey;

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
 * This class represents the ECC keypair.
 *
 * @author Samuel Owino
 */

public class AsymmetricMasterSecret {

    private final ECPublicKey djbPublicKey;
    private final ECPrivateKey djbPrivateKey;


    public AsymmetricMasterSecret(ECPublicKey djbPublicKey, ECPrivateKey djbPrivateKey) {
        this.djbPublicKey = djbPublicKey;
        this.djbPrivateKey = djbPrivateKey;
    }

    public ECPublicKey getDjbPublicKey() {
        return djbPublicKey;
    }


    public ECPrivateKey getPrivateKey() {
        return djbPrivateKey;
    }

}
