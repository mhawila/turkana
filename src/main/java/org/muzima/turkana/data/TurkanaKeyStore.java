package org.muzima.turkana.data;

import org.springframework.stereotype.Component;
import org.whispersystems.libsignal.IdentityKey;
import org.whispersystems.libsignal.IdentityKeyPair;
import org.whispersystems.libsignal.InvalidKeyIdException;
import org.whispersystems.libsignal.SignalProtocolAddress;
import org.whispersystems.libsignal.state.PreKeyRecord;
import org.whispersystems.libsignal.state.SessionRecord;
import org.whispersystems.libsignal.state.SignalProtocolStore;
import org.whispersystems.libsignal.state.SignedPreKeyRecord;
import org.whispersystems.libsignal.util.KeyHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @uthor Willa Mhawila<a.mhawila@gmail.com> on 7/30/19.
 */

@Component
public class TurkanaKeyStore implements SignalProtocolStore {
    private static Map<Integer, PreKeyRecord> PRE_KEY_STORE = new HashMap<>();
    private static Map<SignalProtocolAddress, SessionRecord> SESSION_STORE = new HashMap<>();
    private static Map<Integer, SignedPreKeyRecord> SIGNED_PREKEYS_STORE = new HashMap<>();

    private static final IdentityKeyPair IDENTITY_KEY_PAIR = KeyHelper.generateIdentityKeyPair();

    @Override
    public IdentityKeyPair getIdentityKeyPair() {
        return IDENTITY_KEY_PAIR;
    }

    @Override
    public int getLocalRegistrationId() {
        return 12;
    }

    @Override
    public boolean saveIdentity(SignalProtocolAddress signalProtocolAddress, IdentityKey identityKey) {

        return true;
    }

    @Override
    public boolean isTrustedIdentity(SignalProtocolAddress signalProtocolAddress, IdentityKey identityKey, Direction direction) {
        return true;
    }

    @Override
    public PreKeyRecord loadPreKey(int i) throws InvalidKeyIdException {
        return PRE_KEY_STORE.get(i);
    }

    @Override
    public void storePreKey(int i, PreKeyRecord preKeyRecord) {
        PRE_KEY_STORE.put(i, preKeyRecord);
    }

    @Override
    public boolean containsPreKey(int i) {
        return PRE_KEY_STORE.containsKey(i);
    }

    @Override
    public void removePreKey(int i) {
        if(PRE_KEY_STORE.containsKey(i)) {
            PRE_KEY_STORE.remove(i);
        }
    }

    @Override
    public SessionRecord loadSession(SignalProtocolAddress signalProtocolAddress) {
        if(SESSION_STORE.containsKey(signalProtocolAddress)) {
            return SESSION_STORE.get(signalProtocolAddress);
        }
        return null;
    }

    @Override
    public List<Integer> getSubDeviceSessions(String s) {
        return null;
    }

    @Override
    public void storeSession(SignalProtocolAddress signalProtocolAddress, SessionRecord sessionRecord) {
        SESSION_STORE.put(signalProtocolAddress, sessionRecord);
    }

    @Override
    public boolean containsSession(SignalProtocolAddress signalProtocolAddress) {
        return SESSION_STORE.containsKey(signalProtocolAddress);
    }

    @Override
    public void deleteSession(SignalProtocolAddress signalProtocolAddress) {
        if(SESSION_STORE.containsKey(signalProtocolAddress)) {
            SESSION_STORE.remove(signalProtocolAddress);
        }
    }

    @Override
    public void deleteAllSessions(String s) {

    }

    @Override
    public SignedPreKeyRecord loadSignedPreKey(int i) throws InvalidKeyIdException {
        return SIGNED_PREKEYS_STORE.get(i);
    }

    @Override
    public List<SignedPreKeyRecord> loadSignedPreKeys() {
        return SIGNED_PREKEYS_STORE.values().stream().collect(Collectors.toList());
    }

    @Override
    public void storeSignedPreKey(int i, SignedPreKeyRecord signedPreKeyRecord) {
        SIGNED_PREKEYS_STORE.put(i, signedPreKeyRecord);
    }

    @Override
    public boolean containsSignedPreKey(int i) {
        return SIGNED_PREKEYS_STORE.containsKey(i);
    }

    @Override
    public void removeSignedPreKey(int i) {
        if(SIGNED_PREKEYS_STORE.containsKey(i)) {
            SIGNED_PREKEYS_STORE.remove(i);
        }
    }
}
