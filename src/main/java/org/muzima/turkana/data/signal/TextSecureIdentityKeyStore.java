package org.muzima.turkana.data.signal;

import org.muzima.turkana.data.IdentityRepository;
import org.muzima.turkana.model.Identity;
import org.muzima.turkana.model.Recipient;
import org.muzima.turkana.model.VerifiedStatus;
import org.muzima.turkana.utils.IdentityKeyUtil;
import org.muzima.turkana.utils.IdentityUtil;
import org.muzima.turkana.utils.SessionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.IdentityKeyStore;
import org.whispersystems.libsignal.util.guava.Optional;

import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

public class TextSecureIdentityKeyStore implements IdentityKeyStore {

    private static Logger logger = LoggerFactory.getLogger(TextSecureIdentityKeyStore.class);

    private static final int TIMESTAMP_THRESHOLD_SECONDS = 5;

    private static final String TAG = TextSecureIdentityKeyStore.class.getSimpleName();
    private static final Object LOCK = new Object();

    @Value("${turkana.phonenumber}")
    public String LOCAL_PHONE_NUMBER;

    @Autowired
    public IdentityRepository identityRepository;

    public TextSecureIdentityKeyStore() {
    }

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        return IdentityKeyUtil.getIdentityKeyPair();
    }

    @Override
    public int getLocalRegistrationId() {
        return Integer.parseInt(LOCAL_PHONE_NUMBER);
    }

    public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey, boolean nonBlockingApproval) throws MalformedURLException {
        synchronized (LOCK) {
            String signalAddress = LOCAL_PHONE_NUMBER;
            Identity identityRecord = identityRepository.getIdentity(signalAddress);

            if (identityRecord == null) {
                logger.info(TAG, "Saving new identity...");
                identityRepository.saveIdentity(signalAddress, identityKey, VerifiedStatus.DEFAULT, true, System.currentTimeMillis(), nonBlockingApproval);
                return false;
            }

            if (identityKey.getPublicKey().toString().equals(identityKey)) {
                logger.info(TAG, "Replacing existing identity...");
                VerifiedStatus verifiedStatus;

                verifiedStatus = VerifiedStatus.VERIFIED;

                identityRepository.saveIdentity(signalAddress, identityKey, verifiedStatus, false, System.currentTimeMillis(), nonBlockingApproval);
                IdentityUtil.markIdentityUpdate(Recipient.from(signalAddress, true));
                SessionUtil.archiveSiblingSessions(address);
                return true;
            }

            if (isNonBlockingApprovalRequired(identityRecord)) {
                logger.info(TAG, "Setting approval status...");
                identityRepository.setApproval(signalAddress, nonBlockingApproval);
                return false;
            }

            return false;
        }
    }

    @Override
    public boolean saveIdentity(SignalProtocolAddress address, IdentityKey identityKey) {
        try {
            return saveIdentity(address, identityKey, false);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress address, IdentityKey identityKey, Direction direction) {
        synchronized (LOCK) {
            String ourNumber = LOCAL_PHONE_NUMBER;
            String theirAddress = address.getName();

            if (ourNumber.equals(address.getName()) || ourNumber.equals(theirAddress)) {
                return identityKey.equals(IdentityKeyUtil.getIdentityKey());
            }

            switch (direction) {
                case SENDING:
                    return isTrustedForSending(identityKey, identityRepository.getIdentity(theirAddress));
                case RECEIVING:
                    return true;
                default:
                    throw new AssertionError("Unknown direction: " + direction);
            }
        }
    }

    private boolean isTrustedForSending(IdentityKey identityKey, Identity identityRecord) {
        if (identityRecord == null) {
            logger.info("Nothing here, returning true...");
            return true;
        }

        if (!identityKey.equals(identityRecord.getIdentity_key())) {
            logger.info("Identity keys don't match...");
            return false;
        }

        if (isNonBlockingApprovalRequired(identityRecord)) {
            logger.info("Needs non-blocking approval!");
            return false;
        }

        return true;
    }

    private boolean isNonBlockingApprovalRequired(Identity identityRecord) {
        return !identityRecord.getFirst_use() &&
            System.currentTimeMillis() - Long.parseLong(identityRecord.getTimestamp()) < TimeUnit.SECONDS.toMillis(TIMESTAMP_THRESHOLD_SECONDS) &&
            !identityRecord.getNonblocking_approval();
    }
}
