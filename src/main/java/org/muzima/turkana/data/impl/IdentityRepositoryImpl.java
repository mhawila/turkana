package org.muzima.turkana.data.impl;

import org.muzima.turkana.data.IdentityRepository;
import org.muzima.turkana.model.Identity;
import org.muzima.turkana.model.VerifiedStatus;
import org.whispersystems.libsignal.IdentityKey;

public abstract class IdentityRepositoryImpl implements IdentityRepository {

    @Override
    public Identity getIdentity(String signalAddress) {

        for (Identity identity : findAll()) {
            if (identity.getAddress().equalsIgnoreCase(signalAddress)) return identity;
        }
        return null;
    }

    @Override
    public void saveIdentity(String signalAddress, IdentityKey identityKey, VerifiedStatus aDefault, boolean b, long currentTimeMillis, boolean nonBlockingApproval) {
        save( new Identity(signalAddress,identityKey,identityKey.getFingerprint(),currentTimeMillis));
    }

    @Override
    public void setApproval(String signalAddress, boolean nonBlockingApproval) {

    }
}
