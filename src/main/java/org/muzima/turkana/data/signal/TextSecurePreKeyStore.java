package org.muzima.turkana.data.signal;

import org.muzima.turkana.service.OneTimePrekeyService;
import org.muzima.turkana.service.SignedPrekeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.PreKeyStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.state.SignedPreKeyStore;

import java.util.List;

@Component
public class TextSecurePreKeyStore implements PreKeyStore, SignedPreKeyStore {

    @SuppressWarnings("unused")
    private static final String TAG = TextSecurePreKeyStore.class.getSimpleName();

    private static final Object FILE_LOCK = new Object();

    @Autowired
    SignedPrekeyService signedPrekeyService;

    @Autowired
    OneTimePrekeyService prekeyService;

    @Autowired
    IdentityKeyRepository identityKeyRepository;

    public TextSecurePreKeyStore() {
    }

    @Override
    public PreKeyRecord loadPreKey(int preKeyId) throws InvalidKeyIdException {
        synchronized (FILE_LOCK) {
            PreKeyRecord preKeyRecord = prekeyService.getPreKey(preKeyId);

            if (preKeyRecord == null) throw new InvalidKeyIdException("No such key: " + preKeyId);
            else return preKeyRecord;
        }
    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int signedPreKeyId) throws InvalidKeyIdException {
        synchronized (FILE_LOCK) {
            SignedPreKeyRecord signedPreKeyRecord = signedPrekeyService.getSignedPreKey(signedPreKeyId);

            if (signedPreKeyRecord == null)
                throw new InvalidKeyIdException("No such signed prekey: " + signedPreKeyId);
            else
                return signedPreKeyRecord;
        }
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        synchronized (FILE_LOCK) {
            return signedPrekeyService.getAllSignedPreKeys();
        }
    }

    @Override
    public void storePreKey(int preKeyId, PreKeyRecord record) {
        synchronized (FILE_LOCK) {
            prekeyService.insertPreKey(preKeyId, record);
        }
    }

    @Override
    public void storeSignedPreKey(int signedPreKeyId, SignedPreKeyRecord record) {
        synchronized (FILE_LOCK) {
            signedPrekeyService.insertSignedPreKey(signedPreKeyId, record);
        }
    }

    @Override
    public boolean containsPreKey(int preKeyId) {
        return prekeyService.getPreKey(preKeyId) != null;
    }

    @Override
    public boolean containsSignedPreKey(int signedPreKeyId) {
        return signedPrekeyService.getSignedPreKey(signedPreKeyId) != null;
    }

    @Override
    public void removePreKey(int preKeyId) {
        prekeyService.removePreKey(preKeyId);
    }

    @Override
    public void removeSignedPreKey(int signedPreKeyId) {
        signedPrekeyService.removeSignedPreKey(signedPreKeyId);
    }

    public int getLocalRegistrationId() {
        return 12;
    }

    public boolean saveIdentity(SignalProtocolAddress signalProtocolAddress, IdentityKey identityKey) {
        org.muzima.turkana.model.IdentityKey identityKeyValue = new org.muzima.turkana.model.IdentityKey(identityKey.getPublicKey());
        identityKeyRepository.save(identityKeyValue);
        return true;
    }

    public void purgeKeyStore() {
        identityKeyRepository.deleteAll();
        signedPrekeyService.deleteAll();
        prekeyService.deleteAll();
    }
}
